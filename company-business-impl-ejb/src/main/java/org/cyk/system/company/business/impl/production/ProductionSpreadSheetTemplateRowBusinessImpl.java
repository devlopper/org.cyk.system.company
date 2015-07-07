package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateRowBusiness;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateRow;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateRowDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetTemplateRowBusinessImpl extends AbstractTypedBusinessService<ProductionSpreadSheetTemplateRow, ProductionSpreadSheetTemplateRowDao> implements ProductionSpreadSheetTemplateRowBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionSpreadSheetTemplateRowBusinessImpl(ProductionSpreadSheetTemplateRowDao dao) {
		super(dao);
	}

	
	
}
