package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.SaledProductBusiness;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.persistence.api.product.SaledProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SaledProductBusinessImpl extends AbstractTypedBusinessService<SaledProduct, SaledProductDao> implements SaledProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaledProductBusinessImpl(SaledProductDao dao) {
		super(dao);
	}

	@Override
	public void process(SaledProduct saledProduct) {
		saledProduct.setPrice(saledProduct.getProduct().getPrice().multiply(new BigDecimal(saledProduct.getQuantity())));
	}
	
}
