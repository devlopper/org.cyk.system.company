package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;

public class SalableProductBusinessImpl extends AbstractCollectionBusinessImpl<SalableProduct,SalableProductInstance, SalableProductDao,SalableProductInstanceDao,SalableProductInstanceBusiness> implements SalableProductBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}
	
	@Override
	protected void createMaster(SalableProduct salableProduct,AbstractIdentifiable master) {
		if(master instanceof TangibleProduct){
			((TangibleProduct)master).setIsStockable(salableProduct.getIsProductStockable());
			((TangibleProduct)master).setStockQuantityMovementCollectionInitialValue(salableProduct.getProductStockQuantityMovementCollectionInitialValue());
			((TangibleProduct)master).setProviderParty(salableProduct.getProductProviderParty());
		}
		super.createMaster(salableProduct,master);
	}
	
	@Override
	protected void computeChanges(SalableProduct salableProduct, Builder logMessageBuilder) {
		super.computeChanges(salableProduct, logMessageBuilder);
		FieldHelper.getInstance().copy(salableProduct.getProduct(), salableProduct,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
	}
	
	@Override
	protected SalableProductInstanceBusiness getItemBusiness() {
		return inject(SalableProductInstanceBusiness.class);
	}

	@Override
	protected SalableProductInstanceDao getItemDao() {
		return inject(SalableProductInstanceDao.class);
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractCollectionBusinessImpl.BuilderOneDimensionArray<SalableProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProduct.class);
		}
		
	}

}
