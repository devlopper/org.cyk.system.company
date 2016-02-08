package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementInputBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Sale;
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

@Stateless
public class SaleStockTangibleProductMovementInputBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockTangibleProductMovementInput, SaleStockTangibleProductMovementInputDao,SaleStockInputSearchCriteria> implements SaleStockTangibleProductMovementInputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private IntangibleProductDao intangibleProductDao;
	@Inject private TangibleProductDao tangibleProductDao;
	@Inject private SalableProductDao salableProductDao;
	@Inject private StockableTangibleProductDao stockableTangibleProductDao;
	@Inject private SaleStockOutputDao saleStockOutputDao;
	@Inject private CustomerDao customerDao;
	
	private CompanyReportProducer reportProducer = CompanyBusinessLayer.getInstance().getCompanyReportProducer();
	
	@Inject
	public SaleStockTangibleProductMovementInputBusinessImpl(SaleStockTangibleProductMovementInputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleStockTangibleProductMovementInput instanciateOne(Sale sale) {
		SaleStockTangibleProductMovementInput saleStockInput = new SaleStockTangibleProductMovementInput();
		saleStockInput.setSale(sale);
		saleStockInput.setStockTangibleProductMovement(new StockTangibleProductMovement());
		saleStockInput.getStockTangibleProductMovement().setStockableTangibleProduct(CompanyBusinessLayer.getInstance().getStockableTangibleProductStocking());
		CompanyBusinessLayer.getInstance().getSaleBusiness().selectProduct(saleStockInput.getSale(), CompanyBusinessLayer.getInstance().getSalableProductStocking());
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
