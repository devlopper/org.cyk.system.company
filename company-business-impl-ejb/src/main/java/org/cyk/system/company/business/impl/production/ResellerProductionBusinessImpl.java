package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ResellerProductionBusiness;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.persistence.api.production.ResellerProductionDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ResellerProductionBusinessImpl extends AbstractTypedBusinessService<ResellerProduction, ResellerProductionDao> implements ResellerProductionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ResellerProductionBusinessImpl(ResellerProductionDao dao) {
		super(dao);
	}

}
