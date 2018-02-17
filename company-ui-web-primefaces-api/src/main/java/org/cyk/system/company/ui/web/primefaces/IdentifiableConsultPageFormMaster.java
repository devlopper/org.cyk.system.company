package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionTypeDao;
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
	
	protected void __prepare__() {
		super.__prepare__();
		Form.Detail detail = getDetail();
		detail.setFieldsObjectFromMaster();
		
		if(SalableProductCollection.class.equals(getPropertiesMap().getActionOnClass())){
			//detail.setFieldsObjectFromMaster(SalableProductCollection.FIELD_COST);
			//detail.addReadOnly(Cost.FIELD_VALUE);
			
			/**/
			/*
			DataTable dataTable = instanciateDataTable(SalableProductCollectionItem.class,null,null,Boolean.TRUE);
			
			dataTable.prepare();
			dataTable.build();
			
			dataTable.getColumn("cost.value").__setPropertyFooterPropertyValueBasedOnMaster__();
			*/
			
			IdentifiableEditPageFormMaster.prepareSalableProductCollection(detail,null);
		}else if(Sale.class.equals(getPropertiesMap().getActionOnClass())){
			IdentifiableEditPageFormMaster.prepareSalableProductCollection(detail,Sale.FIELD_SALABLE_PRODUCT_COLLECTION);
			
			MovementCollection movementCollection = inject(MovementCollectionBusiness.class)
					.findByTypeByJoin(inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.SALE_BALANCE), (Sale)getObject())
					.iterator().next()
					;
			addDataTableMovement(movementCollection);
			
		}
	}
	
}
