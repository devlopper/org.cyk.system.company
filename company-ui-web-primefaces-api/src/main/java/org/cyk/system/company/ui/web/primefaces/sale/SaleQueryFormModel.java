package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.command.UICommandable.Parameter;
import org.cyk.ui.api.model.AbstractQueryFormModel;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.page.SelectPageListener;
import org.cyk.utility.common.annotation.user.interfaces.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.FieldOverrides;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter @FieldOverrides(value={@FieldOverride(name=AbstractQueryFormModel.FIELD_IDENTIFIABLE,type=Sale.class)})
public class SaleQueryFormModel extends AbstractQueryFormModel.Default<Sale> implements Serializable {
	private static final long serialVersionUID = -3756660150800681378L;
	
	@Override
	public Sale getIdentifiable() {
		return super.getIdentifiable();
	}

	/**/
	
	@Getter @Setter
	public static class PageAdapter extends SelectPageListener.Adapter.Default<Sale,String> implements Serializable {

		private static final long serialVersionUID = -7392513843271510254L;
		
		public PageAdapter() {
			super(Sale.class);
		}
		
		@Override
		public void afterInitialisationEnded(AbstractBean bean) {
			super.afterInitialisationEnded(bean);
			//final SelectPage selectPage = (SelectPage) bean;
			
		}
		
		@Override
		public org.cyk.ui.web.primefaces.page.SelectPageListener.Type getType() {
			return Type.IDENTIFIER;
		}
		
		@Override
		public void serve(Object data, String actionIdentifier) {
			if(ArrayUtils.contains(new String[]{CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementInput()
					,CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementOutput()}, actionIdentifier)){
				WebNavigationManager.getInstance().redirectToDynamicCreate( findByIdentifier(((SaleQueryFormModel)data).getIdentifier()),SaleCashRegisterMovement.class,
						Arrays.asList(new Parameter(UIManager.getInstance().getActionIdentifierParameter(), actionIdentifier))); 
			}
		}
		
		@Override
		public Sale findByIdentifier(String identifier) {
			return CompanyBusinessLayer.getInstance().getSaleBusiness().findByComputedIdentifier(identifier);
		}
	}
}