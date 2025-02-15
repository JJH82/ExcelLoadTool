package com.kbstar.itsm.exload.controller;

import java.util.Optional;

import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.ui.IPublicCodeEnrollment;

public interface IPublicCodeEnrollmentController {
	
	
	public void setDefaultView(IPublicCodeEnrollment pce);
	
	public void selectionChange(Optional<CodeConfig> codeConfig);

	public void save(CodeConfig codeConfig);
	
	public void remove();
	
	public void execute(CodeConfig codeConfig);


}
