package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

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
		set(saleCashRegisterMovement, report);
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
	
	protected void set(SaleCashRegisterMovement saleCashRegisterMovement,SaleCashRegisterMovementReport report) {
		set(saleCashRegisterMovement.getSale(), report.getSale());
		report.setTitle(languageBusiness.findText("company.report.salecashregistermovement"));
		report.setIdentifier(saleCashRegisterMovement.getCashRegisterMovement().getComputedIdentifier());
		
		Collection<SaleCashRegisterMovement> saleCashRegisterMovements = CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementDao().readBySale(saleCashRegisterMovement.getSale());
		SaleCashRegisterMovement lastSaleCashRegisterMovement = null;
		for(SaleCashRegisterMovement s : saleCashRegisterMovements)
			if(!s.getIdentifier().equals(saleCashRegisterMovement.getIdentifier()) && s.getCashRegisterMovement().getMovement().getBirthDate().before(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getBirthDate()) ){
				if(lastSaleCashRegisterMovement==null)
					lastSaleCashRegisterMovement = s;
				else if(lastSaleCashRegisterMovement.getCashRegisterMovement().getMovement().getBirthDate().before(s.getCashRegisterMovement().getMovement().getBirthDate()))
					lastSaleCashRegisterMovement = s;
			}
			
		report.setAmountDue(format(lastSaleCashRegisterMovement==null ? saleCashRegisterMovement.getSale().getCost().getValue() : lastSaleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
		report.setAccountingPeriod(report.getSale().getAccountingPeriod());
		report.setAmountIn(format(saleCashRegisterMovement.getAmountIn()));
		report.setAmountOut(format(saleCashRegisterMovement.getAmountOut()));
		report.setBalance(format(saleCashRegisterMovement.getBalance().getValue()));
	}
			
	@Override
	protected Logger __logger__() {
		return LOGGER;
	}
	
	
}
