/**
 *
 */
package org.prelle.rpgframework.jfx.skin;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.ResponsiveControl;
import org.prelle.javafx.WindowMode;
import org.prelle.rpgframework.jfx.CharacterDocumentView;
import org.prelle.rpgframework.jfx.DoubleSection;
import org.prelle.rpgframework.jfx.FreePointsNode;
import org.prelle.rpgframework.jfx.RPGFrameworkJFXConstants;
import org.prelle.rpgframework.jfx.Section;

import de.rpgframework.genericrpg.ToDoElement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class CharacterDocumentViewSectionPane extends StackPane implements ResponsiveControl {

	private final static Logger logger = LogManager.getLogger(RPGFrameworkJFXConstants.BASE_LOGGER_NAME);

	@FXML
	private ObservableList<Section> sectionListProperty = FXCollections.observableArrayList();
	@FXML
	private IntegerProperty pointsFreeProperty = new SimpleIntegerProperty();
	@FXML
	private StringProperty pointsNameProperty = new SimpleStringProperty();

	private GridPane col1And2Grid;
	private FreePointsNode points;
	private ScrollPane scroll;

	//-------------------------------------------------------------------
	public CharacterDocumentViewSectionPane() {
		super();
		initComponents();
		initLayout();
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		col1And2Grid = new GridPane();
		col1And2Grid.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-grid");
		scroll = new ScrollPane(col1And2Grid);
		scroll.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-scroll");

		points = new FreePointsNode();
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		scroll.setFitToWidth(true);

		AnchorPane pointsLayer = new AnchorPane(points);
		AnchorPane.setLeftAnchor(points, 0.0);
		AnchorPane.setTopAnchor(points, 0.0);
		pointsLayer.setMouseTransparent(true);
		getChildren().addAll(scroll, pointsLayer);
		StackPane.setAlignment(points, Pos.TOP_LEFT);
		StackPane.setMargin(points, new Insets(20));

		reloadSections();
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		getSectionList().addListener(new ListChangeListener<Section>() {
			public void onChanged(Change<? extends Section> c) {
				reloadSections();
			}
		});

		pointsFreeProperty().addListener( (ov,o,n) -> points.setPoints( (int) n));
		pointsNameProperty().addListener( (ov,o,n) -> points.setName(n));
	}

	//-------------------------------------------------------------------
	public ObservableList<Section> getSectionList() { return sectionListProperty; }

	//-------------------------------------------------------------------
	public IntegerProperty pointsFreeProperty() { return pointsFreeProperty; }
	public void setPointsFree(int value) { pointsFreeProperty.set(value); }
	public int getPointsFree() { return pointsFreeProperty.get(); }

	//-------------------------------------------------------------------
	public StringProperty pointsNameProperty() { return pointsNameProperty; }
	public void setPointsNameProperty(String value) { pointsNameProperty.set(value); }
	public String getPointsNameProperty() { return pointsNameProperty.get(); }

	//-------------------------------------------------------------------
	private static List<Label> convertToDoElements(List<ToDoElement> todos) {
		List<Label> ret = new ArrayList<>();
		for (ToDoElement todo : todos) {
			Label l = new Label(todo.getMessage());
			l.setWrapText(true);
			switch (todo.getSeverity()) {
			case STOPPER: l.setStyle("-fx-text-fill: textcolor-stopper"); break;
			case WARNING: l.setStyle("-fx-text-fill: textcolor-warning"); break;
			case INFO   : l.setStyle("-fx-text-fill: textcolor-info"); break;
			}
			ret.add(l);
		}
		return ret;
	}

//	//--------------------------------------------------------------------
//	private VBox prepareSection(String title, Node content, SectionType type) {
//		VBox bxContent = new VBox();
//		bxContent.setMaxWidth(Double.MAX_VALUE);
//		bxContent.setMaxHeight(Double.MAX_VALUE);
//		bxContent.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-content");
//		if (title!=null) {
//			Label heading = new Label(title);
//			heading.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title");
//			if (type!=null && type==SectionType.LEFT)
//				heading.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title-left");
//			else if (type!=null && type==SectionType.RIGHT)
//				heading.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-title-right");
//			bxContent.getChildren().add(heading);
//		}
//		if (content!=null) {
//			content.getStyleClass().addAll(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-section-content");
//			bxContent.getChildren().add(content);
//			VBox.setVgrow(content, Priority.ALWAYS);
//		}
//
//		return bxContent;
//	}

	//-------------------------------------------------------------------
	private void reloadSections() {
		logger.debug("Reload sections");
		col1And2Grid.getChildren().clear();

		if (getSectionList()==null)
			return;

		int y=0;
		for (Section section : getSectionList()) {
			logger.debug(" Section "+section);
			// Except for first element, add spacing
			if (y>0) {
				Region spacing = new Region();
				spacing.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-spacing");
				col1And2Grid.add(spacing, 0,y, 2,1);
				y++;
			}

			/*
			 * Section Data
			 */
			// a) ToDos
			VBox bxToDosV = new VBox();
			bxToDosV.setId("todoV");
			bxToDosV.setStyle("-fx-spacing: 0.3em;");
			VBox bxToDosH = new VBox();
			bxToDosH.setId("todoH");
			bxToDosH.setStyle("-fx-spacing: 0.5em");
			bxToDosV.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-todos");
			if (section.getToDoList()!=null) {
				bxToDosV.getChildren().addAll(convertToDoElements(section.getToDoList()));
				bxToDosH.getChildren().addAll(convertToDoElements(section.getToDoList()));
				section.getToDoList().addListener(new ListChangeListener<ToDoElement>() {
					public void onChanged(Change<? extends ToDoElement> c) {
						bxToDosV.getChildren().clear();
						bxToDosV.getChildren().addAll(convertToDoElements(section.getToDoList()));
						bxToDosH.getChildren().clear();
						bxToDosH.getChildren().addAll(convertToDoElements(section.getToDoList()));
					}});
			}
			// a2) ToDos for MINIMAL mode


			// b) Content
			Node bxContent = null;
			if (section instanceof DoubleSection) {
				HBox sideBySide = new HBox();
				Node col1 = ((DoubleSection)section).getLeftSection();
				Node col2 = ((DoubleSection)section).getRightSection();
//				sideBySide.getChildren().add(col1);
//				sideBySide.getChildren().add(col2)
//				HBox.setHgrow(sideBySide.getChildren().get(0), Priority.ALWAYS);
//				HBox.setHgrow(sideBySide.getChildren().get(1), Priority.ALWAYS);
//				sideBySide.setStyle("-fx-spacing: 2em");
				col1And2Grid.add(col1, 1, y);
				col1And2Grid.add(col2, 2, y);
				GridPane.setVgrow(col1, Priority.SOMETIMES);
				GridPane.setVgrow(col2, Priority.SOMETIMES);
				GridPane.setHgrow(col1, Priority.ALWAYS);
				bxContent = new VBox(bxToDosH, sideBySide);
			} else {
				bxContent = section;
				col1And2Grid.add(section, 1, y, 2,1);
			}

			// Make ToDos as high as content
//			bxToDos.prefHeightProperty().bind(bxContent.heightProperty());

			// Add to grid
			col1And2Grid.add(bxToDosV, 0, y);
//			col1And2Grid.add(bxContent, 1, y);
			GridPane.setFillWidth(bxContent, true);
			GridPane.setHgrow(bxContent, Priority.ALWAYS);



			y++;
		}
	}

	//-------------------------------------------------------------------
	private List<Node> getNestedChildren(Parent parent) {
		List<Node> ret = new ArrayList<Node>();
		ret.addAll(parent.getChildrenUnmodifiable());
		for (Node child : parent.getChildrenUnmodifiable()) {
			if (child instanceof Parent)
				ret.addAll( ((Parent)child).getChildrenUnmodifiable() );
		}
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.ResponsiveControl#setResponsiveMode(org.prelle.javafx.WindowMode)
	 */
	@Override
	public void setResponsiveMode(WindowMode value) {
		for (Node node : getNestedChildren(col1And2Grid)) {
			if (node.getId()==null)
				continue;
			if (node.getId().equals("todoV")) {
				node.setVisible(value==WindowMode.EXPANDED);
				node.setManaged(value==WindowMode.EXPANDED);
			}
			if (node.getId().equals("todoH")) {
				node.setVisible(value!=WindowMode.EXPANDED);
				node.setManaged(value!=WindowMode.EXPANDED);
			}
		}
	}

}
