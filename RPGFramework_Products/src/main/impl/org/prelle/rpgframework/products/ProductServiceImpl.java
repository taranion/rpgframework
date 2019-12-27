/**
 *
 */
package org.prelle.rpgframework.products;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Persister;
import org.prelle.simplepersist.Serializer;

import de.rpgframework.ConfigContainer;
import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.EventBus;
import de.rpgframework.core.EventType;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.AdventureStory;
import de.rpgframework.products.Product;
import de.rpgframework.products.ProductService;

/**
 * @author Stefan
 *
 */
public class ProductServiceImpl implements ProductService {

	private final static Logger logger = LogManager.getLogger("rpgframework.products");

	private final static String DIRNAME_PLUGINS = "data";
	private final static String DIRNAME_CUSTOM  = "productData";

	private final static String DIRNAME_STORIES    = "advstories";
	private final static String DIRNAME_ADVENTURES = "adventures";
	private final static String DIRNAME_PRODUCTS   = "products";
	private final static String FILENAME_ADVENTURE = "adventure.xml";
	private final static String FILENAME_PRODUCT   = "product.xml";
	private final static String FILENAME_COVER     = "cover.img";

	private Path pluginDir;
	/**
	 * root directory for all data of this service. Contains subdirectories for
	 * each roleplaying system
	 */
	private Path customDir;

	private Map<String,AdventureStory> stories;
	private Map<RoleplayingSystem,Map<String,Adventure>> adventures;
	private Map<RoleplayingSystem,Map<String,Product>> products;

	private Serializer marshaller;

	//--------------------------------------------------------------------
	public ProductServiceImpl(ConfigContainer configRoot) throws IOException {
		marshaller = new Persister();
		stories    = new HashMap<>();
		adventures = new HashMap<>();
		products   = new HashMap<>();

		String dataDir = configRoot.getOption(RPGFramework.PROP_DATADIR).getStringValue();
		customDir = FileSystems.getDefault().getPath(dataDir, DIRNAME_CUSTOM);
		pluginDir = FileSystems.getDefault().getPath(dataDir, DIRNAME_PLUGINS);

		// Ensure that directory exists
		try {
			Files.createDirectories(customDir);
			Files.createDirectories(pluginDir);
		} catch (IOException e) {
			logger.fatal("Could not product data directory: "+e);
			throw e;
		}

	}

	//--------------------------------------------------------------------
	public void initialize() {
		if (RPGFrameworkLoader.getCallback()!=null) RPGFrameworkLoader.getCallback().message("Load product data");
		try {
//			loadData(customDir, "Custom");
			loadDataPlugins();
		} catch (IOException e) {
			logger.fatal("Failed loading product data",e);
		}
	}

	//--------------------------------------------------------------------
	private void loadDataPlugins() throws IOException {
		logger.info("Search for data archives in "+pluginDir);
		DirectoryStream<Path> dirs = Files.newDirectoryStream(pluginDir, "*.jar");
		List<Path> jarFiles = new ArrayList<>();
		for (Path tmp : dirs)
			jarFiles.add(tmp);
		// Sort by name
		Collections.sort(jarFiles);


		double percent = 0;

		int count=0;
		for (Path jarPath : jarFiles) {
			count++;
			logger.debug("Opening JAR file: "+jarPath);
			FileSystem jarFS = FileSystems.newFileSystem(jarPath, null);
			Path baseDir = jarFS.getPath("/");
			loadData(baseDir, jarPath.toString());
			percent = ((double)count) / (double)jarFiles.size();
			if (RPGFrameworkLoader.getCallback()!=null)
				RPGFrameworkLoader.getCallback().progressChanged(0.75 + 0.1*percent);
//			JarFile jar = new JarFile(jarPath.toFile());
//			Enumeration<JarEntry> entries = jar.entries();
//	        while (entries.hasMoreElements()) {
//	            final JarEntry entry = entries.nextElement();
////	            if (entry.getName().contains(".")) {
//	                logger.debug("  File : " + entry.getName());
//	                JarEntry fileEntry = jar.getJarEntry(entry.getName());
//	                InputStream input = jar.getInputStream(fileEntry);
////	                process(input);
////	            }
//	        }
		}
	}

	//--------------------------------------------------------------------
	public void loadData(Path baseDir, String source) throws IOException {
		logger.debug("load data from file "+baseDir+" in filesystem "+baseDir.getFileSystem()+" in source "+source);
		int count;

		count = loadStories(baseDir.resolve(DIRNAME_STORIES));
		logger.info("Loaded "+count+" adventure stories from "+baseDir.resolve(DIRNAME_STORIES));

		count = loadAdventures(baseDir.resolve(DIRNAME_ADVENTURES));
		logger.info("Loaded "+count+" adventures from "+baseDir.resolve(DIRNAME_ADVENTURES));

//		logger.fatal("STOP HERE");
//		System.exit(0);

		count = loadProducts(baseDir.resolve(DIRNAME_PRODUCTS));
		logger.info("Loaded "+count+" products from "+source);
	}

