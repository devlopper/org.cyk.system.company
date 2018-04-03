package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductCollectionPropertiesBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductCollectionProperties;
import org.cyk.system.company.model.sale.SalableProductCollectionPropertiesType;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionPropertiesDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SalableProductCollectionPropertiesBusinessImpl extends AbstractTypedBusinessService<SalableProductCollectionProperties, SalableProductCollectionPropertiesDao> implements SalableProductCollectionPropertiesBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductCollectionPropertiesBusinessImpl(SalableProductCollectionPropertiesDao dao) {
		super(dao);
	}
	
	/**/
	
	@Override
	public SalableProductCollectionProperties instanciateOne() {
		return super.instanciateOne().setType(read(SalableProductCollectionPropertiesType.class, CompanyConstant.Code.SalableProductCollectionProperties.SALE));
	}

}
