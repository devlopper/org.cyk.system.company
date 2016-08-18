package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.party.person.Person;

public class CashRegisterBusinessImpl extends AbstractTypedBusinessService<CashRegister, CashRegisterDao> implements CashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CashRegisterBusinessImpl(CashRegisterDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegister create(CashRegister cashRegister) {
		if(cashRegister.getOwnedCompany()==null)
			cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaultOwnedCompany());
		
		if(cashRegister.getMovementCollection()==null){
			cashRegister.setMovementCollection((inject(MovementCollectionBusiness.class).instanciateOne(cashRegister.getCode()
					, cashRegister.getCode()+MovementAction.INCREMENT, cashRegister.getCode()+MovementAction.DECREMENT)));
			inject(MovementCollectionBusiness.class).create(cashRegister.getMovementCollection());
		}
		return super.create(cashRegister);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<CashRegister> findByPerson(Person person) {
		return dao.readByPerson(person);
	}
}
