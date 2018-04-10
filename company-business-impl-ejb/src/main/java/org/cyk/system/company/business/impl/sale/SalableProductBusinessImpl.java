package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductPropertiesBusiness;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;

public class SalableProductBusinessImpl extends AbstractEnumerationBusinessImpl<SalableProduct, SalableProductDao> implements SalableProductBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}
	
	@Override
	public SalableProduct instanciateOne() {
		return super.instanciateOne().setProperties(inject(SalableProductPropertiesBusiness.class).instanciateOne())
				.addCascadeOperationToMasterFieldNames(SalableProduct.FIELD_PROPERTIES);
	}
	
	@Override
	protected void afterDelete(SalableProduct salableProduct) {
		super.afterDelete(salableProduct);
		inject(SalableProductPropertiesBusiness.class).delete(salableProduct.getProperties());
	}
	
	@Override
	protected void computeChanges(SalableProduct salableProduct, Builder logMessageBuilder) {
		super.computeChanges(salableProduct, logMessageBuilder);
		
		FieldHelper.getInstance().copy(salableProduct.getProduct(), salableProduct,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		
		FieldHelper.getInstance().copy(salableProduct, salableProduct.getProperties(),Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<SalableProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProduct.class);
		}
		
	}

}
