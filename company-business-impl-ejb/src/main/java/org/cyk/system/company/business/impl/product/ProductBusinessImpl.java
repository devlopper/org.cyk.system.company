package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.stock.StockableProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.party.PartyIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.CollectionHelper;

public class ProductBusinessImpl extends AbstractEnumerationBusinessImpl<Product,ProductDao> implements ProductBusiness,Serializable  {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ProductBusinessImpl(ProductDao dao) {
        super(dao);
    }
	
	@Override
    public void setProviderParty(Product product){
    	PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(inject(PartyIdentifiableGlobalIdentifierDao.class)
    			.readByIdentifiableGlobalIdentifierByRole(product.getGlobalIdentifier(), read(BusinessRole.class, RootConstant.Code.BusinessRole.PROVIDER)));
    	if(partyIdentifiableGlobalIdentifier!=null)
    		product.setProviderParty(partyIdentifiableGlobalIdentifier.getParty());
    }
    
    @Override
	protected void beforeCrud(Product product, Crud crud) {
		super.beforeCrud(product, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(product.getStockable())){
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
    public Collection<Product> findByCategory(ProductCategory category) {
    	return dao.readByCategory(category);
    }
    
    /**/
    
    public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<Product> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(Product.class);
		}
	}
	
}
