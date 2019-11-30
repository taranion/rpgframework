/**
 * 
 */
package org.prelle.rpgframework.character;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.character.Attachment;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.character.RuleSpecificCharacterObject;
import de.rpgframework.core.BabylonEvent;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.CommandBus;
import de.rpgframework.core.CommandResult;
import de.rpgframework.core.CommandType;
import de.rpgframework.core.Player;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class BaseCharacterHandleLight implements CharacterHandle {
	
	protected Logger logger = LogManager.getLogger("babylon.chars");
	
	protected String name;
	protected byte[] imageBytes;
	private Path path;
	private RoleplayingSystem ruleIdentifier;
	protected RuleSpecificCharacterObject parsedCharac;
	
	private AttachmentIndex index;
	private Map<AttachmentEntry, Attachment> attachments;

	//--------------------------------------------------------------------
	/**
	 */
	public BaseCharacterHandleLight(Path path, RoleplayingSystem rules) {
		this.path = path;
		this.ruleIdentifier = rules;
		index       = new AttachmentIndex();
		attachments = new HashMap<AttachmentEntry,Attachment>();
	}

	//--------------------------------------------------------------------
	@Override
	public String toString() {
		return name+"@"+path;
	}

	//--------------------------------------------------------------------
	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseCharacterHandleLight) {
			BaseCharacterHandleLight other = (BaseCharacterHandleLight)o;
			return toString().equals(other.toString());
		}
		return false;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgtool.character.CharacterHandle#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgtool.character.CharacterHandle#getCharacter()
	 */
	@Override
	public RuleSpecificCharacterObject getCharacter() {
		if (parsedCharac==null) {
			Attachment attach = getFirstAttachment(Type.CHARACTER, Format.RULESPECIFIC);
			if (attach==null || attach.getData()==null)
				return null;
			
			/*
			 * Convert byte buffer
			 */
			logger.debug("Try to decode character");
			CommandResult result = null;
			try {
				result = CommandBus.fireCommand(this, CommandType.DECODE, 
						ruleIdentifier,
						attach.getData()
						);
			} catch (Exception e) {
				logger.error("Failec decoding character",e);
				StringWriter out = new StringWriter();
				e.printStackTrace(new PrintWriter(out));
				result = new CommandResult(CommandType.DECODE, false, out.toString());
			}
			if (!result.wasProcessed()) {
				logger.fatal("No plugin to decode character");
				return null;
			}
			if (!result.wasSuccessful()) {
				String file = path+System.getProperty("file.separator")+attach.getFilename();
				logger.error("Failed to decode character file "+file+": "+result.getMessage());
				BabylonEventBus.fireEvent(new BabylonEvent(this, BabylonEventType.UI_MESSAGE, 2,"Failed to decode character file "+file+" :\n"+result.getMessage()));
				return null;
			}
			parsedCharac = (RuleSpecificCharacterObject) result.getReturnValue();
			if (parsedCharac==null) {
				logger.error("Command to decode character returned successful, but with NullPointer");
			}
		}
		return parsedCharac;
	}

	//--------------------------------------------------------------------
	/**
	 * @throws IOException 
	 * @see org.prelle.rpgtool.character.CharacterHandle#setCharacter(java.lang.Object)
	 */
	@Override
	public void setCharacter(RuleSpecificCharacterObject charac) throws IOException {
		logger.debug("setCharacter("+charac+" to hande "+super.toString());
		this.parsedCharac = charac;
		if (charac==null)
			return;
		name = charac.getName();
		
		/*
		 * Convert character to byte buffer
		 */
		CommandResult result = CommandBus.fireCommand(this, CommandType.ENCODE, 
				ruleIdentifier,
				charac
				);
		if (!result.wasProcessed()) {
			logger.fatal("No plugin to encode character for "+ruleIdentifier);
			return;
		}
		if (!result.wasSuccessful()) {
			throw new IOException("Failed to encode character: "+result.getMessage());
		}
		byte[] raw = (byte[]) result.getReturnValue();

		// Get existing attachment or create attachment
		Attachment attach = getFirstAttachment(Type.CHARACTER, Format.RULESPECIFIC);
		if (attach==null) {
			CharacterProviderLoader.getCharacterProvider().addAttachment(this, Type.CHARACTER, Format.RULESPECIFIC, null, raw);
		} else {
			attach.setData(raw);
			CharacterProviderLoader.getCharacterProvider().modifyAttachment(this, attach);			
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgtool.character.CharacterHandle#getLastModified()
	 */
	@Override
	public Date getLastModified() {
		try {
			return new Date(Files.getLastModifiedTime(path).toMillis());
		} catch (Exception e) {
			// logger.error("Failed to access file time",e);
			return new Date(0);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#setLastModified(java.util.Date)
	 */
	@Override
	public void setLastModified(Date timestamp) {
		try {
			Files.setLastModifiedTime(path, FileTime.fromMillis(timestamp.getTime()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	//-------------------------------------------------------------------
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the ruleIdentifier
	 */
	public RoleplayingSystem getRuleIdentifier() {
		return ruleIdentifier;
	}

	//-------------------------------------------------------------------
	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	//-------------------------------------------------------------------
	private AttachmentEntry getEntry(String filename) {
		for (AttachmentEntry entry : index) {
			Attachment chk = attachments.get(entry);
			logger.debug("Compare with "+chk+" : "+filename);
			if (chk.getFilename().equals(filename))
				return entry;
		}
		return null;
	}

	//-------------------------------------------------------------------
	public void addAttachment(Attachment att) {
		if (att==null)
			throw new NullPointerException();

		AttachmentEntry entry = getEntry(att.getFilename());
		if (entry==null) {
			entry = new AttachmentEntry();
			index.add(entry);
		}
		entry.file = att.getFilename();
		entry.type = att.getType();
		
		attachments.put(entry,att);
	}

	//-------------------------------------------------------------------
	public void addLoadedAttachment(AttachmentEntry entry, Attachment att) {
		if (att==null)
			throw new NullPointerException();
		attachments.put(entry,att);
	}

	//-------------------------------------------------------------------
	public void removeAttachment(Attachment att) {
		logger.debug("removeAttachment("+att+")");
		AttachmentEntry entry = getEntry(att.getFilename());
		attachments.remove(entry);
		index.remove(entry);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#getAttachments()
	 */
	@Override
	public List<Attachment> getAttachments() {
		return new ArrayList<Attachment>(attachments.values());
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#getAttachments(de.rpgframework.character.CharacterHandle.Type)
	 */
	@Override
	public List<Attachment> getAttachments(Type type) {
		ArrayList<Attachment> ret = new ArrayList<Attachment>();
		for (Attachment att : getAttachments())
			if (att!=null && att.getType()==type)
				ret.add(att);
		
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#getFirstAttachment(de.rpgframework.character.CharacterHandle.Type, de.rpgframework.character.CharacterHandle.Format)
	 */
	@Override
	public Attachment getFirstAttachment(Type type, Format format) {
		for (Attachment att : getAttachments()) {
			if (att==null) {
				logger.error("Found a NullPointer as attachment for "+getName());
				attachments.remove(null);
			}
			if (att!=null && att.getType()==type && att.getFormat()==format)
				return att;
		}
		return null;
	}

	@Override
	public <T> T getFirstAttachmentParsed(Type type, Format format, Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#getImage()
	 */
	@Override
	public byte[] getImage() {
		Attachment att = getFirstAttachment(Type.CHARACTER, Format.IMAGE);
		if (att!=null)
			return att.getData();
		return null;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the index
	 */
	public AttachmentIndex getIndex() {
		return index;
	}

	//-------------------------------------------------------------------
	/**
	 * @param index the index to set
	 */
	public void setIndex(AttachmentIndex index) {
		this.index = index;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterHandle#getOwner()
	 */
	@Override
	public Player getOwner() {
		return null;
	}


}
