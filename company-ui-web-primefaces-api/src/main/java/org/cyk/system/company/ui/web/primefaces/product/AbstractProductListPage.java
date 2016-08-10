package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.product.AbstractProductDetails;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormManyPage;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Getter @Setter
public abstract class AbstractProductListPage<PRODUCT extends Product> extends AbstractCrudManyPage<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
	public static class AbstractAdapter<PRODUCT extends Product> extends AbstractBusinessEntityFormManyPage.BusinessEntityFormManyPageListener.Adapter.Default<PRODUCT> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public AbstractAdapter(Class<PRODUCT> aClass) {
			super(aClass);
			FormConfiguration configuration = createFormConfiguration(Crud.READ, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			configuration.addFieldNames(AbstractProductDetails.FIELD_CODE,AbstractProductDetails.FIELD_NAME);
		}
		
	}
}
