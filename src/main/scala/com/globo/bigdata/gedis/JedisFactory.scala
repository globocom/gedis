package com.globo.bigdata.gedis

import com.globo.bigdata.gedis.settings.{SingleSettings, PoolSettings, SentinelSettings}
import redis.clients.jedis.{Jedis, JedisPoolConfig, JedisSentinelPool, Protocol}

/**
 * Jedis factory object
 */
object JedisFactory {
  val instance = new JedisFactory()
}

/**
 * Jedis factory
 */
class JedisFactory {

  /**
   * Create Jedis Sentinel Pool client
   *
   * @param s Sentinel settings
   * @return
   */
  def jedis(s: SentinelSettings) : JedisSentinelPool = {
    import scala.collection.JavaConverters._
    new JedisSentinelPool(s.master, s.sentinels.asJava, jedisPool(s.pool), s.connectionTimeout, s.socketTimeout, s.password.get, Protocol.DEFAULT_DATABASE, null)
  }

  /**
   * Create single Jedis client
   *
   * @param s Single settings
   * @return
   */
  def jedis(s : SingleSettings) : Jedis = {
    new Jedis(s.host, s.port, s.connectionTimeout, s.socketTimeout)
  }

  /**
   * Maps Gedis [[PoolSettings]] into [[JedisPoolConfig]]
   *
   * @param pool Gedis pool settings
   * @return
   */
  private def jedisPool(pool: PoolSettings) : JedisPoolConfig = {
    val conf = new JedisPoolConfig()
    conf.setMaxWaitMillis(pool.maxWaitInMillis)
    conf.setMaxTotal(pool.maxTotal)
    conf.setMinIdle(pool.minIdle)
    conf.setMaxIdle(pool.maxIdle)
    conf
  }
}
