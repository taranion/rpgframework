module de.rpgframework.core {
	exports de.rpgframework;
	exports de.rpgframework.core;
	exports de.rpgframework.addressbook;
	exports de.rpgframework.genericrpg;
	exports de.rpgframework.adventure;
	exports de.rpgframework.genericrpg.chargen;
	exports de.rpgframework.genericrpg.modification;
	exports de.rpgframework.boot;
	exports de.rpgframework.worldinfo;
	exports de.rpgframework.sound;

	uses de.rpgframework.RPGFrameworkPlugin;
	uses de.rpgframework.RPGFramework;
	uses de.rpgframework.core.CustomDataHandler;

	requires java.prefs;
	requires java.sql;
	requires java.xml;
	requires org.apache.logging.log4j;
	requires simple.persist;
	
	provides de.rpgframework.RPGFramework with org.prelle.rpgframework.RPGFrameworkImpl;
	
}