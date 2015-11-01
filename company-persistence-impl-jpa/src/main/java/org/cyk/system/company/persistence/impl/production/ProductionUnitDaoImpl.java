package org.cyk.system.company.persistence.impl.production;

import java.io.Serializable;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.persistence.api.production.ProductionUnitDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionUnitDaoImpl extends AbstractTypedDao<ProductionUnit> implements ProductionUnitDao,Serializable {

	private static final long serialVersionUID = -1712788156426144935L;

}
