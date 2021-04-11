package io.github.copperlight.scalaplat

import com.typesafe.config.Config
import io.github.copperlight.scalaplat.config.ConfigManager

object CopperlightEnvironment {

  private val NAMESPACE = "copperlight.scalaplat.env."
  private val CONFIG: Config = ConfigManager.get

  val accountEnv: String = CONFIG.getString(NAMESPACE + "account-env")
  val accountId: String = CONFIG.getString(NAMESPACE + "account-id")
  val accountName: String = CONFIG.getString(NAMESPACE + "account-name")
  val accountType: String = CONFIG.getString(NAMESPACE + "account-type")
  val ami: String = CONFIG.getString(NAMESPACE + "ami")
  val app: String = CONFIG.getString(NAMESPACE + "app")
  val asg: String = CONFIG.getString(NAMESPACE + "asg")
  val cluster: String = CONFIG.getString(NAMESPACE + "cluster")
  val domain: String = CONFIG.getString(NAMESPACE + "domain")
  val environment: String = CONFIG.getString(NAMESPACE + "environment")
  val host: String = CONFIG.getString(NAMESPACE + "host")
  val instanceId: String = CONFIG.getString(NAMESPACE + "instance-id")
  val localIp: String = CONFIG.getString(NAMESPACE + "local-ip")
  val region: String = CONFIG.getString(NAMESPACE + "region")
  val stack: String = CONFIG.getString(NAMESPACE + "stack")
  val targetAccount: String = CONFIG.getString(NAMESPACE + "target-account-id")
  val vmtype: String = CONFIG.getString(NAMESPACE + "vmtype")
  val vpcId: String = CONFIG.getString(NAMESPACE + "vpc-id")
  val zone: String = CONFIG.getString(NAMESPACE + "zone")
}