	//--------------------------------------------------------------------
	/**
	 * Called from AdventureSerializationTest
	 */
	public void addStory(AdventureStoryImpl value) {
		stories.put(value.getID(), value);
	}

	//--------------------------------------------------------------------
	private int loadStories(Path ruleDir) {
		logger.debug("  Load adventure stories from "+ruleDir);
		int count = 0;
		try {
			for (Path path : Files.newDirectoryStream(ruleDir, "*.xml")) {
				try {
					AdventureStoryImpl adv = marshaller.read(AdventureStoryImpl.class, Files.newInputStream(path));
					logger.debug("* Story \""+adv.getTitle()+"\"");
					stories.put(adv.getID(), adv);
					count++;
				} catch (Exception e) {
					logger.error("Failed reading adventure story file "+path+": "+e,e);
					BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Failed reading adventure story file "+path+": "+e);
					System.exit(0);
				}
			}
		} catch (IOException e) {
			logger.error("Failed to load all adventure stories",e);
		}
		return count;
	}

//	//--------------------------------------------------------------------
//	public void addAdventure(AdventureImpl value) {
//		Map<String, Adventure> advMap = adventures.get(value.getRules());
//		advMap.put(value.getId(), value);
//	}

	//--------------------------------------------------------------------
	private int loadAdventures(Path baseDir) {
		logger.debug("START loadAdventures");
		int count = 0;
		try {
			DirectoryStream<Path> dir = Files.newDirectoryStream(baseDir);
			for (Path ruleDir : dir) {
				if (!Files.isDirectory(ruleDir))
					continue;
				// Expect sub directory name to be a rulesystem
				String rulesName = ruleDir.getFileName().toString().toUpperCase();
				if (rulesName.endsWith("/"))
					rulesName = rulesName.substring(0, rulesName.length()-1);
				RoleplayingSystem rules;
				try {
					rules = RoleplayingSystem.valueOf(rulesName);
				} catch (IllegalArgumentException e) {
					logger.warn("Ignore adventures of unknown rulesystem '"+rulesName+"'");
					continue;
				}

				/*
				 * Now load all adventures within rule directory
				 */
				logger.debug("Load "+rules+" adventures from "+ruleDir);
				for (Path advPath : Files.newDirectoryStream(ruleDir)) {
					if (!Files.isDirectory(advPath))
						continue;
					logger.trace("Found adventure directory "+advPath);
					Path path = advPath.resolve(FILENAME_ADVENTURE);
					if (!Files.exists(path)) {
						logger.warn("Ignore adventure with missing description: "+path);
						continue;
					}

					try {
						AdventureImpl adv = marshaller.read(AdventureImpl.class, Files.newInputStream(path));
						Path customPath = customDir.resolve(adv.getRules().getName().toLowerCase()).resolve(adv.getId());
						adv.setBaseDirectory(advPath);
						logger.debug("* Adventure \""+adv.getTitle()+"\" _ files expected in "+customPath);

						Map<String, Adventure> advMap = adventures.get(adv.getRules());
						if (advMap==null) {
							advMap = new HashMap<>();
							adventures.put(adv.getRules(), advMap);
						}
						advMap.put(adv.getId(), adv);
						count++;
					} catch (Exception e) {
						logger.error("Failed reading adventure file "+path+": "+e);
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Failed reading adventure file "+path+": "+e);
						System.exit(0);
					}

				}
			}
		} catch (IOException e) {
			logger.error("Failed to load all adventures",e);
		}
		return count;
	}

