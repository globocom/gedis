package com.globo.bigdata.gedis.settings

object DefaultSettings {
  val host : String = "localhost"
  val port : Int = 6379
  val password :Option[String] = None
  val socketTimeout: Int = 10
  val connectionTimeout : Int = 5000
  val pool: PoolSettings = new PoolSettings()

}

class DefaultSettings() {}