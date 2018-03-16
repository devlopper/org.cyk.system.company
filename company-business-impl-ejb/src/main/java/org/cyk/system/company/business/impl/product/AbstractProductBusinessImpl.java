package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.product.AbstractProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.party.PartyIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.model.RootConstant;

public abstract class AbstractProductBusinessImpl<PRODUCT extends Product,DAO extends AbstractProductDao<PRODUCT>> extends AbstractEnumerationBusinessImpl<PRODUCT,DAO> implements AbstractProductBusiness<PRODUCT>,Serializable {
	private static final long serialVersionUID = 2801588592108008404L;

    public AbstractProductBusinessImpl(DAO dao) {
        super(dao);
    }
    
    @Override
	protected void beforeCrud(PRODUCT product, Crud crud) {
		super.beforeCrud(product, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(product.getProviderParty()!=null){
				if(Crud.CREATE.equals(crud)){
					product.addIdentifiables(inject(PartyIdentifiableGlobalIdentifierBusiness.class).instanciateOne()
							.setParty(product.getProviderParty())
							.setRoleFromCode(RootConstant.Code.PartyBusinessRole.PROVIDER)
							.setIdentifiableGlobalIdentifier(product.getGlobalIdentifier())
							);
				}else{
					
				}
				//inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByGlobalIdentifier(product.getProviderParty(), product.getGlobalIdentifier());
				//PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = ;
			}
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
