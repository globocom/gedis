package com.globo.bigdata.gedis

import redis.clients.jedis.Pipeline
import utils.IntegrationSpec

/**
 * Integrations tests for [[GedisClient]]
 * @see [[IntegrationSpec]]
 */
class GedisClientITSpec extends IntegrationSpec {

  describe("enhanced-client-integrated-with-embedded-redis"){

    it("exists-key") {
      enhancedClient().set("key", "persisted-value")
      enhancedClient().exists("key") should be(true)
    }

    it("exists-key-not") {
      enhancedClient().exists("key") should be(false)
    }

    it("get-none-for-key") {
      enhancedClient().get("key") should be(None)
    }

    it("get-value-for-key") {
      enhancedClient().set("key", "persisted-value")
      enhancedClient().get("key") should be(Some("persisted-value"))
    }

    it("mget-none-for-key") {
      enhancedClient().mget("key").head should be(None)
    }

    it("mget-value-for-key") {
      enhancedClient().set("key", "persisted-value")
      enhancedClient().set("key2", "persisted-value2")
      val values = enhancedClient().mget("key", "key2")
      values.head should be(Some("persisted-value"))
      values(1) should be(Some("persisted-value2"))

    }

    it("hget-value-for-key") {
      enhancedClient().hset("hkey", "f1", "v")
      enhancedClient().hget("hkey", "f1") should be(Some("v"))
    }

    it("hget-none-for-key") {
      enhancedClient().hget("hkey", "f1") should be(None)
    }

    it("hgetall-value-for-key") {
      enhancedClient().hmset("hkey", Map("f1" -> "v1", "f2" -> "v2"))
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v1", "f2" -> "v2"))
    }

    it("hgetall-none-for-key") {
      enhancedClient().hgetall("hkey") should be(Map())
    }

    it("hkeys-data-for-key"){
      enhancedClient().hmset("hmkey", Map("f1" -> "v", "f2" -> "vv"))
      enhancedClient().hkeys("hmkey").sorted should be(List("f1", "f2"))
    }

    it("hkeys-none-for-key"){
      enhancedClient().hkeys("hmkey").sorted should be(List())
    }

    it("smembers-data-for-key"){
      enhancedClient().sadd("skey", "alfa", "omega")
      enhancedClient().smembers("skey") should be(Set("alfa", "omega"))
    }

    it("smembers-none-for-key"){
      enhancedClient().smembers("skey") should be(Set())
    }

    it("lrange-data-for-key") {
      enhancedClient().lpush("lkey", "third", "second", "first")
      enhancedClient().lrange("lkey", 0, 0) should be(List("first"))
      enhancedClient().lrange("lkey", 0, 1) should be(List("first", "second"))
      enhancedClient().lrange("lkey", 0, 2) should be(List("first", "second", "third"))
    }

    it("lrange-none-for-key") {
      enhancedClient().lrange("lkey", 0, 0) should be(List())
    }

    it("set-value-for-key") {
      enhancedClient().set("key", "persisted-value")
      enhancedClient().get("key") should be(Some("persisted-value"))

      enhancedClient().set("key", "persisted-value-2")
      enhancedClient().get("key") should be(Some("persisted-value-2"))
    }

    it("incrby-value-for-key") {
      enhancedClient().incrby("key", 1)
      enhancedClient().get("key") should be(Some("1"))

      enhancedClient().incrby("key", 6)
      enhancedClient().get("key") should be(Some("7"))
    }

    it("hset-value-for-key-field"){
      enhancedClient().hset("hkey", "f1", "v")
      enhancedClient().hget("hkey", "f1") should be(Some("v"))

      enhancedClient().hset("hkey", "f1", "v2")
      enhancedClient().hget("hkey", "f1") should be(Some("v2"))
    }

    it("hmset-value-for-key-field"){
      enhancedClient().hmset("hkey", Map("f1" -> "v", "f2" -> "vv"))
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v", "f2" -> "vv"))

      enhancedClient().hmset("hkey", Map("f1" -> "v1", "f2" -> "vv2", "f3" -> "vvv3"))
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v1", "f2" -> "vv2", "f3" -> "vvv3"))
    }

    it("hsetnx-value-for-key-field"){
      enhancedClient().hmset("hkey", Map("f1" -> "v", "f2" -> "vv"))
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v", "f2" -> "vv"))

      enhancedClient().hsetnx("hkey", "f1", "v3-000")
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v", "f2" -> "vv"))

      enhancedClient().hsetnx("hkey", "f3", "v3-000")
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "v", "f2" -> "vv", "f3" -> "v3-000"))
    }

    it("hincrby-value-for-key-field"){
      enhancedClient().hincrby("hkey", "f1", 1)
      enhancedClient().hincrby("hkey", "f2", 1)
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "1", "f2" -> "1"))

      enhancedClient().hincrby("hkey", "f1", 10)
      enhancedClient().hincrby("hkey", "f2", 1)
      enhancedClient().hgetall("hkey") should be(Map("f1" -> "11", "f2" -> "2"))
    }

    it("sadd-value-for-key-field"){
      enhancedClient().sadd("skey", "v1", "v2", "v3")
      enhancedClient().smembers("skey") should be(Set("v1", "v2", "v3"))

      enhancedClient().sadd("skey", "v1", "v2", "v3", "v4")
      enhancedClient().smembers("skey") should be(Set("v1", "v2", "v3", "v4"))
    }

    it("lpush-data-for-key"){
      enhancedClient().lpush("lkey", "a1", "a2", "a3")
      enhancedClient().lrange("lkey", 0, 2).sorted should be(List("a1", "a2", "a3"))

      enhancedClient().lpush("lkey", "a1", "a2", "a3", "a4")
      enhancedClient().lrange("lkey", 0, 3).sorted should be(List("a1", "a2", "a3", "a4"))
    }

    it("del-key-value"){
      enhancedClient().set("key", "value")
      enhancedClient().exists("key") should be (true)

      enhancedClient().del("key")
      enhancedClient().exists("key") should be (false)
    }

    it("pipeline-commands-ok"){

      val my_pipeline : (Pipeline) => Unit = (p) => {
        p.set("pipekey", "val")
        p.incr("keyincr")
        p.incr("keyincr")
      }

      enhancedClient().pipeline(my_pipeline)

      enhancedClient().get("pipekey") should be(Some("val"))
      enhancedClient().get("keyincr") should be(Some("2"))
    }

    it("set-key-ttl"){
      enhancedClient().set("key-with-ttl", "val")
      enhancedClient().set("key-without-ttl", "val")
      enhancedClient().expires("key-with-ttl",10)

      enhancedClient().ttl("key-with-ttl") should be (10L)
      enhancedClient().ttl("nonexistent-key") should be (-2L)
      enhancedClient().ttl("key-without-ttl") should be (-1L)
    }

  }

}
