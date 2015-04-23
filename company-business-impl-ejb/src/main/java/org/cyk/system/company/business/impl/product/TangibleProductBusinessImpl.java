package org.cyk.system.company.business.impl.product;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;

@Stateless
public class TangibleProductBusinessImpl extends AbstractProductBusinessImpl<TangibleProduct,TangibleProductDao> implements TangibleProductBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public TangibleProductBusinessImpl(TangibleProductDao dao) {
        super(dao);
    }
	
	@Override
	protected Set<TangibleProduct> products(Collection<SaleProduct> saleProducts) {
	
		return null;
	}



	@Override
	protected void beforeUpdate(TangibleProduct product, BigDecimal usedCount) {
			
	}
	
	public static void __beforeUpdate__(TangibleProduct product, BigDecimal usedCount) {
		product.setUseQuantity(product.getUseQuantity().subtract(usedCount));
		product.setUsedQuantity(product.getUsedQuantity().add(usedCount));
		
		if(product.getMinimalStockQuantityBlock()!=null)
			ExceptionUtils.getInstance().exception(product.getUseQuantity().compareTo(product.getMinimalStockQuantityBlock())==-1, 
			"exception.tangibleproduct.quantity.cannotbelowerthantoblock",new Object[]{product.getName()});
	}
}
