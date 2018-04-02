package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductPropertiesBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductProperties;
import org.cyk.system.company.model.sale.ValueAddedTaxRate;
import org.cyk.system.company.persistence.api.sale.SalableProductPropertiesDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SalableProductPropertiesBusinessImpl extends AbstractTypedBusinessService<SalableProductProperties, SalableProductPropertiesDao> implements SalableProductPropertiesBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductPropertiesBusinessImpl(SalableProductPropertiesDao dao) {
		super(dao);
	}
	
	/**/
	
	@Override
	public SalableProductProperties instanciateOne() {
		return super.instanciateOne().setValueAddedTaxRate(read(ValueAddedTaxRate.class, CompanyConstant.Code.ValueAddedTaxRate._18));
	}

}
