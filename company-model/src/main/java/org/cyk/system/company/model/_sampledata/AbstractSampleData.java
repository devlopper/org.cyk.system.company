package org.cyk.system.company.model._sampledata;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReportTemplateFile;
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
	}
	
	protected Collection<SaleCashRegisterMovementReportTemplateFile> __getSaleCashRegisterMovementReportTemplateFiles__(){
		Collection<SaleCashRegisterMovementReportTemplateFile> collection = RandomDataProvider.generate(SaleCashRegisterMovementReportTemplateFile.class, 1);
		SaleCashRegisterMovementReportTemplateFile report = collection.iterator().next();
		
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
					
		return collection;
	}
	
	/**/
	
	protected void process(AbstractReportTemplateFile<?> report){}
	
	public static void main(String[] args) {
		AbstractSampleData sampleData = new AbstractSampleData(){private static final long serialVersionUID = 1L;};
		System.out.println(ToStringBuilder.reflectionToString(sampleData.__getSaleReportTemplateFiles__()));
	}
	
}
