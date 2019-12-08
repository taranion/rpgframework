package org.prelle.rpgframework.jfx;

import de.rpgframework.genericrpg.NumericalValue;
import de.rpgframework.genericrpg.NumericalValueController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class NumericalValueField<T,V extends NumericalValue<T>> extends HBox {

	private V value;
	private NumericalValueController<T,V> control;
	private Button dec;
	private Button inc;
	private TextField tfValue;
	private Label lbValue;
	private Callback<V, Integer> valueCallback;
	private Callback<V, String> converter;
	private int minWidthEm;
	private boolean useLabel;

	//--------------------------------------------------------------------
	public NumericalValueField(V val, NumericalValueController<T,V> ctrl) {
		this.value = val;
		this.control = ctrl;
		initComponents();
		initLayout();
		initStyle();
		initInteractivity();
		refresh();
	}

	//--------------------------------------------------------------------
	public NumericalValueField(V val, NumericalValueController<T,V> ctrl, int minWidthEm, boolean useLabel) {
		this.value = val;
		this.control = ctrl;
		this.minWidthEm   = minWidthEm;
		this.useLabel= useLabel;
		initComponents();
		initLayout();
		initStyle();
		initInteractivity();
		refresh();
	}

	//--------------------------------------------------------------------
	private void initComponents() {
		dec  = new Button("\uE0C6");
		inc  = new Button("\uE0C5");
		tfValue = new TextField();
		tfValue.setPrefColumnCount(2);
		tfValue.setEditable(false);
		tfValue.setFocusTraversable(false);
		lbValue = new Label();
	}

	//--------------------------------------------------------------------
	private void initLayout() {
		getChildren().clear();
		setAlignment(Pos.CENTER);
		if (useLabel)
			getChildren().addAll(dec, lbValue, inc);
		else
			getChildren().addAll(dec, tfValue, inc);
	}

	//--------------------------------------------------------------------
	private void initStyle() {
		inc.getStyleClass().add("mini-button");
		dec.getStyleClass().add("mini-button");
		if (minWidthEm>0)
			setStyle("-fx-min-width: "+minWidthEm+"em");
		else
			setStyle("-fx-min-width: 7.5em");

		if (minWidthEm==0)
			lbValue.setStyle("-fx-padding: 0 1em 0 1em");
		else
			lbValue.setStyle("-fx-padding: 0 0.1em 0 0.1em");
	}

	//--------------------------------------------------------------------
	private void initInteractivity() {
		dec.setOnAction(ev -> control.decrease(value));
		inc.setOnAction(ev -> control.increase(value));
	}

	//--------------------------------------------------------------------
	public void refresh() {
		if (control==null) {
			System.err.println("NumericalValueField: Missing control for "+this);
			return;
		}
		dec.setDisable(!control.canBeDecreased(value));
		inc.setDisable(!control.canBeIncreased(value));
		int num = value.getPoints();
		if (valueCallback!=null) 
			num = valueCallback.call(value);
		String text = String.valueOf(num);			
		if (converter!=null) {
			text = converter.call(value);
		}
		tfValue.setText(text);
		lbValue.setText(text);
		
		if (text!=null && text.length()>3)
			lbValue.setStyle("-fx-font-size: smaller");
		else
			lbValue.setStyle(null);
	}

	//--------------------------------------------------------------------
	public int getInt() {
		try {
			return Integer.parseInt(tfValue.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	//--------------------------------------------------------------------
	public void setItem(V val) {
		this.value = val;
		refresh();
	}

	//-------------------------------------------------------------------
	/**
	 * @param valueCallback the valueCallback to set
	 */
	public void setValueCallback(Callback<V, Integer> valueCallback) {
		this.valueCallback = valueCallback;
		initLayout();
	}

	//-------------------------------------------------------------------
	/**
	 * @param converter the converter to set
	 */
	public void setConverter(Callback<V,String> converter) {
		this.converter = converter;
	}

}