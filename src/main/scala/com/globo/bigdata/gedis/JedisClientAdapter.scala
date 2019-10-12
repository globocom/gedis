package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisCluster, JedisSentinelPool, Pipeline}

class JedisClientAdapter(cluster: Option[JedisCluster] = None, pool: Option[JedisSentinelPool] = None, single: Option[Jedis] = None) {

  /**
   * Returns if key exists
   *
   * @param key key's value
   * @return true if the key exists, false otherwise
   * @see https://redis.io/commands/exists
   */
  def exists(key: String): Boolean = {
    if (cluster.isDefined)
      cluster.get.exists(key)
    else if (pool.isDefined)
      pool.get.getResource.exists(key)
    else
      single.get.exists(key)
  }

  /**
   * Get the value of key
   *
   * @param key key's value
   * @return option for existing keys, empty option otherwise
   * @see https://redis.io/commands/get
   */
  def get(key: String): String = {
    if (cluster.isDefined)
      cluster.get.get(key)
    else if (pool.isDefined)
      pool.get.getResource.get(key)
    else
      single.get.get(key)
  }

  /**
   * Returns the values of all specified keys
   *
   * @param key list of keys
   * @return list of values
   * @see https://redis.io/commands/mget
   */
  def mget(key: String*): java.util.List[String] = {
    if (cluster.isDefined)
      cluster.get.mget(key: _*)
    else if (pool.isDefined)
      pool.get.getResource.mget(key: _*)
    else
      single.get.mget(key: _*)
  }

  /**
   * Returns the value associated with field in the hash stored at key
   *
   * @param key   key's value
   * @param field field's name
   * @return option for existing key/field, empty option otherwise
   * @see https://redis.io/commands/hget
   */
  def hget(key: String, field: String): String = {
    if (cluster.isDefined)
      cluster.get.hget(key, field)
    else if (pool.isDefined)
      pool.get.getResource.hget(key, field)
    else
      single.get.hget(key, field)
  }

  /**
   * Returns all fields and values of the hash stored at key
   *
   * @param key key's value
   * @return map with all field names/values
   * @see https://redis.io/commands/hgetall
   */
  def hgetAll(key: String): java.util.Map[String, String] = {
    if (cluster.isDefined)
      cluster.get.hgetAll(key)
    else if (pool.isDefined)
      pool.get.getResource.hgetAll(key)
    else
      single.get.hgetAll(key)
  }

  /**
   * Returns all field names in the hash stored at key
   *
   * @param key key's value
   * @return list with all field names
   * @see https://redis.io/commands/hkeys
   */
  def hkeys(key: String): java.util.Set[String] = {
    if (cluster.isDefined)
      cluster.get.hkeys(key)
    else if (pool.isDefined)
      pool.get.getResource.hkeys(key)
    else
      single.get.hkeys(key)
  }

  /**
   * Returns all the members of the set value stored at key
   *
   * @param key key's value
   * @return set with all elements
   * @see https://redis.io/commands/smembers
   */
  def smembers(key: String): java.util.Set[String] = {
    if (cluster.isDefined)
      cluster.get.smembers(key)
    else if (pool.isDefined)
      pool.get.getResource.smembers(key)
    else
      single.get.smembers(key)
  }

  /**
   * Returns the specified elements of the list stored at key, based on a index range
   *
   * @param key   key's value
   * @param start list index offset start
   * @param end   list index offset end
   * @return list with all elements
   * @see https://redis.io/commands/lrange
   */
  def lrange(key: String, start: Long, end: Long): java.util.List[String] = {
    if (cluster.isDefined)
      cluster.get.lrange(key, start, end)
    else if (pool.isDefined)
      pool.get.getResource.lrange(key, start, end)
    else
      single.get.lrange(key, start, end)
  }

  /**
   * Set key to hold the string value
   *
   * @param key   key's value
   * @param value value to hold
   * @see https://redis.io/commands/set
   */
  def set(key: String, value: String): Unit = {
    if (cluster.isDefined)
      cluster.get.set(key, value)
    else if (pool.isDefined)
      pool.get.getResource.set(key, value)
    else
      single.get.set(key, value)
  }

  /**
   * Increments the number stored at key by increment
   *
   * @param key key's value
   * @param amount
   * @return value after increment
   * @see https://redis.io/commands/incrby
   */
  def incrBy(key: String, amount: Long): Long = {
    if (cluster.isDefined)
      cluster.get.incrBy(key, amount)
    else if (pool.isDefined)
      pool.get.getResource.incrBy(key, amount)
    else
      single.get.incrBy(key, amount)
  }

