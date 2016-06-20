package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.Party;

@Stateless
public class SalableProductInstanceCashRegisterBusinessImpl extends AbstractTypedBusinessService<SalableProductInstanceCashRegister, SalableProductInstanceCashRegisterDao> implements SalableProductInstanceCashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceCashRegisterBusinessImpl(SalableProductInstanceCashRegisterDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SalableProductInstanceCashRegister> findByCriteria(SearchCriteria searchCriteria) {
		prepareFindByCriteria(searchCriteria);
		return dao.readByCriteria(searchCriteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SearchCriteria searchCriteria) {
		return dao.countByCriteria(searchCriteria);
	}
	
	@Override
	public SalableProductInstanceCashRegister create(SalableProductInstanceCashRegister salableProductInstanceCashRegister,Party party) {
		/*salableProductInstanceCashRegister.setGlobalIdentifier(new GlobalIdentifier());
		salableProductInstanceCashRegister.getGlobalIdentifier().setIdentifier(RandomStringUtils.randomAlphanumeric(26));
		((GenericDaoImpl)genericDao).getEntityManager().persist(salableProductInstanceCashRegister.getGlobalIdentifier());
		*/
		salableProductInstanceCashRegister = super.create(salableProductInstanceCashRegister);
		
		RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().create(salableProductInstanceCashRegister,salableProductInstanceCashRegister.getFiniteStateMachineState(),party);
		return salableProductInstanceCashRegister;
	}
	
	@Override
	public void create(Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters,Party party) {
		for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters)
			create(salableProductInstanceCashRegister, party);
	}
	
	@Override
	public SalableProductInstanceCashRegister update(SalableProductInstanceCashRegister salableProductInstanceCashRegister,Party party) {
		salableProductInstanceCashRegister = super.update(salableProductInstanceCashRegister);
		RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().create(salableProductInstanceCashRegister,salableProductInstanceCashRegister.getFiniteStateMachineState(),party);
		return salableProductInstanceCashRegister;
	}
	
	@Override
	public void update(Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters,Party party) {
		for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters)
			update(salableProductInstanceCashRegister, party);
	}
}
