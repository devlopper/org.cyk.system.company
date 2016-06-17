package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.persistence.impl.AbstractCollectionDaoImpl;

public class SalableProductDaoImpl extends AbstractCollectionDaoImpl<SalableProduct,SalableProductInstance> implements SalableProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProduct,readByCashRegister;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduct, _select().where(SalableProduct.FIELD_PRODUCT));
		registerNamedQuery(readByCashRegister, "SELECT r1 FROM SalableProduct r1 WHERE EXISTS("
				+ " SELECT r2 FROM SalableProductInstanceCashRegister r2 WHERE r2.salableProductInstance.collection = r1 AND r2.cashRegister = :cashRegister"
				+ ")");
	}
	
	@Override
	public SalableProduct readByProduct(Product product) {
		return namedQuery(readByProduct).parameter(SalableProduct.FIELD_PRODUCT, product).ignoreThrowable(NoResultException.class).resultOne();
	}

	@Override
	public Collection<SalableProduct> readByCashRegister(CashRegister cashRegister) {
		return namedQuery(readByCashRegister).parameter(SalableProductInstanceCashRegister.FIELD_CASH_REGISTER, cashRegister).resultMany();
	}

	

}
