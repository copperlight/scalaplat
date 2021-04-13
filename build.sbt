
lazy val scalaplat = project.in(file("."))
  .configure(BuildSettings.profile)
  .aggregate(
    `scalaplat-admin`,
    `scalaplat-akka`,
    `scalaplat-env`,
    `scalaplat-guice`,
    `scalaplat-jmh`,
    `scalaplat-json`,
    `scalaplat-launcher`,
    `scalaplat-module-admin`,
    `scalaplat-module-akka`,
    `scalaplat-module-aws`,
    `scalaplat-module-aws2`,
    `scalaplat-module-dynconfig`,
    `scalaplat-module-jmxport`,
    `scalaplat-module-userservice`,
    `scalaplat-servergroups`,
    `scalaplat-service`)
  .settings(BuildSettings.noPackaging: _*)

lazy val `scalaplat-admin` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`, `scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.inject,
    Dependencies.jacksonCore,
    Dependencies.jacksonMapper,
    Dependencies.slf4jApi
  ))

lazy val `scalaplat-akka` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-json`)
  .settings(libraryDependencies ++= Seq(
      Dependencies.akkaActor,
      Dependencies.akkaSlf4j,
      Dependencies.akkaStream,
      Dependencies.iepService,
      Dependencies.jsr250,
      Dependencies.spectatorIpc,
      Dependencies.akkaHttp,
      Dependencies.typesafeConfig,
      Dependencies.akkaHttpTestkit % Test,
      Dependencies.akkaStreamTestkit % Test,
      Dependencies.akkaTestkit % Test
  ))

lazy val `scalaplat-env` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
    Dependencies.scalaLogging,
    Dependencies.typesafeConfig,
    Dependencies.scalaLibrary % Provided,
    Dependencies.logback % Test,
    Dependencies.munit % Test
  ))

lazy val `scalaplat-guice` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.guiceCore,
    Dependencies.guiceMulti,
    Dependencies.slf4jApi,
    Dependencies.jsr250 % Test
  ))

lazy val `scalaplat-jmh` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-json`)
  .enablePlugins(pl.project13.scala.sbt.SbtJmh)

lazy val `scalaplat-json` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
      Dependencies.jacksonCore,
      Dependencies.jacksonJava8,
      Dependencies.jacksonJsr310,
      Dependencies.jacksonMapper,
      Dependencies.jacksonScala,
      Dependencies.jacksonSmile,
      Dependencies.scalaReflect,
      Dependencies.munit % Test
  ))

lazy val `scalaplat-launcher` = project
  .configure(BuildSettings.profile)

lazy val `scalaplat-module-admin` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-admin`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.guiceCore,
    Dependencies.guiceMulti,
    Dependencies.slf4jApi
  ))

lazy val `scalaplat-module-akka` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-akka`)
  .settings(libraryDependencies ++= Seq(
      Dependencies.guiceCore,
      Dependencies.guiceMulti,
      Dependencies.iepGuice
  ))

lazy val `scalaplat-module-aws` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.awsCore,
    Dependencies.awsSTS,
    Dependencies.guiceCore,
    Dependencies.reactiveStreams,
    Dependencies.rxjava2,
    Dependencies.slf4jApi,
    Dependencies.typesafeConfig,
    Dependencies.awsAutoScaling % Test,
    Dependencies.awsCache % Test,
    Dependencies.awsCloudWatch % Test,
    Dependencies.awsDynamoDB % Test,
    Dependencies.awsEC2 % Test,
    Dependencies.awsELB % Test,
    Dependencies.awsELBv2 % Test,
    Dependencies.awsEMR % Test,
    Dependencies.awsLambda % Test,
    Dependencies.awsRDS % Test,
    Dependencies.awsRoute53 % Test
  ))

lazy val `scalaplat-module-aws2` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.aws2Core,
    Dependencies.aws2EC2 % Test,
    Dependencies.aws2STS,
    Dependencies.guiceCore,
    Dependencies.slf4jApi,
    Dependencies.typesafeConfig
  ))

lazy val `scalaplat-module-dynconfig` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`, `scalaplat-module-admin`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.guiceCore,
    Dependencies.guiceMulti,
    Dependencies.slf4jApi
  ))

lazy val `scalaplat-module-jmxport` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
    Dependencies.guiceCore,
    Dependencies.slf4jApi
  ))

lazy val `scalaplat-module-userservice` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-module-admin`, `scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.caffeine,
    Dependencies.guiceCore,
    Dependencies.guiceMulti,
    Dependencies.jacksonMapper,
    Dependencies.slf4jApi,
    Dependencies.typesafeConfig
  ))

lazy val `scalaplat-servergroups` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.jacksonCore,
    Dependencies.slf4jApi,
    Dependencies.equalsVerifier % Test
  ))

lazy val `scalaplat-service` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
    Dependencies.inject,
    Dependencies.jsr250,
    Dependencies.slf4jApi
  ))
