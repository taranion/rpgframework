package org.prelle.rpgframework.jfx.skin;

import org.prelle.rpgframework.jfx.CharacterDocumentView;
import org.prelle.rpgframework.jfx.Section.SectionType;
import org.prelle.rpgframework.jfx.SingleSection;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author Stefan Prelle
 *
 */
public class SingleSectionSkin extends SkinBase<SingleSection> {

	public static final String DEFAULT_STYLE_CLASS = "character-document-view";
	
	private Label heading;
	private HBox headingLine;
	private HBox buttons;
	private VBox bxContent;

	//-------------------------------------------------------------------
	public SingleSectionSkin(SingleSection control) {
		super(control);
		initComponents();
		initLayout();
		initStyle(SectionType.SINGLE);
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		heading = new Label(getSkinnable().getTitle());
		buttons = new HBox();
		bxContent = new VBox();
	}

	//-------------------------------------------------------------------
	private void initStyle(SectionType type) {
		heading.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title");
		if (type!=null && type==SectionType.LEFT)
			heading.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title-left");
		else if (type!=null && type==SectionType.RIGHT)
			heading.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title-right");
		bxContent.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-content");
		buttons.setStyle("-fx-spacing: 0.2em; -fx-background-color: -fx-outer-border;");
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		heading.setMaxWidth(Double.MAX_VALUE);
		headingLine = new HBox(heading, buttons);
		headingLine.setMaxHeight(Double.MAX_VALUE);
		headingLine.setAlignment(Pos.BOTTOM_LEFT);
		HBox.setHgrow(heading, Priority.ALWAYS);
		
		bxContent.setMaxWidth(Double.MAX_VALUE);
		bxContent.setMaxHeight(Double.MAX_VALUE);
		bxContent.getChildren().add(headingLine);
		if (getSkinnable().getContent()!=null) {
			getSkinnable().getContent().getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-section-content");
			bxContent.getChildren().add(getSkinnable().getContent());
			VBox.setVgrow(getSkinnable().getContent(), Priority.ALWAYS);
		}
		
		if (getSkinnable().getSettingsButton()!=null)
			buttons.getChildren().add(getSkinnable().getSettingsButton());
		if (getSkinnable().getDeleteButton()!=null)
			buttons.getChildren().add(getSkinnable().getDeleteButton());
		if (getSkinnable().getAddButton()!=null)
			buttons.getChildren().add(getSkinnable().getAddButton());
		if (getSkinnable().getOtherButton()!=null)
			buttons.getChildren().add(getSkinnable().getOtherButton());
		
		getChildren().add(bxContent);
		VBox.setVgrow(bxContent, Priority.ALWAYS);
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		heading.textProperty().bind(getSkinnable().titleProperty());
		getSkinnable().contentProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				bxContent.getChildren().remove(o);
			if (n!=null) {
				n.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-section-content");
				bxContent.getChildren().add(n);
				VBox.setVgrow(getSkinnable().getContent(), Priority.ALWAYS);
				VBox.setVgrow(n, Priority.ALWAYS);
			}
		});
		getSkinnable().settingsButtonProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				buttons.getChildren().remove(o);
			if (n!=null) {
				buttons.getChildren().add(0,n);
			}
		});
		getSkinnable().deleteButtonProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				buttons.getChildren().remove(o);
			if (n!=null) {
				buttons.getChildren().add(n);
			}
		});
		getSkinnable().addButtonProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				buttons.getChildren().remove(o);
			if (n!=null) {
				buttons.getChildren().add(n);
			}
		});
		getSkinnable().otherButtonProperty().addListener( (ov,o,n) -> {
			if (o!=null)
				buttons.getChildren().remove(o);
			if (n!=null) {
				buttons.getChildren().add(n);
			}
		});
	}
	
}
