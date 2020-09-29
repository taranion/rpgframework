module de.rpgframework.print {
	exports de.rpgframework.print;
	
	opens de.rpgframework.print to simple.persist;

	requires de.rpgframework.core;
	requires simple.persist;
	requires org.apache.logging.log4j;

}