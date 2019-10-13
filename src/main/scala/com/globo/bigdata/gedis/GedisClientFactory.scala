package com.globo.bigdata.gedis

import com.globo.bigdata.gedis.settings.{SentinelSettings, SingleSettings}

/**
 * Gedis client factory object
 */
object GedisClientFactory {
  val errorCallback: (Throwable) => Unit = (t: Throwable) => {}
  val instance = new GedisClientFactory(errorCallback = errorCallback)
}

/**
 * Gedis client factory
 *
 * @param jedisFactory Jedis factory (underline Redis client provider)
 * @param errorCallback error callback handdler
 */
class GedisClientFactory(jedisFactory: JedisFactory = JedisFactory.instance, errorCallback: (Throwable) => Unit = (t: Throwable) => {}) {

  /**
   * Factory method used to create Gedis clients
   *
   * @param sentinelSettings settings for Sentinel connections
   * @param singleSettings settings for Single connections
   * @return [[GedisClient]]
   */
  def client(sentinelSettings: Option[SentinelSettings] = None, singleSettings: Option[SingleSettings] = None) : GedisClient = {
     new GedisClient(
       new GedisClientProvider(
         pool = sentinelSettings.map(s => jedisFactory.jedis(s)),
         single = singleSettings.map(s => jedisFactory.jedis(s)),
         errorCallback = errorCallback
       )
     )
  }

}
