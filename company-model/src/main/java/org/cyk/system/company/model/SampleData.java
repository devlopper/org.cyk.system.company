package org.cyk.system.company.model;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cyk.system.company.model.sale.PointOfSaleReport;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.utility.common.generator.RandomDataProvider;

public class SampleData implements Serializable {

	private static final long serialVersionUID = 5898014003593786829L;

	public SampleData() {}
	
	public static Collection<PointOfSaleReport> getPointOfSaleReports(){
		Collection<PointOfSaleReport> collection = RandomDataProvider.generate(PointOfSaleReport.class, 1);
		PointOfSaleReport report = collection.iterator().next();
		
		report.addLabelValueCollection("Invoice",new String[][]{
				{"Identifiant", report.getSale().getGlobalIdentifier().getIdentifier()}
				,{"Caisse", report.getSale().getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getCashRegister().getGlobalIdentifier().getCode()}
				,{"Date", report.getSale().getGlobalIdentifier().getExistencePeriod().getFrom()}
				,{"Client", report.getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		report.addLabelValueCollection("Payment",new String[][]{
				{"A payer", report.getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		report.addLabelValueCollection("TVA",new String[][]{
				{"Taux TVA", report.getSale().getSalableProductCollection().getAccountingPeriod().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSale().getSalableProductCollection().getCost().getTax()}
				});
		
		return collection;
	}
	
	public static Collection<SaleCashRegisterMovementReport> createSaleCashRegisterMovementReports(){
		Collection<SaleCashRegisterMovementReport> collection = RandomDataProvider.generate(SaleCashRegisterMovementReport.class, 1);
		/*SaleCashRegisterMovementReport report = collection.iterator().next();
		 
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
		*/	
		return collection;
	}
	
	public static void main(String[] args) {
		System.out.println(ToStringBuilder.reflectionToString(getPointOfSaleReports()));
	}
	
}
