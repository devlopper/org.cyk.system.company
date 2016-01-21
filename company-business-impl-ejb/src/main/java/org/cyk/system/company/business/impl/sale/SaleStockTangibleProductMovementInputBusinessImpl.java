package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.CompanyReportProducer.InvoiceParameters;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementInputBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.product.IntangibleProductDao;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleStockOutputDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementInputDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.business.api.file.report.ReportBusiness;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.event.EventParticipation;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.time.Period;
import org.joda.time.DateTime;

@Stateless
public class SaleStockTangibleProductMovementInputBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockTangibleProductMovementInput, SaleStockTangibleProductMovementInputDao,SaleStockInputSearchCriteria> implements SaleStockTangibleProductMovementInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private IntangibleProductDao intangibleProductDao;
	@Inject private TangibleProductDao tangibleProductDao;
	@Inject private SalableProductDao salableProductDao;
	@Inject private StockableTangibleProductDao stockableTangibleProductDao;
	@Inject private SaleStockOutputDao saleStockOutputDao;
	@Inject private CustomerDao customerDao;
	
	private CompanyReportProducer reportProducer = CompanyBusinessLayer.getInstance().getSaleReportProducer();
	
	@Inject
	public SaleStockTangibleProductMovementInputBusinessImpl(SaleStockTangibleProductMovementInputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockTangibleProductMovementInput instanciate(Person person) {
		SaleStockTangibleProductMovementInput saleStockInput = new SaleStockTangibleProductMovementInput();
		saleStockInput.setSale(CompanyBusinessLayer.getInstance().getSaleBusiness().instanciate(person));
		saleStockInput.setStockTangibleProductMovement(new StockTangibleProductMovement());
		saleStockInput.getStockTangibleProductMovement().setStockableTangibleProduct(stockableTangibleProductDao.readByTangibleProduct(tangibleProductDao
				.read(TangibleProduct.STOCKING)));
		CompanyBusinessLayer.getInstance().getSaleBusiness().selectProduct(saleStockInput.getSale(), salableProductDao.readByProduct( intangibleProductDao.read(IntangibleProduct.STOCKING)));
		logInstanceCreated(saleStockInput);
		return saleStockInput;
	}

	@Override
	public SaleStockTangibleProductMovementInput create(SaleStockTangibleProductMovementInput saleStockInput/*,SaleCashRegisterMovement saleCashRegisterMovement*/) {
		CompanyBusinessLayer.getInstance().getSaleBusiness().create(saleStockInput.getSale()/*, saleCashRegisterMovement,Boolean.FALSE*/);
		//InvoiceParameters previous = new InvoiceParameters(saleStockInput, saleCashRegisterMovement);
		
		//saleStockInput.getStockTangibleProductStockMovement().setDate(saleStockInput.getSale().getDate());
		//saleStockInput.setRemainingNumberOfGoods(saleStockInput.getStockTangibleProductStockMovement().getQuantity());
		CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(saleStockInput.getStockTangibleProductMovement());
		
		/*saleStockInput.setEvent(event);
		
		if(saleStockInput.getSale().getCustomer()!=null){
			Customer customer = saleStockInput.getSale().getCustomer();
			customer.setSaleStockInputCount(customer.getSaleStockInputCount().add(BigDecimal.ONE));
			customerDao.update(customer);
		}
		*/
		//SaleReport saleReport = reportProducer.produceInvoice(previous,new InvoiceParameters(saleStockInput, saleCashRegisterMovement));
		//reportBusiness.buildBinaryContent(saleStockInput.getSale(), saleReport, saleStockInput.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE);
	
		saleStockInput = super.create(saleStockInput);
		/*
		if(saleCashRegisterMovement.getIdentifier()!=null){
			exceptionUtils().exception("sale.stock.input.cannotpaywhiledrop");
			//saleCashRegisterMovement.setReport(saleCashRegisterMovement.getSale().getReport());
			//debug(saleCashRegisterMovement.getCashRegisterMovement());
			//debug(saleCashRegisterMovement.getBalance());
			
			//saleReport = reportProducer.produce(saleStockInput.getSale(),saleStockInput, saleCashRegisterMovement,amountToPay);
			//CompanyBusinessLayer.getInstance().persistPointOfSale(saleStockInput.getSale(), saleReport); 
			//saleCashRegisterMovementDao.update(saleCashRegisterMovement);
		}
		*/
		logIdentifiable("Created", saleStockInput);
		return saleStockInput;
	}
	/*
	@Override
	public void complete(SaleStockTangibleProductMovementInput saleStockInput, SaleCashRegisterMovement saleCashRegisterMovement) {
		//saleBusiness.complete(saleStockInput.getSale(), saleCashRegisterMovement,Boolean.FALSE);
		InvoiceParameters previous = new InvoiceParameters(saleStockInput, saleCashRegisterMovement);
		
		SaleReport saleReport = reportProducer.produceInvoice(previous,new InvoiceParameters(saleStockInput, saleCashRegisterMovement));
		//reportBusiness.buildBinaryContent(saleStockInput.getSale(), saleReport, saleStockInput.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE);
	}*/

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void load(SaleStockTangibleProductMovementInput saleStockInput) {
		super.load(saleStockInput);
		saleStockInput.setSaleStockOutputs(saleStockOutputDao.readBySaleStockInput(saleStockInput));
		//saleBusiness.load(saleStockInput.getSale());
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockInputSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}

}
