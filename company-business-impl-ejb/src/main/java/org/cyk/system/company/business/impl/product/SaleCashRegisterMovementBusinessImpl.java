package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.SaleReportProducer.ReceiptParameters;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class SaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SaleCashRegisterMovement, SaleCashRegisterMovementDao> implements SaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SaleDao saleDao;
	@Inject private CustomerDao customerDao;
	@Inject private CashRegisterMovementBusiness cashRegisterMovementBusiness;
	@Inject private CashierBusiness cashierBusiness;

	@Inject
	public SaleCashRegisterMovementBusinessImpl(SaleCashRegisterMovementDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleCashRegisterMovement newInstance(Sale sale,Person person) {
		Cashier cashier = cashierBusiness.findByPerson(person);
		return new SaleCashRegisterMovement(sale,new CashRegisterMovement(cashier.getCashRegister()));
	}
	
	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement) {
		return create(saleCashRegisterMovement,Boolean.TRUE);
	}
	
	@Override
	public SaleCashRegisterMovement create(SaleCashRegisterMovement saleCashRegisterMovement,Boolean generatePos) {
		logDebug("Create sale cash register movement");
		Sale sale = saleCashRegisterMovement.getSale();
		Customer customer = sale.getCustomer();
		Integer soldOut = BigDecimal.ZERO.compareTo(sale.getBalance().getValue());
		Boolean deposit = saleCashRegisterMovement.getCashRegisterMovement().getAmount().signum()>=0;
		if(Boolean.TRUE.equals(deposit)){
			exceptionUtils().exception(soldOut>=0, "validtion.sale.soldout.yes");
			cashRegisterMovementBusiness.deposit(saleCashRegisterMovement.getCashRegisterMovement());
		}else{
			exceptionUtils().exception(soldOut<0, "validtion.sale.soldout.no");
			cashRegisterMovementBusiness.withdraw(saleCashRegisterMovement.getCashRegisterMovement());
		}
		ReceiptParameters previous = new ReceiptParameters(null,saleCashRegisterMovement);
		sale.getBalance().setValue(sale.getBalance().getValue().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
		
		//cumul balance must be link to a date , so do not update a cumulated balance
		//sale.getBalance().setCumul(sale.getBalance().getCumul().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
		
		saleCashRegisterMovement.getBalance().setValue(sale.getBalance().getValue());
		saleDao.update(sale);
		
		if(customer!=null){
			/*Boolean firstSaleCashRegisterMovement = dao.countBySale(sale)==0;
			if(firstSaleCashRegisterMovement){
				customer.setBalance(customer.getBalance().add(sale.getBalance()));
			}else{*/
				customer.setBalance(customer.getBalance().subtract(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
			//}
			
			if(Boolean.TRUE.equals(deposit)){
				customer.setPaid(customer.getPaid().add(saleCashRegisterMovement.getCashRegisterMovement().getAmount()));
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
		
		logDebug("Sale cash register movement created successfully. I={} O={} A={} CT={} CP={} CB={}",saleCashRegisterMovement.getAmountIn(),
				saleCashRegisterMovement.getAmountOut(),saleCashRegisterMovement.getCashRegisterMovement().getAmount(),customer==null?"":customer.getTurnover()
						,customer==null?"":customer.getPaid(),customer==null?"":customer.getPaid());
		
		if(Boolean.TRUE.equals(generatePos)){
			SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().producePaymentReceipt(previous,new ReceiptParameters(null,saleCashRegisterMovement));
			CompanyBusinessLayer.getInstance().persistPointOfSale(saleCashRegisterMovement, saleReport); 
		}
		return saleCashRegisterMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal computeBalance(SaleCashRegisterMovement movement) {
		BigDecimal balance = movement.getSale().getIdentifier()==null/*movement.getSale().getBalance()==null*/?movement.getSale().getCost():movement.getSale().getBalance().getValue();
		if(movement.getCashRegisterMovement().getAmount().signum()==-1)//withdraw
			if(balance.signum()==1)
				return balance.subtract(movement.getCashRegisterMovement().getAmount());
			else
				return balance.add(movement.getCashRegisterMovement().getAmount().abs());
		return balance.subtract(movement.getCashRegisterMovement().getAmount());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleCashRegisterMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void in(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void out(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCashRegisterMovement().setAmount(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Collection<SaleCashRegisterMovement> saleCashRegisterMovements) {
		return RootBusinessLayer.getInstance().createReport(CompanyBusinessLayer.getInstance().getPointOfSalePaymentReportName(),
				saleCashRegisterMovements.iterator().next().getReport(), null, null,null);//TODO many receipt print must be handled
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		return findReport(Arrays.asList(saleCashRegisterMovement));
	}

}
