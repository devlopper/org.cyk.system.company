package org.cyk.system.company.model._sampledata;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollectionReport;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollectionReportTemplateFile;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReportFile;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleReportTemplateFile;
import org.cyk.system.company.model.structure.EmployeeReportTemplateFile;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.utility.common.generator.RandomDataProvider;

public abstract class AbstractSampleData extends org.cyk.system.root.model.AbstractSampleData implements Serializable {

	private static final long serialVersionUID = 3161729607014055851L;

	protected Collection<EmployeeReportTemplateFile> __getEmployeeReports__(){
		Collection<EmployeeReportTemplateFile> collection = RandomDataProvider.generate(EmployeeReportTemplateFile.class, 1);
		//EmployeeReportTemplateFile report = collection.iterator().next();
		return collection;
	}
	
	protected Collection<SaleReportTemplateFile> __getSaleReportTemplateFiles__(){
		Collection<SaleReportTemplateFile> collection = RandomDataProvider.generate(SaleReportTemplateFile.class, 1);
		SaleReportTemplateFile report = collection.iterator().next();
		
		addLabelValues(report,"Invoice",new String[][]{
				{"Identifiant", report.getSale().getGlobalIdentifier().getIdentifier()}
				,{"Commercial", report.getSale().getGlobalIdentifier().getCreatedBy()}
				,{"Date", report.getSale().getGlobalIdentifier().getExistencePeriod().getFromDate()}
				,{"Client", report.getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		addLabelValues(report,"Payment",new String[][]{
				{"A payer", report.getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		addLabelValues(report,"TVA",new String[][]{
				{"Taux TVA", report.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSale().getSalableProductCollection().getCost().getTax()}
				});
		process(report);
		return collection;
	}
	
	/*protected Collection<SaleReportTemplateFile> __getSaleReportTemplateFiles__(){
		Collection<SaleReportTemplateFile> collection = RandomDataProvider.generate(SaleReportTemplateFile.class, 1);
		SaleReportTemplateFile report = collection.iterator().next();
		
		addLabelValues(report,"Invoice",new String[][]{
				{"Identifiant", report.getSale().getGlobalIdentifier().getIdentifier()}
				,{"Caisse", report.getSale().getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getCashRegister().getGlobalIdentifier().getCode()}
				,{"Date", report.getSale().getGlobalIdentifier().getExistencePeriod().getFrom()}
				,{"Client", report.getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		addLabelValues(report,"Payment",new String[][]{
				{"A payer", report.getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		addLabelValues(report,"TVA",new String[][]{
				{"Taux TVA", report.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSale().getSalableProductCollection().getCost().getTax()}
				});
		process(report);
		return collection;
	}*/
	
	protected Collection<SaleCashRegisterMovementReportFile> __getSaleCashRegisterMovementReportTemplateFiles__(){
		Collection<SaleCashRegisterMovementReportFile> collection = RandomDataProvider.generate(SaleCashRegisterMovementReportFile.class, 1);
		SaleCashRegisterMovementReportFile report = collection.iterator().next();
		report.getSaleCashRegisterMovement().setSale(new SaleReport());
		report.getSaleCashRegisterMovement().getSale().generate();
		
		addLabelValues(report,"Invoice",new String[][]{
			{"Identifiant", report.getSaleCashRegisterMovement().getSale().getGlobalIdentifier().getIdentifier()}
			,{"Cashier", report.getSaleCashRegisterMovement().getSale().getGlobalIdentifier().getCreatedBy()}
			,{"Date", report.getSaleCashRegisterMovement().getSale().getGlobalIdentifier().getExistencePeriod().getFromDate()}
			,{"Client", report.getSaleCashRegisterMovement().getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
			});
	
		addLabelValues(report,"Payment",new String[][]{
				{"A payer", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		
		process(report);
		/*
		addLabelValues(report, "Invoice", new String[][]{
			{"Identifiant", "I001"}
			,{"Caisse", "C001"}
			,{"Date", "31/12/9999"}
			,{"Client", "Nom et prénoms"}
			});
		
		addLabelValues(report, "Payment", new String[][]{
			{"A payer", "1000000"}
			,{"Especes", "2222222"}
			,{"A rendre", "8888888"}
			});
		*/			
		return collection;
	}
	
	protected Collection<SaleCashRegisterMovementCollectionReportTemplateFile> __getSaleCashRegisterMovementCollectionReportTemplateFiles__(){
		Collection<SaleCashRegisterMovementCollectionReportTemplateFile> collection = RandomDataProvider.generate(SaleCashRegisterMovementCollectionReportTemplateFile.class, 1);
		SaleCashRegisterMovementCollectionReportTemplateFile report = collection.iterator().next();
		report.setSaleCashRegisterMovementCollection(new SaleCashRegisterMovementCollectionReport());
		report.getSaleCashRegisterMovementCollection().generate();
		
		addLabelValues(report,"Payment",new String[][]{
			{"Identifiant", report.getSaleCashRegisterMovementCollection().getGlobalIdentifier().getIdentifier()}
			,{"Cashier", report.getSaleCashRegisterMovementCollection().getGlobalIdentifier().getCreatedBy()}
			,{"Date", report.getSaleCashRegisterMovementCollection().getGlobalIdentifier().getExistencePeriod().getFromDate()}
			//,{"Client", report.getSaleCashRegisterMovementCollection().getCustomer().getGlobalIdentifier().getIdentifier()}
			});
	
		addLabelValues(report,"Payment",new String[][]{
				{"A payer", report.getSaleCashRegisterMovementCollection().getCashRegisterMovement().getMovement().getValue()}
				});
		
		
		process(report);
		/*
		addLabelValues(report, "Invoice", new String[][]{
			{"Identifiant", "I001"}
			,{"Caisse", "C001"}
			,{"Date", "31/12/9999"}
			,{"Client", "Nom et prénoms"}
			});
		
		addLabelValues(report, "Payment", new String[][]{
			{"A payer", "1000000"}
			,{"Especes", "2222222"}
			,{"A rendre", "8888888"}
			});
		*/			
		return collection;
	}
	
	/**/
	
	protected void process(AbstractReportTemplateFile<?> report){}
	
	public static void main(String[] args) {
		AbstractSampleData sampleData = new AbstractSampleData(){private static final long serialVersionUID = 1L;};
		System.out.println(ToStringBuilder.reflectionToString(sampleData.__getSaleReportTemplateFiles__()));
	}
	
}
