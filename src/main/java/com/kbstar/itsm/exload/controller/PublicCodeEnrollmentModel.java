package com.kbstar.itsm.exload.controller;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;

import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.dao.ISelectMap;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.ui.IPublicCodeEnrollment;
import com.kbstar.itsm.ui.util.Alert;

public class PublicCodeEnrollmentModel implements IPublicCodeEnrollmentController {
	
	
	private GenericVectorDAO<CodeConfig> dao;
	private Vector<CodeConfig> codeConfigList;
	private Optional<CodeConfig> selectedItem = Optional.empty();
	
	private IPublicCodeEnrollment view;
	private ISelectMap selectMap;
	
	
	public PublicCodeEnrollmentModel(GenericVectorDAO<CodeConfig> codeCofing, ISelectMap selectMap) {
		this.dao = codeCofing;
		this.selectMap = selectMap;
		
		codeConfigList = this.dao.loadAll();
	}
	

	@Override
	public void setDefaultView(IPublicCodeEnrollment pce) {
		this.view = pce;
		this.view.displaySavedCodeNameComboData(codeConfigList);
		initSelectedIteam();
	}
	
	private void initSelectedIteam() {
		
		Optional
		.ofNullable(codeConfigList)
		.ifPresent( o -> selectionChange(o.stream().findFirst()) );
	}
	
	@Override
	public void selectionChange(Optional<CodeConfig> codeConfig) {
		selectedItem = codeConfig;
		selectedItem.ifPresent(this.view::displayCodeConfigUI);
	}

	@Override
	public void save(CodeConfig codeConfig) {
		
		if(codeConfig.getName().isEmpty()) return;
		
		if(!isUpdate(codeConfig)) {
			System.out.println(codeConfig.getName());
			add(codeConfig);
		}

		dao.saveAll(codeConfigList);
	}


	private void add(CodeConfig codeConfig) {
		codeConfigList.add(codeConfig);	
		view.clearCodeConfigUI();
		view.updateUI();
	}


	private boolean isUpdate(CodeConfig codeConfig) {
		
		Optional<CodeConfig> updateTarget = codeConfigList.stream()
														.filter( (o) -> codeConfig.getName().equals(o.getName()))
														.findFirst();
		
		
		updateTarget.ifPresent((o) ->{ 
			o.setId(codeConfig.getId());
			o.setName(codeConfig.getName());
			o.setSql(codeConfig.getSql());			
			o.setParameters(codeConfig.getParameters());
			o.setList(codeConfig.getList());
		});
		
		return updateTarget.isPresent();
	}
	


	@Override
	public void remove() {
		
		selectedItem.ifPresent((o) -> {
				codeConfigList.remove(o);
				dao.saveAll(codeConfigList);
			});
		
		view.clearCodeConfigUI();
		view.updateUI();
		
	}

	@Override
	public void execute(CodeConfig codeConfig) {
		try {
			view.displayCodeListGridData(new Vector( selectMap.selectMap(codeConfig) ));
		} catch (SQLException e) {
			Alert.ERROR.show(e.toString());
		}
	}

	

}
