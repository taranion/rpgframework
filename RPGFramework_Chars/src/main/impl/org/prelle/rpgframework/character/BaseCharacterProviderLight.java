/**
 * 
 */
package org.prelle.rpgframework.character;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Persister;
import org.prelle.simplepersist.Serializer;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.RPGFramework;
import de.rpgframework.character.Attachment;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterHandle.Format;
import de.rpgframework.character.CharacterHandle.Type;
import de.rpgframework.character.CharacterProvider;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class BaseCharacterProviderLight implements CharacterProvider {
	
	protected Logger logger = LogManager.getLogger("babylon.chars");
	
	private final static String INDEX = "index.xml";

	private ConfigContainer configRoot;
	private ConfigOption<String> cfgDataDir;
	/**
	 * Pointer to local directory containing player data
	 */
	private Path localBaseDir;
	private Path myselfPlayerDir;
	
	private List<CharacterHandle> cache;
	
	private Serializer serial;
	
	//--------------------------------------------------------------------
	public BaseCharacterProviderLight(ConfigContainer configRoot) throws IOException {
		cache = new ArrayList<CharacterHandle>();
		serial= new Persister();
		
		this.configRoot = configRoot;
		
		prepareConfigNode();
		
		String dataDir = cfgDataDir.getStringValue();

		// Add player specific path
		localBaseDir = FileSystems.getDefault().getPath(dataDir, "player");
		myselfPlayerDir = localBaseDir.resolve("myself");
		logger.info("Expect player data at "+localBaseDir);
		
		// Ensure that directory exists
		try {
			Files.createDirectories(localBaseDir);
			Files.createDirectories(myselfPlayerDir);
		} catch (IOException e) {
			logger.fatal("Could not create player directory: "+e);
			throw e;
		}
	}
	
	//------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private void prepareConfigNode() {
		cfgDataDir = (ConfigOption<String>) configRoot.getOption(RPGFramework.PROP_DATADIR);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#firesEvents()
	 */
	@Override
	public boolean firesEvents() {
		return false;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getNonEventingStorage()
	 */
	@Override
	public CharacterProvider getNonEventingStorage() {
		return null;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgtool.character.CharacterProvider#createCharacter(java.lang.String, java.lang.String)
	 */
	@Override
	public CharacterHandle createCharacter(String name, RoleplayingSystem system) throws IOException {
		if (name==null)
			throw new NullPointerException("Name may not be null");
		name = name.trim();
		logger.debug("createCharacter("+name+", "+system+")");

		String dirName = system.name().toLowerCase();
		Path systemDir = myselfPlayerDir.resolve(dirName);
		Path charDir   = systemDir.resolve(name);
		if (!Files.exists(charDir))
			Files.createDirectories(charDir);
		logger.debug("data for "+name+" is stored in "+charDir);
		BaseCharacterHandleLight handle = new BaseCharacterHandleLight(charDir, system);
		handle.setName(name);
		cache.add(handle);
		logger.info("created handle "+handle);
		return handle;
	}

	//-------------------------------------------------------------------
	private static void recursiveDelete(Path directory) throws IOException {
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   Files.delete(file);
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				   Files.delete(dir);
				   return FileVisitResult.CONTINUE;
			   }

		   });
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgtool.character.CharacterProvider#deleteCharacter(org.prelle.rpgtool.character.CharacterHandle)
	 */
	@Override
	public void deleteCharacter(CharacterHandle handle) throws IOException {
		BaseCharacterHandleLight realHandle = (BaseCharacterHandleLight)handle;
		Path charDirectory = realHandle.getPath();

		logger.info("Deleting character directory "+charDirectory);
		recursiveDelete(charDirectory);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#renameCharacter(de.rpgframework.character.CharacterHandle, java.lang.String)
	 */
	@Override
	public void renameCharacter(CharacterHandle handle, String newName) throws IOException {
		BaseCharacterHandleLight realHandle = (BaseCharacterHandleLight)handle;
		Path charDirectory = realHandle.getPath();
		
		Path newDirectory = charDirectory.getParent().resolve(newName);
		logger.info("Rename "+charDirectory+" to "+newDirectory);
		String oldName = charDirectory.getFileName().toString();
		
		Files.move(charDirectory, newDirectory);
		realHandle.setPath(newDirectory);
		realHandle.setName(newName);
		
		for (Attachment attach : realHandle.getAttachments()) {
			String head = attach.getFilename();
			String tail = "";
			int pos = head.indexOf(".");
			if (pos>0) {
				head = head.substring(0, pos);
				tail = attach.getFilename().substring(pos+1);
			}
			if (head.equals(oldName)) {
				String newAttachmentName = newName+"."+tail;
				Path oldFile = newDirectory.resolve(attach.getFilename());
				Path newFile = newDirectory.resolve(newAttachmentName);
				Files.move(oldFile, newFile);
				attach.setFilename(newAttachmentName);
				logger.debug("Rename "+attach.getFilename()+" to "+newAttachmentName);
				
				// Change index
				realHandle.addAttachment(attach);
			}
		}
		
		// Update index
		serial.write(realHandle.getIndex(), new FileWriter(realHandle.getPath().resolve(INDEX).toFile()));
	}

	//-------------------------------------------------------------------
	private Attachment parseAttachment(BaseCharacterHandleLight handle, Path file) throws IOException {
		String filename = file.getFileName().toString();		
		Type   type   = null;
		byte[] data   = Files.readAllBytes(file);
		
		Attachment attach = null;
		
		// Is it a character
		if (filename.startsWith(handle.getName())) {
			type = Type.CHARACTER;
			if (filename.endsWith(".xml"))
				attach = new Attachment(type, Format.RULESPECIFIC, filename);
			else if (filename.endsWith(".html"))
				attach = new Attachment(type, Format.HTML, filename);
			else if (filename.endsWith(".txt"))
				attach = new Attachment(type, Format.TEXT, filename);
			else if (filename.endsWith(".pdf"))
				attach = new Attachment(type, Format.PDF, filename);
			else if (filename.endsWith(".img"))
				attach = new Attachment(type, Format.IMAGE, filename);
			else if (filename.endsWith(".jpg") || filename.endsWith(".png"))
				attach = new Attachment(type, Format.IMAGE, filename);
			else {
				logger.warn(" Found an unsupported file format for character: "+filename);
				attach = new Attachment(type, Format.RULESPECIFIC_EXTERNAL, filename);
			}
			logger.debug(" "+file+" is a "+type+" "+attach.getFormat());
		} else if (filename.startsWith("portrait")) {
			type = Type.CHARACTER;
			// For backward compatibility
			if (filename.endsWith(".png"))
				attach = new Attachment(type, Format.IMAGE, filename);
			else if (filename.endsWith(".jpg"))
				attach = new Attachment(type, Format.IMAGE, filename);
			else {
				logger.warn(" Found an unsupported file format for character: "+filename);
				return null;
			}
			logger.debug(" "+file+" is a "+type+" "+attach.getFormat());
		} else if (filename.startsWith("background")) {
			type = Type.BACKGROUND;
			if (filename.endsWith(".html"))
				attach = new Attachment(type, Format.HTML, filename);
			else if (filename.endsWith(".txt"))
				attach = new Attachment(type, Format.TEXT, filename);
			else if (filename.endsWith(".pdf"))
				attach = new Attachment(type, Format.PDF, filename);
			else {
				logger.warn(" Found an unsupported file format for character: "+filename);
				return null;
			}
			logger.debug(" "+file+" is a "+type+" "+attach.getFormat());
		} else {
			type = Type.REPORT;
			if (filename.endsWith(".html"))
				attach = new Attachment(type, Format.HTML, filename);
			else if (filename.endsWith(".txt"))
				attach = new Attachment(type, Format.TEXT, filename);
			else if (filename.endsWith(".pdf"))
				attach = new Attachment(type, Format.PDF, filename);
			else {
				// Unclear what to do now
				if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
					attach = new Attachment(Type.BACKGROUND, Format.IMAGE, filename);					
				} else {				
					logger.warn(" Found an unsupported file format for character: "+filename);
					return null;
				}
			}
			logger.debug(" "+file+" is a "+type+" "+attach.getFormat());
		}
		
		attach.setData(data);
		attach.setLastModified(new Date(Files.getLastModifiedTime(file).toMillis()));
		logger.info(" Parsed "+attach);
		
		return attach;
	}

	//-------------------------------------------------------------------
	private Attachment parseAttachment(AttachmentEntry entry, Path file) throws IOException {
		String filename = file.getFileName().toString();
		if (!filename.equals(entry.file)) {
			throw new IllegalArgumentException("Filename in index and file are not identical");
		}
		Type   type   = entry.type;
		byte[] data   = Files.readAllBytes(file);

		Attachment attach = null;

		if (filename.endsWith(".xml"))
			attach = new Attachment(type, Format.RULESPECIFIC, filename);
		else if (filename.endsWith(".html"))
			attach = new Attachment(type, Format.HTML, filename);
		else if (filename.endsWith(".txt"))
			attach = new Attachment(type, Format.TEXT, filename);
		else if (filename.endsWith(".pdf"))
			attach = new Attachment(type, Format.PDF, filename);
		else if (filename.endsWith(".img"))
			attach = new Attachment(type, Format.IMAGE, filename);
		else if (filename.endsWith(".jpg") || filename.endsWith(".png"))
			attach = new Attachment(type, Format.IMAGE, filename);
		else {
			if (filename.endsWith("~")) {
				logger.warn(" Removed Emacs backup file: "+filename);
				Files.delete(file);
				return null;
			} else {
				logger.warn(" Found an unsupported file format for character: "+filename);
				attach = new Attachment(type, Format.RULESPECIFIC_EXTERNAL, filename);
			}
		}
		logger.debug(" "+file+" is a "+type+" "+attach.getFormat());
		
		attach.setData(data);
		attach.setLastModified(new Date(Files.getLastModifiedTime(file).toMillis()));
		logger.debug(" Parsed "+attach);
		
		return attach;
	}

	//-------------------------------------------------------------------
	/**
	 * Read character data from a directory containing:
	 * <li>portrait.(gif|jpg|png|img) - a character image</li>
	 * <li>*.xml - a rulespecific characer representation. The filename (without suffix) is used as default character name</li>
	 * <li>*.pdf - a view-only characer representation</li>
	 */
	private synchronized BaseCharacterHandleLight loadCharacter(RoleplayingSystem system, Path charDir) throws IOException {
		// Search cache
		for (CharacterHandle possibleHandle : cache) {
			if (possibleHandle.getPath().equals(charDir)) {
				logger.trace("Cache hit for "+charDir);
				return (BaseCharacterHandleLight) possibleHandle;
			}
		}
		
		BaseCharacterHandleLight handle = new BaseCharacterHandleLight(null, system);
		handle.setName(charDir.getFileName().toString());
		handle.setPath(charDir);

		/*
		 * Depending on the presence or absence of the index file, load data
		 * from index or build a new index
		 */
		Path indexFile = charDir.resolve(INDEX);
		if (Files.exists(indexFile)) {
			logger.debug("Load character index file");
			handle.setIndex( serial.read(AttachmentIndex.class, new FileReader(indexFile.toString())));
			
			for (AttachmentEntry entry : new ArrayList<AttachmentEntry> (handle.getIndex()) ) {
				try {
					Attachment attach = parseAttachment(entry, charDir.resolve(entry.file));
					if (attach!=null)
						handle.addLoadedAttachment(entry, attach);
					else {
						logger.error("Error parsing attachment "+charDir.resolve(entry.file));
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, "Error parsing attachment "+ charDir.resolve(entry.file)+"\n\nIgnoring it");
						handle.getIndex().remove(entry);
					}
				} catch (NoSuchFileException e) {
					logger.error("Missing file "+e.getFile()+" which is referenced by index "+indexFile);
					logger.error("Remove missing file from index");
					handle.getIndex().remove(entry);
					serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(indexFile.toFile()));
				} catch (IOException e) {
					logger.error("Failed reading "+charDir.resolve(entry.file)+": "+e);
				} catch (Exception e) {
					logger.error("Failed reading "+charDir.resolve(entry.file),e);
				}
			}
			/*
			 * Interprete remaining files
			 */
			DirectoryStream<Path> dir = Files.newDirectoryStream(charDir);
			for (Path tmp : dir) {
				if (!Files.isRegularFile(tmp))
					continue;
				if (tmp.getFileName().toString().equals("index.xml"))
					continue;
				if (tmp.getFileName().toString().equals(".git"))
					continue;
				if (tmp.getFileName().toString().endsWith("~"))
					continue;
				// Do we already know this?
				boolean isNew = true;
				for (AttachmentEntry entry : new ArrayList<AttachmentEntry> (handle.getIndex()) ) {
					Path comp = charDir.resolve(entry.file);
					if (comp.equals(tmp)) {
						// Already know this
						isNew = false;
						break;
					}
				}
				if (isNew) {
					Attachment attach = parseAttachment(handle, tmp);
					if (attach==null) {
						continue;
					}
					/*
					 * Add files to index, except it is rulespecific character data. In this case
					 * check that it doesn't exist in index yet
					 */
					if (attach.getType()==Type.CHARACTER && attach.getFormat()==Format.RULESPECIFIC) {
						// Found rulespecific character data
						Attachment old = handle.getFirstAttachment(Type.CHARACTER, Format.RULESPECIFIC);
						if (old!=null) {
							logger.warn("Found rulespecific character data in "+tmp+" but already use that from "+old.getFilename());
							continue;
						}

					}
					logger.warn("Add previously unknown file "+tmp+" as character attachment "+attach);
					handle.addAttachment(attach);
				}
				
			}
		} else {
			handle.setIndex(new AttachmentIndex());
			/*
			 * Depending on the filename parse attachments
			 */
			logger.warn("Index is missing for "+charDir+". Generate one");
			DirectoryStream<Path> dir = Files.newDirectoryStream(charDir);
			for (Path tmp : dir) {
				if (!Files.isRegularFile(tmp))
					continue;

				logger.trace(" Checking "+tmp);
				Attachment attach = parseAttachment(handle, tmp);
				if (attach!=null) {
					handle.addAttachment(attach);
				} else
					logger.warn(" Ignored "+tmp);
			}
			// Generate an index for next load
			serial.write(handle.getIndex(), new FileWriter(indexFile.toFile()));
		}
		return handle;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getCharacters(de.rpgframework.core.Player, de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public synchronized List<CharacterHandle> getMyCharacters(RoleplayingSystem system) {
		Path systemDir = myselfPlayerDir.resolve(system.name().toLowerCase());
		logger.debug("Get characters under "+systemDir);
		
		List<CharacterHandle> list = new ArrayList<CharacterHandle>();
		if (!Files.exists(systemDir)) {
			try {
				Files.createDirectories(systemDir);
			} catch (IOException e) {
				logger.error("Error creating directory for rule system",e);
			}
			return list;
		}

			// Get all charac
		try {
			/*
			 * Expect each character within its own directory
			 */
			for (Path charDir : Files.newDirectoryStream(systemDir)) {
				if (!Files.isDirectory(charDir))
					continue;
				try {
					BaseCharacterHandleLight handle = loadCharacter(system, charDir);
					if (handle!=null) {
						list.add(handle);
						logger.debug("Found my character "+handle.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.error("Error getting characters: "+e,e);
		}

		return list;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getMyCharacters()
	 */
	@Override
	public synchronized List<CharacterHandle> getMyCharacters() {
		try {
			DirectoryStream<Path> userDir = Files.newDirectoryStream(myselfPlayerDir);
			for (Path systemDir : userDir) {
				if (!Files.isDirectory(systemDir))
					continue;
				logger.debug("****"+systemDir);
				RoleplayingSystem system = null;
				try {
					system = RoleplayingSystem.valueOf(systemDir.getFileName().toString().toUpperCase());
				} catch (IllegalArgumentException e1) {
					logger.error("Found directory that does not match roleplaying system: "+systemDir);
					continue;
				}
				logger.debug("Found directory with "+system+" characters");
				/*
				 * Expect each character within its own directory
				 */
				for (Path charDir : Files.newDirectoryStream(systemDir)) {
					if (!Files.isDirectory(charDir))
						continue;
					try {
						BaseCharacterHandleLight handle = loadCharacter(system, charDir);
						if (handle!=null && !cache.contains(handle)) {
							cache.add(handle);
							logger.debug("Added character to cache: "+handle.getName());
						}
					} catch (Exception e) {
						logger.error("Failed loading character in "+charDir,e);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			logger.error("Failed loading characters",e);
		}
		
		return new ArrayList<CharacterHandle>(cache);
	}

	//-------------------------------------------------------------------
	private void setAttachment(CharacterHandle handle, Attachment attach) throws IOException {
		byte[] data = attach.getData();
		
		if (data==null)
			throw new NullPointerException("No data in attachment");
		if (attach.getFilename()==null)
			throw new NullPointerException("No filename set");
		
		String filename = attach.getFilename();
		
		((BaseCharacterHandleLight)handle).addAttachment(attach);
		
		Path charDir = ((BaseCharacterHandleLight)handle).getPath();
		Path target = charDir.resolve(filename);
		logger.debug("Target is "+target+"   path was "+charDir);
//		((BaseCharacterHandle)handle).setPath(target);
		Files.write(target, data);
		logger.info("Added "+target);
		
		// if there is a modification time in the attachment, set it
		if (attach.getLastModified()!=null) {
			Files.setLastModifiedTime(target, FileTime.fromMillis(attach.getLastModified().getTime()));
		} else
			handle.setLastModified(new Date(System.currentTimeMillis()));
		
		// Update index
		serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(((BaseCharacterHandleLight)handle).getPath().resolve(INDEX).toFile()));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#addAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public Attachment addAttachment(CharacterHandle handle, Type type, Format format, String suggestedName, byte[] data) throws IOException {
		if (data==null)
			throw new NullPointerException("No data in attachment");
		logger.info("Add attachment "+type+"  "+format+"  "+suggestedName );
		
		String guessedname = suggestedName;
		// Eventually override with default values
		switch (type) {
		case CHARACTER:
			switch (format) {
			case RULESPECIFIC: guessedname = handle.getName()+".xml"; break;
			case HTML        : guessedname = handle.getName()+".html"; break;
			case PDF         : guessedname = handle.getName()+".pdf"; break;
			case IMAGE       : guessedname = handle.getName()+".img"; break;
			case TEXT        : guessedname = handle.getName()+".txt"; break;
			case RULESPECIFIC_EXTERNAL:
				break;
			}
			break;
		case BACKGROUND:
			switch (format) {
			case HTML        : guessedname = "background.html"; break;
			case PDF         : guessedname = "background.pdf"; break;
			case TEXT        : guessedname = "background.txt"; break;
			default:
				throw new IllegalArgumentException("Format "+format+" not accepted for character background");
			}
			break;
		default:
		}
		// If suggested name misses a suffix and guessed name is different, overrule
		// suggested name
		if (suggestedName!=null && suggestedName.indexOf(".")<0 && !suggestedName.equals(guessedname)) {
			suggestedName = guessedname;
			logger.debug("  prefer filename "+suggestedName);
		}
		
		Attachment attach = new Attachment(type, format, (suggestedName==null)?guessedname:suggestedName);
		attach.setData(data);
		setAttachment(handle, attach);
		if (handle instanceof BaseCharacterHandleLight)
			((BaseCharacterHandleLight)handle).addAttachment(attach);
		
		logger.info("Filename of added attachment: "+attach.getFilename());
		// Update index
		serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(((BaseCharacterHandleLight)handle).getPath().resolve(INDEX).toFile()));
		
		return attach;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#copyAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public Attachment copyAttachment(CharacterHandle handle, Attachment attach) throws IOException {
		Attachment copy = new Attachment(attach.getType(), attach.getFormat(), attach.getFilename());
		copy.setData(attach.getData());
		copy.setUserData(attach.getParsed());
		copy.setLastModified(attach.getLastModified());
		
		setAttachment(handle, copy);
		
		// Update index
		serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(((BaseCharacterHandleLight)handle).getPath().resolve(INDEX).toFile()));
		return copy;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#modifyAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public void modifyAttachment(CharacterHandle handle, Attachment attach) throws IOException {
		if (attach.getData()==null)
			throw new NullPointerException("No data in attachment");
		if (((BaseCharacterHandleLight)handle).getPath()==null)
			throw new NullPointerException("No path in attachment - needs to be added first");
		
		Path parent = ((BaseCharacterHandleLight)handle).getPath();
		Path target = parent.resolve(attach.getFilename());
		logger.debug("Update attachment "+target);
//		Files.createFile(target);
		Files.write(target, attach.getData(), StandardOpenOption.CREATE,
		         StandardOpenOption.TRUNCATE_EXISTING );
		logger.info("Updated "+target);
		
		// if there is a modification time in the attachment, set it
		if (attach.getLastModified()!=null) {
			Files.setLastModifiedTime(target, FileTime.fromMillis(attach.getLastModified().getTime()));
		} else
			handle.setLastModified(new Date(System.currentTimeMillis()));

//		if (handle instanceof BaseCharacterHandle)
//			((BaseCharacterHandle)handle).removeAttachment(attach);
		
		// Update index
		serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(parent.resolve(INDEX).toFile()));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#removeAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public void removeAttachment(CharacterHandle handle, Attachment attach) throws IOException {
		if (((BaseCharacterHandleLight)handle).getPath()==null)
			throw new NullPointerException("No path in char handle - needs to be added first");
		
		Path target = ((BaseCharacterHandleLight)handle).getPath().resolve(attach.getFilename());
		Files.delete(target);
		logger.info("Deleted "+target);
		
		// Update index
		serial.write(((BaseCharacterHandleLight)handle).getIndex(), new FileWriter(((BaseCharacterHandleLight)handle).getPath().resolve(INDEX).toFile()));
	}

}
