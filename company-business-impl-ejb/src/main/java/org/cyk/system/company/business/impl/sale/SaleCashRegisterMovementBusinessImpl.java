package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.AbstractCompanyBeanAdapter;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementModeDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.computation.ArithmeticOperator;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleDao saleDao;
	@Inject private CustomerDao customerDao;
	@Inject private CashierDao cashierDao;
	@Inject private PersonDao personDao;
	
	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode, String amount) {
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
		saleCashRegisterMovement.setSale(saleDao.readByComputedIdentifier(saleComputedIdentifier));
		saleCashRegisterMovement.setAmountIn(numberBusiness.parseBigDecimal(amount));
		saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		saleCashRegisterMovement.getCashRegisterMovement().setCode(computedIdentifier);
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashierDao.readByPerson(personDao.readByCode(cashierPersonCode)).getCashRegister());
		saleCashRegisterMovement.getCashRegisterMovement().setMovement(inject(MovementBusiness.class).instanciateOne(
				saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection(),amount));
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(UserAccount userAccount,Sale sale,CashRegister cashRegister) {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne();
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovement.setCashRegisterMovement(inject(CashRegisterMovementBusiness.class).instanciateOne(userAccount, cashRegister));
		return saleCashRegisterMovement;
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(Sale sale,CashRegister cashRegister,Boolean input) {
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement(cashRegister,new Movement());
		cashRegisterMovement.setMovement(inject(MovementBusiness.class).instanciateOne(cashRegister.getMovementCollection(),input));
		cashRegisterMovement.setMode(inject(CashRegisterMovementModeDao.class).read(CashRegisterMovementMode.CASH));
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,cashRegisterMovement);
		logInstanceCreated(saleCashRegisterMovement);
		return saleCashRegisterMovement;
	}
	
	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement) {
		return create(saleCashRegisterMovement,Boolean.TRUE);
	}
	
	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement,Boolean generatePos) {
		//exceptionUtils().exception(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO), "");
		Sale sale = saleCashRegisterMovement.getSale();
		Customer customer = sale.getCustomer();
		Integer soldOut = BigDecimal.ZERO.compareTo(sale.getBalance().getValue());
		MovementAction movementAction = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction();
		Boolean deposit = movementAction.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction());
		if(deposit)
			exceptionUtils().exception(soldOut>=0, "exception.salecashregistermovement.in.soldout.yes");
		else
			exceptionUtils().exception(soldOut<0, "exception.salecashregistermovement.in.soldout.no");
		inject(CashRegisterMovementBusiness.class).create(saleCashRegisterMovement.getCashRegisterMovement());
		
		BigDecimal oldBalance = sale.getBalance().getValue(),increment=saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate();
		commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_VALUE,increment);
		exceptionUtils().comparison(!Boolean.TRUE.equals(sale.getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getBalanceCanBeNegative()) 
				&& sale.getBalance().getValue().signum() == -1
				, inject(LanguageBusiness.class).findText("field.balance"),ArithmeticOperator.GTE,BigDecimal.ZERO);
		logTrace("Old balance={} , Increment={} , New balance={}",oldBalance,increment, sale.getBalance().getValue());
		//cumul balance must be link to a date , so do not update a cumulated balance
		//sale.getBalance().setCumul(sale.getBalance().getCumul().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
		
		saleCashRegisterMovement.getBalance().setValue(sale.getBalance().getValue());
		saleDao.update(sale);
		if(customer!=null){
			/*Boolean firstSaleCashRegisterMovement = dao.countBySale(sale)==0;
			if(firstSaleCashRegisterMovement){
				customer.setBalance(customer.getBalance().add(sale.getBalance()));
			}else{*/
				customer.setBalance(customer.getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
			//}
			
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
		
		saleCashRegisterMovement = super.create(saleCashRegisterMovement);
		
		logIdentifiable("Created",saleCashRegisterMovement);
		
		/*if(Boolean.TRUE.equals(generatePos)){
			SaleCashRegisterMovementReport saleCashRegisterMovementReport = inject(CompanyReportProducer.class).produceSaleCashRegisterMovementReport(saleCashRegisterMovement);
			//if(saleCashRegisterMovement.getReport()==null)
			//	saleCashRegisterMovement.setReport(new File());
			rootBusinessLayer.getReportBusiness().buildBinaryContent(saleCashRegisterMovement, saleCashRegisterMovementReport
					,saleCashRegisterMovement.getSale().getSalableProductCollection().getAccountingPeriod().getSaleConfiguration().getSaleCashRegisterMovementReportTemplate().getTemplate(), Boolean.TRUE); 
		}*/
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
		increment = increment.abs();
		if(movementAction.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getIncrementAction()))
			return saleCashRegisterMovement.getSale().getBalance().getValue().subtract(increment);
		else if(movementAction.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction()))
			return saleCashRegisterMovement.getSale().getBalance().getValue().add(increment);
		return null;
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
	
	@Override
	public SaleCashRegisterMovement delete(SaleCashRegisterMovement saleCashRegisterMovement) {
		if(saleCashRegisterMovement.getSale().getCustomer()!=null){
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAYMENT_COUNT, BigDecimal.ONE.negate());
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovement.getSale().getCustomer(), Customer.FIELD_PAID
					, saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate());
		}
		inject(CashRegisterMovementBusiness.class).delete(saleCashRegisterMovement.getCashRegisterMovement());
		saleCashRegisterMovement.setCashRegisterMovement(null);
		return super.delete(saleCashRegisterMovement);
	}
	
	/**/
	
	public static interface Listener{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		
		public static class Adapter extends AbstractCompanyBeanAdapter implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
		
			
			public static class Default extends Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				
			}
			
		}
		
	}

	
}
