package org.prelle.rpgframework.jfx;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class FreePointsNodeSkin extends SkinBase<FreePointsNode> {

	private VBox content;
	private Label points;
	private Label name;
	private Circle clip;

	//-------------------------------------------------------------------
	public FreePointsNodeSkin(FreePointsNode skinnable) {
		super(skinnable);
		initComponents();
		initLayout();
		updateClipping();
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		points = new Label(getStringFor(getSkinnable().getPoints()));
		points.getStyleClass().add("points");

		name = new Label(getSkinnable().getName());
		name.getStyleClass().add("name");
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		content = new VBox();
		content.setStyle("-fx-spacing: 0.2em;");
		content.getChildren().addAll(points, name);
		content.setAlignment(Pos.CENTER);

		Group grp = new Group(content);
		getChildren().add(grp);

	}

	//-------------------------------------------------------------------
	private static String getStringFor(Number n) {
		int intVal = (int)(float)n;
		if (intVal==(Float)n) {
			return String.valueOf( intVal );
		} else {
			return String.valueOf(n);
		}
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		getSkinnable().pointsProperty().addListener( (ov,o,n) -> points.setText(getStringFor(n)));

		getSkinnable().nameProperty().addListener( (ov,o,n) -> name.setText(n));

		content.widthProperty().addListener( (ov,o,n) -> {updateSize(); updateClipping();});
		content.heightProperty().addListener( (ov,o,n) -> {updateSize(); updateClipping();});
		getSkinnable().heightProperty().addListener( (ov,o,n) -> {updateSize(); updateClipping();});
//		getSkinnable().widthProperty().addListener( (ov,o,n) -> System.out.println("Skinnable.width="+n));
	}

	//-------------------------------------------------------------------
	private void updateSize() {
		if (content.getHeight()<=0 || content.getWidth()<=0)
			return;
		double size = Math.max(content.getWidth(), content.getHeight());
//		System.out.println("Size = min("+content.getWidth()+", "+content.getHeight()+") = "+size);
		content.resize(size, size);
//		content.requestLayout();
//		getSkinnable().setPrefSize(size, size);
////		getSkinnable().resize(size, size);
//		getSkinnable().getParent().requestLayout();
	}

	//-------------------------------------------------------------------
	private void updateClipping() {
//		System.out.println("Clip for "+content.getWidth()+"x+"+content.getHeight());
		Region reference = getSkinnable();
		double x = reference.widthProperty().get()/2.2;
		double y = reference.heightProperty().get()/2.2;
		double rad = Math.min(x,y);
		clip = new Circle(x,y,rad,	Paint.valueOf("black"));
		getSkinnable().setClip(clip);
	}

}