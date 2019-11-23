package org.prelle.rpgframework.jfx;

import org.prelle.javafx.NodeWithTitle;
import org.prelle.javafx.ScreenManagerProvider;
import org.prelle.rpgframework.jfx.skin.SingleSectionSkin;

import de.rpgframework.genericrpg.ToDoElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

public abstract class SingleSection extends Section implements NodeWithTitle {

	public enum SectionType {
		SINGLE,
		LEFT,
		RIGHT,
	}
	
	private StringProperty titleProperty = new SimpleStringProperty();
	private ObjectProperty<Node> contentProperty = new SimpleObjectProperty<>();
	private ObservableList<ToDoElement> todoListProperty = FXCollections.observableArrayList();
	private ObjectProperty<Button> deleteButton = new SimpleObjectProperty<Button>();
	private ObjectProperty<Button> addButton = new SimpleObjectProperty<Button>();
	private ObjectProperty<Button> settingsButton = new SimpleObjectProperty<Button>();
	protected ScreenManagerProvider managerProvider;
	
	//-------------------------------------------------------------------
	public SingleSection(ScreenManagerProvider provider) {
		deleteButton = new SimpleObjectProperty<Button>();
		addButton = new SimpleObjectProperty<Button>();
		settingsButton = new SimpleObjectProperty<Button>();
		setSkin(new SingleSectionSkin(this));
		this.managerProvider = provider;
	}
	//-------------------------------------------------------------------
	public SingleSection(ScreenManagerProvider provider, String title, Node content) {
		this(provider);
		titleProperty.set(title);
		contentProperty.set(content);
	}
	
	//-------------------------------------------------------------------
	public StringProperty titleProperty() { return titleProperty; }
	public void setTitle(String value) { titleProperty.set(value); }
	public String getTitle() { return titleProperty.get(); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<Node> contentProperty() { return contentProperty; }
	public void setContent(Node value) { contentProperty.set(value); }
	public Node getContent() { return contentProperty.get(); }
	
	//-------------------------------------------------------------------
	public ObservableList<ToDoElement> getToDoList() { return todoListProperty; }

	//-------------------------------------------------------------------
	public ObjectProperty<Button> deleteButtonProperty() { return deleteButton; }
	public Button getDeleteButton() { return deleteButton.get(); }
	public void setDeleteButton(Button value) { this.deleteButton.set(value); }

	//-------------------------------------------------------------------
	public ObjectProperty<Button> addButtonProperty() { return addButton; }
	public Button getAddButton() { return addButton.get(); }
	public void setAddButton(Button value) { this.addButton.set(value); }

	//-------------------------------------------------------------------
	public ObjectProperty<Button> settingsButtonProperty() { return settingsButton; }
	public Button getSettingsButton() { return settingsButton.get(); }
	public void setSettingsButton(Button value) { this.settingsButton.set(value); }

	//-------------------------------------------------------------------
	public ScreenManagerProvider getManagerProvider() { return managerProvider; }
	
}