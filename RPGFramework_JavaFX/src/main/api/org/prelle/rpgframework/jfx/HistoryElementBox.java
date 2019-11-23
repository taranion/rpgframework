package org.prelle.rpgframework.jfx;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.util.PropertyResourceBundle;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.genericrpg.HistoryElement;
import de.rpgframework.genericrpg.Reward;
import de.rpgframework.genericrpg.modification.Modification;
import de.rpgframework.genericrpg.modification.Modifyable;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.ProductService;
import de.rpgframework.products.ProductServiceLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class HistoryElementBox extends VBox {
	
	private final static DateFormat FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);
	
	//-------------------------------------------------------------------
	public HistoryElementBox(HistoryElement data, PropertyResourceBundle RES, StringConverter<Modification> converter, RoleplayingSystem rules) {
		setSpacing(10);
		getStyleClass().addAll("history-element");
		setMaxWidth(Double.MAX_VALUE);
		setAlignment(Pos.TOP_CENTER);

		// Heading
		Label label = new Label(data.getName());
		label.setWrapText(true);
		label.getStyleClass().add("text-subheader");
		getChildren().add(label);
		
		// Divide in left and right half
		VBox leftRight = new VBox(10);
		leftRight.setAlignment(Pos.TOP_CENTER);
		getChildren().add(leftRight);
		
		// Left half: cover			
		ImageView image = new ImageView();
		image.setFitWidth(150);
		image.setFitHeight(200);
		leftRight.getChildren().add(image);
		
		if (data.getAdventureID()!=null && ProductServiceLoader.getInstance()!=null) {
			ProductService prodServ = ProductServiceLoader.getInstance();
			Adventure adv = prodServ.getAdventure(rules, data.getAdventureID());
			if (adv!=null && adv.getCover()!=null) {
				Image img = new Image(new ByteArrayInputStream(adv.getCover()));
				image.setImage(img);
			}
		}
		
		// Right half: content
		VBox pane = new VBox();
		leftRight.getChildren().add(pane);
		
		// Date
		if (data.getStart()!=null) {
			String date = FORMAT.format(data.getStart());
			if (data.getStart().getTime()!=data.getEnd().getTime())
				date += " - "+FORMAT.format(data.getEnd());
			Label lblDate = new Label(date);
			lblDate.getStyleClass().add("text-body");
			pane.getChildren().add(lblDate);
		}

		Label lblRewards = new Label(RES.getString("label.history.gained"));
		lblRewards.getStyleClass().add("text-small-subheader");
		pane.getChildren().add(lblRewards);
		
		// Collect EP
		int sumEP = 0;
		for (Reward rew : data.getGained())
			sumEP += rew.getExperiencePoints();
		Label ep = new Label("   "+sumEP+" "+RES.getString("label.history.exp"));
		pane.getChildren().add(ep);
		
		// Other rewards
		for (Modifyable rew : data.getGained()) {
			for (Modification mod : rew.getModifications()) {
				String modString = (converter!=null)?converter.toString(mod):mod.toString();
				pane.getChildren().add(new Label("   "+modString));
			}
		}
		
		Label lblSpent = new Label(RES.getString("label.history.spent"));
		lblSpent.getStyleClass().add("text-small-subheader");
		pane.getChildren().add(lblSpent);
		
		// Points spent
		for (Modification mod : data.getSpent()) {
			String modString = (converter!=null)?converter.toString(mod):mod.toString();
			pane.getChildren().add(new Label("   "+mod.getExpCost()+" "+RES.getString("label.history.exp")+": "+modString));
		}
	}

}