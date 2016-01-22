package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyReportProducer.ReceiptParameters;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementOutputBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementInputDao;
import org.cyk.system.company.persistence.api.sale.SaleStockOutputDao;
import org.cyk.system.root.business.api.event.EventBusiness;
import org.cyk.system.root.business.api.file.report.ReportBusiness;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.event.Event;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class SaleStockTangibleProductMovementOutputBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockTangibleProductMovementOutput, SaleStockOutputDao,SaleStockOutputSearchCriteria> implements SaleStockTangibleProductMovementOutputBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private StockTangibleProductMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private EventBusiness eventBusiness;
	@Inject private SaleStockTangibleProductMovementInputDao saleStockInputDao;
	@Inject private CustomerDao customerDao;
	@Inject private ReportBusiness reportBusiness;
	
	@Inject
	public SaleStockTangibleProductMovementOutputBusinessImpl(SaleStockOutputDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleStockTangibleProductMovementOutput instanciate(Person person,SaleStockTangibleProductMovementInput input) {
		SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.instanciate(input.getSale(), person,Boolean.TRUE);
		SaleStockTangibleProductMovementOutput output = new SaleStockTangibleProductMovementOutput(input,saleCashRegisterMovement,new StockTangibleProductMovement());
		output.getStockTangibleProductMovement().setStockableTangibleProduct(CompanyBusinessLayer.getInstance().getStockableTangibleProductStocking());
		output.getStockTangibleProductMovement().setMovement(RootBusinessLayer.getInstance().getMovementBusiness().instanciate(
				output.getStockTangibleProductMovement().getStockableTangibleProduct().getMovementCollection(), Boolean.FALSE));
		return output;
	}

	@Override
	public SaleStockTangibleProductMovementOutput create(SaleStockTangibleProductMovementOutput saleStockOutput) {
		CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().create(saleStockOutput.getSaleCashRegisterMovement());
		
		CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(saleStockOutput.getStockTangibleProductMovement());
		
		//commonUtils.increment(BigDecimal.class, saleStockOutput, SaleStockTangibleProductMovementOutput.FIELD_REMAINING_NUMBER_OF_GOODS
		//		, saleStockOutput.getStockTangibleProductMovement().getStockableTangibleProduct().getMovementCollection().getValue());
		//saleStockOutput.setRemainingNumberOfGoods(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().add(saleStockOutput.getStockTangibleProductMovement().getQuantity()));
		/*
		BigDecimal outputQuantity = null;//saleStockOutput.getStockTangibleProductStockMovement().getQuantity();
		exceptionUtils().exception(outputQuantity.signum()>0, "salestockoutput.quantitymustbenegative");
		SaleStockTangibleProductMovementInput saleStockInput = saleStockInputDao.read(saleStockOutput.getSaleStockInput().getIdentifier());
		exceptionUtils().exception(outputQuantity.abs().compareTo(saleStockInput.getRemainingNumberOfGoods())>0, "salestockoutput.quantitymustbelessthanorequalsinstock");
		ReceiptParameters previous = new ReceiptParameters(saleStockOutput);
		saleCashRegisterMovementBusiness.create(saleStockOutput.getSaleCashRegisterMovement(),Boolean.FALSE);
		*/
		//saleStockOutput.getStockTangibleProductStockMovement().setDate(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		//tangibleProductStockMovementBusiness.create(saleStockOutput.getStockTangibleProductStockMovement());
		//saleStockOutput.getSaleStockInput().setRemainingNumberOfGoods(
		//		saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().add(saleStockOutput.getStockTangibleProductStockMovement().getQuantity()));
		
		/*
		saleStockOutput.setRemainingNumberOfGoods(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods());
		saleStockInputDao.update(saleStockOutput.getSaleStockInput());
		
		if(saleStockOutput.getSaleStockInput().getSale().getCustomer()!=null){
			Customer customer = saleStockOutput.getSaleStockInput().getSale().getCustomer();
			customer.setSaleStockOutputCount(customer.getSaleStockOutputCount().add(BigDecimal.ONE));
			customerDao.update(customer);
		}
		*/
		
		/*if(saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().equals(BigDecimal.ZERO) && saleStockOutput.getSaleStockInput().getEvent()!=null){
			Event event = saleStockOutput.getSaleStockInput().getEvent();
			saleStockOutput.getSaleStockInput().setEvent(null);
			saleStockInputDao.update(saleStockOutput.getSaleStockInput());
			eventBusiness.delete(event);
		}*/
		
		saleStockOutput = super.create(saleStockOutput);
		//debug(previous);
		//debug(new ReceiptParameters(saleStockOutput));
		//SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().producePaymentReceipt(previous,new ReceiptParameters(saleStockOutput));
		//reportBusiness.buildBinaryContent(saleStockOutput.getSaleCashRegisterMovement(), saleReport,
		//		saleStockOutput.getSaleCashRegisterMovement().getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE); 
		
		return saleStockOutput;
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleStockTangibleProductMovementOutput> findBySaleStockInput(SaleStockTangibleProductMovementInput saleStockInput) {
		return dao.readBySaleStockInput(saleStockInput);
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockOutputSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}
	
}
