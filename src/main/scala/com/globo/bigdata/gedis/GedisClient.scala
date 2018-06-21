package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisSentinelPool, Pipeline}

trait RedisClient {
  def exists(key: String): Boolean

  def get(key: String): Option[String]

  def mget(key: String*): List[Option[String]]

  def hget(key: String, field: String): Option[String]

  def hgetall(key: String): Map[String, String]

  def hkeys(key : String): List[String]

  def smembers(key: String): Set[String]

  def lrange(key: String, start: Long, end: Long): List[String]

  def set(key: String, value: String): Unit

  def incrby(key: String, amount: Long): Long

  def hset(key: String, field: String, value: String): Unit

  def hmset(key: String, values: Map[String, String]): Unit

  def hsetnx(key: String, field: String, value: String): Unit

  def hincrby(key: String, field: String, amount: Long): Long

  def sadd(key: String, values: String*): Option[Long]

  def lpush(key: String, values: String*): Unit

  def del(key: String*): Unit

  def pipeline[T](f: Function1[Pipeline, T]): Unit
}

class GedisClient(provider: GedisClientProvider) extends RedisClient {

  import scala.collection.JavaConverters._

  def exists(key: String): Boolean = {
    provider.withClient(_.exists(key))
  }

  def get(key: String): Option[String] = {
    Option(provider.withClient(_.get(key)))
  }

  def mget(key: String*): List[Option[String]] = {
    provider.withClient(_.mget(key:_*).asScala.map(item => Option(item)).toList)
  }

  def hget(key: String, field: String): Option[String] = {
    Option(provider.withClient(_.hget(key, field)))
  }

  def hgetall(key: String): Map[String, String] = {
    provider.withClient(_.hgetAll(key)).asScala.toMap
  }

  def hkeys(key : String): List[String] = {
    provider.withClient(c => c.hkeys(key)).asScala.toList
  }

  def smembers(key: String): Set[String] = {
    provider.withClient(_.smembers(key)).asScala.toSet
  }

  def lrange(key: String, start: Long, end: Long): List[String] = {
    provider.withClient(_.lrange(key, start, end)).asScala.toList
  }

  def set(key: String, value: String): Unit = {
    provider.withClient(_.set(key, value))
  }

  def incrby(key: String, amount: Long): Long = {
    provider.withClient(_.incrBy(key, amount))
  }
  def hset(key: String, field: String, value: String): Unit = {
    provider.withClient(_.hset(key, field, value))
  }

  def hmset(key: String, values: Map[String, String]): Unit = {
    provider.withClient(_.hmset(key, values.asJava))
  }

  def hsetnx(key: String, field: String, value: String): Unit = {
    provider.withClient(_.hsetnx(key, field, value))
  }

  def hincrby(key: String, field: String, amount: Long): Long = {
    provider.withClient(_.hincrBy(key, field, amount))
  }

  def sadd(key: String, values: String*): Option[Long] = {
    Option(provider.withClient(c => c.sadd(key, values: _*)))
  }

  def lpush(key: String, values: String*): Unit = {
    provider.withClient(c => c.lpush(key, values : _*))
  }

  def del(keys: String*): Unit = {
    provider.withClient(c => c.del(keys : _*))
  }

  def pipeline[T](pipelinedFunction: (Pipeline) => T): Unit = {
    provider.withPipeline(pipelinedFunction)
  }
}

object GedisClient {

  def apply(pool : Option[JedisSentinelPool] = None, single : Option[Jedis] = None, errorCallback: (Throwable) => Unit = (t: Throwable) => {}): GedisClient = {
    if (pool.isEmpty && single.isEmpty){
      throw new Exception("Neither sentinel pool or single Jedis defined")
    } else {
      new GedisClient(new GedisClientProvider(pool, single, errorCallback))
    }
  }

}
