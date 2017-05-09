package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementModeDao;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.generator.StringGeneratorBusiness;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.mathematics.MovementDao;

public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return CashRegisterMovementBusinessImpl.Listener.COLLECTION;
	}
	
	@Override
	public CashRegisterMovement instanciateOne() {
		CashRegisterMovement cashRegisterMovement = super.instanciateOne();
		cashRegisterMovement.setMode(inject(CashRegisterMovementModeDao.class).read(CompanyConstant.Code.CashRegisterMovementMode.CASH));
		return cashRegisterMovement;
	}
	
	@Override
	public CashRegisterMovement instanciateOne(String code,String name,String amount,String cashRegisterCode,String cashRegisterMovementModeCode) {
		CashRegisterMovement cashRegisterMovement = instanciateOne();
		cashRegisterMovement.setCode(code);
		cashRegisterMovement.setName(name);
		cashRegisterMovement.setMode(inject(CashRegisterMovementModeDao.class).read(cashRegisterMovementModeCode));
		if(cashRegisterMovement.getMode()!=null){
			if(!CompanyConstant.Code.CashRegisterMovementMode.CASH.equals(cashRegisterMovementModeCode)){
				cashRegisterMovement.setSupportingDocument(inject(FileBusiness.class).instanciateOne());
			}
		}
		if(StringUtils.isNotBlank(cashRegisterCode)){
			setCashRegister(cashRegisterMovement, inject(CashRegisterDao.class).read(cashRegisterCode));
		}
		
		if(cashRegisterMovement.getMovement()!=null)
			cashRegisterMovement.getMovement().setValue(new BigDecimal(amount));
		
		return cashRegisterMovement;
	}
	
	@Override
	public CashRegisterMovement instanciateOne(String code,String name,String amount,String cashRegisterCode) {
		return instanciateOne(code, name, amount, cashRegisterCode,CompanyConstant.Code.CashRegisterMovementMode.CASH);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CashRegisterMovement instanciateOne(UserAccount userAccount,CashRegister cashRegister) {
		CashRegisterMovement cashRegisterMovement = instanciateOne();
		setCashRegister(cashRegisterMovement, cashRegister);
		return cashRegisterMovement;
	}
		
	@Override
	protected void beforeCreate(CashRegisterMovement cashRegisterMovement) {
		super.beforeCreate(cashRegisterMovement);
		if(isNotIdentified(cashRegisterMovement.getMovement())){
			exceptionUtils().exception(!cashRegisterMovement.getCashRegister().getMovementCollection().equals(cashRegisterMovement.getMovement().getCollection()),
					"movementcollectiondoesnotmacth");
			cashRegisterMovement.set(cashRegisterMovement.getMovement(), GlobalIdentifier.FIELD_CODE,GlobalIdentifier.FIELD_NAME,GlobalIdentifier.FIELD_EXISTENCE_PERIOD);
			inject(MovementBusiness.class).create(cashRegisterMovement.getMovement());
		}
		
		if(cashRegisterMovement.getMode().getCode().equals(CompanyConstant.Code.CashRegisterMovementMode.CASH) && 
				cashRegisterMovement.getStampDutyInterval()==null){
			cashRegisterMovement.setStampDutyInterval(inject(IntervalBusiness.class).findByCollectionByValue(CompanyConstant.Code.IntervalCollection.STAMP_DUTY
					,cashRegisterMovement.getMovement().getValue(), 2));
		}
		
		exceptionUtils().exception(!ArrayUtils.contains(new String[]{CompanyConstant.Code.CashRegisterMovementMode.CASH}, cashRegisterMovement.getMode().getCode())
				&& StringUtils.isBlank(cashRegisterMovement.getSupportingDocument().getCode()), "supportingdocumentidentifierrequired");
		
		exceptionUtils().exception(ArrayUtils.contains(new String[]{CompanyConstant.Code.CashRegisterMovementMode.CHEQUE}, cashRegisterMovement.getMode().getCode())
				&& StringUtils.isBlank(cashRegisterMovement.getSupportingDocument().getGenerator()), "supportingdocumentgeneratorrequired");
		
			
	}
			
	@Override
	protected void beforeUpdate(CashRegisterMovement cashRegisterMovement) {
		super.beforeUpdate(cashRegisterMovement);
		if(cashRegisterMovement.getMovement()!=null)
			inject(MovementBusiness.class).update(cashRegisterMovement.getMovement());
	}
	
	@Override
	protected void beforeDelete(CashRegisterMovement cashRegisterMovement) {
		super.beforeDelete(cashRegisterMovement);
		if(cashRegisterMovement.getMovement()!=null){
			Movement movement = cashRegisterMovement.getMovement();
			cashRegisterMovement.setMovement(null);
			inject(MovementBusiness.class).delete(movement);
		}
	}
	
	@Override
	protected void deleteFileIdentifiableGlobalIdentifier(CashRegisterMovement arg0) {
		
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<CashRegisterMovement> findByCashRegister(CashRegister cashRegister) {
		return dao.readByCashRegister(cashRegister);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void setCashRegister(CashRegisterMovement cashRegisterMovement,CashRegister cashRegister) {
		cashRegisterMovement.setCashRegister(cashRegister);
		if(cashRegisterMovement.getCashRegister()!=null)
			cashRegisterMovement.setMovement(inject(MovementBusiness.class).instanciateOne(cashRegisterMovement.getCashRegister().getMovementCollection()));
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
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<CashRegisterMovement>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<CashRegisterMovement> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					@Override
					public void afterCreate(CashRegisterMovement cashRegisterMovement) {
						super.afterCreate(cashRegisterMovement);
						if(StringUtils.isBlank(cashRegisterMovement.getCode()))
							cashRegisterMovement.setCode(inject(StringGeneratorBusiness.class).generateIdentifier(cashRegisterMovement,null
									,inject(AccountingPeriodBusiness.class).findCurrent()
								.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()));
						
						inject(CashRegisterMovementDao.class).update(cashRegisterMovement);
						if(cashRegisterMovement.getMovement()!=null){
							cashRegisterMovement.getMovement().setCode(RootConstant.Code.generate(cashRegisterMovement.getMovement().getCollection(), cashRegisterMovement.getCode()));
							inject(MovementDao.class).update(cashRegisterMovement.getMovement());
						}
					}
					
				}
			}
			
		}
	}
}
