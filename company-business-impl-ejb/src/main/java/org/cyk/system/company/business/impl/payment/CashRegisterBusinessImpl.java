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
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;
import org.cyk.system.root.model.party.person.Person;

@Deprecated
public class CashRegisterBusinessImpl extends AbstractEnumerationBusinessImpl<CashRegister, CashRegisterDao> implements CashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CashRegisterBusinessImpl(CashRegisterDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegister instanciateOne() {
		CashRegister cashRegister =  super.instanciateOne();
		cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaulted());
		return cashRegister;
	}
	
	/*@Override
	public CashRegister create(CashRegister cashRegister) {
		if(cashRegister.getOwnedCompany()==null)
			cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaulted());
		
		if(cashRegister.getMovementCollection()==null){
			cashRegister.setMovementCollection((inject(MovementCollectionBusiness.class).instanciateOne(cashRegister.getCode()
					,RootConstant.Code.Interval.MOVEMENT_COLLECTION_VALUE, RootConstant.Code.MovementAction.INCREMENT, RootConstant.Code.MovementAction.DECREMENT)));
		}
		
		createIfNotIdentified(cashRegister.getMovementCollection());
		
		return super.create(cashRegister);
	}*/
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<CashRegister> findByPerson(Person person) {
		return dao.readByPerson(person);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CashRegister instanciateOneRandomly(String code) {
		CashRegister cashRegister = super.instanciateOneRandomly(code);
		cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaulted());
		/*cashRegister.setMovementCollection(inject(MovementCollectionBusiness.class).instanciateOneRandomly(code));
		cashRegister.getMovementCollection().getDecrementAction().getInterval().getLow().setValue(BigDecimal.ZERO);
		cashRegister.getMovementCollection().getDecrementAction().getInterval().getLow().setValue(BigDecimal.ZERO);
		*/
		return cashRegister;
	}
	
	public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<CashRegister> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(CashRegister.class);
			addFieldCodeName();
			addParameterArrayElementString(CashRegister.FIELD_MOVEMENT_COLLECTION);
		}
		
	}	
}
