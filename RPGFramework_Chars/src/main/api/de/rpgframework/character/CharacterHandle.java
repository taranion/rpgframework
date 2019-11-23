/**
 * 
 */
package de.rpgframework.character;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import de.rpgframework.core.Player;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public interface CharacterHandle {
	
	public enum Format {
		RULESPECIFIC,
		RULESPECIFIC_EXTERNAL,
		HTML,
		TEXT,
		PDF,
		IMAGE
	}
	
	public enum Type {
		CHARACTER,
		BACKGROUND,
		REPORT,
	}

	public String getName();
	
	public Player getOwner();
	
	public RoleplayingSystem getRuleIdentifier();
	
	//--------------------------------------------------------------------
	public List<Attachment> getAttachments();
	
	//--------------------------------------------------------------------
	public List<Attachment> getAttachments(Type type);
	
	//--------------------------------------------------------------------
	public Attachment getFirstAttachment(Type type, Format format);
	
	//--------------------------------------------------------------------
	public <T> T getFirstAttachmentParsed(Type type, Format format, Class<T> cls);
	
	//--------------------------------------------------------------------
	public RuleSpecificCharacterObject getCharacter();
	
	//--------------------------------------------------------------------
	public Path getPath();
	
	//--------------------------------------------------------------------
	/**
	 * Convenience method for getFirstAttachment(CHARACTER, IMAGE)
	 */
	public byte[] getImage();
	
	//--------------------------------------------------------------------
	/**
	 * Update the handle with the character. This leads to storage
	 * on disk.
	 * @param charac
	 * @throws IOException
	 */
	public void setCharacter(RuleSpecificCharacterObject charac) throws IOException;
	
	public Date getLastModified();
	
	public void setLastModified(Date timestamp);
	
}
