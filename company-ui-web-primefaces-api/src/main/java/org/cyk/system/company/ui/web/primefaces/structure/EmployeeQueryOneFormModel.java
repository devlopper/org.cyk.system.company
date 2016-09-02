package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.model.network.UniformResourceLocatorParameter;
import org.cyk.ui.api.model.AbstractQueryOneFormModel;
import org.cyk.ui.web.api.WebNavigationManager;
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
		
		@Override
		public void serve(Object data, String actionIdentifier) {
			if(CompanyBusinessLayer.getInstance().getActionPrintEmployeeEmploymentContract().equals(actionIdentifier)){
				WebNavigationManager.getInstance().redirectTo(CompanyWebManager.getInstance().getOutcomePrintEmployeeEmploymentContract(),new Object[]{
					UniformResourceLocatorParameter.IDENTIFIABLE,data
				}); 
			}
		}
	}
}