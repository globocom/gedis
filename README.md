# Gedis
Resilient Jedis adapter for Scala made with love :heart: by Globo.com

## Install

Add in sbt dependency: 
```
"com.globo.bigdata" %% "gedis" % "0.0.2"
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