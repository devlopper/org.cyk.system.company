package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.api.Crud;

public class TangibleProductBusinessImpl extends AbstractProductBusinessImpl<TangibleProduct,TangibleProductDao> implements TangibleProductBusiness {
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public TangibleProductBusinessImpl(TangibleProductDao dao) {
        super(dao);
    }
	
	@Override
	protected void beforeCrud(TangibleProduct tangibleProduct, Crud crud) {
		super.beforeCrud(tangibleProduct, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Crud.CREATE.equals(crud)){
				if(Boolean.TRUE.equals(tangibleProduct.getIsStockable())){
					tangibleProduct.addIdentifiables(inject(StockableTangibleProductBusiness.class).instanciateOne().setTangibleProduct(tangibleProduct));
				}		
			}
		}
		
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractProductBusinessImpl.BuilderOneDimensionArray<TangibleProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(TangibleProduct.class);
		}
		
	}	
}
