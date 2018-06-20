package com.globo.bigdata.gedis.settings

case class PoolSettings(maxTotal: Int = 8, minIdle: Int = 0, maxIdle: Int = 8, maxWaitInMillis: Int = 10){}

case class SentinelSettings(master: String, sentinels : Set[String], password: Option[String] = DefaultSettings.password, socketTimeout: Int = DefaultSettings.socketTimeout, connectionTimeout : Int = DefaultSettings.connectionTimeout, pool: PoolSettings = DefaultSettings.pool) {}