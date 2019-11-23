package org.prelle.rpgframework.jfx;

import org.prelle.javafx.NodeWithTitleSkeleton;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * @author Stefan Prelle
 *
 */
public class SectionContent extends NodeWithTitleSkeleton {
	
	private ObjectProperty<Button> deleteButton;
	private ObjectProperty<Button> addButton;

	//-------------------------------------------------------------------
	public SectionContent() {
		deleteButton = new SimpleObjectProperty<Button>();
		addButton = new SimpleObjectProperty<Button>();
	}

	//-------------------------------------------------------------------
	/**
	 * @param title
	 * @param content
	 */
	public SectionContent(String title, Node content) {
		super(title, content);
		deleteButton = new SimpleObjectProperty<Button>();
		addButton = new SimpleObjectProperty<Button>();
	}

	//-------------------------------------------------------------------
	public ObjectProperty<Button> deleteButtonProperty() { return deleteButton; }
	public Button getDeleteButton() { return deleteButton.get(); }
	public void setDeleteButton(Button value) { this.deleteButton.set(value); }

	//-------------------------------------------------------------------
	public ObjectProperty<Button> addButtonProperty() { return addButton; }
	public Button getAddButton() { return addButton.get(); }
	public void setAddButton(Button value) { this.addButton.set(value); }
	
}
