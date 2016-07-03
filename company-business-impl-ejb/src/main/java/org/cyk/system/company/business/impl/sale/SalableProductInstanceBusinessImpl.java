package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

@Stateless
public class SalableProductInstanceBusinessImpl extends AbstractCollectionItemBusinessImpl<SalableProductInstance, SalableProductInstanceDao,SalableProduct> implements SalableProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceBusinessImpl(SalableProductInstanceDao dao) {
		super(dao);
	}
	
	@Override
	public SalableProductInstance create(SalableProductInstance salableProductInstance) {
		if(StringUtils.isBlank(salableProductInstance.getName()))
			salableProductInstance.setName(salableProductInstance.getCollection().getName());
		return super.create(salableProductInstance);
	}

	@Override
	public void create(SalableProduct salableProduct, Set<String> codes) {
		Collection<SalableProductInstance> salableProductInstances = new ArrayList<>();
		for(String code : codes){
			SalableProductInstance salableProductInstance = new SalableProductInstance();
			salableProductInstance.setCollection(salableProduct);
			salableProductInstance.setCode(code);
			salableProductInstance.setName(code);
			salableProductInstances.add(salableProductInstance);
		}
			
		CompanyBusinessLayer.getInstance().getSalableProductInstanceBusiness().create(salableProductInstances);
	}
	
	@Override
	public Collection<SalableProductInstance> findWhereNotAssociatedToCashRegister() {
		return dao.readWhereNotAssociatedToCashRegister();
	}

	@Override
	public Collection<SalableProductInstance> findByCollectionByCashRegisterByFiniteStateMachineState(SalableProduct salableProduct, CashRegister cashRegister, FiniteStateMachineState finiteStateMachineState) {
		return dao.readByCollectionByCashRegisterByFiniteStateMachineState(salableProduct,cashRegister, finiteStateMachineState);
	}
}
