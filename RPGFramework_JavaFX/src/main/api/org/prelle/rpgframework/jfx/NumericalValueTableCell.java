package org.prelle.rpgframework.jfx;

import de.rpgframework.genericrpg.NumericalValue;
import de.rpgframework.genericrpg.NumericalValueController;
import javafx.scene.control.TableCell;

/**
 * @author Stefan
 *
 * T = Type (e.g. Attribute), C = ValueType (e.g. AttributeValue), R = RowType
 */
public class NumericalValueTableCell<T,R extends NumericalValue<T>, C extends NumericalValue<T>> extends TableCell<R, C> {

	private NumericalValueController<T,C> ctrl;

	//--------------------------------------------------------------------
	public NumericalValueTableCell(NumericalValueController<T,C> ctrl) {
		this.ctrl = ctrl;
	}

	//--------------------------------------------------------------------
	/**
	 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
	 */
	@Override
	public void updateItem(C item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(new NumericalValueField<>(item, ctrl));
		}
	}
}
