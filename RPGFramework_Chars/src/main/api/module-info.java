module de.rpgframework.chars {
	exports de.rpgframework.character;
	exports de.rpgframework.print;

	requires transitive de.rpgframework.core;
	
	requires org.apache.logging.log4j;
	
	provides de.rpgframework.RPGFrameworkPlugin with org.prelle.rpgframework.character.PluginRoleplayingSystems;

}