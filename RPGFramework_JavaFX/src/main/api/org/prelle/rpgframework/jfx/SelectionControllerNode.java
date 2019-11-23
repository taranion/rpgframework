/**
 * 
 */
package org.prelle.rpgframework.jfx;

import java.util.List;
import java.util.function.BiFunction;

import de.rpgframework.genericrpg.SelectableItem;
import de.rpgframework.genericrpg.SelectedValue;
import de.rpgframework.genericrpg.SelectionController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * @author prelle
 *
 */
public class SelectionControllerNode<T extends SelectableItem, V extends SelectedValue<T>> extends Control {

	private static final String DEFAULT_STYLE_CLASS = "selection-control";
	
	private ObjectProperty<SelectionController<T,V>> control;
	
	private ObjectProperty<ObservableList<T>> availableProperty = new SimpleObjectProperty<ObservableList<T>>(FXCollections.observableArrayList());
	private ObjectProperty<ObservableList<V>> selectedProperty = new SimpleObjectProperty<ObservableList<V>>(FXCollections.observableArrayList());
	
	private ObjectProperty<Callback<ListView<T>, ListCell<T>>> availableCellFactoryProperty;
	private ObjectProperty<Callback<ListView<V>, ListCell<V>>> selectedCellFactoryProperty;
	private ObjectProperty<BiFunction<T, List<?>, ?>> optionCallbackProperty;
	
	private BooleanProperty showHeadingsProperty;
	private StringProperty  availableHeadingProperty;
	private StringProperty  selectedHeadingProperty;
	private StringProperty  availablePlaceholderProperty;
	private StringProperty  selectedPlaceholderProperty;
	
	private ObjectProperty<T> showHelpForProperty;
	private StringProperty  availableStyleProperty;
	private StringProperty  selectedStyleProperty;

	//-------------------------------------------------------------------
	public SelectionControllerNode(SelectionController<T,V> control) {
		if (control==null)
			throw new NullPointerException("Controller may not be null");
		this.control = new SimpleObjectProperty<SelectionController<T,V>>(control);
		availableCellFactoryProperty = new SimpleObjectProperty<>();
		selectedCellFactoryProperty = new SimpleObjectProperty<>();
		optionCallbackProperty = new SimpleObjectProperty<>();
		showHeadingsProperty = new SimpleBooleanProperty(true);
		availableHeadingProperty = new SimpleStringProperty(RPGFrameworkJFXConstants.UI.getString("label.available"));
		selectedHeadingProperty  = new SimpleStringProperty(RPGFrameworkJFXConstants.UI.getString("label.selected"));
		availablePlaceholderProperty = new SimpleStringProperty();
		selectedPlaceholderProperty  = new SimpleStringProperty();
		showHelpForProperty = new SimpleObjectProperty<>();
		availableStyleProperty = new SimpleStringProperty();
		selectedStyleProperty  = new SimpleStringProperty();
		
		setSkin(new SelectionControlTwoColumnSkin<>(this));
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		
		
		this.control.addListener( (ov,o,n) -> refresh());
		refresh();
	}

	//-------------------------------------------------------------------
	public ObjectProperty<SelectionController<T,V>> getControllerProperty() { return control;}
	public SelectionController<T,V> getController() { return control.get();}
	public void setController(SelectionController<T,V> value) { control.set(value); }

	//-------------------------------------------------------------------
	public void refresh() {
		availableProperty.get().clear();
		availableProperty.get().addAll(control.get().getAvailable());
		selectedProperty.get().clear();
		selectedProperty.get().addAll(control.get().getSelected());
	}
	
	//-------------------------------------------------------------------
	public ObjectProperty<ObservableList<T>> availableProperty() { return availableProperty; }
	public ObservableList<T> getAvailable() { return availableProperty.get(); }
	public void setAvailable(ObservableList<T> value) { availableProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<ObservableList<V>> selectedProperty() { return selectedProperty; }
	public ObservableList<V> getSelected() { return selectedProperty.get(); }
	public void setSelected(ObservableList<V> value) { selectedProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<BiFunction<T, List<?>, ?>> optionCallbackProperty() { return optionCallbackProperty; }
	public BiFunction<T, List<?>, ?> getOptionCallback() { return optionCallbackProperty.get(); }
	public void setOptionCallback(BiFunction<T, List<?>, ?> value) { optionCallbackProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<Callback<ListView<T>, ListCell<T>>> availableCellFactoryProperty() { return availableCellFactoryProperty; }
	public Callback<ListView<T>, ListCell<T>> getAvailableCellFactory() { return availableCellFactoryProperty.get(); }
	public void setAvailableCellFactory(Callback<ListView<T>, ListCell<T>> value) { availableCellFactoryProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<Callback<ListView<V>, ListCell<V>>> selectedCellFactoryProperty() { return selectedCellFactoryProperty; }
	public Callback<ListView<V>, ListCell<V>> getSelectedCellFactory() { return selectedCellFactoryProperty.get(); }
	public void setSelectedCellFactory(Callback<ListView<V>, ListCell<V>> value) { selectedCellFactoryProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public BooleanProperty showHeadingsProperty() { return showHeadingsProperty; }
	public boolean getShowHeadings() { return showHeadingsProperty.get(); }
	public void setShowHeadings(boolean value) { showHeadingsProperty.set(value); }
	
	//-------------------------------------------------------------------
	public StringProperty availableHeadingProperty() { return availableHeadingProperty; }
	public String getAvailableHeading() { return availableHeadingProperty.get(); }
	public void setAvailableHeading(String value) { availableHeadingProperty.set(value); }
	
	//-------------------------------------------------------------------
	public StringProperty selectedHeadingProperty() { return selectedHeadingProperty; }
	public String getSelectedHeading() { return selectedHeadingProperty.get(); }
	public void setSelectedHeading(String value) { selectedHeadingProperty.set(value); }
	
	//-------------------------------------------------------------------
	public StringProperty availablePlaceholderProperty() { return availablePlaceholderProperty; }
	public String getAvailablePlaceholder() { return availablePlaceholderProperty.get(); }
	public void setAvailablePlaceholder(String value) { availablePlaceholderProperty.set(value); }
	
	//-------------------------------------------------------------------
	public StringProperty selectedPlaceholderProperty() { return selectedPlaceholderProperty; }
	public String getSelectedPlaceholder() { return selectedPlaceholderProperty.get(); }
	public void setSelectedPlaceholder(String value) { selectedPlaceholderProperty.set(value); }
	
	//-------------------------------------------------------------------
	public ObjectProperty<T> showHelpForProperty() { return showHelpForProperty; }
	public T getShowHelpFor() { return showHelpForProperty.get(); }
	public void setShowHelpFor(T value) { showHelpForProperty.setValue(value); }
	
	//-------------------------------------------------------------------
	public StringProperty availableStyleProperty() { return availableStyleProperty; }
	public String getAvailableStyle() { return availableStyleProperty.get(); }
	public void setAvailableStyle(String value) { availableStyleProperty.set(value); }
	
	//-------------------------------------------------------------------
	public StringProperty selectedStyleProperty() { return selectedStyleProperty; }
	public String getSelectedStyle() { return selectedStyleProperty.get(); }
	public void setSelectedStyle(String value) { selectedStyleProperty.set(value); }

}
