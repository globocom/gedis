package com.globo.bigdata.gedis.settings

/**
 * Default connection settings object
 */
object DefaultSettings {
  val host : String = "localhost"
  val port : Int = 6379
  val password :Option[String] = None
  val socketTimeout: Int = 10
  val connectionTimeout : Int = 5000
  val maxAttempts: Int = 3
  val pool: PoolSettings = PoolSettings()
}

class DefaultSettings() {}