package org.cyk.system.company.business.impl.product;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductFamilyBusiness;
import org.cyk.system.company.model.product.ProductFamily;
import org.cyk.system.company.persistence.api.product.ProductFamilyDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class ProductFamilyBusinessImpl extends AbstractDataTreeTypeBusinessImpl<ProductFamily,ProductFamilyDao> implements ProductFamilyBusiness {
	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public ProductFamilyBusinessImpl(ProductFamilyDao dao) {
        super(dao);
    }
	/*
	@Override
    public ProductType create(ProductType productType,OwnedCompany ownedCompany) {
		return __create__(productType,ownedCompany);
    }
	
	@Override
	public ProductType create(ProductType productType) {
		return __create__(productType,ownedCompanyBusiness.findDefaulted());
	}
	
	private ProductType __create__(ProductType productType,OwnedCompany ownedCompany) {
    	dao.create(productType);
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent(ownedCompany);
    	if(accountingPeriod!=null)
    		if(accountingPeriodProductTypeDao.readByAccountingPeriodByEntity(accountingPeriod, productType)==null)
    			accountingPeriodProductTypeDao.create(new AccountingPeriodProductType(accountingPeriod, productType));
    	return productType;
    }
	*/
}
