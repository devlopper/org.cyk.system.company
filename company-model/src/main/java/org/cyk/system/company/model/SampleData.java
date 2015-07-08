package org.cyk.system.company.model;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.product.SaleReport;
import org.cyk.utility.common.generator.RandomDataProvider;

public class SampleData implements Serializable {

	private static final long serialVersionUID = 5898014003593786829L;

	public SampleData() {}
	
	public static SaleReport saleReport(){
		SaleReport report = new SaleReport();
		report.generate();
		return report;
	}
	
	public static Collection<SaleReport> createSaleReports(){
		Collection<SaleReport> collection = RandomDataProvider.generate(SaleReport.class, 3);
		int i = 0;
		for(SaleReport saleReport : collection)
			switch(i++){
			case 0:
				saleReport.setDone(Boolean.FALSE);
				break;
			case 1:
				saleReport.getSaleCashRegisterMovement().setVatRate(null);
				break;
			case 2:
				break;
			}	
		return collection;
	}
	
	public static void main(String[] args) {
		System.out.println(createSaleReports());
	}
	
}
