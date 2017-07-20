package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.ui.api.model.AbstractQueryOneFormModel;
import org.cyk.ui.web.primefaces.page.AbstractSelectOnePage;
import org.cyk.ui.web.primefaces.page.party.AbstractActorQueryOneFormModel;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.FieldOverrides;

@Getter @Setter @FieldOverrides(value={@FieldOverride(name=AbstractQueryOneFormModel.FIELD_IDENTIFIABLE,type=Employee.class)})
public class EmployeeQueryOneFormModel extends AbstractActorQueryOneFormModel<Employee> implements Serializable {
	private static final long serialVersionUID = -3756660150800681378L;
		
	/**/
	
	@Getter @Setter
	public static class PageAdapter extends AbstractActorSelectOnePageAdapter<Employee> implements Serializable {
		private static final long serialVersionUID = -7392513843271510254L;
		
		public PageAdapter() {
			super(Employee.class);
		}
		
		protected void initialiseSelect(AbstractSelectOnePage<?> selectPage){
			super.initialiseSelect(selectPage);
			//CompanyWebManager.getInstance().initialiseSelectClassroomSession(selectPage, AbstractQueryOneFormModel.FIELD_IDENTIFIABLE, null,null,null);
		}
		/*
		protected ReportTemplate getReportTemplate(){
			return WebManager.getInstance().getIdentifiableFromRequestParameter(ReportTemplate.class,UniformResourceLocatorParameter.REPORT_IDENTIFIER);
		}
		
		@Override
		public void processContentTitleDoSomethingTextParameters(FindDoSomethingTextParameters findDoSomethingTextParameters,String actionIdentifier) {
			super.processContentTitleDoSomethingTextParameters(findDoSomethingTextParameters,actionIdentifier);
			if(RootBusinessLayer.getInstance().getActionPrint().equals(actionIdentifier)){
				findDoSomethingTextParameters.setForWhat(inject(LanguageBusiness.class).findDoPrintReportText(getReportTemplate()).getValue());	
			}	
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void serve(Object data, String actionIdentifier) {
			Employee employee = ((AbstractActorQueryOneFormModel<Employee>)data).getIdentifiable();
			
			if(RootBusinessLayer.getInstance().getActionPrint().equals(actionIdentifier)){
				WebNavigationManager.getInstance().redirectToFileConsultManyPage(Arrays.asList(inject(EmployeeBusiness.class)
						.findReportFile(employee, getReportTemplate(),Boolean.TRUE)), FileExtension.PDF);
			}
			
		}*/
	}
}