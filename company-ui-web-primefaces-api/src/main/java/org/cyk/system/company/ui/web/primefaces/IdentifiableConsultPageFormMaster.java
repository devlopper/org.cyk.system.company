package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionTypeDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.userinterface.container.Form;

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
	
	@SuppressWarnings("unchecked")
	protected void __prepare__() {
		super.__prepare__();
		Form.Detail detail = getDetail();
		detail.setFieldsObjectFromMaster();
		
		if(ClassHelper.getInstance().isInstanceOf(Product.class, (Class<?>) getPropertiesMap().getActionOnClass())){
			IdentifiableEditPageFormMaster.prepareProduct(detail, (Class<? extends Product>) getPropertiesMap().getActionOnClass());
		}else if(SalableProductCollection.class.equals(getPropertiesMap().getActionOnClass())){
			IdentifiableEditPageFormMaster.prepareSalableProductCollection(detail,null,Boolean.TRUE);
		}else if(SalableProduct.class.equals(getPropertiesMap().getActionOnClass())){
			IdentifiableEditPageFormMaster.prepareSalableProduct(detail,Boolean.TRUE);
		}else if(Sale.class.equals(getPropertiesMap().getActionOnClass())){
			IdentifiableEditPageFormMaster.prepareSalableProductCollection(detail,Sale.FIELD_SALABLE_PRODUCT_COLLECTION,Boolean.TRUE);
			
			MovementCollection movementCollection = inject(MovementCollectionBusiness.class)
					.findByTypeByJoin(inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.SALE_BALANCE), (Sale)getObject())
					.iterator().next()
					;
			addDataTableMovement(movementCollection);
			
		}
	}
	
}
