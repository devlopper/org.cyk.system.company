package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private CashierDao cashierDao;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegisterMovement create(CashRegisterMovement cashRegisterMovement) {
		inject(MovementBusiness.class).create(cashRegisterMovement.getMovement());
		super.create(cashRegisterMovement);
		if(cashRegisterMovement.getComputedIdentifier()==null)
			cashRegisterMovement.setComputedIdentifier(generateIdentifier(cashRegisterMovement,CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFIER
					,inject(AccountingPeriodBusiness.class).findCurrent()
				.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()));
		dao.update(cashRegisterMovement);
		return cashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CashRegisterMovement instanciateOne(Person person) {
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
		cashRegisterMovement.setCashRegister(cashierDao.readByPerson(person).getCashRegister());
		cashRegisterMovement.setMovement(inject(MovementBusiness.class)
				.instanciateOne(cashRegisterMovement.getCashRegister().getMovementCollection(), Boolean.TRUE));
		return cashRegisterMovement;
	}
	
	@Override
	public CashRegisterMovement delete(CashRegisterMovement cashRegisterMovement) {
		inject(MovementBusiness.class).delete(cashRegisterMovement.getMovement());
		cashRegisterMovement.setMovement(null);
		return super.delete(cashRegisterMovement);
	}

}
