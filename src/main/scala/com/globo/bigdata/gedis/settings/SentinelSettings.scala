package com.globo.bigdata.gedis.settings

/**
 * Settings used for Redis Sentinel connections
 *
 * @param master master node name
 * @param sentinels sentinels nodes names
 * @param password connection password
 * @param socketTimeout socket timeout
 * @param connectionTimeout connection timeout
 * @param pool connection pool settings
 * @see [[com.globo.bigdata.gedis.settings.PoolSettings]]
 */
case class SentinelSettings(master: String, sentinels : Set[String], password: Option[String] = DefaultSettings.password, socketTimeout: Int = DefaultSettings.socketTimeout, connectionTimeout : Int = DefaultSettings.connectionTimeout, pool: PoolSettings = DefaultSettings.pool) {}