	//--------------------------------------------------------------------
	private int loadProducts(Path baseDir) {
		logger.debug("START loadProducts");
		int count = 0;
		if (!Files.exists(baseDir)) {
			logger.warn("No local product data found - even directory "+baseDir+" is missing");
			return count;
		}

		try {
			DirectoryStream<Path> dir = Files.newDirectoryStream(baseDir);
			for (Path ruleDir : dir) {
				if (!Files.isDirectory(ruleDir))
					continue;
				// Expect sub directory name to be a rulesystem
				String rulesName = ruleDir.getFileName().toString().toUpperCase();
				if (rulesName.endsWith("/") || rulesName.endsWith("\\"))
					rulesName = rulesName.substring(0, rulesName.length()-1);
				RoleplayingSystem rules;
				try {
					rules = RoleplayingSystem.valueOf(rulesName);
				} catch (IllegalArgumentException e) {
					logger.warn("Ignore products of unknown rulesystem '"+rulesName+"'");
					continue;
				}

				/*
				 * Now load all adventures within rule directory
				 */
				logger.debug("Load "+rules+" products from "+ruleDir);
				for (Path advPath : Files.newDirectoryStream(ruleDir)) {
					if (!Files.isDirectory(advPath))
						continue;
					logger.trace("  Found product directory "+advPath);
					Path path = advPath.resolve(FILENAME_PRODUCT);
					if (!Files.exists(path)) {
						logger.warn("Ignore product with missing description: "+path);
						continue;
					}

					try {
						ProductImpl prod = marshaller.read(ProductImpl.class, Files.newInputStream(path));
						prod.setBaseDirectory(advPath);
						logger.debug("* Product \""+prod.getTitle()+"\"");

						/*
						 * Load images
						 */
						Path filePath = advPath.resolve(FILENAME_COVER);
						try {
							byte[] imgData = Files.readAllBytes(filePath);
							prod.setImage(imgData);
							logger.trace("  Set image for "+prod.getTitle()+"/"+filePath+" with "+imgData.length+" bytes");
						} catch (IOException e) {
							logger.error("Failed reading cover image "+filePath+": "+e);
						}

						for (RoleplayingSystem rules2 : prod.getRoleplayingSystem()) {
							Map<String, Product> prodMap = products.get(rules2);
							if (prodMap==null) {
								prodMap = new HashMap<>();
								products.put(rules2, prodMap);
							}
							prodMap.put(prod.getId(), prod);
						}
						count++;
					} catch (Exception e) {
						logger.error("Failed reading product file "+path+": "+e,e);
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Failed reading product file "+path+": "+e);
					}

				}
			}
		} catch (IOException e) {
			logger.error("Failed to load all products from "+baseDir,e);
		}
		return count;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getProducts(de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public List<Product> getProducts(RoleplayingSystem rules) {
		if (!products.containsKey(rules))
			return new ArrayList<>();
		return new ArrayList<>(products.get(rules).values());
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#loadCover(de.rpgframework.products.Product)
	 */
	@Override
	public byte[] loadCover(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getAdventures(de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public List<Adventure> getAdventures(RoleplayingSystem rules) {
		if (!adventures.containsKey(rules))
			return new ArrayList<>();
		return new ArrayList<>(adventures.get(rules).values());
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getAdventure(de.rpgframework.core.RoleplayingSystem, java.lang.String)
	 */
	@Override
	public Adventure getAdventure(RoleplayingSystem rules, String id) {
		if (adventures.containsKey(rules))
			return adventures.get(rules).get(id);
		return null;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getAdventureStory(java.lang.String)
	 */
	@Override
	public AdventureStory getAdventureStory(String id) {
		return stories.get(id);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getAdventureStories()
	 */
	@Override
	public List<AdventureStory> getAdventureStories() {
		return new ArrayList<>(stories.values());
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#getProductWith(de.rpgframework.products.Adventure)
	 */
	@Override
	public List<Product> getProductWith(Adventure adventure) {
		ArrayList<Product> ret = new ArrayList<>();
		if (products.containsKey(adventure.getRules())) {
			for (Product prod : products.get(adventure.getRules()).values()) {
				if (prod.getAdventures().contains(adventure))
					ret.add(prod);

			}
		}
		return ret;
	}

	//-------------------------------------------------------------------
	private AdventureStory createAdventureStory(String id, String title, Locale locale) throws IOException {
		Path storyPath = customDir.getFileSystem().getPath(customDir.toString(), DIRNAME_STORIES);
		if (!Files.exists(storyPath)) {
			logger.info("Creating adventure story directory "+storyPath);
			try {
				Files.createDirectories(storyPath);
			} catch (IOException e) {
				logger.error("Failed creating adventure story directory",e);
				throw e;
			}
		}

		// Create content
		LocalizedAdventureContent content = new LocalizedAdventureContent(locale);
		content.setTitle(title);

		AdventureStoryImpl story = new AdventureStoryImpl();
		story.addContent(content);

		Path outFile = storyPath.resolve(id+".xml");
		marshaller.write(story, new FileWriter(outFile.toFile()));

		return story;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.ProductService#createAdventure(java.lang.String, java.lang.String, de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public Adventure createAdventure(String id, RoleplayingSystem rules, Locale locale, String title) throws IOException {
		Path advPath = customDir.getFileSystem().getPath(customDir.toString(), DIRNAME_ADVENTURES, rules.name().toLowerCase(), id);
		if (!Files.exists(advPath)) {
			logger.info("Creating adventure directory "+advPath);
			try {
				Files.createDirectories(advPath);
			} catch (IOException e) {
				logger.error("Failed creating adventure directory",e);
				throw e;
			}
		}

		// Create content
		AdventureStory story = createAdventureStory(id, title, locale);

		AdventureImpl ret = new AdventureImpl(id, rules, story, advPath);
		saveAdventure(ret);

		EventBus.fireEvent(this, EventType.ADVENTURE_ADDED, ret);
		return ret;
	}

	//-------------------------------------------------------------------
	@Override
	public Adventure createAdventure(String id, String title, RoleplayingSystem rules) throws IOException {
		return createAdventure(id, rules, Locale.getDefault(), title);
	}

	//-------------------------------------------------------------------
	@Override
	public void deleteAdventure(Adventure value) throws IOException {
		logger.error("TODO: deleteAdventure");
	}

	//-------------------------------------------------------------------
	@Override
	public void saveAdventure(Adventure data) throws IOException {
		Path advFile = data.getBaseDirectory().resolve("adventure.xml");
		logger.info("Save adventure to "+advFile);
		marshaller.write(data, advFile.toFile());
	}

}
