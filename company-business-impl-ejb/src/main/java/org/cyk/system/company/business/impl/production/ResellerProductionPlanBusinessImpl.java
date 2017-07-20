package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ResellerProductionPlanBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.persistence.api.production.ResellerProductionPlanDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class ResellerProductionPlanBusinessImpl extends AbstractTypedBusinessService<ResellerProductionPlan, ResellerProductionPlanDao> implements ResellerProductionPlanBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ResellerProductionPlanBusinessImpl(ResellerProductionPlanDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ResellerProductionPlan> findByReseller(Reseller reseller){
		return dao.readByReseller(reseller);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ResellerProductionPlan> findByProductionPlan(ProductionPlan productionPlan) {
		return dao.readByProductionPlan(productionPlan);
	}

}
