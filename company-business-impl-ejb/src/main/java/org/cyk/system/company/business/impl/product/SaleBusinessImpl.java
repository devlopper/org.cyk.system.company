package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}

	@Override
	public void process(Sale sale) {
		sale.setCost(BigDecimal.ZERO);
		for(SaledProduct saleProduct : sale.getSaledProducts())
			sale.setCost( sale.getCost().add(saleProduct.getPrice() ));
	}
	
}
