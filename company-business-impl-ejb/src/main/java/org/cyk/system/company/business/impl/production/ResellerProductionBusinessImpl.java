package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ResellerProductionBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.persistence.api.production.ResellerDao;
import org.cyk.system.company.persistence.api.production.ResellerProductionPlanDao;
import org.cyk.system.company.persistence.api.production.ResellerProductionDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ResellerProductionBusinessImpl extends AbstractTypedBusinessService<ResellerProduction, ResellerProductionDao> implements ResellerProductionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private ResellerProductionPlanDao resellerProductionPlanDao;
	@Inject private ResellerDao resellerDao;
	
	@Inject
	public ResellerProductionBusinessImpl(ResellerProductionDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ResellerProduction> findByProduction(Production production) {
		return dao.readByProduction(production);
	}
	
	@Override
	public ResellerProduction create(ResellerProduction resellerProduction) {
		ResellerProductionPlan resellerProduct = resellerProductionPlanDao.readByResellerByProductionPlan(resellerProduction.getReseller()
				, resellerProduction.getProduction().getTemplate());
		resellerProduction.getAmount().setSystem(resellerProduction.getTakenQuantity().multiply(resellerProduct.getSaleUnitPrice()));
		resellerProduction.getAmount().computeGap();
		resellerProduction.setAmountGapCumul(resellerProduction.getReseller().getAmountGap().add(resellerProduction.getAmount().getGap()));
		resellerProduction.setDiscount(resellerProduct.getTakingUnitPrice().subtract(resellerProduct.getSaleUnitPrice()));
		resellerProduction.setCommission(resellerProduction.getTakenQuantity().subtract(resellerProduction.getReturnedQuantity())
				.multiply(resellerProduct.getCommissionRate()));
		resellerProduction.setPayable(resellerProduction.getAmount().getGap().add(resellerProduction.getDiscount()).add(resellerProduction.getCommission()));
		resellerProduction.setPayableCumul(resellerProduction.getReseller().getPayable().add(resellerProduction.getPayable()));
		super.create(resellerProduction);
		
		resellerProduction.getReseller().setAmountGap(resellerProduction.getAmountGapCumul());
		resellerProduction.getReseller().setPayable(resellerProduction.getPayableCumul());
		resellerDao.update(resellerProduction.getReseller());
		
		return resellerProduction;
	}

}
