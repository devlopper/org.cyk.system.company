package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementModeDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementCollectionDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
import org.cyk.system.root.model.CommonBusinessAction;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.utility.common.LogMessage;
import org.cyk.utility.common.helper.ConditionHelper;

@Deprecated
public class SaleCashRegisterMovementBusinessImpl extends AbstractCollectionItemBusinessImpl<SaleCashRegisterMovement, SaleCashRegisterMovementDao,SaleCashRegisterMovementCollection> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTION;
	}
	
	@Override
	protected Object[] getPropertyValueTokens(SaleCashRegisterMovement saleCashRegisterMovement, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE,GlobalIdentifier.FIELD_NAME}, name))
			return new Object[]{saleCashRegisterMovement.getCollection()};
		return super.getPropertyValueTokens(saleCashRegisterMovement, name);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(String collectionCode,String saleCode, String amount) {		
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne(inject(SaleCashRegisterMovementCollectionDao.class).read(collectionCode),saleCode,amount);
		
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(SaleCashRegisterMovementCollection collection,String saleCode, String amount) {
		return instanciateOne(collection,inject(SaleDao.class).read(saleCode),commonUtils.getBigDecimal(amount));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(SaleCashRegisterMovementCollection collection,Sale sale, BigDecimal amount) {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne(collection,Boolean.FALSE);
		saleCashRegisterMovement.setCode(sale.getCode());
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovement.setAmount(amount);
		inject(SaleCashRegisterMovementCollectionBusiness.class).add(collection,saleCashRegisterMovement);
		/*saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		saleCashRegisterMovement.getCashRegisterMovement().setCode(computedIdentifier);
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashierDao.readByPerson(personDao.read(cashierPersonCode)).getCashRegister());
		/*saleCashRegisterMovement.getCashRegisterMovement().setMovement(inject(MovementBusiness.class).instanciateOne(
				saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection(),amount));
		*/
		//saleCashRegisterMovement.getBalance()._set(sale.getBalance());
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(UserAccount userAccount,Sale sale,CashRegister cashRegister) {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne();
		setSale(saleCashRegisterMovement, sale);
		//saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		/*for(SalableProductCollectionItem salableProductCollectionItem : inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()))
			saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().getCollection().add(
					new SalableProductCollectionItemSaleCashRegisterMovement(salableProductCollectionItem,saleCashRegisterMovement));
		*/
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void setSale(SaleCashRegisterMovement saleCashRegisterMovement,Sale sale) {
		saleCashRegisterMovement.setSale(sale);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS) @Deprecated
	public SaleCashRegisterMovement instanciateOne(Sale sale,CashRegister cashRegister,Boolean input) {
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement(cashRegister,new Movement());
		//cashRegisterMovement.setMovement(inject(MovementBusiness.class).instanciateOne(cashRegister.getMovementCollection(),input));
		cashRegisterMovement.setMode(inject(CashRegisterMovementModeDao.class).read(CompanyConstant.Code.CashRegisterMovementMode.CASH));
		SaleCashRegisterMovement saleCashRegisterMovement = null;//new SaleCashRegisterMovement(sale,cashRegisterMovement);
		logInstanceCreated(saleCashRegisterMovement);
		return saleCashRegisterMovement;
	}
	
	@Deprecated
	public SaleCashRegisterMovement createOLD(SaleCashRegisterMovement saleCashRegisterMovement) {
		LogMessage.Builder logMessageBuilder = createLogMessageBuilder(CommonBusinessAction.CREATE);
		
		/*Customer customer = sale.getCustomer();
		Integer soldOut = BigDecimal.ZERO.compareTo(sale.getBalance().getValue());
		BigDecimal amount = null;
		Boolean deposit = null;
		if(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction()==null){
			amount = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue();
			deposit = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().signum() <= 0;
		}else{
			exceptionUtils().exception(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction()
					.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction())
					&& saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().signum()<0, "");
			deposit = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction()
				.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction());
		}
		
		if(deposit)
			exceptionUtils().exception(soldOut>=0, "exception.salecashregistermovement.in.soldout.yes");
		else
			exceptionUtils().exception(soldOut<0, "exception.salecashregistermovement.in.soldout.no");
		inject(CashRegisterMovementBusiness.class).create(saleCashRegisterMovement.getCashRegisterMovement());
		*/
		//updateSale(saleCashRegisterMovement, Crud.CREATE, logMessageBuilder);
		/*if(customer!=null){
			customer.setBalance(customer.getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
		
			if(Boolean.TRUE.equals(deposit)){
				customer.setPaid(customer.getPaid().add(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
				customer.setPaymentCount(customer.getPaymentCount().add(BigDecimal.ONE));
			}else{
				//TODO something to be done??? I do not know
			}
			
			customerDao.update(customer);
			sale.getCustomer().setTurnover(customer.getTurnover());// Because of live object in test case
			sale.getCustomer().setPaid(customer.getPaid());
			sale.getCustomer().setBalance(customer.getBalance());
			sale.getCustomer().setPaymentCount(customer.getPaymentCount());
			saleCashRegisterMovement.getBalance().setCumul(customer.getBalance());
		}
		*/
		/*saleCashRegisterMovement = */super.create(saleCashRegisterMovement);
		//if(saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().isSynchonizationEnabled()){
			//inject(SalableProductCollectionItemSaleCashRegisterMovementBusiness.class).create(saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().getCollection());
		//}
		
		/*
		logIdentifiable("Created",saleCashRegisterMovement);
		*/
		/*if(Boolean.TRUE.equals(generatePos)){
			SaleCashRegisterMovementReport saleCashRegisterMovementReport = inject(CompanyReportProducer.class).produceSaleCashRegisterMovementReport(saleCashRegisterMovement);
			//if(saleCashRegisterMovement.getReport()==null)
			//	saleCashRegisterMovement.setReport(new File());
			rootBusinessLayer.getReportBusiness().buildBinaryContent(saleCashRegisterMovement, saleCashRegisterMovementReport
					,saleCashRegisterMovement.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getSaleCashRegisterMovementReportTemplate().getTemplate(), Boolean.TRUE); 
		}*/
		logTrace(logMessageBuilder);
		return saleCashRegisterMovement;
	}
	
	@Override
	protected void beforeCrud(SaleCashRegisterMovement saleCashRegisterMovement, Crud crud) {
		super.beforeCrud(saleCashRegisterMovement, crud);
		//if(saleCashRegisterMovement.getAmount().signum()==-1)
			throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("amount").setDomainNameIdentifier("sale")
				.setValue1(saleCashRegisterMovement.getAmount()).setValue2(BigDecimal.ZERO).setGreater(Boolean.FALSE).setEqual(Boolean.TRUE));
		//exceptionUtils().comparison(saleCashRegisterMovement.getAmount().signum()==-1, "amount", ArithmeticOperator.GT, BigDecimal.ZERO);
	}
		
	@Override
	protected void afterCrud(SaleCashRegisterMovement saleCashRegisterMovement, Crud crud) {
		super.afterCrud(saleCashRegisterMovement, crud);
		if(Crud.isCreateOrUpdate(crud)){
			BigDecimal paid  = sumAmount(inject(SaleCashRegisterMovementDao.class).readWhereExistencePeriodFromDateIsLessThan(saleCashRegisterMovement));//TODO should be a request
			//saleCashRegisterMovement.getBalance().setValue(saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue().subtract(paid).subtract(saleCashRegisterMovement.getAmount()));
			//System.out.println("SaleCashRegisterMovementBusinessImpl.afterCrud() "+crud+" "+saleCashRegisterMovement.getCode()+" "+paid+" "+saleCashRegisterMovement.getAmount()+" "+saleCashRegisterMovement.getBalance().getValue());
			dao.update(saleCashRegisterMovement);
			
			//we need to update those coming after
			for(SaleCashRegisterMovement next : dao.readWhereExistencePeriodFromDateIsGreaterThan(saleCashRegisterMovement)){
				update(next);
			}
		}
		
		//TODO use listener
		if(!Crud.READ.equals(crud)){
			//inject(SaleBusiness.class).computeBalance(saleCashRegisterMovement.getSale());
			inject(SaleDao.class).update(saleCashRegisterMovement.getSale());
		}
	}
		
	@Override
	protected void afterCreate(SaleCashRegisterMovement saleCashRegisterMovement) {
		super.afterCreate(saleCashRegisterMovement);
		//updateSale(saleCashRegisterMovement, Crud.CREATE, createLogMessageBuilder(CommonBusinessAction.CREATE));
	}
	
	/*private void updateSale(SaleCashRegisterMovement saleCashRegisterMovement,Crud crud,LogMessage.Builder logMessageBuilder){
		logMessageBuilder.addParameters("saleCashRegisterMovement.cashRegisterMovement.movement.value",saleCashRegisterMovement.getAmount());
		Sale sale = saleCashRegisterMovement.getSale();
		BigDecimal oldBalance=sale.getBalance().getValue(),increment=null;
		logMessageBuilder.addParameters("sale.balance.value",oldBalance);
		if(Crud.CREATE.equals(crud)){
			//When cash register increase or decrease then sale cash register respectively decrease or increase
			increment=saleCashRegisterMovement.getAmount().negate();
		}else if(Crud.UPDATE.equals(crud)) {
			BigDecimal oldCashRegisterValue = commonUtils.getValueIfNotNullElseDefault(saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCashRegister().getMovementCollection().getValue(),BigDecimal.ZERO);
			inject(CashRegisterMovementBusiness.class).update(saleCashRegisterMovement.getCollection().getCashRegisterMovement());
			BigDecimal newCashRegisterValue = commonUtils.getValueIfNotNullElseDefault(saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCashRegister().getMovementCollection().getValue(),BigDecimal.ZERO);
			increment = oldCashRegisterValue.subtract(newCashRegisterValue);
		}else if(Crud.DELETE.equals(crud)) {
			increment=saleCashRegisterMovement.getAmount();
			
		}else
			return;
		
		logMessageBuilder.addParameters("increment",increment,"sale.balance.newValue",sale.getBalance().getValue());
		
		inject(SaleBusiness.class).computeBalance(sale);
		inject(SaleDao.class).update(sale);

	}*/
	
	@Override
	public BigDecimal sumAmount(Collection<SaleCashRegisterMovement> saleCashRegisterMovements) {
		BigDecimal sum = BigDecimal.ZERO;
		if(saleCashRegisterMovements!=null)
			for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovements)
				sum = sum.add(saleCashRegisterMovement.getAmount());
		return sum;
	}
		
	@Override
	protected void afterUpdate(SaleCashRegisterMovement saleCashRegisterMovement) {
		super.afterUpdate(saleCashRegisterMovement);
		//updateSale(saleCashRegisterMovement, Crud.UPDATE, createLogMessageBuilder(CommonBusinessAction.UPDATE));
	}
	
	@Override
	protected void beforeDelete(SaleCashRegisterMovement saleCashRegisterMovement) {
		super.beforeDelete(saleCashRegisterMovement);
		LogMessage.Builder logMessageBuilder = createLogMessageBuilder(CommonBusinessAction.DELETE);
		if(saleCashRegisterMovement.getSale().getCustomer()!=null){
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAYMENT_COUNT, BigDecimal.ONE.negate());
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAID, saleCashRegisterMovement.getAmount().negate());
		}
		//updateSale(saleCashRegisterMovement, Crud.DELETE, logMessageBuilder);
		//saleCashRegisterMovement.setSale(null);
		
		logTrace(logMessageBuilder);
	}
			
	@Override
	protected void deleteFileIdentifiableGlobalIdentifier(SaleCashRegisterMovement saleCashRegisterMovement) {
		
	}
		
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeBalance(SaleCashRegisterMovement saleCashRegisterMovement,SaleCashRegisterMovement previous) {
		BigDecimal saleCashRegisterMovementAmount = saleCashRegisterMovement.getAmount();
		BigDecimal balance = null;
		if(isIdentified(saleCashRegisterMovement)){
			if(previous==null){
				//balance = saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue();
			}else{
				balance = previous.getBalance().getValue();
			}
		}else{
			//balance = saleCashRegisterMovement.getSale().getIdentifier()==null
			//	?saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue()
			//	:saleCashRegisterMovement.getSale().getBalance().getValue();
			
		}
		
		
		MovementAction action = saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().getAction();
		if(action==null || action.equals(saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCashRegister().getMovementCollection().getType()
				.getIncrementAction())){
			balance = balance.subtract(saleCashRegisterMovementAmount);
		}else{
			if(balance.signum()==1)
				balance =  balance.subtract(saleCashRegisterMovementAmount);
			else
				balance =  balance.add(saleCashRegisterMovementAmount.abs());
		}	
		saleCashRegisterMovement.getBalance().setValue(balance);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeBalance(SaleCashRegisterMovement saleCashRegisterMovement) {
		computeBalance(saleCashRegisterMovement,null);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement, MovementAction movementAction,BigDecimal increment) {
		//Increment value is added to the cash register but subtracted to sale balance
		/*if(movementAction!=null && saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction().equals(movementAction) )
			increment = increment.negate();
		else if(movementAction!=null && saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction().equals(movementAction))
			;
		*/
		return null;//inject(MovementActionBusiness.class).computeValue(movementAction, saleCashRegisterMovement.getSale().getBalance().getValue(), increment.negate());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleCashRegisterMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
	
	

	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<SaleCashRegisterMovement>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<SaleCashRegisterMovement> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					
				}
			}
		}
		
	}

	
}
