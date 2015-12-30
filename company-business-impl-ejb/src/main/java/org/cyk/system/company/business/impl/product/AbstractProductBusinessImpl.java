package org.cyk.system.company.business.impl.product;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT> {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject protected AccountingPeriodProductDao accountingPeriodProductDao;
	@Inject protected AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject protected OwnedCompanyBusiness ownedCompanyBusiness;
	
    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
     
    @Override
    public Collection<PRODUCT> findByCategory(ProductCategory category) {
    	return dao.readByCategory(category);
    }
    
    @Override
    public PRODUCT create(PRODUCT product,OwnedCompany ownedCompany) {
    	return __create__(product,ownedCompany);
    }
    
    @Override
    public PRODUCT create(PRODUCT product) {
    	return __create__(product,ownedCompanyBusiness.findDefaultOwnedCompany());
    }
    
    private PRODUCT __create__(PRODUCT product,OwnedCompany ownedCompany) {
    	dao.create(product);
    	AccountingPeriod accountingPeriod = accountingPeriodBusiness.findCurrent(ownedCompany);
    	if(accountingPeriod!=null)
    		if(accountingPeriodProductDao.readByAccountingPeriodByProduct(accountingPeriod, product)==null)
    			accountingPeriodProductDao.create(new AccountingPeriodProduct(accountingPeriod, product));
    	return product;
    }
        
    @Override
	public void consume(Collection<SaleProduct> saleProducts) {
		Set<PRODUCT> products = products(saleProducts);
		
		for(PRODUCT product : products){
			BigDecimal usedCount = BigDecimal.ZERO;
			for(SaleProduct saleProduct : saleProducts)
				if(saleProduct.getProduct().equals(product))
					usedCount = usedCount.add(saleProduct.getQuantity());
			
			//product.setUsedCount(product.getUsedCount().add(usedCount));
			beforeUpdate(product,usedCount);
			dao.update(product);
		}
	}
    
    protected abstract Set<PRODUCT> products(Collection<SaleProduct> saleProducts);
    
    protected abstract void beforeUpdate(PRODUCT product,BigDecimal usedCount);
 
}
