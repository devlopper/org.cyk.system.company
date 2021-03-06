package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.ui.web.primefaces.product.ProductIdentifiableEditPageFormMaster;
import org.cyk.system.company.ui.web.primefaces.sale.SaleIdentifiableEditPageFormMaster;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.mathematics.movement.MovementCollectionTypeDao;
import org.cyk.utility.common.userinterface.container.form.FormDetail;

public class IdentifiableConsultPageFormMaster extends org.cyk.ui.web.primefaces.IdentifiableConsultPageFormMaster implements Serializable {
	private static final long serialVersionUID = -6211058744595898478L;
	
	@Override
	protected void __setFromRequestParameter__() {
		
	}
	
	@Override
	protected void ____addName____() {
		if(Sale.class.equals(getPropertiesMap().getActionOnClass())){
			
		}else
			super.____addName____();
	}
	
	protected void __prepare__() {
		super.__prepare__();
		Class<?> actionOnClass = (Class<?>) getPropertiesMap().getActionOnClass();
		FormDetail detail = getDetail();
		detail.setFieldsObjectFromMaster();
		
		if(Product.class.equals(actionOnClass)){
			inject(ProductBusiness.class).setProviderParty((Product) getObject());
			ProductIdentifiableEditPageFormMaster.prepareProduct(detail, actionOnClass);
			addDataTableJoinGlobalIdentifier(PartyIdentifiableGlobalIdentifier.class);			
		}else if(ProductStore.class.equals(actionOnClass)){
			ProductIdentifiableEditPageFormMaster.prepareProductStore(detail,actionOnClass);
		}else if(SalableProduct.class.equals(actionOnClass)){
			SaleIdentifiableEditPageFormMaster.prepareSalableProduct(detail);
		}else if(Sale.class.equals(actionOnClass)){
			SaleIdentifiableEditPageFormMaster.prepareSalableProductStoreCollection(detail,Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION,Boolean.TRUE);
			
			MovementCollection movementCollection = inject(MovementCollectionBusiness.class)
					.findByTypeByJoin(inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.SALE_BALANCE), (Sale)getObject())
					.iterator().next()
					;
			addDataTableMovement(movementCollection);
			
		}
	}
	
}
