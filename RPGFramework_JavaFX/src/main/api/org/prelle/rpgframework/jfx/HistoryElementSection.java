package org.prelle.rpgframework.jfx;

import java.util.List;
import java.util.PropertyResourceBundle;

import org.prelle.javafx.ScreenManagerProvider;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.genericrpg.HistoryElement;
import de.rpgframework.genericrpg.modification.Modification;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

/**
 * @author Stefan Prelle
 *
 */
public class HistoryElementSection extends ListSection<HistoryElement> {

	private RoleplayingSystem rules;
	private PropertyResourceBundle UI;
	private ObjectProperty<StringConverter<Modification>> converter = new SimpleObjectProperty<>();
	
//	protected ListView<HistoryElement> list;

	//-------------------------------------------------------------------
	/**
	 * @param provider
	 * @param title
	 * @param content
	 */
	public HistoryElementSection(ScreenManagerProvider provider, String title, PropertyResourceBundle UI, StringConverter<Modification> converter, RoleplayingSystem rules) {
		super(title, provider, UI);
		this.UI = UI;
		this.converter.set( converter );
		this.rules = rules;
		list.setCellFactory(lv -> new HistoryElementListCell(UI, this.converter, rules));
		setAddButton(null);
//		initComponents();
//		initLayout();
		list.setStyle("-fx-min-width: 35em; -fx-pref-width: 50em; -fx-pref-height: 55em;");
		list.setMaxHeight(Double.MAX_VALUE);
	}
	
//	//-------------------------------------------------------------------
//	private void initComponents() {
//		list = new ListView<HistoryElement>();
//		list.setCellFactory(lv -> new HistoryElementListCell(UI, converter, rules));
//	}
//	
//	//-------------------------------------------------------------------
//	private void initLayout() {
//		setContent(list);
//	}
//
//	//-------------------------------------------------------------------
//	public void setData(List<HistoryElement> data) {
//		list.getItems().setAll(data);
//		refresh();
//	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.Section#refresh()
	 */
	@Override
	public void refresh() {
		list.refresh();
	}

//	//-------------------------------------------------------------------
//	public ReadOnlyObjectProperty<HistoryElement> selectedProperty() {
//		return list.getSelectionModel().selectedItemProperty();
//	}

	//-------------------------------------------------------------------
	public void onAdd() {
		
	}

	//-------------------------------------------------------------------
	public void onDelete() {
		
	}

}
