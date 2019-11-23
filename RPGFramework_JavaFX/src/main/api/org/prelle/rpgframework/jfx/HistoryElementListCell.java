package org.prelle.rpgframework.jfx;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.util.PropertyResourceBundle;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.genericrpg.HistoryElement;
import de.rpgframework.genericrpg.modification.Modification;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.ProductService;
import de.rpgframework.products.ProductServiceLoader;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * @author Stefan Prelle
 *
 */
public class HistoryElementListCell extends ListCell<HistoryElement> {

	private PropertyResourceBundle UI;
	private ReadOnlyObjectProperty<StringConverter<Modification>> converter;
	
	private RoleplayingSystem rules;
	private Label lbName;
	private Label lbDate;
	private ImageView iView;
	private Label lbExp;
	private HBox bxCollapsed;

	//-------------------------------------------------------------------
	public HistoryElementListCell(PropertyResourceBundle UI, ReadOnlyObjectProperty<StringConverter<Modification>> converter, RoleplayingSystem rules) {
		this.UI = UI;
		this.converter = converter;
		this.rules = rules;
		lbName = new Label();
		lbDate = new Label();
		lbExp  = new Label();
		iView  = new ImageView();
		iView.setFitHeight(80);
		iView.setPreserveRatio(true);
		
		VBox bxNameDate = new VBox(5, lbName, lbDate);
		Region space = new Region();
		space.setMaxWidth(Double.MAX_VALUE);
		bxCollapsed = new HBox(iView, bxNameDate, space, lbExp);
		HBox.setHgrow(space, Priority.ALWAYS);
		
		lbExp.setStyle("-fx-font-size: 200%; -fx-font-weight: bold");
		lbName.getStyleClass().add("base");
	}

	//-------------------------------------------------------------------
	public void updateItem(HistoryElement item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			lbName.setText(item.getName());
			if (item.getEnd()!=null)
				lbDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(item.getEnd()));
			else if (item.getStart()!=null)
				lbDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(item.getStart()));
			lbExp.setText(String.valueOf(item.getTotalExperience()));
			
			if (item.getAdventureID()!=null && ProductServiceLoader.getInstance()!=null) {
				ProductService prodServ = ProductServiceLoader.getInstance();
				Adventure adv = prodServ.getAdventure(rules, item.getAdventureID());
				if (adv!=null && adv.getCover()!=null) {
					Image img = new Image(new ByteArrayInputStream(adv.getCover()));
					iView.setImage(img);
				} else
					iView.setImage(null);
			}
			
//			HistoryElementBox box = new HistoryElementBox(item,UI, converter.get());
//			box.setUserData(item);
//
//			TitledPane pane = new TitledPane(name, box);
//			pane.setExpanded(false);
			setGraphic(bxCollapsed);
		}
	}

}
