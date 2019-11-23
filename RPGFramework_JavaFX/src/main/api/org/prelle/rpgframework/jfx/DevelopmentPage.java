package org.prelle.rpgframework.jfx;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.PropertyResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.prelle.javafx.AppBarButton;
import org.prelle.javafx.AppBarToggleButton;
import org.prelle.javafx.SymbolIcon;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.genericrpg.HistoryElement;
import de.rpgframework.genericrpg.modification.Modification;
import javafx.beans.property.BooleanProperty;
import javafx.util.StringConverter;

/**
 * @author prelle
 *
 */
public abstract class DevelopmentPage extends CharacterDocumentView {

	private final static PropertyResourceBundle UI = RPGFrameworkJFXConstants.UI;

	private enum SortOrder {
		OLDEST_FIRST,
		NEWEST_FIRST;

		public String toString() {
			return UI.getString("screen.development.sortorder."+this.name().toLowerCase());
		}
	}

	private RoleplayingSystem rules;
	protected HistoryElementSection history;

	private AppBarButton btnAddExp;
	private AppBarToggleButton cbAggregate;
	private AppBarToggleButton btnSort;

	private StringConverter<Modification> converter;
	private SortOrder order;

	//-------------------------------------------------------------------
	/**
	 */
	public DevelopmentPage(PropertyResourceBundle uiResources, RoleplayingSystem rules) {
		super();
		this.rules = rules;

		initComponents();
		initLayout();
		initInteractivity();
	}

	//--------------------------------------------------------------------
	public BooleanProperty aggregatedProperty() { return cbAggregate.selectedProperty(); }
	public void setAggregated(boolean value) { cbAggregate.selectedProperty().setValue(value); }
	public boolean shallBeAggregated() { return cbAggregate.selectedProperty().get(); }

	//--------------------------------------------------------------------
	private void initComponents() {
		order = SortOrder.OLDEST_FIRST;
		history = new HistoryElementSection(this, UI.getString("label.development"), UI, converter, rules);

		/*
		 * Add Button for XP
		 */
		btnAddExp = new AppBarButton(UI.getString("label.addreward"), new SymbolIcon("add"));

		btnSort = new AppBarToggleButton(order.toString(), new SymbolIcon("sort"));

		cbAggregate = new AppBarToggleButton(UI.getString("screen.development.aggregate"), new SymbolIcon("backtowindow"));
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		getCommandBar().setOpen(true);
		getSectionList().add(history);

		getCommandBar().getPrimaryCommands().add(btnAddExp);
		getCommandBar().getPrimaryCommands().add(btnSort);
//		getCommandBar().getPrimaryCommands().add(cbAggregate);  // Unused, because it is buggy
	}
	//-------------------------------------------------------------------
	private void initInteractivity() {
		history.selectedProperty().addListener( (ov,o,n) -> {
			if (n!=null) {
				HistoryElementBox box = new HistoryElementBox(n, UI, converter, rules);
//				setDescriptionHeading(n.getName());
				setDescriptionNode(box);
			} else {
				setDescriptionHeading(null);
				setDescriptionNode(null);
			}
		});
		btnAddExp.setOnAction(event -> openAdd());

		btnSort.setOnAction(ev -> {
			if (order==SortOrder.NEWEST_FIRST) {
				order = SortOrder.OLDEST_FIRST;
			} else {
				order = SortOrder.NEWEST_FIRST;
			}
			btnSort.setText(order.toString());
			
			LogManager.getLogger("rpgframework.jfx").info("Change sort order");
			Collections.sort(history.list.getItems(), new Comparator<HistoryElement>() {
				public int compare(HistoryElement o1, HistoryElement o2) {
					Date d1 = (o1.getEnd()!=null)?o1.getEnd():o1.getStart();
					Date d2 = (o2.getEnd()!=null)?o2.getEnd():o2.getStart();
					if (d1==null || d2==null) return 0;
					return d1.compareTo(d2);
				}
			});
			if (order==SortOrder.NEWEST_FIRST) {
				Collections.reverse(history.list.getItems());
			}
		});

		cbAggregate.setOnAction(ev -> {
			LogManager.getLogger("rpgframework.jfx").info("CLICK2");
			refresh();
			});
	}

	//-------------------------------------------------------------------
	public void setConverter(StringConverter<Modification> converter) {
		this.converter = converter;
	}

	//--------------------------------------------------------------------
	public abstract void refresh();

	//--------------------------------------------------------------------
	public abstract HistoryElement openAdd();

	//--------------------------------------------------------------------
	/**
	 * @return TRUE, if a changes was made
	 */
	public abstract boolean openEdit(HistoryElement elem);

}
