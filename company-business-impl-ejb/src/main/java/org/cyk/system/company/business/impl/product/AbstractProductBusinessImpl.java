package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT>,Serializable {
	
	private static final long serialVersionUID = 2801588592108008404L;

    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
     
    @Override @TransactionAttribute(TransactionAttributeType.NEVER)
    public Collection<PRODUCT> findByCategory(ProductCategory category) {
    	return dao.readByCategory(category);
    }
    
    @Override
	protected void afterCreate(PRODUCT product) {
		super.afterCreate(product);
		OwnedCompany ownedCompany = product.getOwnedCompany();
		if(ownedCompany == null)
			ownedCompany = inject(OwnedCompanyBusiness.class).findDefaulted();
		AccountingPeriod accountingPeriod = inject(AccountingPeriodBusiness.class).findCurrent(ownedCompany);
    	if(accountingPeriod!=null)
    		if(inject(AccountingPeriodProductDao.class).readByAccountingPeriodByEntity(accountingPeriod, product)==null)
    			createIfNotIdentified(new AccountingPeriodProduct(accountingPeriod, product));
	}
    
    @Override
	protected void beforeDelete(PRODUCT product) {
		super.beforeDelete(product);
		inject(AccountingPeriodProductBusiness.class).delete(inject(AccountingPeriodProductDao.class).readByEntity(product));
	}
    
    /*
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
    */
    
    protected abstract Set<PRODUCT> products(Collection<SalableProductCollectionItem> saleProducts);
    
    protected abstract void beforeUpdate(PRODUCT product,BigDecimal usedCount);

    /**/
    
    public static class BuilderOneDimensionArray<T extends Product> extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<T> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray(Class<T> outputClass) {
			super(outputClass);
		}
		
	}	
    
}
