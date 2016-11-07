package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.UserAccount;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegisterMovement create(CashRegisterMovement cashRegisterMovement) {
		if(isNotIdentified(cashRegisterMovement.getMovement())){
			exceptionUtils().exception(!cashRegisterMovement.getCashRegister().getMovementCollection().equals(cashRegisterMovement.getMovement().getCollection()),
					"movementcollectiondoesnotmacth");
			inject(MovementBusiness.class).create(cashRegisterMovement.getMovement());
		}
		super.create(cashRegisterMovement);
		if(cashRegisterMovement.getCode()==null)
			cashRegisterMovement.setCode(generateIdentifier(cashRegisterMovement,CompanyBusinessLayer.Listener.CASH_MOVEMENT_IDENTIFIER
					,inject(AccountingPeriodBusiness.class).findCurrent()
				.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()));
		dao.update(cashRegisterMovement);
		return cashRegisterMovement;
	}
	
	@Override
	public CashRegisterMovement update(CashRegisterMovement cashRegisterMovement) {
		if(cashRegisterMovement.getMovement()!=null)
			inject(MovementBusiness.class).update(cashRegisterMovement.getMovement());
		return super.update(cashRegisterMovement);
	}
	
	@Override
	public CashRegisterMovement delete(CashRegisterMovement cashRegisterMovement) {
		if(cashRegisterMovement.getMovement()!=null)
			inject(MovementBusiness.class).delete(cashRegisterMovement.getMovement());
		cashRegisterMovement.setMovement(null);
		return super.delete(cashRegisterMovement);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<CashRegisterMovement> findByCashRegister(CashRegister cashRegister) {
		return dao.readByCashRegister(cashRegister);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CashRegisterMovement instanciateOne(UserAccount userAccount,CashRegister cashRegister) {
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
		setCashRegister(cashRegisterMovement, cashRegister);
		return cashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void setCashRegister(CashRegisterMovement cashRegisterMovement,CashRegister cashRegister) {
		cashRegisterMovement.setCashRegister(cashRegister);
		if(cashRegisterMovement.getCashRegister()!=null)
			cashRegisterMovement.setMovement(inject(MovementBusiness.class)
				.instanciateOne(cashRegisterMovement.getCashRegister().getMovementCollection(), Boolean.TRUE));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CashRegisterMovement instanciateOne(UserAccount userAccount) {
		CashRegister cashRegister;
		if(userAccount.getUser() instanceof Person){
			Collection<CashRegister> cashRegisters = inject(CashRegisterDao.class).readByPerson((Person) userAccount.getUser());
			if(cashRegisters==null || cashRegisters.isEmpty())
				cashRegister = null;
			else
				cashRegister = cashRegisters.iterator().next();
		}else{
			cashRegister = null;
		}
		return instanciateOne(userAccount,cashRegister);
	}
}
