/**
 * 
 */
package org.prelle.rpgframework.jfx;

import de.rpgframework.character.HardcopyPluginData;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class HelpTextPane extends VBox {
	
	private Label heading;
	private Label pageReference;
	private Label helpText;

	//-------------------------------------------------------------------
	public HelpTextPane() {
		getStyleClass().add("description-text");
		
		heading = new Label();
		heading.setWrapText(true);
		pageReference = new Label();
		helpText = new Label();
		helpText.setWrapText(true);
		
		heading.getStyleClass().add("title");
		pageReference.getStyleClass().add("subtitle");
		helpText.getStyleClass().add("body");
		
		getChildren().addAll(heading, pageReference, helpText);
		VBox.setMargin(helpText, new Insets(20, 0, 0, 0));
	}

	//-------------------------------------------------------------------
	public void setData(HardcopyPluginData value) {
		if (value==null) {
			heading.setText(null);
			pageReference.setText(null);
			helpText.setText(null);
		} else {
			heading.setText(value.getName());
			pageReference.setText(value.getProductName()+" "+value.getPage());
			helpText.setText(value.getHelpText());
		}
	}

	//-------------------------------------------------------------------
	public void setData(String name, String ref, String text) {
			heading.setText(name);
			pageReference.setText(ref);
			helpText.setText(text);
	}

}
