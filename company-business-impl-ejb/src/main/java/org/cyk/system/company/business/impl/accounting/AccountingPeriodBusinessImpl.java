package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
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
	
	/*
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal computeTurnover(BigDecimal cost, BigDecimal valueAddedTax) {
		AccountingPeriod accountingPeriod = findCurrent();
		if(Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost()))
			return cost.subtract(valueAddedTax);
		else
			return cost;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal computeTurnover(Sale sale) {
		return computeTurnover(sale.getCost(), sale.getValueAddedTax());
	}
	*/

	@Override
	public BigDecimal computeValueAddedTax(AccountingPeriod accountingPeriod,BigDecimal amount) {
		//System.out.println(amount+" / ("+BigDecimal.ONE.add(accountingPeriod.getValueAddedTaxRate())+"-"+amount+")");
		return Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost())?
			amount.divide(BigDecimal.ONE.add(accountingPeriod.getValueAddedTaxRate())).subtract(amount)
			:accountingPeriod.getValueAddedTaxRate().multiply(amount);
	}
	
	@Override
	public BigDecimal computeTurnover(AccountingPeriod accountingPeriod,BigDecimal amount,BigDecimal valueAddedTax) {
		return Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost())?amount.subtract(valueAddedTax)
				:amount;
	}
	
	
	
	/*
	@Override
	public void process(SaleProduct saleProduct) {
		saleProduct.setValueAddedTax(computeValueAddedTax(saleProduct.getPrice()));
		saleProduct.setTurnover(computeTurnover(saleProduct.getPrice(), saleProduct.getValueAddedTax()));
	}
	*/

}
