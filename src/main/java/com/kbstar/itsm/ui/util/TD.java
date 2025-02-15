package com.kbstar.itsm.ui.util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.kbstar.itsm.ui.common.IView;

public class TD {
	
	private GridBagConstraints layoutConst = new GridBagConstraints();
	
	private int rowspan;
	private int colspan; 
	private Component component;
	

	
	private TD(Builder builder) {
		
		this.rowspan = builder.rowspan;
		this.colspan = builder.colspan;
		this.component = builder.component;

		layoutConst.gridheight = this.rowspan;
		layoutConst.gridwidth = this.colspan;

		layoutConst.weightx = builder.width;
		layoutConst.weighty = builder.height;
		layoutConst.anchor = builder.align;
		layoutConst.fill =  builder.fill;
		layoutConst.insets =  builder.padding;
		
	}

	
	
	public static class Builder {
		
		private Component component;
		private int rowspan = 1;
		private int colspan = 1;
		
		private double width = 1;
		private double height = 1;
		
		private int fill = GridBagConstraints.NONE;
		private Insets padding = new Insets(2, 2, 2, 2) ;
		private int align = GridBagConstraints.WEST;
		
		
		public Builder(Component component ) {
			initBuilder("", component);
		}
		
		public Builder(String name,Component component ) {
			initBuilder(name, component);
		}
		
		public Builder(Enum name,Component component ) {
			initBuilder(name.getDeclaringClass() + "." + name.toString(), component);
		}
		
		public Builder(IView view ) {
			initBuilder("", view.getComponent());
		}
		
		public Builder(String name,IView view ) {
			initBuilder(name, view.getComponent());
		}

		public Builder(Enum name,IView view ) {
			initBuilder(name.getDeclaringClass() + "." + name.toString(), view.getComponent());
		}
		
		private void initBuilder(String name, Component c) {
			this.component = c;
			this.component.setName(name);
			this.fill = GridBagConstraints.BOTH;
			this.height = 0;
		}

		
		public Builder rowspan(int rowspan) {
			this.rowspan = rowspan;
			return this;
		}
		
		public Builder colspan(int colspan) {
			this.colspan = colspan;
			return this;
		}
		
		public Builder heightRate(double hight) {
			this.fill = GridBagConstraints.BOTH;
			this.height = hight;
			return this;
		}

		public Builder widthRate(double width) {
			this.fill = GridBagConstraints.BOTH;
			this.width = width;
			return this;
		}

		public Builder maxHeight() {
			this.fill = GridBagConstraints.BOTH;
			this.height = 50;
			return this;
		}

		public Builder minHeight() {
			this.fill = GridBagConstraints.BOTH;
			this.height = 0;
			return this;
		}
		
		public Builder midHeight() {
			this.fill = GridBagConstraints.BOTH;
			this.height = 25;
			return this;
		}
		
		
		public Builder maxWidth() {
			this.fill = GridBagConstraints.BOTH;
			this.width = 50;
			return this;
		}
		
		public Builder midWidth() {
			this.fill = GridBagConstraints.BOTH;
			this.width = 25;
			return this;
		}

		public Builder minWidth() {
			this.fill = GridBagConstraints.BOTH;
			this.width = 0;
			return this;
		}

		
		public Builder padding(Insets padding) {
			this.padding = padding;
			return this;
		}
		
		public Builder zorePadding() {
			this.padding = new Insets(0,0,0,0);
			return this;
		}

		
		public Builder top() {
			this.align = GridBagConstraints.NORTH;
			this.fill = GridBagConstraints.HORIZONTAL;
			return this;
		}
		
		public Builder botton() {
			this.align = GridBagConstraints.SOUTH;
			this.fill = GridBagConstraints.HORIZONTAL;
			return this;
		}
		
		public Builder middle() {
			this.align = GridBagConstraints.WEST;
			this.fill = GridBagConstraints.HORIZONTAL;
			return this;
		}

		
		public Builder center() {
			this.align = GridBagConstraints.CENTER;
			this.fill = GridBagConstraints.VERTICAL;
			return this;
		}

		public Builder right() {
			this.align = GridBagConstraints.EAST;
			this.fill = GridBagConstraints.VERTICAL;
			return this;
		}
		
		public TD build() {
			return new TD(this);
		}
		
	}

	public int getRowspan() {
		return rowspan;
	}


	public int getColspan() {
		return colspan;
	}


	public Component getComponent() {
		return component;
	}
	
	
	public GridBagConstraints getLayoutConst() {
		return layoutConst;
	}



	public static void main(String args[]) {
		
		
	}


}
