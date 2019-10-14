package com.globo.bigdata.gedis.settings

/**
 * Settings for Redis connection pool
 *
 * @param maxTotal max connections
 * @param minIdle min iddle connections
 * @param maxIdle max iddle connections
 * @param maxWaitInMillis max waiting time in milliseconds
 */
case class PoolSettings(maxTotal: Int = 8, minIdle: Int = 0, maxIdle: Int = 8, maxWaitInMillis: Int = 10){}

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