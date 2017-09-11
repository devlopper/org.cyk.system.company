package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.impl.time.AbstractIdentifiablePeriodBusinessImpl;
import org.cyk.system.root.model.time.Period;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.TimeHelper;

public class AccountingPeriodBusinessImpl extends AbstractIdentifiablePeriodBusinessImpl<AccountingPeriod, AccountingPeriodDao> implements AccountingPeriodBusiness,Serializable {

	private static final long serialVersionUID = 3562957667624866162L;

	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	
	@Inject
	public AccountingPeriodBusinessImpl(AccountingPeriodDao dao) {
		super(dao);
	}
	
	@Override
	public AccountingPeriod instanciateOne() {
		AccountingPeriod accountingPeriod =  super.instanciateOne();
		Integer year = TimeHelper.getInstance().getYear(new Date());
		accountingPeriod.setExistencePeriod(new Period( new TimeHelper.Builder.String.Adapter.Default("1/1/"+year+" 0:0").execute()
				, new TimeHelper.Builder.String.Adapter.Default("31/12/"+year+" 23:59").execute()));
		accountingPeriod.getSaleConfiguration().setIdentifierGenerator(inject(StringGeneratorBusiness.class).instanciateOne("FACT","0", 8l, null, null,8l));
		accountingPeriod.getSaleConfiguration().setCashRegisterMovementIdentifierGenerator(inject(StringGeneratorBusiness.class).instanciateOne("PAIE","0", 8l, null, null,8l));
		return accountingPeriod;
	}
	
	@Override
	protected void beforeCreate(AccountingPeriod accountingPeriod) {
		super.beforeCreate(accountingPeriod);
		exceptionUtils().exception(findCurrent(accountingPeriod.getOwnedCompany())!=null, "exception.accountingperiod.oneisrunning");
		createIfNotIdentified(accountingPeriod.getSaleConfiguration().getIdentifierGenerator());
		createIfNotIdentified(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator());
	}
	
	@Override
	protected void beforeDelete(AccountingPeriod accountingPeriod) {
		super.beforeDelete(accountingPeriod);
		if(accountingPeriod.getSaleConfiguration().getIdentifierGenerator()!=null)
			inject(StringGeneratorBusiness.class).delete(accountingPeriod.getSaleConfiguration().getIdentifierGenerator());
		if(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()!=null)
			inject(StringGeneratorBusiness.class).delete(accountingPeriod.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator());
	}
	
	@Override
	protected void afterCreate(AccountingPeriod accountingPeriod) {
		super.afterCreate(accountingPeriod);
		AccountingPeriodProductDao accountingPeriodProductDao = inject(AccountingPeriodProductDao.class);
		ProductDao productDao = inject(ProductDao.class);
		for(Product product : productDao.readAll())
			accountingPeriodProductDao.create(new AccountingPeriodProduct(accountingPeriod, product));
		
	}

	@Override
	public void close(AccountingPeriod accountingPeriod) {
		//accountingPeriod.setClosed(Boolean.TRUE);
		dao.update(accountingPeriod);
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findCurrent(OwnedCompany ownedCompany) {
		return dao.readWhereDateBetweenPeriodByOwnedCompany(universalTimeCoordinated(), ownedCompany);
	}
	@Override
	public AccountingPeriod findCurrent() {
		return findCurrent(ownedCompanyBusiness.findDefaulted());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findPrevious(OwnedCompany ownedCompany) {
		List<AccountingPeriod> list = new ArrayList<AccountingPeriod>(dao.readWhereToDateLessThanByDateByOwnedCompany(universalTimeCoordinated(), ownedCompany));
		return list.size()>=2?list.get(1):null;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findPrevious() {
		return findPrevious(ownedCompanyBusiness.findDefaulted());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal computeValueAddedTax(AccountingPeriod accountingPeriod,BigDecimal amount) {
		BigDecimal vat;
		if(Boolean.TRUE.equals(accountingPeriod.getSaleConfiguration().getValueAddedTaxIncludedInCost()))
			vat = amount.subtract(amount.divide(BigDecimal.ONE.add(accountingPeriod.getSaleConfiguration().getValueAddedTaxRate()),RoundingMode.DOWN));
		else
			vat = accountingPeriod.getSaleConfiguration().getValueAddedTaxRate().multiply(amount);
		logDebug("VAT of amount {} is {}", amount,vat);
		return vat;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal computeTurnover(AccountingPeriod accountingPeriod,BigDecimal amount,BigDecimal valueAddedTax) {
		BigDecimal turnover;
		if(Boolean.TRUE.equals(accountingPeriod.getSaleConfiguration().getValueAddedTaxIncludedInCost()))
			turnover = amount.subtract(valueAddedTax);
		else
			turnover = amount;
		logDebug("Turnover of amount {} is {}", amount,turnover);
		return turnover;
	}
	
	/**/
	
	public static class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
		private static final long serialVersionUID = 5585791722273454192L;
		
		//@Override
		//public void processOnConsume(Sale sale, Crud crud, Boolean first) {
			/*BigDecimal sign = null;
			if(Crud.CREATE.equals(crud)){
				sign = BigDecimal.ONE;
			}else if(Crud.UPDATE.equals(crud)) {
				sign = BigDecimal.ONE;
			}else if(Crud.DELETE.equals(crud)) {
				sign = BigDecimal.ONE.negate();
			}*/
			/*
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_VALUE, sale.getCost().getValue().multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_TAX, sale.getCost().getTax().multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_TURNOVER, sale.getCost().getTurnover().multiply(sign));
			*/
		//	inject(AccountingPeriodDao.class).update(sale.getAccountingPeriod());
		//}
	}
	
	public static class BuilderOneDimensionArray extends org.cyk.system.root.business.impl.helper.InstanceHelper.BuilderOneDimensionArray<AccountingPeriod> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(AccountingPeriod.class);
			addFieldCodeName().addParameterArrayElementString(AccountingPeriod.FIELD_OWNED_COMPANY,FieldHelper.getInstance()
					.buildPath(AccountingPeriod.FIELD_SALE_CONFIGURATION,SaleConfiguration.FIELD_VALUE_ADDED_TAX_RATE));
		}
		
	}
}
