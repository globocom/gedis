package com.globo.bigdata.gedis

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{doReturn, doThrow, never, verify, reset}
import org.scalatest.BeforeAndAfterEach
import redis.clients.jedis.{Jedis, JedisSentinelPool, Pipeline}
import utils.UnitSpec

class GedisClientProviderUtilsSpec {
  def catchError(t : Throwable) = {}
  def executeWithJedis(jedis : Jedis) = {}
  def executeWithPipeline(pipeline : Pipeline) = {}
}

class GedisClientProviderSpec extends UnitSpec with BeforeAndAfterEach {

  val exception = new RuntimeException()
  val helper = mock[GedisClientProviderUtilsSpec]

  override def beforeEach() {
    reset(helper)
  }

  describe("withClient-jedis") {
    val jedis = mock[Jedis]
    val provider = new GedisClientProvider(single = Some(jedis), errorCallback = helper.catchError)

    it("execute-operation") {
      provider.withClient(helper.executeWithJedis)

      verify(helper).executeWithJedis(jedis)
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithJedis(any())

      a [RuntimeException] should be thrownBy {
        provider.withClient(helper.executeWithJedis)
      }

      verify(helper).executeWithJedis(jedis)
      verify(helper).catchError(exception)
    }

  }

  describe("withClient-pool") {
    val pool = mock[JedisSentinelPool]
    val jedis = mock[Jedis]
    val provider = new GedisClientProvider(pool = Some(pool), errorCallback = helper.catchError)

    doReturn(jedis, Nil: _*).when(pool).getResource

    it("execute-operation") {
      provider.withClient(helper.executeWithJedis)

      verify(helper).executeWithJedis(jedis)
      verify(jedis).close()
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithJedis(any())

      a [RuntimeException] should be thrownBy {
        provider.withClient(helper.executeWithJedis)
      }

      verify(helper).executeWithJedis(jedis)
      verify(jedis).close()
      verify(helper).catchError(exception)
    }
  }

  describe("withPipeline") {
    val jedis = mock[Jedis]
    val pipeline = mock[Pipeline]
    val provider = new GedisClientProvider(single = Some(jedis), errorCallback = helper.catchError)

    doReturn(pipeline, Nil: _*).when(jedis).pipelined

    it("execute-operations") {
      provider.withPipeline(helper.executeWithPipeline)

      verify(helper).executeWithPipeline(pipeline)
      verify(pipeline).sync()
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithPipeline(any())

      a [RuntimeException] should be thrownBy {
        provider.withPipeline(helper.executeWithPipeline)
      }

      verify(helper).executeWithPipeline(pipeline)
      verify(pipeline).sync()
      verify(helper).catchError(exception)
    }
  }

}
