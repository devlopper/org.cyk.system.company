package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemSaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementModeDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.mathematics.MovementActionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.CommonBusinessAction;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.LogMessage;
import org.cyk.utility.common.computation.ArithmeticOperator;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CashierDao cashierDao;
	@Inject private PersonDao personDao;
	
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
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE}, name))
			return new Object[]{saleCashRegisterMovement.getCashRegisterMovement()};
		return super.getPropertyValueTokens(saleCashRegisterMovement, name);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode, String amount) {
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
		//saleCashRegisterMovement.setSale(saleDao.readByComputedIdentifier(saleComputedIdentifier));
		saleCashRegisterMovement.setAmountIn(numberBusiness.parseBigDecimal(amount));
		saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		saleCashRegisterMovement.getCashRegisterMovement().setCode(computedIdentifier);
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashierDao.readByPerson(personDao.read(cashierPersonCode)).getCashRegister());
		/*saleCashRegisterMovement.getCashRegisterMovement().setMovement(inject(MovementBusiness.class).instanciateOne(
				saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection(),amount));
		*/
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(UserAccount userAccount,Sale sale,CashRegister cashRegister) {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne();
		setSale(saleCashRegisterMovement, sale);
		saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		setCashRegister(userAccount, saleCashRegisterMovement, cashRegister);
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
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void setCashRegister(UserAccount userAccount,SaleCashRegisterMovement saleCashRegisterMovement,CashRegister cashRegister) {
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashRegister);
		saleCashRegisterMovement.getCashRegisterMovement().setMovement(cashRegister == null ? null : inject(MovementBusiness.class).instanciateOne(cashRegister.getMovementCollection()));
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS) @Deprecated
	public SaleCashRegisterMovement instanciateOne(Sale sale,CashRegister cashRegister,Boolean input) {
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement(cashRegister,new Movement());
		//cashRegisterMovement.setMovement(inject(MovementBusiness.class).instanciateOne(cashRegister.getMovementCollection(),input));
		cashRegisterMovement.setMode(inject(CashRegisterMovementModeDao.class).read(CompanyConstant.Code.CashRegisterMovementMode.CASH));
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,cashRegisterMovement);
		logInstanceCreated(saleCashRegisterMovement);
		return saleCashRegisterMovement;
	}
	
	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement) {
		LogMessage.Builder logMessageBuilder = createLogMessageBuilder(CommonBusinessAction.CREATE);
		if(isNotIdentified(saleCashRegisterMovement.getSale())){
			inject(SaleBusiness.class).create(saleCashRegisterMovement.getSale());
		}
		if(isNotIdentified(saleCashRegisterMovement.getCashRegisterMovement())){
			inject(CashRegisterMovementBusiness.class).create(saleCashRegisterMovement.getCashRegisterMovement());
		}
		
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
		updateSale(saleCashRegisterMovement, Crud.CREATE, logMessageBuilder);
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
			inject(SalableProductCollectionItemSaleCashRegisterMovementBusiness.class).create(saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().getCollection());
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
	
	private void updateSale(SaleCashRegisterMovement saleCashRegisterMovement,Crud crud,LogMessage.Builder logMessageBuilder){
		logMessageBuilder.addParameters("saleCashRegisterMovement.cashRegisterMovement.movement.value",saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue());
		Sale sale = saleCashRegisterMovement.getSale();
		BigDecimal oldBalance=sale.getBalance().getValue(),increment=null,newBalance=null;
		logMessageBuilder.addParameters("sale.balance.value",oldBalance);
		if(Crud.CREATE.equals(crud)){
			//When cash register increase or decrease then sale cash register respectively decrease or increase
			increment=saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate();
		}else if(Crud.UPDATE.equals(crud)) {
			BigDecimal oldCashRegisterValue = saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getValue();
			inject(CashRegisterMovementBusiness.class).update(saleCashRegisterMovement.getCashRegisterMovement());
			BigDecimal newCashRegisterValue = saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getValue();
			increment = oldCashRegisterValue.subtract(newCashRegisterValue);
		}else if(Crud.DELETE.equals(crud)) {
			increment=saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue();
			
		}else
			return;
		newBalance = sale.getBalance().getValue().add(increment);
		exceptionUtils().comparison(!Boolean.TRUE.equals(sale.getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getBalanceCanBeNegative()) 
				&& newBalance.signum() == -1, inject(LanguageBusiness.class).findText("field.balance"),ArithmeticOperator.GTE,BigDecimal.ZERO);
		sale.getBalance().setValue(newBalance);
		if(Crud.isCreateOrUpdate(crud)){
			saleCashRegisterMovement.getBalance().setValue(sale.getBalance().getValue());
		}
		logMessageBuilder.addParameters("increment",increment,"sale.balance.newValue",sale.getBalance().getValue());
		inject(SaleDao.class).update(sale);
	}
	
	@Override
	public SaleCashRegisterMovement update(SaleCashRegisterMovement saleCashRegisterMovement) {
		LogMessage.Builder logMessageBuilder = createLogMessageBuilder(CommonBusinessAction.UPDATE);
		updateSale(saleCashRegisterMovement, Crud.UPDATE, logMessageBuilder);
		logTrace(logMessageBuilder);
		return super.update(saleCashRegisterMovement);
	}
	
	@Override
	public SaleCashRegisterMovement delete(SaleCashRegisterMovement saleCashRegisterMovement) {
		LogMessage.Builder logMessageBuilder = createLogMessageBuilder(CommonBusinessAction.DELETE);
		if(saleCashRegisterMovement.getSale().getCustomer()!=null){
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAYMENT_COUNT, BigDecimal.ONE.negate());
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAID
					, saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate());
		}
		updateSale(saleCashRegisterMovement, Crud.DELETE, logMessageBuilder);
		if(isIdentified(saleCashRegisterMovement.getCashRegisterMovement()))
			inject(CashRegisterMovementBusiness.class).delete(saleCashRegisterMovement.getCashRegisterMovement());
		
		saleCashRegisterMovement.setCashRegisterMovement(null);
		saleCashRegisterMovement.setSale(null);
		saleCashRegisterMovement = super.delete(saleCashRegisterMovement);
		logTrace(logMessageBuilder);
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement) {
		BigDecimal saleCashRegisterMovementAmount = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue();
		MovementAction action = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction();
		BigDecimal balance = saleCashRegisterMovement.getSale().getIdentifier()==null?saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue()
				:saleCashRegisterMovement.getSale().getBalance().getValue();
		if(action.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction()))//withdraw
			if(balance.signum()==1)
				return balance.subtract(saleCashRegisterMovementAmount);
			else
				return balance.add(saleCashRegisterMovementAmount.abs());
		return balance.subtract(saleCashRegisterMovementAmount);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement, MovementAction movementAction,BigDecimal increment) {
		//Increment value is added to the cash register but subtracted to sale balance
		/*if(movementAction!=null && saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction().equals(movementAction) )
			increment = increment.negate();
		else if(movementAction!=null && saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction().equals(movementAction))
			;
		*/
		return inject(MovementActionBusiness.class).computeValue(movementAction, saleCashRegisterMovement.getSale().getBalance().getValue(), increment.negate());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleCashRegisterMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void in(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void out(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		return null;//rootBusinessLayer.getReportBusiness().buildBinaryContent(saleCashRegisterMovement.getReport(),
				//CompanyBusinessLayer.getInstance().getPointOfSalePaymentReportName()+Constant.CHARACTER_UNDESCORE+StringUtils.defaultString(saleCashRegisterMovement.getCashRegisterMovement().getCode(), saleCashRegisterMovement.getIdentifier().toString()));
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
