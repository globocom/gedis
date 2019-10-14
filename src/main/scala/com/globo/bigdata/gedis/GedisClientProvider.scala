package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisSentinelPool, Pipeline}

/**
 * Gedis client provider
 *
 * Delegates operations to some underline Redis client
 *
 * @param pool          sentinel client
 * @param single        jedis client
 * @param errorCallback error callback handler
 */
class GedisClientProvider(pool: Option[JedisSentinelPool] = None, single: Option[Jedis] = None, errorCallback: (Throwable) => Unit = (t: Throwable) => {}) {

  /**
   * Recover valid Redis client then pass it to a handler function
   *
   * @param f handler
   * @tparam T handler result type
   * @return handler result
   */
  def withClient[T](f: (Jedis) => T): T = {
    exceptionTrap {
      if (pool.isDefined) {
        useClientAndClose(pool.get.getResource) {
          client => f(client)
        }
      } else {
        f(single.get)
      }
    }
  }

  /**
   * Create a Redis pipeline from a valid client, then pass it to a handler function
   *
   * @param f handler
   * @tparam T handler return type
   * @return handler result
   */
  def withPipeline[T](f: (Pipeline) => T): T = {
    withClient(c => {
      usePipelineAndSync(c.pipelined()) { pipeline => f(pipeline) }
    })
  }

  /**
   * Call command passing a valid client, then close the client
   *
   * @param client Redis client
   * @param code command handler to run
   * @tparam B command result type
   * @return command result
   */
  private def useClientAndClose[B](client: Jedis)(code: Jedis => B): B =
    try
      code(client)
    finally
      client.close()

  /**
   * Call command passing a pipeline, then wait for response (sync the pipeline)
   *
   * @param pipeline
   * @param code
   * @tparam B
   * @return
   */
  private def usePipelineAndSync[B](pipeline: Pipeline)(code: Pipeline => B): B =
    try
      code(pipeline)
    catch {
      case t: Throwable => {
        throw t
      }
    }
    finally
      pipeline.sync()

  /**
   * Wrap function/command exception and redirect to predefined error handler
   *
   * @param code function/command handler
   * @tparam T function/command result type
   * @return function/command result
   */
  private def exceptionTrap[T](code: => T) = {
    try {
      code
    } catch {
      case t: Throwable => {
        errorCallback(t)
        throw t
      }
    }
  }
}
