package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisCluster, JedisSentinelPool, Pipeline}

/**
 * Base Redis client contract
 */
trait RedisClient {

  /**
   * Returns if key exists
   *
   * @param key key's value
   * @return true if the key exists, false otherwise
   * @see https://redis.io/commands/exists
   */
  def exists(key: String): Boolean

  /**
   * Get the value of key
   *
   * @param key key's value
   * @return option for existing keys, empty option otherwise
   * @see https://redis.io/commands/get
   */
  def get(key: String): Option[String]

  /**
   * Returns the values of all specified keys
   *
   * @param key list of keys
   * @return list of values
   * @see https://redis.io/commands/mget
   */
  def mget(key: String*): List[Option[String]]

  /**
   * Returns the value associated with field in the hash stored at key
   *
   * @param key   key's value
   * @param field field's name
   * @return option for existing key/field, empty option otherwise
   * @see https://redis.io/commands/hget
   */
  def hget(key: String, field: String): Option[String]

  /**
   * Returns all fields and values of the hash stored at key
   *
   * @param key key's value
   * @return map with all field names/values
   * @see https://redis.io/commands/hgetall
   */
  def hgetall(key: String): Map[String, String]

  /**
   * Returns all field names in the hash stored at key
   *
   * @param key key's value
   * @return list with all field names
   * @see https://redis.io/commands/hkeys
   */
  def hkeys(key: String): List[String]

  /**
   * Returns all the members of the set value stored at key
   *
   * @param key key's value
   * @return set with all elements
   * @see https://redis.io/commands/smembers
   */
  def smembers(key: String): Set[String]

  /**
   * Returns the specified elements of the list stored at key, based on a index range
   *
   * @param key   key's value
   * @param start list index offset start
   * @param end   list index offset end
   * @return list with all elements
   * @see https://redis.io/commands/lrange
   */
  def lrange(key: String, start: Long, end: Long): List[String]

  /**
   * Set key to hold the string value
   *
   * @param key   key's value
   * @param value value to hold
   * @see https://redis.io/commands/set
   */
  def set(key: String, value: String): Unit

  /**
   * Increments the number stored at key by increment
   *
   * @param key key's value
   * @param amount
   * @return value after increment
   * @see https://redis.io/commands/incrby
   */
  def incrby(key: String, amount: Long): Long

  /**
   * Sets field in the hash stored at key to value
   *
   * @param key   key's value
   * @param field field's name
   * @param value value to hold
   * @see https://redis.io/commands/hset
   */
  def hset(key: String, field: String, value: String): Unit

  /**
   * Sets the specified fields to their respective values in the hash stored at key
   *
   * @param key    key's value
   * @param values map to hold
   * @see https://redis.io/commands/hmset
   */
  def hmset(key: String, values: Map[String, String]): Unit

  /**
   * Sets field in the hash stored at key to value, only if field does not yet exist
   *
   * @param key   key's value
   * @param field field's name
   * @param value value to hold
   * @see https://redis.io/commands/hsetnx
   */
  def hsetnx(key: String, field: String, value: String): Unit

  /**
   * Increments the number stored at field in the hash stored at key by increment
   *
   * @param key    key's value
   * @param field  field's name
   * @param amount increment amount
   * @return value after increment
   * @see https://redis.io/commands/hincrby
   */
  def hincrby(key: String, field: String, amount: Long): Long

  /**
   * Add the specified members to the set stored at key
   *
   * @param key    key's value
   * @param values to add on set
   * @return number of elements added
   * @see https://redis.io/commands/sadd
   */
  def sadd(key: String, values: String*): Option[Long]

  /**
   * Insert all the specified values at the head of the list stored at key
   *
   * @param key    key's value
   * @param values to add on list
   * @see https://redis.io/commands/lpush
   */
  def lpush(key: String, values: String*): Unit

  /**
   * Removes the specified keys
   *
   * @param key key's value
   * @see https://redis.io/commands/del
   */
  def del(key: String*): Unit

  /**
   * Parallel command execution using Redis Pipelines
   *
   * @param f pipeline response handler
   * @tparam T command response type
   * @see https://redis.io/topics/pipelining
   */
  def pipeline[T](f: Function1[Pipeline, T]): Unit
}

/**
 * Gedis implementation
 *
 * @param provider The Redis provider
 */
class GedisClient(provider: GedisClientProvider) extends RedisClient {

  import scala.collection.JavaConverters._

  /**
   * @see [[RedisClient.exists()]]
   */
  def exists(key: String): Boolean = {
    provider.withClient(_.exists(key))
  }

