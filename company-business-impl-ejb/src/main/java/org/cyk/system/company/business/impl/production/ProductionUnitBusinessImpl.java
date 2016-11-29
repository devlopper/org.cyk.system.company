package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionUnitBusiness;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.persistence.api.production.ProductionUnitDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class ProductionUnitBusinessImpl extends AbstractTypedBusinessService<ProductionUnit, ProductionUnitDao> implements ProductionUnitBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ProductionUnitBusinessImpl(ProductionUnitDao dao) {
		super(dao);
	}
	
	@Override
	public ProductionUnit create(ProductionUnit productionUnit) {
		if(productionUnit.getCompany().getIdentifier()==null){
			inject(CompanyBusiness.class).create(productionUnit.getCompany());
		}
		return super.create(productionUnit);
	}

}
