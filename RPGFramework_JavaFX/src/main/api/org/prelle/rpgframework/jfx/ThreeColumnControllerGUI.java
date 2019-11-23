/**
 * 
 */
package org.prelle.rpgframework.jfx;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import de.rpgframework.character.HardcopyPluginData;
import de.rpgframework.genericrpg.ModifyableValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * This GUI component is used to operate with DynamicModificationController.
 * The first column lists the available options that can be selected.
 * The second column lists the already selected items and may offer to
 * modify their values
 * The third column is reserved for additional informations.
 * 
 * @author Stefan
 *
 */
public class ThreeColumnControllerGUI<T extends HardcopyPluginData, M extends ModifyableValue<T>> extends Control {

	private ObjectProperty<ObservableList<T>> itemsAvailable;
	private ObjectProperty<ObservableList<M>> itemsSelected;
	private ObjectProperty<Node> trashArea;
	
	private StringProperty placeholderAvailable;
	private StringProperty placeholderSelected;
	
	private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryAvailable;
	private ObjectProperty<Callback<ListView<M>, ListCell<M>>> cellFactorySelected;
	
	private ObjectProperty<T> selected;
	private BooleanProperty headersVisible;
	private StringProperty column1Header;
	private StringProperty column2Header;
	private StringProperty column3Header;
	
	private ObjectProperty<BiConsumer<MouseEvent,T>> dragAvailableStarted;
	private ObjectProperty<BiConsumer<MouseEvent,M>> dragSelectedStarted;
	private ObjectProperty<Consumer<DragEvent>> dropAtAvailable;
	private ObjectProperty<Consumer<DragEvent>> dropAtSelected;
	
	//--------------------------------------------------------------------
	/**
	 */
	public ThreeColumnControllerGUI() {
		itemsAvailable = new SimpleObjectProperty<>(FXCollections.observableArrayList());
		itemsSelected  = new SimpleObjectProperty<>(FXCollections.observableArrayList());
		trashArea      = new SimpleObjectProperty<>();
		
		placeholderAvailable = new SimpleStringProperty();
		placeholderSelected  = new SimpleStringProperty();
		cellFactoryAvailable = new SimpleObjectProperty<>();
		cellFactorySelected  = new SimpleObjectProperty<>();
		
		selected = new SimpleObjectProperty<T>();
		headersVisible = new SimpleBooleanProperty();
		column1Header  = new SimpleStringProperty();
		column2Header  = new SimpleStringProperty();
		column3Header  = new SimpleStringProperty();
		
		dragAvailableStarted = new SimpleObjectProperty<>();
		dragSelectedStarted  = new SimpleObjectProperty<>();
		dropAtAvailable = new SimpleObjectProperty<>();
		dropAtSelected  = new SimpleObjectProperty<>();
		
		setSkin(new ThreeColumnControllerGUISkin<>(this));
	}
	
	//--------------------------------------------------------------------
	public ObjectProperty<ObservableList<T>> itemsAvailableProperty() {return itemsAvailable;}
	public ObservableList<T> getItemsAvailable() { return itemsAvailable.get(); }
	public void setItemsAvailable(ObservableList<T> val) { itemsAvailable.set(val); }
	
	//--------------------------------------------------------------------
	public ObjectProperty<ObservableList<M>> itemsSelectedProperty() {return itemsSelected;}
	public ObservableList<M> getItemsSelected() { return itemsSelected.get(); }
	public void setItemsSelected(ObservableList<M> val) { itemsSelected.set(val); }
	
	//--------------------------------------------------------------------
	public ObjectProperty<Node> trashAreaProperty() {return trashArea;}
	public Node getTrashArea() { return trashArea.get(); }
	public void setTrashArea(Node val) { trashArea.set(val); }
	
	//--------------------------------------------------------------------
	public ObjectProperty<T> selectedProperty() {return selected;}
	public T getSelected() { return selected.get(); }
	public void setSelected(T val) { selected.set(val); }
	
