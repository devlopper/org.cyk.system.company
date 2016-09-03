package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT>,Serializable {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject protected AccountingPeriodProductDao accountingPeriodProductDao;
	@Inject protected AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject protected OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject protected SalableProductCollectionItemDao saleProductDao;
	
    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
     
    @Override @TransactionAttribute(TransactionAttributeType.NEVER)
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
    		if(accountingPeriodProductDao.readByAccountingPeriodByEntity(accountingPeriod, product)==null)
    			accountingPeriodProductDao.create(new AccountingPeriodProduct(accountingPeriod, product));
    	return product;
    }
    
    @Override
    public PRODUCT delete(PRODUCT product){
    	for(AccountingPeriodProduct accountingPeriodProduct : accountingPeriodProductDao.readByEntity(product))
    		inject(AccountingPeriodProductBusiness.class).delete(accountingPeriodProduct);
    	return super.delete(product); 
    }
        
    @SuppressWarnings("null")
	@Override
	public void consume(Sale sale, Crud crud, Boolean first) {
		Collection<SalableProductCollectionItem> saleProducts = null;//saleProductDao.readBySale(sale);
    	Set<PRODUCT> products = products(saleProducts);
		
		for(PRODUCT product : products){
			BigDecimal usedCount = BigDecimal.ZERO;
			for(SalableProductCollectionItem saleProduct : saleProducts)
				if(saleProduct.getSalableProduct().equals(product))
					usedCount = usedCount.add(saleProduct.getQuantity());
			
			//product.setUsedCount(product.getUsedCount().add(usedCount));
			beforeUpdate(product,usedCount);
			dao.update(product);
		}
	}
    
    protected abstract Set<PRODUCT> products(Collection<SalableProductCollectionItem> saleProducts);
    
    protected abstract void beforeUpdate(PRODUCT product,BigDecimal usedCount);

    /**/
    
    
}
