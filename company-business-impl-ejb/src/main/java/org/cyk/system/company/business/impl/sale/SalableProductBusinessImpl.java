package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;

public class SalableProductBusinessImpl extends AbstractCollectionBusinessImpl<SalableProduct,SalableProductInstance, SalableProductDao,SalableProductInstanceDao,SalableProductInstanceBusiness> implements SalableProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private ProductDao productDao;
	@Inject private SalableProductInstanceDao salableProductInstanceDao;
	
	@Inject
	public SalableProductBusinessImpl(SalableProductDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SalableProduct instanciateOne(String productCode, String unitPrice) {
		SalableProduct salableProduct = new SalableProduct();
		salableProduct.setProduct(productDao.read(productCode));
		salableProduct.setPrice(commonUtils.getBigDecimal(unitPrice));
		return salableProduct;
	}

	@Override
	public List<SalableProduct> instanciateMany(String[][] arguments) {
		List<SalableProduct> list = new ArrayList<>();
		for(String[] info : arguments)
			list.add(instanciateOne(info[0], info.length>1?info[1]:null));
		return list;
	}
	
	@Override
	public SalableProduct create(SalableProduct salableProduct) {
		if(StringUtils.isBlank(salableProduct.getCode()))
			salableProduct.setCode(salableProduct.getProduct().getCode());
		if(StringUtils.isBlank(salableProduct.getName()))
			salableProduct.setName(salableProduct.getProduct().getName());
		return super.create(salableProduct);
	}

	@Override
	protected SalableProductInstanceBusiness getItemBusiness() {
		return CompanyBusinessLayer.getInstance().getSalableProductInstanceBusiness();
	}

	@Override
	protected SalableProductInstanceDao getItemDao() {
		return salableProductInstanceDao;
	}
}
