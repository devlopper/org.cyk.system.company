package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.company.persistence.api.stock.StockableProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.party.PartyIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.CollectionHelper;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT>,Serializable {
	private static final long serialVersionUID = 2801588592108008404L;

    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
    
    @Override
	public void setIsStockable(PRODUCT product) {
    	product.setIsStockable(inject(StockableProductDao.class).readByProduct(product)!=null);
	}
    
    @Override
    public void setProviderParty(PRODUCT product){
    	PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(inject(PartyIdentifiableGlobalIdentifierDao.class)
    			.readByIdentifiableGlobalIdentifierByRole(product.getGlobalIdentifier(), read(BusinessRole.class, RootConstant.Code.BusinessRole.PROVIDER)));
    	if(partyIdentifiableGlobalIdentifier!=null)
    		product.setProviderParty(partyIdentifiableGlobalIdentifier.getParty());
    }
    
    @Override
	protected void beforeCrud(PRODUCT product, Crud crud) {
		super.beforeCrud(product, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(product.getIsStockable())){
				if(Crud.CREATE.equals(crud)){
					product.addIdentifiables(inject(StockableProductBusiness.class).instanciateOne().setProduct(product)
							.setQuantityMovementCollectionInitialValue(product.getStockQuantityMovementCollectionInitialValue()));
				}
			}
			
			if(product.getProviderParty()!=null){
				if(Crud.CREATE.equals(crud)){	
					product.addIdentifiables(inject(PartyIdentifiableGlobalIdentifierBusiness.class).instanciateOne()
							.setParty(product.getProviderParty())
							.setBusinessRoleFromCode(RootConstant.Code.BusinessRole.PROVIDER)
							.setIdentifiableGlobalIdentifier(product.getGlobalIdentifier())
							);
				}else{
					
				}
			}
			
			if(product.getStore()!=null){
				if(Crud.CREATE.equals(crud)){
					product.addIdentifiables(inject(ProductStoreBusiness.class).instanciateOne()
							.setProduct(product)
							.setStore(product.getStore())
							);
				}else{
					
				}
			}
		}else if(Crud.DELETE.equals(crud)){
			inject(StockableProductBusiness.class).delete(inject(StockableProductDao.class).readByProduct(product));
		}
		
		
	}
    
    @Override @TransactionAttribute(TransactionAttributeType.NEVER)
    public Collection<PRODUCT> findByCategory(ProductCategory category) {
    	return dao.readByCategory(category);
    }
    
    /**/
    
    public static class BuilderOneDimensionArray<T extends Product> extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<T> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray(Class<T> outputClass) {
			super(outputClass);
		}
		
	}	
    
}
