package com.globo.bigdata.gedis.settings

case class SingleSettings(host: String = "localhost", port: Int = 6379, password: Option[String] = DefaultSettings.password, socketTimeout: Int = DefaultSettings.socketTimeout, connectionTimeout : Int = DefaultSettings.connectionTimeout) {}
