
lazy val scalaplat = project.in(file("."))
  .configure(BuildSettings.profile)
  .aggregate(
    `scalaplat-admin`,
    `scalaplat-guice`,
    `scalaplat-launcher`,
    `scalaplat-module-admin`,
    `scalaplat-module-aws`,
    `scalaplat-module-aws2`,
    `scalaplat-module-dynconfig`,
    `scalaplat-module-jmxport`,
    `scalaplat-module-userservice`,
    `scalaplat-env`,
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

lazy val `scalaplat-guice` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.guiceCore,
    Dependencies.guiceMulti,
    Dependencies.slf4jApi,
    Dependencies.jsr250 % "test"
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

lazy val `scalaplat-module-aws` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.awsCore,
    Dependencies.awsAutoScaling % "test",
    Dependencies.awsCache % "test",
    Dependencies.awsCloudWatch % "test",
    Dependencies.awsDynamoDB % "test",
    Dependencies.awsEC2 % "test",
    Dependencies.awsELB % "test",
    Dependencies.awsELBv2 % "test",
    Dependencies.awsEMR % "test",
    Dependencies.awsLambda % "test",
    Dependencies.awsRDS % "test",
    Dependencies.awsRoute53 % "test",
    Dependencies.awsSTS,
    Dependencies.guiceCore,
    Dependencies.reactiveStreams,
    Dependencies.rxjava2,
    Dependencies.slf4jApi,
    Dependencies.typesafeConfig
  ))

lazy val `scalaplat-module-aws2` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-env`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.aws2Core,
    Dependencies.aws2EC2 % "test",
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

lazy val `scalaplat-env` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
    Dependencies.slf4jApi,
    Dependencies.typesafeConfig
  ))

lazy val `scalaplat-servergroups` = project
  .configure(BuildSettings.profile)
  .dependsOn(`scalaplat-service`)
  .settings(libraryDependencies ++= Seq(
    Dependencies.jacksonCore,
    Dependencies.slf4jApi,
    Dependencies.equalsVerifier % "test"
  ))

lazy val `scalaplat-service` = project
  .configure(BuildSettings.profile)
  .settings(libraryDependencies ++= Seq(
    Dependencies.inject,
    Dependencies.jsr250,
    Dependencies.slf4jApi
  ))
