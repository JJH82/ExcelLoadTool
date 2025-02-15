package com.kbstar.itsm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.kbstar.itsm.test.util.GeneratorUtil;
import com.kbstar.itsm.ui.model.JGrid;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class TestJGrid extends JFrame {
	
	private enum NAMES {
		MAPING_GRID
		,BTN
	}

	public TestJGrid() {
		
		setTitle("Excel Load Tool");
		setSize(700,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		new Table.Builder(this)
		.tr()
		.td(
		
			new TD.Builder(NAMES.MAPING_GRID,
					new JScrollPane(
							JGrid.builder()
								.colName("SQL컬럼명")
									.width(20)
									.editorCell(new  DefaultCellEditor(new JComboBox<>(new DefaultComboBoxModel<String>(값_3개생성()))))
								.nextCol()
								.colName("DB테이블").width(20).nextCol()
								.colName("컬럼명").width(20).nextCol()
								.colName("공통코드")
									.width(20)
									.editorCell(new  DefaultCellEditor(new JComboBox<>(new DefaultComboBoxModel<String>(값_3개생성()))))
								.nextCol()
								.colName("크기").width(10).nextCol()
								.colName("키여부")
									.width(5)
									.editorCell(new DefaultCellEditor(new JCheckBox()))
								.nextCol()
								.colName("필수")
									.width(5)
									.editorCell(new DefaultCellEditor(new JCheckBox()))
								.nextCol()
								
								.build()
					)
				).build()
			)
		.tr()
		.td( new TD.Builder(NAMES.BTN,new JButton("테스트")).build() )
		.build();
		
		JGrid grid = Table.getElementByNameInJScrollPanel(NAMES.MAPING_GRID,JGrid.class);
		
		Vector<Map<String,String>> list = new Vector<>();
		Map<String,String> m = new HashMap<>();
		m.put("SQL컬럼명", "테스트");
		list.add(m);
		
		grid.setListData(list);
		
		JButton btn = Table.getElementByName(NAMES.BTN,JButton.class);
		btn.addActionListener( e -> {
			System.out.println(list.get(0).get("SQL컬럼명"));
			System.out.println(list.get(0).get("DB테이블"));
		}
				);
		
		setVisible(true);
		
		
	}
	private Vector<String> 값_3개생성() {

		return GeneratorUtil.toVector(3, i ->  "값 : "+ i );	
	
	}
	
	public static void main(String[] args) {
		new TestJGrid();
	}
	
}
