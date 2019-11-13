package com.globo.bigdata.gedis.settings

import redis.clients.jedis.HostAndPort

/**
 * Settings used for Redis Cluster connections
 *
 * @param clusterNodes set with cluster nodes
 * @param password connection password
 * @param socketTimeout socket timeout
 * @param connectionTimeout connection timeout
 * @param maxAttempts max connection attempts
 * @param pool connection pool settings
 * @see [[com.globo.bigdata.gedis.settings.PoolSettings]]
 */
case class ClusterSettings(clusterNodes: Set[HostAndPort], password: Option[String] = DefaultSettings.password, socketTimeout: Int = DefaultSettings.socketTimeout, connectionTimeout : Int = DefaultSettings.connectionTimeout, maxAttempts : Int = DefaultSettings.maxAttempts, pool: PoolSettings = DefaultSettings.pool)

