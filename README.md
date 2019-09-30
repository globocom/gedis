# Gedis

[![Build Status](https://travis-ci.com/globocom/gedis.svg?branch=master)](https://travis-ci.com/globocom/gedis)
Resilient Jedis adapter for Scala made with love :heart: by Globo.com

## Install

Add in sbt dependency: 
```
"com.globo.bigdata" %% "gedis" % "0.0.4"
```

## Usage

You can use for Single Instance and Sentinel with all Jedis supported functions

### Single

```
val client:GedisClient = GedisClient(single = Option(new Jedis(host, port)))
```

### Sentinel
```
val client:GedisClient = GedisClient(pool = Option(new JedisSentinelPool(masterName, hosts.asJava, password)))
```

### Commands

All Jedis commands are supported and are simple as:
```
client.get("key")
```

## Contribute

For development and contributing, please follow [Contributing Guide](https://github.com/globocom/gedis/blob/master/CONTRIBUTING.md) and ALWAYS respect the [Code of Conduct](https://github.com/globocom/gedis/blob/master/CODE_OF_CONDUCT.md)
