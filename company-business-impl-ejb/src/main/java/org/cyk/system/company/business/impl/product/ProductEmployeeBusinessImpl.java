package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductEmployeeBusiness;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.persistence.api.product.ProductEmployeeDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class ProductEmployeeBusinessImpl extends AbstractTypedBusinessService<ProductEmployee, ProductEmployeeDao> implements ProductEmployeeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	//@Inject private ProductDao productDao;
	
	@Inject
	public ProductEmployeeBusinessImpl(ProductEmployeeDao dao) {
		super(dao);
	}
 
	@Override
	public Collection<ProductEmployee> findPerformersBySale(Sale sale) {
		Collection<ProductEmployee> collection = dao.readPerformersBySale(sale);
		/*if(collection.isEmpty()){
			for(SaleProduct saleProduct : sale.getSaleProducts())
				if(saleProduct.getProduct() instanceof ProductCollection){
					for(Product product : productDao.readByCollection((ProductCollection) saleProduct.getProduct()))
						collection.add(new ProductEmployee(product, null));
				}else
					collection.add(new ProductEmployee(saleProduct.getProduct(), null));
		}*/
		return collection;
	}

	
	
}
