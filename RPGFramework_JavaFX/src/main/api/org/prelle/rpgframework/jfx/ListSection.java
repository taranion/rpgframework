package org.prelle.rpgframework.jfx;

import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.ScreenManagerProvider;
import org.prelle.javafx.SymbolIcon;
import org.prelle.rpgframework.jfx.SingleSection;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;

/**
 * @author Stefan Prelle
 *
 */
public abstract class ListSection<T> extends SingleSection {

	protected final static Logger logger = LogManager.getLogger("rpgframework.jfx");

	private PropertyResourceBundle RES;

	protected ListView<T> list;
	protected ScreenManagerProvider provider;

	private ObjectProperty<T> showHelpFor = new SimpleObjectProperty<>();

	//-------------------------------------------------------------------
	public ListSection(String title, ScreenManagerProvider provider, PropertyResourceBundle RES) {
		super(provider, title, null);
		this.RES = RES;
		this.provider = provider;
		
		initComponents();
		setContent(list);
		
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		list = new ListView<T>();
		setDeleteButton( new Button(null, new SymbolIcon("delete")) );
		setAddButton( new Button(null, new SymbolIcon("add")) );
		getDeleteButton().setTooltip(new Tooltip(RES.getString("button.delete.tooltip")));
		getAddButton().setTooltip(new Tooltip(RES.getString("button.add.tooltip")));
		getDeleteButton().setDisable(true);
		settingsButtonProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				o.setOnAction(null);
			if (n!=null)
				n.setOnAction(ev -> onSettings());
		});
	}

	//-------------------------------------------------------------------
	protected void initInteractivity() {
		showHelpFor.bind(list.getSelectionModel().selectedItemProperty());
		list.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> {
			logger.warn("Select "+n+"    delete="+getDeleteButton());
			if (getDeleteButton()!=null) {
				logger.warn("set delete button active ="+(n==null));
				getDeleteButton().setDisable(n==null);
			}
			});
		getAddButton().setOnAction(ev -> onAdd());
		getDeleteButton().setOnAction(ev -> onDelete());
	}

	//-------------------------------------------------------------------
	public void setData(List<T> data) {
		list.getItems().clear();
		list.getItems().addAll(data);
	}

	//-------------------------------------------------------------------
	public ReadOnlyObjectProperty<T> showHelpForProperty() {
		return showHelpFor;
	}

	//-------------------------------------------------------------------
	public ListView<T> getListView() {
		return list;
	}

	//-------------------------------------------------------------------
	protected abstract void onAdd();

	//-------------------------------------------------------------------
	protected abstract void onDelete();

	//-------------------------------------------------------------------
	protected void onSettings() {
		logger.warn("onSettings() not overloaded in "+getClass());
	}

}
