package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ResellerProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.company.persistence.api.production.ResellerProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ResellerProductBusinessImpl extends AbstractTypedBusinessService<ResellerProduct, ResellerProductDao> implements ResellerProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ResellerProductBusinessImpl(ResellerProductDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ResellerProduct> findByReseller(Reseller reseller){
		return dao.readByReseller(reseller);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ResellerProduct> findByProduct(Product product){
		return dao.readByProduct(product);
	}

}
