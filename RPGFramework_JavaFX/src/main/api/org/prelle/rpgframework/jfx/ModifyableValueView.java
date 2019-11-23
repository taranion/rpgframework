/**
 * 
 */
package org.prelle.rpgframework.jfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.StringConverter;
import de.rpgframework.genericrpg.ModifyableValue;

/**
 * @author Stefan
 *
 */
public class ModifyableValueView<T> extends Control {
	
	private ObservableValue<ObservableList<ModifyableValue<T>>> items;
	
	private BooleanProperty startColumnVisible;
	private StringConverter<T> nameConverter;
	
	//--------------------------------------------------------------------
	/**
	 */
	public ModifyableValueView() {
		items = new SimpleObjectProperty<ObservableList<ModifyableValue<T>>>(FXCollections.observableArrayList());
		startColumnVisible = new SimpleBooleanProperty(false);
		
	}
	
	//--------------------------------------------------------------------
	public ObservableValue<ObservableList<ModifyableValue<T>>> itemsProperty() {
		return items;
	}
	
	//--------------------------------------------------------------------
	public ObservableList<ModifyableValue<T>> getItems() {
		return items.getValue();
	}
	
	//--------------------------------------------------------------------
	public BooleanProperty startColumnVisibleProperty() {return startColumnVisible;}
	public boolean isStartColumnVisible() {
		return startColumnVisible.get();
	}
	public void setStartColumnVisible(boolean val) {
		startColumnVisible.set(val);
	}

	//--------------------------------------------------------------------
	/**
	 * @return the nameConverter
	 */
	public StringConverter<T> getNameConverter() {
		return nameConverter;
	}

	//--------------------------------------------------------------------
	/**
	 * @param nameConverter the nameConverter to set
	 */
	public void setNameConverter(StringConverter<T> nameConverter) {
		this.nameConverter = nameConverter;
	}

}
