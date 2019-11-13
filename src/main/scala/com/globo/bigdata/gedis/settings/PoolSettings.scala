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