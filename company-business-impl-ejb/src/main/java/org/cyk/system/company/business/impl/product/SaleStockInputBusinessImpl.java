package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.company.persistence.api.product.SaleStockOutputDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.event.EventParticipation;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.model.time.Period;
import org.joda.time.DateTime;

@Stateless
public class SaleStockInputBusinessImpl extends AbstractTypedBusinessService<SaleStockInput, SaleStockInputDao> implements SaleStockInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private SaleBusiness saleBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private EventBusiness eventBusiness;
	@Inject private SaleStockOutputDao saleStockOutputDao;
	
	@Inject
	public SaleStockInputBusinessImpl(SaleStockInputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockInput newInstance(Person person) {
		logDebug("Instanciate sale stock input");
		SaleStockInput saleStockInput = new SaleStockInput();
		saleStockInput.setSale(saleBusiness.newInstance(person));
		saleStockInput.setTangibleProductStockMovement(new TangibleProductStockMovement());
		saleStockInput.getTangibleProductStockMovement().setTangibleProduct(tangibleProductBusiness.find(TangibleProduct.SALE_STOCK));
		saleBusiness.selectProduct(saleStockInput.getSale(), intangibleProductBusiness.find(IntangibleProduct.SALE_STOCK));
		logDebug("Sale stock input instanciate");
		return saleStockInput;
	}

	@Override
	public void create(SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Create sale stock input");
		saleBusiness.create(saleStockInput.getSale(), saleCashRegisterMovement);
		saleStockInput.getTangibleProductStockMovement().setDate(saleStockInput.getSale().getDate());
		saleStockInput.setRemainingNumberOfGoods(saleStockInput.getTangibleProductStockMovement().getQuantity());
		tangibleProductStockMovementBusiness.create(saleStockInput.getTangibleProductStockMovement());
		
		Date start = new DateTime(saleStockInput.getSale().getDate()).plusDays(7).withTimeAtStartOfDay().toDate();
		Event event = new Event(null, RootBusinessLayer.getInstance().getReminderEventType(), "REMINDER", "REMIND GOODS", 
				new Period(start, new DateTime(start).plusHours(23).toDate()));
		
		event.setOwner(cashierBusiness.findByCashRegister(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister()).getEmployee().getPerson());
		event.getEventParticipations().add(new EventParticipation(saleStockInput.getSale().getCustomer().getPerson()));
		eventBusiness.create(event);
		saleStockInput.setEvent(event);
		create(saleStockInput);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleStockInput> findByCriteria(DefaultSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(DefaultSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void load(SaleStockInput saleStockInput) {
		super.load(saleStockInput);
		saleStockInput.setSaleStockOutputs(saleStockOutputDao.readBySaleStockInput(saleStockInput));
		saleBusiness.load(saleStockInput.getSale());
	}

}
