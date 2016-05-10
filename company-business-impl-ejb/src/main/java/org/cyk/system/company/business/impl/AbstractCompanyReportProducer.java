package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractRootReportProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCompanyReportProducer extends AbstractRootReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCompanyReportProducer.class);
	
	@Override
	public SaleReport produceSaleReport(Sale sale) {
		logDebug("Prepare Sale report");
		SaleReport saleReport = new SaleReport();
		set(sale, saleReport);
		return saleReport;
	}
	
	@Override
	public SaleCashRegisterMovementReport produceSaleCashRegisterMovementReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Prepare Payment report");
		SaleCashRegisterMovementReport report = new SaleCashRegisterMovementReport();
		set(saleCashRegisterMovement.getSale(), report.getSale());
		report.setTitle(languageBusiness.findText("company.report.salecashregistermovement"));
		report.setIdentifier(saleCashRegisterMovement.getCashRegisterMovement().getComputedIdentifier());
		report.setAmountDue(format(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
		report.setAccountingPeriod(report.getSale().getAccountingPeriod());
		report.setAmountIn(format(saleCashRegisterMovement.getAmountIn()));
		report.setAmountOut(format(saleCashRegisterMovement.getAmountOut()));
		report.setBalance(format(saleCashRegisterMovement.getBalance().getValue()));
		return report;
	}
	
	protected void set(Sale sale,SaleReport report){
		Company company = sale.getCashier().getCashRegister().getOwnedCompany().getCompany();
		BigDecimal numberOfProducts = BigDecimal.ZERO;
		for(SaleProduct sp : sale.getSaleProducts())
			numberOfProducts = numberOfProducts.add(sp.getQuantity());
		
		report.setTitle(languageBusiness.findText("company.report.sale"));
		report.setIdentifier(sale.getComputedIdentifier());
		report.setCashRegisterIdentifier(sale.getCashier().getCashRegister().getCode());
		report.setDate(timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN));
		set(sale.getCustomer(), report.getCustomer());
		report.setNumberOfProducts(numberBusiness.format(numberOfProducts));
		report.setAmountDue(numberBusiness.format(sale.getCost().getValue()));
		report.setWelcomeMessage(languageBusiness.findText("company.report.pointofsale.welcome"));
		report.setGoodByeMessage(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		report.getAccountingPeriod().getCompany().setName(company.getName());
		report.getAccountingPeriod().getCompany().setImage(rootBusinessLayer.getFileBusiness().findInputStream(company.getImage()));

		contactCollectionBusiness.load(company.getContactCollection());
		report.getAccountingPeriod().getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		
		report.setVatRate(format(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
		report.setAmountDueNoTaxes(format(sale.getCost().getValue().subtract(sale.getCost().getTax()).setScale(2)));
		report.setVatAmount(format(sale.getCost().getTax().setScale(2)));
	}
		
	protected void valueAddedTaxesPart(SaleReport saleReport,Sale sale){
		//labelValue(saleReport.getTaxInfos(),LABEL_VAT_RATE, format(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
		labelValue(LABEL_AMOUNT_VAT_EXCLUDED, format(sale.getCost().getValue().subtract(sale.getCost().getTax()).setScale(2,RoundingMode.HALF_DOWN)));
		labelValue(LABEL_VAT_AMOUNT, format(sale.getCost().getTax().setScale(2,RoundingMode.HALF_DOWN)));
	}
		
	@Override
	protected Logger __logger__() {
		return LOGGER;
	}
	
	public static final String LABEL_AMOUNT_TO_PAY = "amount.to.pay";
	public static final String LABEL_CASH = "cash";
	public static final String LABEL_AMOUNT_PAID = "";
	public static final String LABEL_AMOUNT_TO_OUT = "amount.to.out";
	public static final String LABEL_AMOUNT_DU = "company.report.pointofsale.amount.du";
	
	public static final String LABEL_VAT_RATE = "company.report.pointofsale.vat.rate";
	public static final String LABEL_VAT_AMOUNT = "company.report.pointofsale.vat.amount";
	public static final String LABEL_AMOUNT_VAT_EXCLUDED = "company.report.pointofsale.amount.vat.excluded";

}
