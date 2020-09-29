/**
 *
 */
package de.rpgframework.print.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Persister;
import org.prelle.simplepersist.Serializer;

import de.rpgframework.ConfigContainer;
import de.rpgframework.RPGFramework;
import de.rpgframework.ResourceI18N;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PrintManager;
import de.rpgframework.print.PrintTemplate;

/**
 * @author Stefan
 *
 */
public class PrintManagerImpl implements PrintManager {

	private final static Logger logger = LogManager.getLogger("babylon.print");
	private final static ResourceBundle RES = ResourceBundle.getBundle(PrintManagerImpl.class.getName());


	private final static String DIRNAME_CUSTOM  = "print";

	@SuppressWarnings("unused")
	private ConfigContainer configRoot;

	/**
	 * root directory for all data of this service. Contains subdirectories for
	 * each roleplaying system
	 */
	private Path customDir;

	private Serializer marshaller;

	//--------------------------------------------------------------------
	/**
	 */
	public PrintManagerImpl(ConfigContainer configRoot) throws IOException {
		this.configRoot = configRoot;
		prepareConfigNode();

		marshaller = new Persister();

		String dataDir = configRoot.getOption(RPGFramework.PROP_DATADIR).getStringValue();
		customDir = FileSystems.getDefault().getPath(dataDir, DIRNAME_CUSTOM);

		// Ensure that directory exists
		try {
			Files.createDirectories(customDir);
		} catch (IOException e) {
			logger.fatal("Could not create print template directory: "+e);
			throw e;
		}
	}

	//------------------------------------------------------------------
	private void prepareConfigNode() {
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintManager#getAvailableTemplates(de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public List<PrintTemplate> getAvailableTemplates(RoleplayingSystem system) {
		List<PrintTemplate> ret = new ArrayList<PrintTemplate>();
		// Read files from directory
		Path templateDir = customDir.resolve(system.name().toLowerCase());
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(templateDir, file -> file.getFileName().toString().endsWith(".xml"));
			for (Path file : stream) {
				String name = file.getFileName().toString();
				name = name.substring(0, name.length()-4);
				String xml = new String(Files.readAllBytes(file), "UTF-8");
				try {
					PrintTemplate template = marshaller.read(PrintTemplate.class, xml);
					template.setName(name);
					if (template.getBackgroundImageFileName()!=null) {
						Path imgFile = templateDir.resolve(template.getBackgroundImageFileName());
						if (Files.exists(imgFile))
							template.setBackgroundImage(imgFile);
						else {
							logger.error("Template file "+template+" references a non existing background image "+imgFile);
							BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2,
									ResourceI18N.format(RES,"error.printtemplate.nosuchbackground", name, imgFile.toString())
									);
						}
					}
					ret.add(template);
					logger.info("Read template '"+name+"' from "+file);
				} catch (Exception e) {
					logger.error("Failed reading print template "+file,e);
					BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 1, "Cannot read print template "+file+"\n"+e);
				}
			}
		} catch (NoSuchFileException e) {
			logger.debug("No print template for "+system+": "+e);
		} catch (IOException e) {
			logger.error("Error reading print templates",e);
			StringWriter foo = new StringWriter();
			e.printStackTrace(new PrintWriter(foo));
			BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Error reading print templates.\n\n"+foo.toString());
		}

		return ret;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintManager#saveTemplate(de.rpgframework.core.RoleplayingSystem, de.rpgframework.print.PrintTemplate)
	 */
	@Override
	public void saveTemplate(RoleplayingSystem system, PrintTemplate value) {
		Path file = customDir.resolve(system.name().toLowerCase()).resolve(value.getName()+".xml");
		logger.info("Save print template '"+value.getName()+"' to "+file);

		// Convert to XML
		StringWriter out = new StringWriter();
		try {
			marshaller.write(value, out);
		} catch (IOException e) {
			logger.error("Error converting print template to XML ",e);
			StringWriter foo = new StringWriter();
			e.printStackTrace(new PrintWriter(foo));
			BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Error converting print template to XML \n\n"+foo.toString());
			return;
		}
		// Now save to file
		try {
			Files.createDirectories(file.getParent());
			Files.write(file, out.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			logger.error("Error writing print template to "+file,e);
			StringWriter foo = new StringWriter();
			e.printStackTrace(new PrintWriter(foo));
			BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Error writing print template to "+file+"\n\n"+foo.toString());
		}
	}

//	//--------------------------------------------------------------------
//	/**
//	 * @see de.rpgframework.print.PrintManager#createElementCell(de.rpgframework.print.PDFPrintElement)
//	 */
//	@Override
//	public ElementCell createElementCell(PDFPrintElement value) {
//		return new ElementCellImpl(value);
//	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintManager#createPageDefinition(int)
	 */
	@Override
	public PageDefinition createPageDefinition(int columns) {
		return new PageDefinition(columns);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintManager#createTemplate(java.util.List)
	 */
	@Override
	public PrintTemplate createTemplate(List<PageDefinition> items) {
		return new PrintTemplate(items);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintManager#addBackgroundImage(de.rpgframework.core.RoleplayingSystem, java.nio.file.Path)
	 */
	@Override
	public void addBackgroundImage(RoleplayingSystem system, Path file) {
		assert system!=null;
		Path templateDir = customDir.resolve(system.name().toLowerCase());
		Path imgFile = templateDir.resolve(file.getFileName());
		logger.info("Copy "+file+" as new background image "+imgFile);

		try {
			Files.createDirectories(imgFile);
			Files.copy(file, imgFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("Failed writing/replacing file "+imgFile+" with "+file,e);
			StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 1, "Error copying image to "+imgFile+"\n\n"+out);
		}
	}

}