	//--------------------------------------------------------------------
	public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryAvailableProperty() {return cellFactoryAvailable;}
	public Callback<ListView<T>, ListCell<T>> getCellFactoryAvailable() { return cellFactoryAvailable.get(); }
	public void setCellFactoryAvailable(Callback<ListView<T>, ListCell<T>> val) { cellFactoryAvailable.set(val); }
	
	//--------------------------------------------------------------------
	public ObjectProperty<Callback<ListView<M>, ListCell<M>>> cellFactorySelectedProperty() {return cellFactorySelected;}
	public Callback<ListView<M>, ListCell<M>> getCellFactorySelected() { return cellFactorySelected.get(); }
	public void setCellFactorySelected(Callback<ListView<M>, ListCell<M>> val) { cellFactorySelected.set(val); }
	
	//--------------------------------------------------------------------
	public StringProperty placeholderAvailableProperty() {return placeholderAvailable;}
	public String getPlaceholderAvailable() { return placeholderAvailable.get(); }
	public void setPlaceholderAvailable(String val) { placeholderAvailable.set(val); }
	
	//--------------------------------------------------------------------
	public StringProperty placeholderSelectedProperty() {return placeholderSelected;}
	public String getPlaceholderSelected() { return placeholderSelected.get(); }
	public void setPlaceholderSelected(String val) { placeholderSelected.set(val); }
	
	//--------------------------------------------------------------------
	public BooleanProperty headersVisibleProperty() {return headersVisible;}
	public boolean isHeadersVisible() { return headersVisible.get(); }
	public void setHeadersVisible(boolean val) { headersVisible.set(val); }
	
	//--------------------------------------------------------------------
	public StringProperty column1HeaderProperty() {return column1Header;}
	public String getColumn1Header() { return column1Header.get(); }
	public void setColumn1Header(String val) { column1Header.set(val); }
	
	//--------------------------------------------------------------------
	public StringProperty column2HeaderProperty() {return column2Header;}
	public String getColumn2Header() { return column2Header.get(); }
	public void setColumn2Header(String val) { column2Header.set(val); }
	
	//--------------------------------------------------------------------
	public StringProperty column3HeaderProperty() {return column3Header;}
	public String getColumn3Header() { return column3Header.get(); }
	public void setColumn3Header(String val) { column3Header.set(val); }
	
	//--------------------------------------------------------------------
	ObjectProperty<BiConsumer<MouseEvent,T>> dragAvailableProperty() {return dragAvailableStarted;}
	BiConsumer<MouseEvent,T> getDragAvailable() { return dragAvailableStarted.get(); }
	public void setDragAvailable(BiConsumer<MouseEvent,T> val) { dragAvailableStarted.set(val); }
	
	//--------------------------------------------------------------------
	ObjectProperty<BiConsumer<MouseEvent,M>> dragSelectedProperty() {return dragSelectedStarted;}
	BiConsumer<MouseEvent,M> getDragSelected() { return dragSelectedStarted.get(); }
	public void setDragSelected(BiConsumer<MouseEvent,M> val) { dragSelectedStarted.set(val); }
	
	//--------------------------------------------------------------------
	ObjectProperty<Consumer<DragEvent>> dropAtAvailableDProperty() {return dropAtAvailable;}
	Consumer<DragEvent> getDropAtAvailable() { return dropAtAvailable.get(); }
	public void setDropAtAvailable(Consumer<DragEvent> val) { dropAtAvailable.set(val); }
	
	//--------------------------------------------------------------------
	ObjectProperty<Consumer<DragEvent>> dropAtSelectedDProperty() {return dropAtSelected;}
	Consumer<DragEvent> getDropAtSelected() { return dropAtSelected.get(); }
	public void setDropAtSelected(Consumer<DragEvent> val) { dropAtSelected.set(val); }

}
