package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.search.DefaultSearchCriteria;

@Stateless
public class SaleStockOutputBusinessImpl extends AbstractTypedBusinessService<SaleStockOutput, SaleStockOutputDao> implements SaleStockOutputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private EventBusiness eventBusiness;
	@Inject private SaleStockInputDao saleStockInputDao;
	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleStockOutputBusinessImpl(SaleStockOutputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockOutput newInstance(Person person,SaleStockInput saleStockInput) {
		SaleStockOutput saleStockOutput = new SaleStockOutput(saleStockInput,new SaleCashRegisterMovement(saleStockInput.getSale(), 
				new CashRegisterMovement(cashierBusiness.findByPerson(person).getCashRegister())),new TangibleProductStockMovement());
		saleStockOutput.getTangibleProductStockMovement().setTangibleProduct(tangibleProductBusiness.find(TangibleProduct.SALE_STOCK));
		return saleStockOutput;
	}

	@Override
	public SaleStockOutput create(SaleStockOutput saleStockOutput) {
		saleCashRegisterMovementBusiness.create(saleStockOutput.getSaleCashRegisterMovement());
		saleStockOutput.getTangibleProductStockMovement().setDate(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		tangibleProductStockMovementBusiness.create(saleStockOutput.getTangibleProductStockMovement());
		saleStockOutput.getSaleStockInput().setRemainingNumberOfGoods(
				saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().add(saleStockOutput.getTangibleProductStockMovement().getQuantity()));
		saleStockInputDao.update(saleStockOutput.getSaleStockInput());
		
		if(saleStockOutput.getSaleStockInput().getSale().getCustomer()!=null){
			Customer customer = saleStockOutput.getSaleStockInput().getSale().getCustomer();
			customer.setSaleStockOutputCount(customer.getSaleStockOutputCount().add(BigDecimal.ONE));
			customerDao.update(customer);
		}
		
		if(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().equals(BigDecimal.ZERO)){
			Event event = saleStockOutput.getSaleStockInput().getEvent();
			saleStockOutput.getSaleStockInput().setEvent(null);
			saleStockInputDao.update(saleStockOutput.getSaleStockInput());
			eventBusiness.delete(event);
		}
		
		return super.create(saleStockOutput);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleStockOutput> findByCriteria(DefaultSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(DefaultSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}

	@Override
	public Collection<SaleStockOutput> findBySaleStockInput(SaleStockInput saleStockInput) {
		return dao.readBySaleStockInput(saleStockInput);
	}
	
}
