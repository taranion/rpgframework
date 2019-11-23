/**
 * 
 */
package org.prelle.rpgframework.jfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import de.rpgframework.genericrpg.ModifyableValue;

/**
 * @author Stefan
 *
 */
public class ModifyableValueViewSkin<T> extends SkinBase<ModifyableValueView<T>> {

	private TableView<ModifyableValue<T>> content;
	
	private TableColumn<ModifyableValue<T>, String> colName;
	@SuppressWarnings("unused")
	private TableColumn<ModifyableValue<T>, Number> colStart;
	private TableColumn<ModifyableValue<T>, Number> colPoints;
	private TableColumn<ModifyableValue<T>, Number> colMod;
	private TableColumn<ModifyableValue<T>, Number> colValue;
	@SuppressWarnings("unused")
	private TableColumn<ModifyableValue<T>, Number> colCost;

	//--------------------------------------------------------------------
	public ModifyableValueViewSkin(ModifyableValueView<T> control) {
		super(control);
		initComponents();
		initLayout();
		
		getChildren().add(content); // Call getControlChildren of SkinBase
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		content = new TableView<ModifyableValue<T>>();
		content.itemsProperty().bind(getSkinnable().itemsProperty());
		
		colName  = new TableColumn<>("Name");
		colStart = new TableColumn<>("Start");
		colPoints= new TableColumn<>("Punkte");
		colMod   = new TableColumn<>("Mod.");
		colValue = new TableColumn<>("Wert");
		colCost  = new TableColumn<>("Kosten");
		
		colName.setCellValueFactory(new Callback<CellDataFeatures<ModifyableValue<T>,String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ModifyableValue<T>, String> param) {
				/*
				 * Get the display name for the value - either by
				 * calling the StringConverter if exists or simple
				 * toString()
				 */
				String name = param.getValue().getModifyable().toString();
				if (getSkinnable().getNameConverter()!=null) 
					name = getSkinnable().getNameConverter().toString(param.getValue().getModifyable());
				return new SimpleStringProperty(name);
			}
		});
		colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
		colMod.setCellValueFactory(new PropertyValueFactory<>("modifier"));
		colValue.setCellValueFactory(new PropertyValueFactory<>("modifiedValue"));
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		content.setMaxWidth(Double.MAX_VALUE);
	}

}
