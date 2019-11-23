module de.rpgframework.javafx {
	exports org.prelle.rpgframework.jfx;

	requires transitive javafx.extensions;
	requires javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires transitive de.rpgframework.core;
	requires transitive de.rpgframework.chars;
	requires transitive de.rpgframework.products;
	requires org.apache.logging.log4j;
	requires javafx.fxml;
}