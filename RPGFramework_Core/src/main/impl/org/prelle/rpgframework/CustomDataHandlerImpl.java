/**
 * 
 */
package org.prelle.rpgframework;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.RPGFramework;
import de.rpgframework.core.CustomDataHandler;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class CustomDataHandlerImpl implements CustomDataHandler {
	
	protected Logger logger = LogManager.getLogger("babylon.custom");
	
	private ConfigContainer configRoot;
	private ConfigOption<String> cfgDataDir;
	private Path localBaseDir;

	//--------------------------------------------------------------------
	public CustomDataHandlerImpl(ConfigContainer configRoot) throws IOException {
		this.configRoot = configRoot;
		prepareConfigNode();
		
		String dataDir = cfgDataDir.getStringValue();

		// Add player specific path
		localBaseDir = FileSystems.getDefault().getPath(dataDir, "custom");
		logger.info("Custom descriptions expected at "+localBaseDir);
		if (!Files.exists(localBaseDir)) {
			Files.createDirectories(localBaseDir);
		}
		
		prepareCustomDescriptions(RoleplayingSystem.CORIOLIS);
		prepareCustomDescriptions(RoleplayingSystem.SHADOWRUN6);
		prepareCustomDescriptions(RoleplayingSystem.SPLITTERMOND);
	}
	
	//------------------------------------------------------------------
	private void prepareCustomDescriptions(RoleplayingSystem rules) {
		Path dir = getCustomDataPath(rules);
		Path file = dir.resolve("fallback-help.properties");
		try {
			if (!Files.exists(dir)) 
				Files.createDirectories(dir);
			
			if (!Files.exists(file)) {
				Properties pro = new Properties();
				String comment = "You can use this file to define custom descriptions to skills, spells, qualities, adept powers, .... "
						+"\nBasically any data Genesis is not allowed to provide due to license restrictions.\n"
						+"\nSee: https://rpgframework.atlassian.net/wiki/spaces/SR6HELP/pages/420118574/Custom+descriptions"
						+"\n\nThis file can be shared between installations of Genesis";
				pro.store(new FileWriter(file.toFile()), comment);
			}
		} catch (IOException e) {
			logger.error("Failed preparing custom descriptions: "+e);
		}
	}
	
	//------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private void prepareConfigNode() {
		cfgDataDir = (ConfigOption<String>) configRoot.getOption(RPGFramework.PROP_DATADIR);
	}
	
	//------------------------------------------------------------------
	@Override
	public List<String> getAvailableCustomIDs(RoleplayingSystem rules) {
		List<String> ret = new ArrayList<>();
		// Find directory specific to roleplaying system
		Path rpgDir = localBaseDir.resolve(rules.name().toLowerCase());
		if (!Files.exists(rpgDir)) {
			try {
				Files.createDirectories(rpgDir);
			} catch (IOException e) {
				logger.error("Could not create custom data directory "+rpgDir,e);
				return ret;
			}
		}
		try {
			DirectoryStream<Path> dir = Files.newDirectoryStream(rpgDir, "*.xml");
			dir.forEach(filepath -> {
				String fname = filepath.getFileName().toString();
				ret.add(fname.substring(0, fname.length()-4));
			});
		} catch (IOException e) {
			logger.error("Failed reading custom data directory "+rpgDir,e);
		}
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.core.CustomDataHandler#getCustomData(de.rpgframework.core.RoleplayingSystem, java.lang.String)
	 */
	@Override
	public CustomDataPackage getCustomData(RoleplayingSystem rules, String identifier) {
		// Find directory specific to roleplaying system
		Path rpgDir = localBaseDir.resolve(rules.name().toLowerCase());
		
		CustomDataPackage ret = new CustomDataPackage();
		
		// Data file
		ret.datafile = rpgDir.resolve(identifier+".xml");
		// Regular properties
		try {
			Path path = rpgDir.resolve(identifier+".properties");
			if (path!=null && Files.exists(path))
				ret.properties = new PropertyResourceBundle(Files.newInputStream(rpgDir.resolve(identifier+".properties")));
			else if (!"fallback".equals(identifier))
				logger.error("Expect custom data property file "+path);
		} catch (IOException e) {
			logger.error("Failed obtaining custom data for "+rules+"/"+identifier,e);
			return null;
		}
		// Help properties
		try {
			Path helpPath = rpgDir.resolve(identifier+"-help.properties");
			if (helpPath!=null && Files.exists(helpPath))
				ret.helpProperties = new PropertyResourceBundle(Files.newInputStream(helpPath));
			else if (!"fallback".equals(identifier))
				logger.error("Expect custom data help property file "+helpPath);
			else
				logger.warn("Expect user provided translations in "+helpPath);
		} catch (IOException e) {
			logger.error("Failed obtaining custom data for "+rules+"/"+identifier,e);
			return null;
		}
		
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.core.CustomDataHandler#getCustomDataPath(de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public Path getCustomDataPath(RoleplayingSystem rules) {
		return localBaseDir.resolve(rules.name().toLowerCase());
	}

	//-------------------------------------------------------------------
	public void setCustomText(RoleplayingSystem rules, String key, String value) {
		Path rpgDir = getCustomDataPath(rules);
		
		// Help properties
		try {
			Path helpPath = rpgDir.resolve("fallback-help.properties");
			Properties allKeys = new Properties();
			if (helpPath!=null && Files.exists(helpPath)) {
				allKeys.load(new FileReader(helpPath.toFile()));
			}
			allKeys.put(key, value);
			allKeys.store(new FileWriter(helpPath.toFile()), "Modified "+Instant.now());
		} catch (IOException e) {
			logger.error("Failed writing updated fallback-help for "+rules,e);
		}
		
	}

}
