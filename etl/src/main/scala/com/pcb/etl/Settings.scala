package com.pcb.etl

import akka.actor.{ActorSystem, Extension, ExtensionId, ExtensionIdProvider, ExtendedActorSystem}
import com.typesafe.config.Config
     
class SettingsImpl(config: Config) extends Extension {
  val dataPath: String = config.getString("etl.data.path")
  val directory: String = config.getString("etl.sources.directory")
  val industryFile: String = config.getString("etl.sources.industry")
}

object Settings extends ExtensionId[SettingsImpl] with ExtensionIdProvider {
     
  override def lookup = Settings
     
  override def createExtension(system: ExtendedActorSystem) =
    new SettingsImpl(system.settings.config)
     
  /**
   * Java API: retrieve the Settings extension for the given system.
   */
  override def get(system: ActorSystem): SettingsImpl = super.get(system)
}


