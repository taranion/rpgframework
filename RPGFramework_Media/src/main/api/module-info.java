module de.rpgframework.media {
	exports de.rpgframework.media.map;
	exports de.rpgframework.media;
	exports de.rpgframework.music;
	exports de.rpgframework.devices;
	exports de.rpgframework.support.combat;
	exports de.rpgframework.support.combat.map;
	exports de.rpgframework.session;

	requires java.prefs;
	requires java.sql;
	requires java.xml;
	requires org.apache.logging.log4j;
	requires transitive de.rpgframework.core;
	requires transitive de.rpgframework.products;
	
}