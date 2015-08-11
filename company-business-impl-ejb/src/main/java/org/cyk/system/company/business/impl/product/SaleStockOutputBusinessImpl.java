package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.SaleReportProducer.ReceiptParameters;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class SaleStockOutputBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockOutput, SaleStockOutputDao,SaleStockOutputSearchCriteria> implements SaleStockOutputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private EventBusiness eventBusiness;
	@Inject private SaleStockInputDao saleStockInputDao;
	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleStockOutputBusinessImpl(SaleStockOutputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockOutput newInstance(Person person,SaleStockInput saleStockInput) {
		SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person);
		SaleStockOutput saleStockOutput = new SaleStockOutput(saleStockInput,saleCashRegisterMovement,new TangibleProductStockMovement());
		saleStockOutput.getTangibleProductStockMovement().setTangibleProduct(tangibleProductBusiness.find(TangibleProduct.SALE_STOCK));
		return saleStockOutput;
	}

	@Override
	public SaleStockOutput create(SaleStockOutput saleStockOutput) {
		BigDecimal outputQuantity = saleStockOutput.getTangibleProductStockMovement().getQuantity();
		exceptionUtils().exception(outputQuantity.signum()>0, "salestockoutput.quantitymustbenegative");
		SaleStockInput saleStockInput = saleStockInputDao.read(saleStockOutput.getSaleStockInput().getIdentifier());
		exceptionUtils().exception(outputQuantity.abs().compareTo(saleStockInput.getRemainingNumberOfGoods())>0, "salestockoutput.quantitymustbelessthanorequalsinstock");
		ReceiptParameters previous = new ReceiptParameters(saleStockOutput);
		saleCashRegisterMovementBusiness.create(saleStockOutput.getSaleCashRegisterMovement(),Boolean.FALSE);
		saleStockOutput.getTangibleProductStockMovement().setDate(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		tangibleProductStockMovementBusiness.create(saleStockOutput.getTangibleProductStockMovement());
		saleStockOutput.getSaleStockInput().setRemainingNumberOfGoods(
				saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().add(saleStockOutput.getTangibleProductStockMovement().getQuantity()));
		saleStockOutput.setRemainingNumberOfGoods(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods());
		saleStockInputDao.update(saleStockOutput.getSaleStockInput());
		
		if(saleStockOutput.getSaleStockInput().getSale().getCustomer()!=null){
			Customer customer = saleStockOutput.getSaleStockInput().getSale().getCustomer();
			customer.setSaleStockOutputCount(customer.getSaleStockOutputCount().add(BigDecimal.ONE));
			customerDao.update(customer);
		}
		
		if(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().equals(BigDecimal.ZERO) && saleStockOutput.getSaleStockInput().getEvent()!=null){
			Event event = saleStockOutput.getSaleStockInput().getEvent();
			saleStockOutput.getSaleStockInput().setEvent(null);
			saleStockInputDao.update(saleStockOutput.getSaleStockInput());
			eventBusiness.delete(event);
		}
		
		saleStockOutput = super.create(saleStockOutput);
		//debug(previous);
		//debug(new ReceiptParameters(saleStockOutput));
		SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().producePaymentReceipt(previous,new ReceiptParameters(saleStockOutput));
		CompanyBusinessLayer.getInstance().persistPointOfSale(saleStockOutput.getSaleCashRegisterMovement(), saleReport); 
		
		return saleStockOutput;
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleStockOutput> findBySaleStockInput(SaleStockInput saleStockInput) {
		return dao.readBySaleStockInput(saleStockInput);
	}
	
}
