package com.globo.bigdata.gedis

import com.globo.bigdata.gedis.settings.{SentinelSettings, SingleSettings}

object GedisClientFactory {
  val errorCallback: (Throwable) => Unit = (t: Throwable) => {}
  val instance = new GedisClientFactory(errorCallback = errorCallback)
}

class GedisClientFactory(jedisFactory: JedisFactory = JedisFactory.instance, errorCallback: (Throwable) => Unit = (t: Throwable) => {}) {

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
