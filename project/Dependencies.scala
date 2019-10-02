import sbt._
object Dependencies {

  val projectDependencies = Seq(
    "redis.clients" % "jedis" % "3.1.0"
  )

  val testsDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1",
    "org.mockito" % "mockito-core" % "2.28.2",
    "junit" % "junit" % "4.12",
    "com.github.sebruck" %% "scalatest-embedded-redis" % "0.2.0"
  )

  val rootDependencies = projectDependencies ++ testsDependencies.map(_ % Test)
}
