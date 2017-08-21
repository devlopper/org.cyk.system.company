package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;

public class SalableProductBusinessImpl extends AbstractCollectionBusinessImpl<SalableProduct,SalableProductInstance, SalableProductDao,SalableProductInstanceDao,SalableProductInstanceBusiness> implements SalableProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SalableProductInstanceDao salableProductInstanceDao;
	
	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}

	@Override
	protected SalableProduct __instanciateOne__(String[] values,org.cyk.system.root.business.api.TypedBusiness.InstanciateOneListener<SalableProduct> listener) {
		super.__instanciateOne__(values, listener);
		listener.getSetListener().setIndex(10);
		set(listener.getSetListener(), SalableProduct.FIELD_PRODUCT);
		set(listener.getSetListener(), SalableProduct.FIELD_PRICE);
		return listener.getInstance();
	}
	
	@Override
	protected void beforeCreate(SalableProduct salableProduct) {
		super.beforeCreate(salableProduct);
		if(salableProduct.getProduct()==null){	
			salableProduct.setProduct(inject(ProductDao.class).read(salableProduct.getCode()));
		}else{
			if(StringUtils.isBlank(salableProduct.getCode()))
				salableProduct.setCode(salableProduct.getProduct().getCode());
			if(StringUtils.isBlank(salableProduct.getName()))
				salableProduct.setName(salableProduct.getProduct().getName());	
		}
	}
		
	@Override
	public void create(Class<? extends Product> aClass,String code, String name, BigDecimal price) {
		Product product = TangibleProduct.class.equals(aClass) ? new TangibleProduct(code, name, null) : new IntangibleProduct(code, name, null);
		inject(GenericBusiness.class).create(product);
		SalableProduct salableProduct = new SalableProduct(product, price);
		create(salableProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
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
