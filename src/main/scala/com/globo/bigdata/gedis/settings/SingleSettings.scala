package com.globo.bigdata.gedis.settings

/**
 * Settings for single Redis connection
 *
 * @param host host name
 * @param port port number
 * @param password connection password
 * @param socketTimeout socket timeout
 * @param connectionTimeout connection timeout
 */
case class SingleSettings(host: String = "localhost", port: Int = 6379, password: Option[String] = DefaultSettings.password, socketTimeout: Int = DefaultSettings.socketTimeout, connectionTimeout : Int = DefaultSettings.connectionTimeout) {}
