package org.cyk.system.company.business.impl.__data__;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionProperties;
import org.cyk.system.company.model.sale.SalableProductCollectionPropertiesType;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.ValueAddedTaxRate;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.impl.__data__.DataSet;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.mathematics.Interval;
import org.cyk.system.root.model.mathematics.IntervalCollection;
import org.cyk.system.root.model.security.BusinessServiceCollection;

public class RealDataSet extends DataSet implements Serializable {

	private static final long serialVersionUID = -2798230163660365442L;

	public RealDataSet() {
		super(CompanyBusinessLayer.class);
		
		file();
		structure();
		company();
		sale();
		
		security();
		
	}

	private void file(){
		addClass(File.class);
		addClass(ReportTemplate.class);
	}
	
	private void security(){
		addClass(BusinessServiceCollection.class);
	}
		
	private void structure(){
		addClass(DivisionType.class);
    }
	
	private void company(){
		addClass(Company.class);
		addClass(OwnedCompany.class);
		addClass(EmploymentAgreementType.class);
		addClass(AccountingPeriod.class);
	}
	
	private void sale(){
		addClass(Product.class);
		addClass(SalableProduct.class);
		addClass(ValueAddedTaxRate.class);
		sheetNameMap.put(SalableProductCollectionPropertiesType.class, "SalableProductCollectionPT");
		sheetNameMap.put(SalableProductCollectionProperties.class, "SalableProductCollectionP");
		addClass(SalableProductCollectionPropertiesType.class);
		
		//addClass(CashRegisterMovementMode.class);
		
		addClass(IntervalCollection.class);
		addClass(Interval.class);
		
		//addClass(CashRegister.class);
    }
	
	/**/
	
	public static class Adapter extends DataSet.Listener.Adapter.Default implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@Override
		public void processRelatedClasses(Class<?> aClass,Collection<Class<?>> classes) {
			super.processRelatedClasses(aClass, classes);
			if(Product.class.equals(aClass)){
				classes.addAll(Arrays.asList(Product.class,OwnedCompany.class,Company.class,AccountingPeriod.class));
			}else if(Sale.class.equals(aClass)){
				classes.addAll(Arrays.asList(SalableProductCollectionPropertiesType.class,SalableProductCollectionProperties.class,SalableProduct.class,OwnedCompany.class,Company.class,AccountingPeriod.class
						,ValueAddedTaxRate.class));
			}else if(AccountingPeriod.class.equals(aClass)){
				classes.addAll(Arrays.asList(OwnedCompany.class,Company.class));
			}
		}

    }
}
