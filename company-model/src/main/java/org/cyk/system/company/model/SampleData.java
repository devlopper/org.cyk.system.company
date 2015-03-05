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
		return RandomDataProvider.generate(SaleReport.class, 1);
	}
	
	public static void main(String[] args) {
		System.out.println(createSaleReports());
	}
	
}
