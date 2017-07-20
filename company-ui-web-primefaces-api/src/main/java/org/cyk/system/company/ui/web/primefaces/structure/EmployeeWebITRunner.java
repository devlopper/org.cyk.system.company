package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.model.geography.LocationFormModel;
import org.cyk.ui.api.model.language.LanguageCollectionFormModel;
import org.cyk.ui.web.primefaces.test.automation.Form;
import org.cyk.ui.web.primefaces.test.automation.party.AbstractActorWebITRunner;

public class EmployeeWebITRunner extends AbstractActorWebITRunner<Employee> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String[] getListMenuItemPath() {
		return new String[]{"commandable_personel_management","commandable_list_employee_"};
	}
	@Override
	public String getCode(Crud crud) {
		return "emp001";
	}
	@Override
	public void fillForm(Form form,Crud crud) {
		switch(crud){
		case CREATE:
			form.addInputText(EmployeeEditPage.Form.FIELD_NAME,"yao")
	        	.addInputText(EmployeeEditPage.Form.FIELD_LAST_NAMES, "evelyne")
	        	.addInputOneRadio(EmployeeEditPage.Form.FIELD_SEX, 0)
	        	.addInputCalendar(EmployeeEditPage.Form.FIELD_BIRTH_DATE, "01/02/1993")
	        	.addInputOneAutoComplete(LocationFormModel.FIELD_LOCALITY, "ab", 1)
	        	.addInputOneAutoComplete(EmployeeEditPage.Form.FIELD_NATIONALITY, "cot", 0)
	        	.addInputPersonImage(EmployeeEditPage.Form.FIELD_IMAGE, Boolean.TRUE)
	        	.addInputOneAutoComplete(EmployeeEditPage.Form.FIELD_JOB_FUNCTION, "pro", 0)
	        	.addInputOneAutoComplete(LanguageCollectionFormModel.FIELD_LANGUAGE_1, "fr", 0)
	        	.addInputOneRadio(EmployeeEditPage.Form.FIELD_EMPLOYMENT_AGREEMENT_TYPE, 1)
		        ;
			break;
		case READ:
			break;
		case UPDATE:
			
			break;
		case DELETE:
			break;
		}
	}

	
}
