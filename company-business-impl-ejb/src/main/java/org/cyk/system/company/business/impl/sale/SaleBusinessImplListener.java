package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.utility.common.cdi.BeanAdapter;

public interface SaleBusinessImplListener{
	
	Boolean isReportUpdatable(SaleBusiness saleBusiness,Sale sale);
	
	void reportUpdated(SaleBusiness saleBusiness,SaleReport saleReport,Boolean invoice);
	
	public static class Adapter extends BeanAdapter implements SaleBusinessImplListener,Serializable{

		private static final long serialVersionUID = -855978096046996503L;

		@Override public Boolean isReportUpdatable(SaleBusiness saleBusiness, Sale sale) {
			return null;
		}
		
		@Override public void reportUpdated(SaleBusiness saleBusiness, SaleReport saleReport,Boolean invoice) {}
		
	}
}