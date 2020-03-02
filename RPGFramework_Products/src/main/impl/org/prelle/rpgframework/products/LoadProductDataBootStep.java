package org.prelle.rpgframework.products;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigOption;
import de.rpgframework.PluginDescriptor;
import de.rpgframework.PluginRegistry;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.boot.BootStep;
import de.rpgframework.products.ProductDataPlugin;

/**
 * @author Stefan
 *
 */
public class LoadProductDataBootStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework.products");

	private ProductServiceImpl service;
	
	//-------------------------------------------------------------------
	public LoadProductDataBootStep(ProductServiceImpl service) {
		this.service = service;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "LOAD_PRODUCTDATA";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 20;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return false;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getConfiguration()
	 */
	@Override
	public List<ConfigOption<?>> getConfiguration() {
		List<ConfigOption<?>> ret = new ArrayList<ConfigOption<?>>();
		return ret;
	}

	//-------------------------------------------------------------------
	public List<ProductDataPlugin> loadPlugin(Path jarFile) {
		List<ProductDataPlugin> plugins = new ArrayList<>();
		try {
			ClassLoader loader = URLClassLoader.newInstance(new URL[]{jarFile.toUri().toURL()}, PluginRegistry.class.getClassLoader());
			logger.debug(" search for plugins in "+jarFile);
			ServiceLoader.load(ProductDataPlugin.class, loader).forEach(plugin -> {
				Package pack = plugin.getClass().getPackage();
				logger.debug("Found plugin "+plugin.getClass());
				logger.debug("  Implementor: "+pack.getImplementationVendor()+"   Version: "+pack.getImplementationVersion());

				plugins.add(plugin);

			});
		} catch (Throwable e) {
			logger.fatal("Failed loading plugin(s) from "+jarFile,e);
		}

		return plugins;
	}

	//-------------------------------------------------------------------
	private List<ProductDataPlugin> loadPluginFromClassPath() {
		List<ProductDataPlugin> plugins = new ArrayList<>();
		try {
			logger.debug(" search for plugins in classpath");
			ServiceLoader.load(ProductDataPlugin.class).forEach(plugin -> {
				Package pack = plugin.getClass().getPackage();
				logger.debug("Found plugin "+plugin.getClass());
				logger.debug("  Implementor: "+pack.getImplementationVendor()+"   Version: "+pack.getImplementationVersion());

				plugins.add(plugin);
			});
		} catch (Throwable e) {
			logger.fatal("Failed loading plugin(s) from classpath",e);
		}

		return plugins;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute()
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		logger.info("START----------Load product data------------------------");
		
		logger.debug("  load instances of "+ProductDataPlugin.class);
		PluginRegistry registry = RPGFrameworkLoader.getInstance().getPluginRegistry();
		List<PluginDescriptor> installed = registry.getKnownPlugins();
		
		// Add local plugins to classpath
		for (PluginDescriptor pluginDesc : installed) {
			int loaded = 0;
			try {
				for (ProductDataPlugin plugin : loadPlugin(pluginDesc.localFile)) {
					logger.info("  Product Data Plugin '"+pluginDesc.name+"' ");
					plugin.initialize(RPGFrameworkLoader.getInstance(), service);
					loaded++;
				}
			} catch (Exception e) {
				logger.error("Failed loading plugins from "+pluginDesc.localFile,e);
			}
			logger.debug("Loaded "+loaded+" plugins from "+pluginDesc.localFile);
		}
		
		// Add plugins from classpath to registry
		int loaded = 0;
		for (ProductDataPlugin plugin : loadPluginFromClassPath()) {
			plugin.initialize(RPGFrameworkLoader.getInstance(), service);
			loaded++;
		}
		logger.info("Loaded "+loaded+" plugins from classpath");
		
		logger.debug("STOP ----------Load product data-------------------------");
		// TODO Auto-generated method stub
		callback.progressChanged(1.0);
		return true;
	}

}
