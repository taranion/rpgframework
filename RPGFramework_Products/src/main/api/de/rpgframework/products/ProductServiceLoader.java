/**
 *
 */
package de.rpgframework.products;

/**
 * @author prelle
 *
 */
public class ProductServiceLoader {
	
	private static ProductService instance;

	//--------------------------------------------------------------------
	public static ProductService getInstance() {
			return instance;
	}

	//--------------------------------------------------------------------
	public static void registerInstance(ProductService service) {
		instance = service;
	}

}
