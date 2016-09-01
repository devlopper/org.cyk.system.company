package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.BalanceReport;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.CostReport;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.company.model.sale.InvoiceReport;
import org.cyk.system.company.model.sale.PaymentReceiptReport;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionReport;
import org.cyk.system.company.model.sale.SalableProductReport;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.system.company.model.sale.SaleConfigurationReport;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.CompanyReport;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.impl.file.report.AbstractRootReportProducer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCompanyReportProducer extends AbstractRootReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCompanyReportProducer.class);
	
	private InvoiceReport produceInvoiceReport(Sale sale) {
		logDebug("Prepare Sale report");
		InvoiceReport report = new InvoiceReport();
		report.setTitle(languageBusiness.findText("company.report.sale"));
		report.setFooter(languageBusiness.findText("company.report.pointofsale.welcome"));
		report.setHeader(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		SaleReport saleReport = new SaleReport();
		set(sale, saleReport);
		report.setSale(saleReport);
		
		report.generate();
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
				{"Taux TVA", report.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSale().getSalableProductCollection().getCost().getTax()}
				});
		
		return report;
	}
	
	private PaymentReceiptReport producePaymentReceiptReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		PaymentReceiptReport report = new PaymentReceiptReport();
		report.setTitle(languageBusiness.findText("company.report.sale"));
		report.setFooter(languageBusiness.findText("company.report.pointofsale.welcome"));
		report.setHeader(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		SaleCashRegisterMovementReport saleCashRegisterMovementReport = report.getSaleCashRegisterMovement();
		SaleReport saleReport = new SaleReport();
		saleReport.generate();
		saleCashRegisterMovementReport.setSale(saleReport);
		set(saleCashRegisterMovement, saleCashRegisterMovementReport);
		
		report.generate();
		//debug(saleCashRegisterMovementReport);
		report.addLabelValueCollection("MyPayment",new String[][]{
				{"Identifiant", report.getSaleCashRegisterMovement().getGlobalIdentifier().getIdentifier()}
				,{"Caisse", report.getSaleCashRegisterMovement().getCashRegisterMovement().getCashRegister().getGlobalIdentifier().getCode()}
				,{"Date", report.getSaleCashRegisterMovement().getGlobalIdentifier().getExistencePeriod().getFrom()}
				//,{"Client", report.getSaleCashRegisterMovement().getSale().getCustomer().getGlobalIdentifier().getIdentifier()}
				});
		
		report.addLabelValueCollection("Payment",new String[][]{
				{"A payer", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getValue()}
				});
		
		report.addLabelValueCollection("TVA",new String[][]{
				{"Taux TVA", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate()}
				,{"Montant Hors Taxe", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getValue()}
				,{"TVA", report.getSaleCashRegisterMovement().getSale().getSalableProductCollection().getCost().getTax()}
				});
		
		return report;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <REPORT extends AbstractReportTemplateFile<REPORT>> REPORT produce(Class<REPORT> reportClass, AbstractIdentifiable identifiable) {
		if(InvoiceReport.class.equals(reportClass)){
			if(identifiable instanceof Sale)
				return (REPORT) produceInvoiceReport((Sale)identifiable);
		}else if(PaymentReceiptReport.class.equals(reportClass)){
			if(identifiable instanceof SaleCashRegisterMovement)
				return (REPORT) producePaymentReceiptReport((SaleCashRegisterMovement)identifiable);
		}
		return super.produce(reportClass, identifiable);
	}
	
	@Override
	public SaleCashRegisterMovementReport produceSaleCashRegisterMovementReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Prepare Payment report");
		SaleCashRegisterMovementReport report = new SaleCashRegisterMovementReport();
		set(saleCashRegisterMovement, report);
		return report;
	}
	
	protected void set(Balance balance,BalanceReport report){
		
	}
	
	protected void set(Cost cost,CostReport report){
		//report.setAmountDueNoTaxes(format(cost.getValue().subtract(cost.getTax()).setScale(2)));
		report.setTax(format(cost.getTax().setScale(2)));
	}
	
	protected void set(SaleConfiguration saleConfiguration,SaleConfigurationReport report){
		report.setValueAddedTaxRate(format(saleConfiguration.getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
	}
	
	protected void set(Company company,CompanyReport report){
		report.getGlobalIdentifier().setImage(inject(FileBusiness.class).findInputStream(company.getImage()));
		contactCollectionBusiness.load(company.getContactCollection());
		set(company.getContactCollection(),report.getContactCollection());
	}
	
	protected void set(AccountingPeriod accountingPeriod,AccountingPeriodReport report){
		set(accountingPeriod.getSaleConfiguration(),report.getSaleConfiguration());
	}
	
	protected void set(SalableProduct salableProduct,SalableProductReport report){
		
	}
	
	protected void set(SalableProductCollection salableProductCollection,SalableProductCollectionReport report){
		
	}
	
	protected void set(Sale sale,SaleReport report){
		set(sale.getSalableProductCollection(),report.getSalableProductCollection());
		set(sale.getBalance(),report.getBalance());
		
		
		
	}
	
	protected void set(SaleCashRegisterMovement saleCashRegisterMovement,SaleCashRegisterMovementReport report) {
		//set(saleCashRegisterMovement.getSale(), report.getSale());
		//report.setTitle(languageBusiness.findText("company.report.salecashregistermovement"));
		//report.setIdentifier(saleCashRegisterMovement.getCashRegisterMovement().getCode());
		
		Collection<SaleCashRegisterMovement> saleCashRegisterMovements = inject(SaleCashRegisterMovementDao.class).readBySale(saleCashRegisterMovement.getSale());
		SaleCashRegisterMovement lastSaleCashRegisterMovement = null;
		for(SaleCashRegisterMovement s : saleCashRegisterMovements)
			if(!s.getIdentifier().equals(saleCashRegisterMovement.getIdentifier()) && s.getCashRegisterMovement().getMovement().getBirthDate().before(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getBirthDate()) ){
				if(lastSaleCashRegisterMovement==null)
					lastSaleCashRegisterMovement = s;
				else if(lastSaleCashRegisterMovement.getCashRegisterMovement().getMovement().getBirthDate().before(s.getCashRegisterMovement().getMovement().getBirthDate()))
					lastSaleCashRegisterMovement = s;
			}
			
		//report.setAmountDue(format(lastSaleCashRegisterMovement==null ? saleCashRegisterMovement.getSale().getCost().getValue() : lastSaleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
		//report.setAccountingPeriod(report.getSale().getAccountingPeriod());
		report.setAmountIn(format(saleCashRegisterMovement.getAmountIn()));
		report.setAmountOut(format(saleCashRegisterMovement.getAmountOut()));
		report.setBalance(format(saleCashRegisterMovement.getBalance().getValue()));
	}
			
	@Override
	protected Logger __logger__() {
		return LOGGER;
	}
	
	
}
