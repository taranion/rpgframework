module de.rpgframework.chars {
	exports de.rpgframework.character;
	exports de.rpgframework.print;

	uses de.rpgframework.character.RulePlugin;
	uses de.rpgframework.products.ProductDataPlugin;
	requires transitive de.rpgframework.core;
	
	requires org.apache.logging.log4j;
	requires simple.persist;
	requires de.rpgframework.products;
	
	provides de.rpgframework.RPGFrameworkPlugin with org.prelle.rpgframework.character.PluginRoleplayingSystems;

	opens org.prelle.rpgframework.character to simple.persist;
	
}