  /**
   * Sets field in the hash stored at key to value
   *
   * @param key   key's value
   * @param field field's name
   * @param value value to hold
   * @see https://redis.io/commands/hset
   */
  def hset(key: String, field: String, value: String): Unit = {
    if (cluster.isDefined)
      cluster.get.hset(key, field, value)
    else if (pool.isDefined)
      pool.get.getResource.hset(key, field, value)
    else
      single.get.hset(key, field, value)
  }

  /**
   * Sets the specified fields to their respective values in the hash stored at key
   *
   * @param key    key's value
   * @param values map to hold
   * @see https://redis.io/commands/hmset
   */
  def hmset(key: String, values: java.util.Map[String, String]): Unit = {
    if (cluster.isDefined)
      cluster.get.hmset(key, values)
    else if (pool.isDefined)
      pool.get.getResource.hmset(key, values)
    else
      single.get.hmset(key, values)
  }

  /**
   * Sets field in the hash stored at key to value, only if field does not yet exist
   *
   * @param key   key's value
   * @param field field's name
   * @param value value to hold
   * @see https://redis.io/commands/hsetnx
   */
  def hsetnx(key: String, field: String, value: String): Unit = {
    if (cluster.isDefined)
      cluster.get.hsetnx(key, field, value)
    else if (pool.isDefined)
      pool.get.getResource.hsetnx(key, field, value)
    else
      single.get.hsetnx(key, field, value)
  }

  /**
   * Increments the number stored at field in the hash stored at key by increment
   *
   * @param key    key's value
   * @param field  field's name
   * @param amount increment amount
   * @return value after increment
   * @see https://redis.io/commands/hincrby
   */
  def hincrBy(key: String, field: String, amount: Long): Long = {
    if (cluster.isDefined)
      cluster.get.hincrBy(key, field, amount)
    else if (pool.isDefined)
      pool.get.getResource.hincrBy(key, field, amount)
    else
      single.get.hincrBy(key, field, amount)
  }

  /**
   * Add the specified members to the set stored at key
   *
   * @param key    key's value
   * @param values to add on set
   * @return number of elements added
   * @see https://redis.io/commands/sadd
   */
  def sadd(key: String, values: String*): Long = {
    if (cluster.isDefined)
      cluster.get.sadd(key, values:_*)
    else if (pool.isDefined)
      pool.get.getResource.sadd(key, values:_*)
    else
      single.get.sadd(key, values:_*)
  }

  /**
   * Insert all the specified values at the head of the list stored at key
   *
   * @param key    key's value
   * @param values to add on list
   * @see https://redis.io/commands/lpush
   */
  def lpush(key: String, values: String*): Unit = {
    if (cluster.isDefined)
      cluster.get.lpush(key, values:_*)
    else if (pool.isDefined)
      pool.get.getResource.lpush(key, values:_*)
    else
      single.get.lpush(key, values:_*)
  }

  /**
   * Removes the specified keys
   *
   * @param key key's value
   * @see https://redis.io/commands/del
   */
  def del(key: String*): Unit = {
    if (cluster.isDefined)
      cluster.get.del(key:_*)
    else if (pool.isDefined)
      pool.get.getResource.del(key:_*)
    else
      single.get.del(key:_*)
  }

  /**
   * Parallel command execution using Redis Pipelines
   *
   * @see https://redis.io/topics/pipelining
   */
  def pipelined(): Pipeline = {
    val pipeline = new Pipeline()

    if (cluster.isDefined)
      pipeline.setClient(cluster.get.getConnectionFromSlot(0).getClient)
    else if (pool.isDefined)
      pipeline.setClient(pool.get.getResource.getClient)
    else
      pipeline.setClient(single.get.getClient)

    pipeline
  }

  /**
   * Close client connection
   */
  def close(): Unit = {
    if (cluster.isDefined)
      cluster.get.close()
    else if (pool.isDefined)
      pool.get.getResource.close()
    else
      single.get.close()
  }

  /**
   * Set key expiration time
   *
   * @param key key's value
   * @param ttl key's time to live
   * @see https://redis.io/commands/expire
   */
  def expire(key: String, ttl: Int): Unit = {
    if (cluster.isDefined)
      cluster.get.expire(key, ttl)
    else if (pool.isDefined)
      pool.get.getResource.expire(key, ttl)
    else
      single.get.expire(key, ttl)
  }

  /**
   * Recover key expiration time
   *
   * @param key key's value
   * @see https://redis.io/commands/ttl
   */
  def ttl(key: String): Long = {
    if (cluster.isDefined)
      cluster.get.ttl(key)
    else if (pool.isDefined)
      pool.get.getResource.ttl(key)
    else
      single.get.ttl(key)
  }

}
