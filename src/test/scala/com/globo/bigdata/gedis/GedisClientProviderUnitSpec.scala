package com.globo.bigdata.gedis

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{doReturn, doThrow, never, reset, verify}
import org.scalatest.BeforeAndAfterEach
import redis.clients.jedis.{Jedis, JedisCluster, JedisSentinelPool, Pipeline}
import utils.UnitSpec

/**
 * Utility test class
 */
class GedisClientProviderUtilsSpec {
  def catchError(t : Throwable) = {}
  def executeWithJedisAdapter(adapter : JedisClientAdapter) = {}
  def executeWithPipeline(pipeline : Pipeline) = {}
}

/**
 * Unity tests for [[GedisClientProvider]]
 * @see [[UnitSpec]]
 */
class GedisClientProviderSpec extends UnitSpec with BeforeAndAfterEach {

  val exception = new RuntimeException()
  val helper = mock[GedisClientProviderUtilsSpec]

  override def beforeEach() {
    reset(helper)
  }

  describe("withClient-jedis") {
    val jedis = mock[Jedis]
    val jedisAdapter = new JedisClientAdapter(single = Some(jedis))
    val provider = new GedisClientProvider(jedisAdapter, errorCallback = helper.catchError)

    it("execute-operation") {
      provider.withClient(helper.executeWithJedisAdapter)

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithJedisAdapter(any())

      a [RuntimeException] should be thrownBy {
        provider.withClient(helper.executeWithJedisAdapter)
      }

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(helper).catchError(exception)
    }

  }

  describe("withClient-pool") {
    val pool = mock[JedisSentinelPool]
    val jedis = mock[Jedis]
    val jedisAdapter = new JedisClientAdapter(pool = Some(pool))
    val provider = new GedisClientProvider(jedisAdapter, errorCallback = helper.catchError)

    doReturn(jedis, Nil: _*).when(pool).getResource

    it("execute-operation") {
      provider.withClient(helper.executeWithJedisAdapter)

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(jedis).close()
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithJedisAdapter(any())

      a [RuntimeException] should be thrownBy {
        provider.withClient(helper.executeWithJedisAdapter)
      }

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(jedis).close()
      verify(helper).catchError(exception)
    }
  }

  describe("withClient-cluster") {
    val cluster = mock[JedisCluster]
    val jedisAdapter = new JedisClientAdapter(cluster = Some(cluster))
    val provider = new GedisClientProvider(jedisAdapter, errorCallback = helper.catchError)

    it("execute-operation") {
      provider.withClient(helper.executeWithJedisAdapter)

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(helper, never()).catchError(any())
    }

    it("execute-error-callback") {
      doThrow(exception).when(helper).executeWithJedisAdapter(any())

      a [RuntimeException] should be thrownBy {
        provider.withClient(helper.executeWithJedisAdapter)
      }

      verify(helper).executeWithJedisAdapter(jedisAdapter)
      verify(helper).catchError(exception)
    }
  }


  describe("withPipeline") {
    val pipeline = mock[Pipeline]
    val jedisAdapter = mock[JedisClientAdapter]
    val provider = new GedisClientProvider(jedisAdapter, errorCallback = helper.catchError)

    doReturn(pipeline, Nil: _*).when(jedisAdapter).pipelined

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
