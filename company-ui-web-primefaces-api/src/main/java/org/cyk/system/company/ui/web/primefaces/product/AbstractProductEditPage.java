package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Getter @Setter
public abstract class AbstractProductEditPage<PRODUCT extends Product> extends AbstractCrudOnePage<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Getter @Setter
	public static abstract class AbstractForm<PRODUCT extends Product> extends AbstractBusinessIdentifiedEditFormModel<PRODUCT> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
	
	public static abstract class AbstractAdapter<PRODUCT extends Product> extends AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.Adapter.Default<PRODUCT> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public AbstractAdapter(Class<PRODUCT> aClass) {
			super(aClass);
			FormConfiguration configuration = createFormConfiguration(Crud.CREATE, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			configuration.addRequiredFieldNames(AbstractForm.FIELD_CODE,AbstractForm.FIELD_NAME);
			
			configuration = createFormConfiguration(Crud.UPDATE, UIManager.getInstance().businessEntityInfos(entityTypeClass).getUserInterface().getLabelId());
			configuration.addRequiredFieldNames(AbstractForm.FIELD_CODE,AbstractForm.FIELD_NAME);
			
			configuration = createFormConfiguration(Crud.DELETE);
			configuration.addFieldNames(AbstractForm.FIELD_CODE,AbstractForm.FIELD_NAME);
			
		}
		
	}
	
}
