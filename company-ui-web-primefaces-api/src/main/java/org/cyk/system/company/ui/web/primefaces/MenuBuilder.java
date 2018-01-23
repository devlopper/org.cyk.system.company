package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Company;

public class MenuBuilder extends org.cyk.ui.web.primefaces.MenuBuilder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void populateNodeIdentifiablesManagePackageFromClass(Collection<Class<?>> packageFromClass) {
		super.populateNodeIdentifiablesManagePackageFromClass(packageFromClass);
		packageFromClass.addAll(Arrays.asList(AccountingPeriod.class,CashRegister.class,Product.class
				,Sale.class,StockableTangibleProduct.class,Company.class));
	}
	
}