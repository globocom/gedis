package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisCluster, JedisSentinelPool, Pipeline}

/**
 * Gedis client provider
 *
 * Delegates operations to some underline Redis client
 *
 * @param adapter       client adapter
 * @param errorCallback error callback handler
 */
class GedisClientProvider(adapter: JedisClientAdapter, errorCallback: (Throwable) => Unit = (t: Throwable) => {}) {

  /**
   * Recover valid Redis client then pass it to a handler function
   *
   * @param f handler
   * @tparam T handler result type
   * @return handler result
   */
  def withClient[T](f: (JedisClientAdapter) => T): T = {
    try {
      f(adapter)
    } catch {
      case t: Throwable => {
        errorCallback(t)
        throw t
      }
    } finally {
      adapter.close()
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

}
