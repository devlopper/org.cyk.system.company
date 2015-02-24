package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyValueGenerator;
import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.persistence.api.product.PaymentDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class PaymentBusinessImpl extends AbstractTypedBusinessService<Payment, PaymentDao> implements PaymentBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CompanyValueGenerator companyValueGenerator;
	@Inject private SaleDao saleDao;

	@Inject
	public PaymentBusinessImpl(PaymentDao dao) {
		super(dao);
	}
	
	@Override
	public void in(Payment payment) {
		payment.setAmountOut(BigDecimal.ZERO);
		updatePayment(payment);
	}
	
	@Override
	public void out(Payment payment) {
		updatePayment(payment);
	}
	
	private void updatePayment(Payment payment){
		payment.setAmountPaid(payment.getAmountIn().subtract(payment.getAmountOut()));
	}
	
	@Override
	public Payment create(Payment payment) {
		return create(payment, Boolean.FALSE);
	}
	
	@Override
	public Payment create(Payment payment, Boolean payback) {
		exceptionUtils().exception(payment.getSale()==null, "validtion.payment.sale.missing");
		exceptionUtils().exception(payment.getAmountIn()==null, "validtion.payment.amount.in.missing");
		exceptionUtils().exception(payment.getAmountOut()==null, "validtion.payment.amount.out.missing");
		exceptionUtils().exception(payment.getAmountPaid()==null, "validtion.payment.amount.paid.missing");
		exceptionUtils().exception(BigDecimal.ZERO.compareTo(payment.getAmountPaid())==0, "validtion.payment.amount.paid.iszero");
		Integer soldOut = BigDecimal.ZERO.compareTo(payment.getSale().getBalance());
		Boolean positivePaid = payment.getAmountPaid().signum()==1;
		if(!Boolean.TRUE.equals(payback)){//pay	
			//payment.setAmountPaid(payment.getAmountIn().subtract(payment.getAmountOut()));//is it the right place
			exceptionUtils().exception(soldOut>=0, "validtion.sale.soldout.yes");
			exceptionUtils().exception(!Boolean.TRUE.equals(positivePaid), "validtion.sale.pay.negative");
			//payment.getSale().setBalance(payment.getSale().getBalance().subtract(payment.getAmountPaid()));
		}else{//refund
			exceptionUtils().exception(soldOut<0, "validtion.sale.soldout.no");
			exceptionUtils().exception(Boolean.TRUE.equals(positivePaid), "validtion.sale.payback.positive");
			
		}
		
		payment.getSale().setBalance(payment.getSale().getBalance().subtract(payment.getAmountPaid()));
		payment.setDate(universalTimeCoordinated());
		payment.setIdentificationNumber(companyValueGenerator.paymentIdentificationNumber(payment));
		saleDao.update(payment.getSale());
		return super.create(payment);
	}
	
	@Override
	public BigDecimal computeBalance(Payment payment) {
		BigDecimal balance = payment.getSale().getBalance()==null?payment.getSale().getCost():payment.getSale().getBalance();
		if(payment.getAmountPaid().signum()==-1)
			return balance.add(payment.getAmountPaid());
		return balance.subtract(payment.getAmountPaid());
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Payment> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}
}
