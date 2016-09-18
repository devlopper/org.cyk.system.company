package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.network.UniformResourceLocatorParameter;
import org.cyk.system.root.persistence.api.file.FileIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.file.FileRepresentationTypeDao;
import org.cyk.system.root.persistence.impl.Utils;
import org.cyk.ui.api.model.AbstractQueryOneFormModel;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.page.AbstractSelectOnePage;
import org.cyk.ui.web.primefaces.page.party.AbstractActorQueryOneFormModel;
import org.cyk.utility.common.FileExtension;
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
		
		@SuppressWarnings("unchecked")
		@Override
		public void serve(Object data, String actionIdentifier) {
			if(CompanyBusinessLayer.getInstance().getActionPrintEmployeeEmploymentContract().equals(actionIdentifier)){
				/*WebNavigationManager.getInstance().redirectTo(CompanyWebManager.getInstance().getOutcomePrintEmployeeEmploymentContract(),new Object[]{
					UniformResourceLocatorParameter.IDENTIFIABLE,data
				});*/ 
				Employee employee = ((AbstractActorQueryOneFormModel<Employee>)data).getIdentifiable();
				
				WebNavigationManager.getInstance().redirectToReportFileConsultPageOrReportFileGeneratePageIfNotExist(employee
						, CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT);
				/*
				FileIdentifiableGlobalIdentifier.SearchCriteria searchCriteria = new FileIdentifiableGlobalIdentifier.SearchCriteria();
		    	searchCriteria.addIdentifiableGlobalIdentifier(employee);
		    	searchCriteria.addRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT));
		    	Collection<FileIdentifiableGlobalIdentifier> fileIdentifiableGlobalIdentifiers = inject(FileIdentifiableGlobalIdentifierDao.class).readByCriteria(searchCriteria);
				if(fileIdentifiableGlobalIdentifiers.isEmpty())
					WebNavigationManager.getInstance().redirectToReportFileGeneratePage(employee, FileExtension.PDF);
				else
					WebNavigationManager.getInstance().redirectToFileConsultManyPage(Utils.getFiles(fileIdentifiableGlobalIdentifiers), FileExtension.PDF);
				*/
			}
		}
	}
}