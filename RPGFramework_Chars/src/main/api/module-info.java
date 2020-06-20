module de.rpgframework.chars {
	exports de.rpgframework.character;
	exports de.rpgframework.print;
	exports de.rpgframework.genericrpg.data;

	uses de.rpgframework.character.RulePlugin;
	requires transitive de.rpgframework.core;
	
	requires org.apache.logging.log4j;
	requires simple.persist;
	requires java.desktop;
	
	provides de.rpgframework.RPGFrameworkPlugin with org.prelle.rpgframework.character.PluginRoleplayingSystems;

	opens org.prelle.rpgframework.character to simple.persist;
	opens org.prelle.rpgframework.print to simple.persist;
	
}