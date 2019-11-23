/**
 *
 */
package de.rpgframework.products;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.products.ProductServiceImpl;

import de.rpgframework.RPGFramework;

/**
 * @author prelle
 *
 */
public class ProductDataPlugin {

	protected final static Logger logger = LogManager.getLogger("babylon.products");

	//-------------------------------------------------------------------
	public ProductDataPlugin() {
	}

	//-------------------------------------------------------------------
	public void initialize(RPGFramework framework, ProductServiceImpl prodServ) {
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
//		try {
//			url = new URL(url, getClass().getPackage().getName().replace(".", "/"));
//		} catch (MalformedURLException e1) {
//			logger.error("Malformed URL: "+e1);
//		}
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			logger.error("Cannot convert URL to URI: "+url+"  :  "+e);
			return;
		}

		try {
			/*
			 * Depending on whether to load from JAR file or directory
			 */
//			logger.fatal("1 "+url);
			if (url.getProtocol().equals("file") && !url.toString().endsWith(".jar")) {
				// For local execution within IDE
				try {
					url = new URL(url.toExternalForm()+getClass().getPackage().getName().replace(".", "/"));
				} catch (MalformedURLException e1) {
					logger.error("Malformed URL: "+e1);
				}
				logger.debug("Load product data from directory "+url.getFile());
				Path base = Path.of(url.toURI());
				prodServ.loadData(base, url.getFile());
			} else if (url.getProtocol().equals("file") && url.toString().endsWith(".jar")) {
				logger.debug("Load product data from JAR file "+url.toExternalForm());
				Path jarPath = Paths.get(url.toURI());
				FileSystem jarFS = FileSystems.newFileSystem(jarPath, null);
				Path base = jarFS.getPath("/"+getClass().getPackage().getName().replace(".", "/"));
				try {
					url = new URL(url, getClass().getPackage().getName().replace(".", "/"));
				} catch (MalformedURLException e1) {
					logger.error("Malformed URL: "+e1);
				}
				prodServ.loadData(base, url.getFile());
			} else if (url.getProtocol().equals("jrt")) {
				logger.debug("Load product data from Java Runtime Image "+url.toExternalForm());
				try {
					url = new URL(url, getClass().getPackage().getName().replace(".", "/"));
				} catch (MalformedURLException e1) {
					logger.error("Malformed URL: "+e1);
				}
				Path path = Path.of(url.toURI());
				FileSystem jrtFS = path.getFileSystem();
				Path base = jrtFS.getPath("/modules/"+getClass().getModule().getName()+"/"+url.getPath());
				logger.debug("Load product data from Java Runtime Image: "+base);
				prodServ.loadData(base, url.getFile());
			} else {
				FileSystem fs = FileSystems.getFileSystem(uri);
				logger.info("fs = "+fs);
			}
		} catch (Exception e) {
			logger.error("Cannot open product location "+uri,e);
		}
	}

}
