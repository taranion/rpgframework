module de.rpgframework.chars {
	exports de.rpgframework.character;
	exports de.rpgframework.print;

	uses de.rpgframework.character.RulePlugin;
	requires transitive de.rpgframework.core;
	
	requires org.apache.logging.log4j;
	requires simple.persist;
	
	provides de.rpgframework.RPGFrameworkPlugin with org.prelle.rpgframework.character.PluginRoleplayingSystems;

}