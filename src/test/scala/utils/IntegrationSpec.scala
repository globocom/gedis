package utils

import com.github.sebruck.EmbeddedRedis
import com.globo.bigdata.gedis.settings.SingleSettings
import com.globo.bigdata.gedis.{GedisClient, GedisClientFactory}
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import redis.embedded.RedisServer

/**
 * Integration test abstraction
 *
 * Controls embedded Redis service setup / teardown
 */
@RunWith(classOf[JUnitRunner])
abstract class IntegrationSpec extends FunSpec
with OneInstancePerTest
with Matchers
with BeforeAndAfterEach
with BeforeAndAfterAll
with EmbeddedRedis
with MockitoSugar {
  def spy[T](obj: T): T = Mockito.spy(obj)

  private val REDIS_TEST_HOST = "localhost"
  private val REDIS_TEST_PORT = 6399
  private var redis: RedisServer = null
  private var client : GedisClient = null

  def enhancedClient() : GedisClient = {
    if(client == null){
        client = GedisClientFactory.instance.client(singleSettings = Some(new SingleSettings(host = REDIS_TEST_HOST, port = REDIS_TEST_PORT)))
    }
    client
  }

  override def beforeAll() = {
    redis = startRedis(port = REDIS_TEST_PORT)
    println(s"Embedded Redis started at port $REDIS_TEST_PORT")
  }

  override def afterAll() = {
    stopRedis(redis)
    println(s"Embedded Redis stopped at port $REDIS_TEST_PORT")
  }

  /**
    * WARNING: DELETE ALL DATA IN REDIS - DO NOT USE PRODUCTION OR QA DATABASES, JUST EMBEDDED
    * @return
    */
  override def beforeEach(): Unit = {
   enhancedClient().pipeline(p => { p.flushAll() })//Clear
  }
}