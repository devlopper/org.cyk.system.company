package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

@Deprecated
public class SalableProductInstanceBusinessImpl extends AbstractEnumerationBusinessImpl<SalableProductInstance, SalableProductInstanceDao> implements SalableProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceBusinessImpl(SalableProductInstanceDao dao) {
		super(dao);
	}
	
	@Override
	public void create(SalableProduct salableProduct, Set<String> codes) {
		Collection<SalableProductInstance> salableProductInstances = new ArrayList<>();
		for(String code : codes){
			SalableProductInstance salableProductInstance = new SalableProductInstance();
			salableProductInstance.setCode(code);
			salableProductInstance.setName(code);
			salableProductInstances.add(salableProductInstance);
		}
			
		inject(SalableProductInstanceBusiness.class).create(salableProductInstances);
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
