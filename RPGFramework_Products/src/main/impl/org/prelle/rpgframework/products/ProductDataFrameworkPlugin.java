package org.prelle.rpgframework.products;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.RPGFrameworkLoader.FunctionType;
import de.rpgframework.RPGFrameworkPlugin;
import de.rpgframework.boot.StandardBootSteps;
import de.rpgframework.products.ProductServiceLoader;

/**
 * @author prelle
 *
 */
public class ProductDataFrameworkPlugin implements RPGFrameworkPlugin {
	
	private final static Logger logger = LogManager.getLogger("babylon.products");
	
	private ProductServiceImpl prodServ;

	@Override
	public FunctionType getType() {
		return FunctionType.CHARACTERS_AND_RULES;
	}

	@Override
	public void initialize(RPGFramework framework) {
		logger.debug("START: initialize");
		try {
			prodServ = new ProductServiceImpl(framework.getConfiguration());
			logger.debug("Set product service");
			ProductServiceLoader.registerInstance(prodServ);
			
			// Add steps
			LoadProductDataBootStep updater = new LoadProductDataBootStep(prodServ);
			RPGFrameworkLoader.getInstance().addStepDefinition(StandardBootSteps.PRODUCT_DATA, updater);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.fatal("Failed initializing product data",e);
		} finally {
			logger.debug("STOP : initialize");
		}
	}

}
