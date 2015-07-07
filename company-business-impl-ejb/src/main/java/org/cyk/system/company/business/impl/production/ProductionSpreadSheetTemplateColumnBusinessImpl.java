package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateColumnBusiness;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateColumnDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetTemplateColumnBusinessImpl extends AbstractTypedBusinessService<ProductionSpreadSheetTemplateColumn, ProductionSpreadSheetTemplateColumnDao> implements ProductionSpreadSheetTemplateColumnBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionSpreadSheetTemplateColumnBusinessImpl(ProductionSpreadSheetTemplateColumnDao dao) {
		super(dao);
	}

	
	
}
