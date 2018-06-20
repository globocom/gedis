package com.globo.bigdata.gedis

import redis.clients.jedis.{Jedis, JedisSentinelPool, Pipeline}

class GedisClientProvider(pool : Option[JedisSentinelPool] = None, single : Option[Jedis] = None, errorCallback: (Throwable) => Unit = (t: Throwable) => {}){

  def withClient[T](f : (Jedis) => T) : T = {
    exceptionTrap{
      if(pool.isDefined){
        useClientAndClose(pool.get.getResource){
          client => f(client)
        }
      }else{
        f(single.get)
      }
    }
  }

  def withPipeline[T](f : (Pipeline) => T) : T = {
      withClient(c => {
        usePipelineAndSync(c.pipelined()) { pipeline => f(pipeline) }
      })
  }

  private def useClientAndClose[B](client: Jedis)(code: Jedis => B): B =
    try
      code(client)
    finally
      client.close()

  private def usePipelineAndSync[B](pipeline: Pipeline)(code: Pipeline => B): B =
    try
      code(pipeline)
      catch{
        case t: Throwable => {
          throw t
        }
      }
    finally
      pipeline.sync()


  private def exceptionTrap[T](code: => T) = {
    try{
      code
    }catch {
      case t: Throwable => {
        errorCallback(t)
        throw t
      }
    }
  }
}
