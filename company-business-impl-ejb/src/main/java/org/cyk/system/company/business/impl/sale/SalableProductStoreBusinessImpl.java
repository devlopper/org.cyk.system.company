package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductPropertiesBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreBusiness;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.LoggingHelper;

public class SalableProductStoreBusinessImpl extends AbstractTypedBusinessService<SalableProductStore, SalableProductStoreDao> implements SalableProductStoreBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductStoreBusinessImpl(SalableProductStoreDao dao) {
		super(dao);
	}
	
	@Override
	protected void computeChanges(SalableProductStore salableProductStore, LoggingHelper.Message.Builder loggingMessageBuilder) {
		super.computeChanges(salableProductStore, loggingMessageBuilder);
		FieldHelper.getInstance().copy(salableProductStore.getProductStore(), salableProductStore,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		if(salableProductStore.getSalableProductProperties() == null) {
			if(salableProductStore.getProductStore()!=null) {
				SalableProduct salableProduct = inject(SalableProductDao.class).readByProduct(salableProductStore.getProductStore().getProduct());
				if(salableProduct!=null){
					salableProductStore.setSalableProductProperties(salableProduct.getProperties());
				}
			}
		}
	}
	
	@Override
	protected void afterDelete(SalableProductStore salableProductStore) {
		super.afterDelete(salableProductStore);
		if(inject(SalableProductDao.class).countByProperties(salableProductStore.getSalableProductProperties()) == 0 ){
			inject(SalableProductPropertiesBusiness.class).delete(salableProductStore.getSalableProductProperties());
		}
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<SalableProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProduct.class);
		}
		
	}

}
