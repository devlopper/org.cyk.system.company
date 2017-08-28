package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer.Listener;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.impl.DataSet;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.geography.ContactCollection;
import org.cyk.system.root.model.mathematics.Interval;
import org.cyk.system.root.model.mathematics.IntervalCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.security.BusinessServiceCollection;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.joda.time.DateTime;

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
		/*
		addClass(Company.class);
		addClass(OwnedCompany.class);
		
		String companyName = null;
		for(Listener listener : Listener.COLLECTION){
			String value = listener.getCompanyName();
			if(value!=null)
				companyName = value;
		}
		
		company.setImage(read(File.class, CompanyConstant.Code.File.COMPANYLOGO));
		
		OwnedCompany ownedCompany = new OwnedCompany();
		ownedCompany.setCompany(company);
		ownedCompany.setSelected(Boolean.TRUE);
		
		installObject(-1,inject(OwnedCompanyBusiness.class),ownedCompany);
		
		FiniteStateMachine salableProductInstanceCashRegisterFiniteStateMachine = rootDataProducerHelper.createFiniteStateMachine(CompanyConstant.GIFT_CARD_WORKFLOW
    			, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SEND,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE}
				, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD
				,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}
    			,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED, new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}, new String[][]{
    			{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_ASSIGNED,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SEND,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SENT,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD}
    			,{CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE,CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED}
    	});
		
		AccountingPeriod accountingPeriod = new AccountingPeriod();
		accountingPeriod.setOwnedCompany(ownedCompany);
		Integer currentYear = new DateTime().getYear();
		accountingPeriod.setExistencePeriod(new Period(new DateTime(currentYear, 1, 1, 0, 0).toDate(), new DateTime(currentYear, 12, 31, 23, 59).toDate()));
		
		accountingPeriod.getSaleConfiguration().setValueAddedTaxRate(BigDecimal.ZERO);
		accountingPeriod.getSaleConfiguration().setIdentifierGenerator(stringGenerator("FACT","0", 8l, null, null,8l));
		accountingPeriod.getSaleConfiguration().setCashRegisterMovementIdentifierGenerator(stringGenerator("PAIE","0", 8l, null, null,8l));
		//accountingPeriod.getSaleConfiguration().setFiniteStateMachine(finiteStateMachine);
		accountingPeriod.getSaleConfiguration().setSalableProductInstanceCashRegisterFiniteStateMachine(salableProductInstanceCashRegisterFiniteStateMachine);
		accountingPeriod.getSaleConfiguration().setSalableProductInstanceCashRegisterSaleConsumeState(inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD));
		
		inject(StringGeneratorBusiness.class).create(accountingPeriod.getSaleConfiguration().getIdentifierGenerator());
		inject(StringGeneratorBusiness.class).create(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator());
		//accountingPeriodBusiness.create(accountingPeriod);
		for(Listener listener : Listener.COLLECTION)
			listener.handleAccountingPeriodToInstall(accountingPeriod);
		installObject(ACCOUNTING_PERIOD,inject(AccountingPeriodBusiness.class),accountingPeriod);
		
		dataSet.addClass(EmploymentAgreementType.class);
		*/
	}
	
	private void sale(){
		addClass(IntangibleProduct.class);
		addClass(TangibleProduct.class);
		addClass(SalableProduct.class);
		addClass(CashRegisterMovementMode.class);
		
		addClass(IntervalCollection.class);
		addClass(Interval.class);
		
		addClass(CashRegister.class);
    }
}
