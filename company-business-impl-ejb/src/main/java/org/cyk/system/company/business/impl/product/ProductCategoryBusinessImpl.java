package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductCategoryDao;
import org.cyk.system.company.persistence.api.product.ProductCategoryDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ProductCategoryBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ProductCategory,ProductCategoryDao> implements ProductCategoryBusiness {

	private static final long serialVersionUID = -5970296090669949506L;

	@Inject private AccountingPeriodProductCategoryDao accountingPeriodProductCategoryDao;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	
	@Inject
    public ProductCategoryBusinessImpl(ProductCategoryDao dao) {
        super(dao);
    }
	
	@Override
    public ProductCategory create(ProductCategory productCategory,OwnedCompany ownedCompany) {
		return __create__(productCategory,ownedCompany);
    }
	
	@Override
	public ProductCategory create(ProductCategory productCategory) {
		return __create__(productCategory,ownedCompanyBusiness.findDefaultOwnedCompany());
	}
	
	private ProductCategory __create__(ProductCategory productCategory,OwnedCompany ownedCompany) {
    	dao.create(productCategory);
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent(ownedCompany);
    	if(accountingPeriod!=null)
    		if(accountingPeriodProductCategoryDao.readByAccountingPeriodByEntity(accountingPeriod, productCategory)==null)
    			accountingPeriodProductCategoryDao.create(new AccountingPeriodProductCategory(accountingPeriod, productCategory));
    	return productCategory;
    }

}
