package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;
import org.cyk.utility.common.helper.StringHelper;

public class SalableProductBusinessImpl extends AbstractCollectionBusinessImpl<SalableProduct,SalableProductInstance, SalableProductDao,SalableProductInstanceDao,SalableProductInstanceBusiness> implements SalableProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SalableProductInstanceDao salableProductInstanceDao;
	
	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}
	
	@Override
	protected void computeChanges(SalableProduct salableProduct, Builder logMessageBuilder) {
		super.computeChanges(salableProduct, logMessageBuilder);
		if(salableProduct.getProduct()!=null && StringHelper.getInstance().isBlank(salableProduct.getCode()))
			org.cyk.utility.common.helper.FieldHelper.getInstance().copy(salableProduct.getProduct(), salableProduct
					,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE));
		
		if(salableProduct.getProduct()!=null && StringHelper.getInstance().isBlank(salableProduct.getName()))
			org.cyk.utility.common.helper.FieldHelper.getInstance().copy(salableProduct.getProduct(), salableProduct
					,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
	}
	
	@Override @Deprecated
	public void create(Class<? extends Product> aClass,String code, String name, BigDecimal price) {
		/*Product product = TangibleProduct.class.equals(aClass) ? new TangibleProduct(code, name, null) : new IntangibleProduct(code, name, null);
		inject(GenericBusiness.class).create(product);
		SalableProduct salableProduct = new SalableProduct(product, price);
		create(salableProduct);
		*/
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER) @Deprecated
	public Collection<SalableProduct> findByCashRegister(CashRegister cashRegister) {
		return dao.readByCashRegister(cashRegister);
	}

	@Override
	protected SalableProductInstanceBusiness getItemBusiness() {
		return inject(SalableProductInstanceBusiness.class);
	}

	@Override
	protected SalableProductInstanceDao getItemDao() {
		return salableProductInstanceDao;
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractCollectionBusinessImpl.BuilderOneDimensionArray<SalableProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProduct.class);
		}
		
	}

}
