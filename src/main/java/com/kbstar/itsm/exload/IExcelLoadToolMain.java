package com.kbstar.itsm.exload;

import com.kbstar.itsm.exload.ExcelLoadToolMain.VIEWS;
import com.kbstar.itsm.ui.common.IView;

public interface IExcelLoadToolMain extends IView {

	void changeContentView(Enum<VIEWS> view);

}
