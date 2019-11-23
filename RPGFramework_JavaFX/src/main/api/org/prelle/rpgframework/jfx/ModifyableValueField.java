package org.prelle.rpgframework.jfx;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

class ModifyableValueField extends HBox {
		private Button dec, inc;
		private TextField value;
		
		public ModifyableValueField() {
			dec  = new Button("<");
			inc  = new Button(">");
			value = new TextField();
			value.setPrefColumnCount(1);
			this.getChildren().addAll(dec, value, inc);
		}
		public void setText(String val) {
			this.value.setText(val);
		}
	}