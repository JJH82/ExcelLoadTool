package com.kbstar.itsm.exload;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kbstar.itsm.exload.controller.ExcelLoadToolMainModel;
import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.exload.controller.LoaderExcelFormModel;
import com.kbstar.itsm.exload.controller.PublicCodeEnrollmentModel;
import com.kbstar.itsm.exload.controller.RegisterExcelExportFormModel;
import com.kbstar.itsm.exload.dao.CodeConfigDAO;
import com.kbstar.itsm.exload.dao.CodeValueDAO;
import com.kbstar.itsm.exload.dao.MappingInfoDAO;
import com.kbstar.itsm.exload.dao.SqlDAO;
import com.kbstar.itsm.exload.ui.IMenuBar;
import com.kbstar.itsm.exload.ui.IToolBar;
import com.kbstar.itsm.exload.ui.LoaderExcelForm;
import com.kbstar.itsm.exload.ui.MenuBar;
import com.kbstar.itsm.exload.ui.PublicCodeEnrollment;
import com.kbstar.itsm.exload.ui.RegisterExcelExportForm;
import com.kbstar.itsm.exload.ui.ToolBar;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;



//user.dir 실행 위치
public class ExcelLoadToolMain implements IExcelLoadToolMain{
	
	private JFrame frame = new JFrame();
	
	private IExcelLoadToolMainContoller mainCtrl;
	
	private IMenuBar menuBar = new MenuBar();
	private IToolBar toolBar = new ToolBar();
	
	private Map<Enum<VIEWS>, Supplier<Component>> contentViewMap = new HashMap<Enum<VIEWS>, Supplier<Component>>();
	
	public static enum NAMES {
		CONTENT_VEIW;
	}
	
	
	public static enum VIEWS {
		CLEAR_VIEW,
		PUBLIC_CODE_VEIW,
		REGISTER_EXCEL_EXPORT,
		LOADER_EXCEL_VIEW
		;
	}
	
	
	public ExcelLoadToolMain(IExcelLoadToolMainContoller mainCtrl ) {

		contentViewMap.put(VIEWS.CLEAR_VIEW, JPanel::new);
		
		
		contentViewMap.put(VIEWS.REGISTER_EXCEL_EXPORT, () -> {
			return new RegisterExcelExportForm(
					new RegisterExcelExportFormModel(new MappingInfoDAO(),new CodeConfigDAO(),new SqlDAO())
					).getComponent();
		});
		

		contentViewMap.put(VIEWS.PUBLIC_CODE_VEIW, () -> {
			return new PublicCodeEnrollment(
					new PublicCodeEnrollmentModel(
							new CodeConfigDAO(), new CodeValueDAO())
					).getComponent();	
		});
		
		
		contentViewMap.put(VIEWS.LOADER_EXCEL_VIEW, () -> {
			return new LoaderExcelForm(
						new LoaderExcelFormModel()
					).getComponent();	
		});
		
		changeContentView(VIEWS.CLEAR_VIEW);
		initUI();
				
		
		this.mainCtrl = mainCtrl;
		this.mainCtrl.setMenuBar(menuBar);
		this.mainCtrl.setToolBar(toolBar);
		this.mainCtrl.setMainView(this);
		
	}
	

	
	private void initUI() {

		frame.setTitle("Excel Load Tool");
		frame.setSize(1920,1040);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
	@Override
	public void changeContentView(Enum<VIEWS> view) {
		
		frame.getContentPane().removeAll();

		new Table
		.Builder(frame)
		.tr()
		.td(
				new TD.Builder(menuBar)
				.zorePadding()
				.heightRate(1)
				.build()
			)
		.tr()
		.td(
				new TD.Builder(toolBar)
				.zorePadding()
				.widthRate(100)
				.heightRate(1)
				.build()					
			)
		.tr()
		.td(
				new TD.Builder(NAMES.CONTENT_VEIW, contentViewMap.get(view).get() )
				.zorePadding()
				.heightRate(98)
				.build()					
			)

		.build();
		
		frame.revalidate();
		
	}

	
	
	public static void main(String[] args) {
		
		
		IExcelLoadToolMainContoller mainCtrl = new ExcelLoadToolMainModel();
		ExcelLoadToolMain main = new ExcelLoadToolMain(mainCtrl);
		//Panel dbConnPanel = new DbConnPanel(); 
		//main.add(menuBar,BorderLayout.PAGE_START);

		main.getComponent().setVisible(true);
	}


	@Override
	public Component getComponent() {
		return frame;
	}


}
