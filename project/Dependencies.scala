import sbt._

// format: off

object Dependencies {
  object Versions {
    val akka       = "2.6.10"
    val akkaHttpV  = "10.2.1"
    val aws        = "1.11.892"
    val aws2       = "2.15.20"
    val graal      = "20.2.0"
    val guice      = "4.1.0"
    val jackson    = "2.11.3"
    val log4j      = "2.13.3"
    val scala      = "2.13.4"
    val servo      = "0.13.0"
    val slf4j      = "1.7.30"
  }

  import Versions._

  val akkaActor          = "com.typesafe.akka" %% "akka-actor" % akka
  val akkaHttpCaching    = "com.typesafe.akka" %% "akka-http-caching" % akkaHttpV
  val akkaHttpCore       = "com.typesafe.akka" %% "akka-http-core" % akkaHttpV
  val akkaHttpTestkit    = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV
  val akkaSlf4j          = "com.typesafe.akka" %% "akka-slf4j" % akka
  val akkaTestkit        = "com.typesafe.akka" %% "akka-testkit" % akka
  val awsAutoScaling     = "com.amazonaws" % "aws-java-sdk-autoscaling" % aws
  val awsCache           = "com.amazonaws" % "aws-java-sdk-elasticache" % aws
  val awsCloudWatch      = "com.amazonaws" % "aws-java-sdk-cloudwatch" % aws
  val awsCore            = "com.amazonaws" % "aws-java-sdk-core" % aws
  val awsDynamoDB        = "com.amazonaws" % "aws-java-sdk-dynamodb" % aws
  val awsEC2             = "com.amazonaws" % "aws-java-sdk-ec2" % aws
  val awsELB             = "com.amazonaws" % "aws-java-sdk-elasticloadbalancing" % aws
  val awsELBv2           = "com.amazonaws" % "aws-java-sdk-elasticloadbalancingv2" % aws
  val awsEMR             = "com.amazonaws" % "aws-java-sdk-emr" % aws
  val awsLambda          = "com.amazonaws" % "aws-java-sdk-lambda" % aws
  val awsRDS             = "com.amazonaws" % "aws-java-sdk-rds" % aws
  val awsRoute53         = "com.amazonaws" % "aws-java-sdk-route53" % aws
  val awsSES             = "com.amazonaws" % "aws-java-sdk-ses" % aws
  val awsSTS             = "com.amazonaws" % "aws-java-sdk-sts" % aws
  val aws2Core           = "software.amazon.awssdk" % "core" % aws2
  val aws2DynamoDB       = "software.amazon.awssdk" % "dynamodb" % aws2
  val aws2EC2            = "software.amazon.awssdk" % "ec2" % aws2
  val aws2SES            = "software.amazon.awssdk" % "ses" % aws2
  val aws2STS            = "software.amazon.awssdk" % "sts" % aws2
  val caffeine           = "com.github.ben-manes.caffeine" % "caffeine" % "2.8.6"
  val equalsVerifier     = "nl.jqno.equalsverifier" % "equalsverifier" % "3.5"
  val frigga             = "com.netflix.frigga" % "frigga" % "0.24.0"
  val graalJs            = "org.graalvm.js" % "js" % graal
  val graalJsEngine      = "org.graalvm.js" % "js-scriptengine" % graal
  val guiceCore          = "com.google.inject" % "guice" % guice
  val guiceMulti         = "com.google.inject.extensions" % "guice-multibindings" % guice
  val inject             = "javax.inject" % "javax.inject" % "1"
  val jacksonCore        = "com.fasterxml.jackson.core" % "jackson-core" % jackson
  val jacksonMapper      = "com.fasterxml.jackson.core" % "jackson-databind" % jackson
  val jodaTime           = "joda-time" % "joda-time" % "2.10.6"
  val jsonSchema         = "com.github.java-json-tools" % "json-schema-validator" % "2.2.14"
  val jsr250             = "javax.annotation" % "jsr250-api" % "1.0"
  val junit              = "junit" % "junit" % "4.12"
  val junitInterface     = "com.novocode" % "junit-interface" % "0.11"
  val jzlib              = "com.jcraft" % "jzlib" % "1.1.3"
  val log4jApi           = "org.apache.logging.log4j" % "log4j-api" % log4j
  val log4jCore          = "org.apache.logging.log4j" % "log4j-core" % log4j
  val log4jJcl           = "org.apache.logging.log4j" % "log4j-jcl" % log4j
  val log4jJul           = "org.apache.logging.log4j" % "log4j-jul" % log4j
  val log4jSlf4j         = "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j
  val reactiveStreams    = "org.reactivestreams" % "reactive-streams" % "1.0.3"
  val rxjava             = "io.reactivex" % "rxjava" % "1.3.8"
  val rxjava2            = "io.reactivex.rxjava2" % "rxjava" % "2.2.20"
  val scalaCompiler      = "org.scala-lang" % "scala-compiler" % scala
  val scalaLibrary       = "org.scala-lang" % "scala-library" % scala
  val scalaLogging       = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  val scalaReflect       = "org.scala-lang" % "scala-reflect" % scala
  val scalatest          = "org.scalatest" %% "scalatest" % "3.2.2"
  val slf4jApi           = "org.slf4j" % "slf4j-api" % slf4j
  val slf4jLog4j         = "org.slf4j" % "slf4j-log4j12" % slf4j
  val slf4jSimple        = "org.slf4j" % "slf4j-simple" % slf4j
  val snappy             = "org.xerial.snappy" % "snappy-java" % "1.1.7.7"
  val typesafeConfig     = "com.typesafe" % "config" % "1.4.0"
}

// format: on
