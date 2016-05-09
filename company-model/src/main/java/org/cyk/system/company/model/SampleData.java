package org.cyk.system.company.model;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.utility.common.generator.RandomDataProvider;

public class SampleData implements Serializable {

	private static final long serialVersionUID = 5898014003593786829L;

	public SampleData() {}
	
	public static Collection<SaleReport> createSaleReports(){
		Collection<SaleReport> collection = RandomDataProvider.generate(SaleReport.class, 1);
		SaleReport report = collection.iterator().next();
		
		report.addLabelValueCollection("Invoice",new String[][]{
				{"Identifiant", "I001"}
				,{"Caisse", "C001"}
				,{"Date", "31/12/9999"}
				,{"Client", "Nom et prénoms"}
				});
		
		report.addLabelValueCollection("Payment",new String[][]{
				{"A payer", "1000000"}
				});
		
		report.addLabelValueCollection("TVA",new String[][]{
				{"Taux TVA", "9999999"}
				,{"Montant Hors Taxe", "2222222"}
				,{"TVA", "8888888"}
				});
			
		return collection;
	}
	
	public static Collection<SaleCashRegisterMovementReport> createSaleCashRegisterMovementReports(){
		Collection<SaleCashRegisterMovementReport> collection = RandomDataProvider.generate(SaleCashRegisterMovementReport.class, 1);
		SaleCashRegisterMovementReport report = collection.iterator().next();
		
		report.addLabelValueCollection("Invoice",new String[][]{
				{"Identifiant", "I001"}
				,{"Caisse", "C001"}
				,{"Date", "31/12/9999"}
				,{"Client", "Nom et prénoms"}
				});
		
		report.addLabelValueCollection("Payment",new String[][]{
				{"A payer", "1000000"}
				,{"Especes", "2222222"}
				,{"A rendre", "8888888"}
				});
			
		return collection;
	}
	
	public static void main(String[] args) {
		System.out.println(createSaleReports());
	}
	
}