  /**
   * @see [[RedisClient.get()]]
   */
  def get(key: String): Option[String] = {
    Option(provider.withClient(_.get(key)))
  }

  /**
   * @see [[RedisClient.mget()]]
   */
  def mget(key: String*): List[Option[String]] = {
    provider.withClient(_.mget(key: _*).asScala.map(item => Option(item)).toList)
  }

  /**
   * @see [[RedisClient.hget()]]
   */
  def hget(key: String, field: String): Option[String] = {
    Option(provider.withClient(_.hget(key, field)))
  }

  /**
   * @see [[RedisClient.hgetall()]]
   */
  def hgetall(key: String): Map[String, String] = {
    provider.withClient(_.hgetAll(key)).asScala.toMap
  }

  /**
   * @see [[RedisClient.hkeys()]]
   */
  def hkeys(key: String): List[String] = {
    provider.withClient(c => c.hkeys(key)).asScala.toList
  }

  /**
   * @see [[RedisClient.smembers()]]
   */
  def smembers(key: String): Set[String] = {
    provider.withClient(_.smembers(key)).asScala.toSet
  }

  /**
   * @see [[RedisClient.lrange()]]
   */
  def lrange(key: String, start: Long, end: Long): List[String] = {
    provider.withClient(_.lrange(key, start, end)).asScala.toList
  }

  /**
   * @see [[RedisClient.set()]]
   */
  def set(key: String, value: String): Unit = {
    provider.withClient(_.set(key, value))
  }

  /**
   * @see [[RedisClient.incrby()]]
   */
  def incrby(key: String, amount: Long): Long = {
    provider.withClient(_.incrBy(key, amount))
  }

  /**
   * @see [[RedisClient.hset()]]
   */
  def hset(key: String, field: String, value: String): Unit = {
    provider.withClient(_.hset(key, field, value))
  }

  /**
   * @see [[RedisClient.hmset()]]
   */
  def hmset(key: String, values: Map[String, String]): Unit = {
    provider.withClient(_.hmset(key, values.asJava))
  }

  /**
   * @see [[RedisClient.hsetnx()]]
   */
  def hsetnx(key: String, field: String, value: String): Unit = {
    provider.withClient(_.hsetnx(key, field, value))
  }

  /**
   * @see [[RedisClient.hincrby()]]
   */
  def hincrby(key: String, field: String, amount: Long): Long = {
    provider.withClient(_.hincrBy(key, field, amount))
  }

  /**
   * @see [[RedisClient.sadd()]]
   */
  def sadd(key: String, values: String*): Option[Long] = {
    Option(provider.withClient(c => c.sadd(key, values: _*)))
  }

  /**
   * @see [[RedisClient.lpush()]]
   */
  def lpush(key: String, values: String*): Unit = {
    provider.withClient(c => c.lpush(key, values: _*))
  }

  /**
   * @see [[RedisClient.del()]]
   */
  def del(keys: String*): Unit = {
    provider.withClient(c => c.del(keys: _*))
  }

  /**
   * @see [[RedisClient.pipeline()]]
   */
  def pipeline[T](pipelinedFunction: (Pipeline) => T): Unit = {
    provider.withPipeline(pipelinedFunction)
  }

  /**
   * Set a time to live in seconds on the specified key
   *
   * @param key key's value
   * @param ttl timeout
   */
  def expires(key: String, ttl: Int): Unit = {
    provider.withClient(c => c.expire(key, ttl))
  }

  /**
   * Returns the remaining time to live in seconds of a key
   *
   * @param key key's value
   * @return remaining time to live
   */
  def ttl(key: String): Long = {
    provider.withClient(c => c.ttl(key))
  }
}

/**
 * Gedis client object
 */
object GedisClient {

  /**
   * Factory for Gedis clients
   *
   * @param pool          settings for sentinel mode operation
   * @param single        settings for single mode operation
   * @param errorCallback error handler
   * @return new Gedis client
   */
  def apply(cluster : Option[JedisCluster] = None, pool : Option[JedisSentinelPool] = None, single : Option[Jedis] = None, errorCallback: (Throwable) => Unit = (t: Throwable) => {}): GedisClient = {
    if (cluster.isEmpty && pool.isEmpty && single.isEmpty){
      throw new Exception("Neither cluster, sentinel pool or single Jedis defined")
    } else {
      new GedisClient(new GedisClientProvider(new JedisClientAdapter(cluster, pool, single), errorCallback))
    }
  }

}
