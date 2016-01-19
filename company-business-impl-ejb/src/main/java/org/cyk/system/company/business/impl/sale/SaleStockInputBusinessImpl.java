package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.CompanyReportProducer.InvoiceParameters;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleStockInputBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleStockInputDao;
import org.cyk.system.company.persistence.api.sale.SaleStockOutputDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.business.api.file.report.ReportBusiness;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.event.EventParticipation;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.time.Period;
import org.joda.time.DateTime;

@Stateless
public class SaleStockInputBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockInput, SaleStockInputDao,SaleStockInputSearchCriteria> implements SaleStockInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private SaleBusiness saleBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private StockTangibleProductMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private EventBusiness eventBusiness;
	@Inject private ReportBusiness reportBusiness;
	
	@Inject private SaleStockOutputDao saleStockOutputDao;
	@Inject private CustomerDao customerDao;
	
	private CompanyReportProducer reportProducer = CompanyBusinessLayer.getInstance().getSaleReportProducer();
	
	@Inject
	public SaleStockInputBusinessImpl(SaleStockInputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockInput newInstance(Person person) {
		logDebug("Instanciate sale stock input");
		SaleStockInput saleStockInput = new SaleStockInput();
		saleStockInput.setSale(saleBusiness.newInstance(person));
		saleStockInput.setTangibleProductStockMovement(new StockTangibleProductMovement());
		//saleStockInput.getTangibleProductStockMovement().setTangibleProduct(tangibleProductBusiness.find(TangibleProduct.SALE_STOCK));
		//saleBusiness.selectProduct(saleStockInput.getSale(), intangibleProductBusiness.find(IntangibleProduct.SALE_STOCK));
		logDebug("Sale stock input instanciated");
		return saleStockInput;
	}

	@Override
	public void create(SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Create sale stock input");
		//saleBusiness.create(saleStockInput.getSale(), saleCashRegisterMovement,Boolean.FALSE);
		InvoiceParameters previous = new InvoiceParameters(saleStockInput, saleCashRegisterMovement);
		
		//saleStockInput.getTangibleProductStockMovement().setDate(saleStockInput.getSale().getDate());
		//saleStockInput.setRemainingNumberOfGoods(saleStockInput.getTangibleProductStockMovement().getQuantity());
		tangibleProductStockMovementBusiness.create(saleStockInput.getTangibleProductStockMovement());
		
		Date start = new DateTime(saleStockInput.getSale().getDate()).plusDays(7).withTimeAtStartOfDay().toDate();
		Event event = new Event(null, RootBusinessLayer.getInstance().getReminderEventType(), "REMINDER", "REMIND GOODS", 
				new Period(start, new DateTime(start).plusHours(23).toDate()));
		
		event.setOwner(cashierBusiness.findByCashRegister(saleCashRegisterMovement.getCashRegisterMovement().getCashRegister()).getPerson());
		event.getEventParticipations().add(new EventParticipation(saleStockInput.getSale().getCustomer().getPerson()));
		eventBusiness.create(event);
		saleStockInput.setEvent(event);
		
		if(saleStockInput.getSale().getCustomer()!=null){
			Customer customer = saleStockInput.getSale().getCustomer();
			customer.setSaleStockInputCount(customer.getSaleStockInputCount().add(BigDecimal.ONE));
			customerDao.update(customer);
		}
		
		SaleReport saleReport = reportProducer.produceInvoice(previous,new InvoiceParameters(saleStockInput, saleCashRegisterMovement));
		//reportBusiness.buildBinaryContent(saleStockInput.getSale(), saleReport, saleStockInput.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE);
	
		create(saleStockInput);
		
		if(saleCashRegisterMovement.getIdentifier()!=null){
			exceptionUtils().exception("sale.stock.input.cannotpaywhiledrop");
			//saleCashRegisterMovement.setReport(saleCashRegisterMovement.getSale().getReport());
			//debug(saleCashRegisterMovement.getCashRegisterMovement());
			//debug(saleCashRegisterMovement.getBalance());
			
			//saleReport = reportProducer.produce(saleStockInput.getSale(),saleStockInput, saleCashRegisterMovement,amountToPay);
			//CompanyBusinessLayer.getInstance().persistPointOfSale(saleStockInput.getSale(), saleReport); 
			//saleCashRegisterMovementDao.update(saleCashRegisterMovement);
		}
		
	}
	
	@Override
	public void complete(SaleStockInput saleStockInput, SaleCashRegisterMovement saleCashRegisterMovement) {
		//saleBusiness.complete(saleStockInput.getSale(), saleCashRegisterMovement,Boolean.FALSE);
		InvoiceParameters previous = new InvoiceParameters(saleStockInput, saleCashRegisterMovement);
		
		SaleReport saleReport = reportProducer.produceInvoice(previous,new InvoiceParameters(saleStockInput, saleCashRegisterMovement));
		//reportBusiness.buildBinaryContent(saleStockInput.getSale(), saleReport, saleStockInput.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void load(SaleStockInput saleStockInput) {
		super.load(saleStockInput);
		saleStockInput.setSaleStockOutputs(saleStockOutputDao.readBySaleStockInput(saleStockInput));
		saleBusiness.load(saleStockInput.getSale());
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockInputSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}

}
