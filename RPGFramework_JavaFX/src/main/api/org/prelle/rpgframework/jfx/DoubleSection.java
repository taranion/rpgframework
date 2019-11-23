package org.prelle.rpgframework.jfx;

import de.rpgframework.genericrpg.ToDoElement;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class DoubleSection extends Section {
	
	private Section leftSection;
	private Section rightSection;
	
	//-------------------------------------------------------------------
	public DoubleSection(Section left, Section right) {
		setLeftSection(left);
		setRightSection(right);
//		leftSection = left;
//		rightSection = right;
//		todos.addAll(left.getToDoList());
//		todos.addAll(right.getToDoList());
//		
//		// Update lists
//		left.getToDoList().addListener(new ListChangeListener<ToDoElement>() {
//			public void onChanged(Change<? extends ToDoElement> c) {
//				refreshList();
//			}});
//		right.getToDoList().addListener(new ListChangeListener<ToDoElement>() {
//			public void onChanged(Change<? extends ToDoElement> c) {
//				refreshList();
//			}});
	}
	
	//-------------------------------------------------------------------
	private void refreshList() {
		todos.clear();
		if (leftSection!=null)
			todos.addAll(leftSection.getToDoList());
		if (rightSection!=null)
			todos.addAll(rightSection.getToDoList());
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.Section#getToDoList()
	 */
	@Override
	public ObservableList<ToDoElement> getToDoList() {
		return todos;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the leftSection
	 */
	public Section getLeftSection() {
		return leftSection;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the rightSection
	 */
	public Section getRightSection() {
		return rightSection;
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.Section#refresh()
	 */
	@Override
	public void refresh() {
		if (leftSection!=null)
			leftSection.refresh();
		if (rightSection!=null)
			rightSection.refresh();
	}

	//-------------------------------------------------------------------
	/**
	 * @param leftSection the leftSection to set
	 */
	public void setLeftSection(Section left) {
		this.leftSection = left;

		if (left!=null) {
			todos.addAll(left.getToDoList());

			// Update lists
			left.getToDoList().addListener(new ListChangeListener<ToDoElement>() {
				public void onChanged(Change<? extends ToDoElement> c) {
					refreshList();
				}});
		}

		refreshList();
	}

	//-------------------------------------------------------------------
	/**
	 * @param rightSection the rightSection to set
	 */
	public void setRightSection(Section right) {
		this.rightSection = right;

		if (right!=null) {
		todos.addAll(right.getToDoList());
		
		// Update lists
		right.getToDoList().addListener(new ListChangeListener<ToDoElement>() {
			public void onChanged(Change<? extends ToDoElement> c) {
				refreshList();
			}});
		}
		refreshList();
	}

}