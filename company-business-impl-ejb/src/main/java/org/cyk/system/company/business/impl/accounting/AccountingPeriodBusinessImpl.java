package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.event.AbstractIdentifiablePeriodBusinessImpl;

@Stateless
public class AccountingPeriodBusinessImpl extends AbstractIdentifiablePeriodBusinessImpl<AccountingPeriod, AccountingPeriodDao> implements AccountingPeriodBusiness,Serializable {

	private static final long serialVersionUID = 3562957667624866162L;

	@Inject private ProductDao productDao;
	@Inject private AccountingPeriodProductDao accountingPeriodProductDao;
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	
	@Inject
	public AccountingPeriodBusinessImpl(AccountingPeriodDao dao) {
		super(dao);
	}

	@Override
	public AccountingPeriod create(AccountingPeriod accountingPeriod) {
		exceptionUtils().exception(findCurrent(accountingPeriod.getOwnedCompany())!=null, "exception.accountingperiod.oneisrunning");
		AccountingPeriod previous = findPrevious(accountingPeriod.getOwnedCompany());
		exceptionUtils().exception(previous!=null && Boolean.FALSE.equals(previous.getClosed()), "exception.accountingperiod.previousnotclosed");
		super.create(accountingPeriod);
		for(Product product : productDao.readAll())
			accountingPeriodProductDao.create(new AccountingPeriodProduct(accountingPeriod, product));
		return accountingPeriod;
	}
	
	@Override
	public void close(AccountingPeriod accountingPeriod) {
		accountingPeriod.setClosed(Boolean.TRUE);
		dao.update(accountingPeriod);
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findCurrent(OwnedCompany ownedCompany) {
		return dao.readWhereDateBetweenPeriodByOwnedCompany(universalTimeCoordinated(), ownedCompany);
	}
	@Override
	public AccountingPeriod findCurrent() {
		return findCurrent(ownedCompanyBusiness.findDefaultOwnedCompany());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findPrevious(OwnedCompany ownedCompany) {
		List<AccountingPeriod> list = new ArrayList<AccountingPeriod>(dao.readWhereToDateLessThanByDateByOwnedCompany(universalTimeCoordinated(), ownedCompany));
		return list.size()>=2?list.get(1):null;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AccountingPeriod findPrevious() {
		return findPrevious(ownedCompanyBusiness.findDefaultOwnedCompany());
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
		
		@Override
		public void processOnConsume(Sale sale, Crud crud, Boolean first) {
			BigDecimal sign = null;
			if(Crud.CREATE.equals(crud)){
				sign = BigDecimal.ONE;
			}else if(Crud.UPDATE.equals(crud)) {
				sign = BigDecimal.ONE;
			}else if(Crud.DELETE.equals(crud)) {
				sign = BigDecimal.ONE.negate();
			}
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_VALUE, sale.getCost().getValue().multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_TAX, sale.getCost().getTax().multiply(sign));
			commonUtils.increment(BigDecimal.class, sale.getAccountingPeriod().getSaleResults().getCost(), Cost.FIELD_TURNOVER, sale.getCost().getTurnover().multiply(sign));
			CompanyBusinessLayer.getInstance().getAccountingPeriodDao().update(sale.getAccountingPeriod());
		}
	}
}
