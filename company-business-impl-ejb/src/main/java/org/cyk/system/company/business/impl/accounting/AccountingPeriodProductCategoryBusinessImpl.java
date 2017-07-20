package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductCategoryBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductCategoryDao;
import org.cyk.system.company.persistence.api.product.ProductCategoryDao;

public class AccountingPeriodProductCategoryBusinessImpl extends AbstractAccountingPeriodResultsBusinessImpl<AccountingPeriodProductCategory, AccountingPeriodProductCategoryDao,ProductCategory> implements AccountingPeriodProductCategoryBusiness, Serializable {

	private static final long serialVersionUID = -1843616492544404846L;
	
	@Inject private ProductCategoryDao productCategoryDao;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	
	@Inject
	public AccountingPeriodProductCategoryBusinessImpl(AccountingPeriodProductCategoryDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<AccountingPeriodProductCategory> findByAccountingPeriod(AccountingPeriod accountingPeriod) {
		return dao.readByAccountingPeriodByProductCategories(accountingPeriod, productCategoryDao.readRoots());
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<AccountingPeriodProductCategory> findHierarchies() {
		return dao.readByAccountingPeriodByProductCategories(accountingPeriodBusiness.findCurrent(),productCategoryDao.readRoots());
	}
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<AccountingPeriodProductCategory> findHierarchiesByAccountingPeriod(AccountingPeriod accountingPeriod) {
		return dao.readByAccountingPeriodByProductCategories(accountingPeriod, productCategoryDao.readRoots());
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<AccountingPeriodProductCategory> findHierarchies(Collection<ProductCategory> productCategories) {
		return dao.readByAccountingPeriodByProductCategories(accountingPeriodBusiness.findCurrent(), productCategories);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<AccountingPeriodProductCategory> findHierarchiesByAccountingPeriod(AccountingPeriod accountingPeriod,Collection<ProductCategory> productCategories) {
		return dao.readByAccountingPeriodByProductCategories(accountingPeriod, productCategories);
	}

}
