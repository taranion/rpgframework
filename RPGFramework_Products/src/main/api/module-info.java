module de.rpgframework.products {
	exports de.rpgframework.products;
	exports org.prelle.rpgframework.products to simple.persist;
	opens org.prelle.rpgframework.products to simple.persist;
	
	uses de.rpgframework.products.ProductDataPlugin;
	provides de.rpgframework.RPGFrameworkPlugin with org.prelle.rpgframework.products.ProductDataFrameworkPlugin;

	requires transitive de.rpgframework.core;
	requires simple.persist;
	requires org.apache.logging.log4j;
	requires java.xml;

}