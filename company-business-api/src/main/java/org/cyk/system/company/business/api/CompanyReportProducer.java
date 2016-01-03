package org.cyk.system.company.business.api;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.RootReportProducer;

import lombok.Getter;
import lombok.Setter;

public interface CompanyReportProducer extends RootReportProducer {

	SaleReport produceInvoice(InvoiceParameters previousStateParameters,InvoiceParameters currentStateParameters);
	
	SaleReport producePaymentReceipt(ReceiptParameters previousStateParameters,ReceiptParameters currentStateParameters);
	
	/**/
	
	@Getter @Setter
	public static abstract class AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		protected SaleCashRegisterMovement saleCashRegisterMovement;
		protected BigDecimal amountToPay;
		protected BigDecimal amountPaid;
		protected BigDecimal amountToOut;
		
		public AbstractParameters(SaleCashRegisterMovement saleCashRegisterMovement) {
			super();
			this.saleCashRegisterMovement = saleCashRegisterMovement;
			Sale sale = saleCashRegisterMovement.getSale();
			amountToPay = Boolean.TRUE.equals(sale.getDone())?saleCashRegisterMovement.getSale().getBalance().getValue():sale.getCost();
			amountPaid = saleCashRegisterMovement.getAmountIn();
			amountToOut = amountPaid.subtract(amountToPay);
		}
		
	}
	
	@Getter @Setter
	public static class InvoiceParameters extends AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		private Sale sale;
		private SaleStockInput saleStockInput;
		
		public InvoiceParameters(Sale sale,SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement) {
			super(saleCashRegisterMovement);
			this.sale = sale;
			this.saleStockInput = saleStockInput;
		}
		
		public InvoiceParameters(SaleStockInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement) {
			this(saleCashRegisterMovement.getSale(),saleStockInput,saleCashRegisterMovement);
		}
	}
	
	@Getter @Setter
	public static class ReceiptParameters extends AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		private SaleStockOutput saleStockOutput;
		protected BigDecimal numberOfGoodsInStock;
		protected BigDecimal numberOfGoodsDelivered;
		
		public ReceiptParameters(SaleStockOutput saleStockOutput,SaleCashRegisterMovement saleCashRegisterMovement) {
			super(saleCashRegisterMovement);
			this.saleStockOutput = saleStockOutput;
			if(saleStockOutput!=null){
				numberOfGoodsInStock = saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods();
				numberOfGoodsDelivered = saleStockOutput.getTangibleProductStockMovement().getQuantity().abs();
			}
		}
		
		public ReceiptParameters(SaleStockOutput saleStockOutput) {
			this(saleStockOutput,saleStockOutput.getSaleCashRegisterMovement());
		}
	}
}
