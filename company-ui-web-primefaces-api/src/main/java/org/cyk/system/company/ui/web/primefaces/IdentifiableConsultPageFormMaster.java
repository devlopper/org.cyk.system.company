package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.utility.common.userinterface.collection.DataTable;
import org.cyk.utility.common.userinterface.container.Form;

public class IdentifiableConsultPageFormMaster extends org.cyk.ui.web.primefaces.IdentifiableConsultPageFormMaster implements Serializable {
	private static final long serialVersionUID = -6211058744595898478L;
	
	@Override
	protected void __setFromRequestParameter__() {
		
	}
	
	protected void __prepare__() {
		super.__prepare__();
		Form.Detail detail = getDetail();
		detail.setFieldsObjectFromMaster();
		
		if(SalableProductCollection.class.equals(getPropertiesMap().getActionOnClass())){
			detail.setFieldsObjectFromMaster(SalableProductCollection.FIELD_COST);
			detail.addReadOnly(Cost.FIELD_VALUE);
			
			/**/
			DataTable dataTable = instanciateDataTable(SalableProductCollectionItem.class,null,null,Boolean.TRUE);
			
			dataTable.prepare();
			dataTable.build();
			
			dataTable.getColumn("cost.value").__setPropertyFooterPropertyValueBasedOnMaster__();
		}
		
	}
	
}
