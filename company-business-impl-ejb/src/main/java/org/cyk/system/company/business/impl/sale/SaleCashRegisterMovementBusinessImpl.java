package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyReportProducer.ReceiptParameters;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.Constant;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	private RootBusinessLayer rootBusinessLayer = RootBusinessLayer.getInstance();
	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
	@Inject private SaleDao saleDao;
	@Inject private CustomerDao customerDao;
	@Inject private CashierDao cashierDao;
	@Inject private PersonDao personDao;
	
	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciate(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode, String amount) {
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
		saleCashRegisterMovement.setSale(saleDao.readByComputedIdentifier(saleComputedIdentifier));
		saleCashRegisterMovement.setAmountIn(numberBusiness.parseBigDecimal(amount));
		saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		saleCashRegisterMovement.getCashRegisterMovement().setComputedIdentifier(computedIdentifier);
		saleCashRegisterMovement.getCashRegisterMovement().setCashRegister(cashierDao.readByPerson(personDao.readByCode(cashierPersonCode)).getCashRegister());
		saleCashRegisterMovement.getCashRegisterMovement().setMovement(RootBusinessLayer.getInstance().getMovementBusiness().instanciate(
				saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection(),amount));
		return saleCashRegisterMovement;
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciate(Sale sale,Person person,Boolean input) {
		Cashier cashier = cashierDao.readByPerson(person);
		CashRegisterMovement cashRegisterMovement = new CashRegisterMovement(cashier.getCashRegister(),new Movement());
		cashRegisterMovement.setMovement(RootBusinessLayer.getInstance().getMovementBusiness().instanciate(cashier.getCashRegister().getMovementCollection(),input));
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
		logIdentifiable("Creating",saleCashRegisterMovement);
		Sale sale = saleCashRegisterMovement.getSale();
		Customer customer = sale.getCustomer();
		Integer soldOut = BigDecimal.ZERO.compareTo(sale.getBalance().getValue());
		MovementAction movementAction = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction();
		Boolean deposit = movementAction.equals(saleCashRegisterMovement.getSale().getCashier().getCashRegister().getMovementCollection().getIncrementAction());
		if(deposit)
			exceptionUtils().exception(soldOut>=0, "exception.salecashregistermovement.in.soldout.yes");
		else
			exceptionUtils().exception(soldOut<=0, "exception.salecashregistermovement.in.soldout.no");
		companyBusinessLayer.getCashRegisterMovementBusiness().create(saleCashRegisterMovement.getCashRegisterMovement());
		
		ReceiptParameters previous = new ReceiptParameters(null,saleCashRegisterMovement);
		BigDecimal oldBalance = sale.getBalance().getValue(),increment=saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue().negate();
		commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_VALUE,increment);
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
		
		if(Boolean.TRUE.equals(generatePos)){
			SaleReport saleReport = CompanyBusinessLayer.getInstance().getCompanyReportProducer().producePaymentReceipt(previous,new ReceiptParameters(null,saleCashRegisterMovement));
			if(saleCashRegisterMovement.getReport()==null)
				saleCashRegisterMovement.setReport(new File());
			rootBusinessLayer.getReportBusiness().buildBinaryContent(saleCashRegisterMovement, saleReport
					,saleCashRegisterMovement.getSale().getAccountingPeriod().getSaleConfiguration().getPointOfSaleReportTemplate().getTemplate(), Boolean.TRUE); 
		}
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement saleCashRegisterMovement) {
		BigDecimal saleCashRegisterMovementAmount = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue();
		MovementAction action = saleCashRegisterMovement.getCashRegisterMovement().getMovement().getAction();
		BigDecimal balance = saleCashRegisterMovement.getSale().getIdentifier()==null?saleCashRegisterMovement.getSale().getCost().getValue()
				:saleCashRegisterMovement.getSale().getBalance().getValue();
		if(action.equals(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getMovementCollection().getDecrementAction()))//withdraw
			if(balance.signum()==1)
				return balance.subtract(saleCashRegisterMovementAmount);
			else
				return balance.add(saleCashRegisterMovementAmount.abs());
		return balance.subtract(saleCashRegisterMovementAmount);
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
		return rootBusinessLayer.getReportBusiness().buildBinaryContent(saleCashRegisterMovement.getReport(),
				CompanyBusinessLayer.getInstance().getPointOfSalePaymentReportName()+Constant.CHARACTER_UNDESCORE+StringUtils.defaultString(saleCashRegisterMovement.getCashRegisterMovement().getComputedIdentifier(), saleCashRegisterMovement.getIdentifier().toString()));
	}

}
