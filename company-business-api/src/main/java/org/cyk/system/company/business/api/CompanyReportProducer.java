package org.cyk.system.company.business.api;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.file.report.RootReportProducer;

import lombok.Getter;
import lombok.Setter;

public interface CompanyReportProducer extends RootReportProducer {
	
	@Getter @Setter @Deprecated
	public static abstract class AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		protected Sale sale;
		protected SaleCashRegisterMovement saleCashRegisterMovement;
		protected BigDecimal amountToPay;
		protected BigDecimal amountPaid;
		protected BigDecimal amountToOut;
		
		public AbstractParameters(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement) {
			super();
			this.sale =  sale;
			this.saleCashRegisterMovement = saleCashRegisterMovement;
			amountToPay = sale.getBalance().getValue();
			if(saleCashRegisterMovement==null){
				
			}else{
				//amountPaid = saleCashRegisterMovement.getAmountIn();
				amountToOut = amountPaid.subtract(amountToPay);
			}
			
		}
		
	}
	
	@Deprecated
	@Getter @Setter
	public static class InvoiceParameters extends AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		//private SaleStockTangibleProductMovementInput saleStockInput;
		
		public InvoiceParameters(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement) {
			super(sale,saleCashRegisterMovement);
			//this.saleStockInput = saleStockInput;
		}
		
		public InvoiceParameters(SaleCashRegisterMovement saleCashRegisterMovement) {
			this(saleCashRegisterMovement.getSale(),saleCashRegisterMovement);
		}
	}
	
	@Deprecated
	@Getter @Setter
	public static class ReceiptParameters extends AbstractParameters implements Serializable{

		private static final long serialVersionUID = 6982660096264368704L;
		
		//private SaleStockTangibleProductMovementOutput saleStockOutput;
		protected BigDecimal numberOfGoodsInStock;
		protected BigDecimal numberOfGoodsDelivered;
		
		public ReceiptParameters(SaleCashRegisterMovement saleCashRegisterMovement) {
			super(saleCashRegisterMovement.getSale(),saleCashRegisterMovement);
			//this.saleStockOutput = saleStockOutput;
			//if(saleStockOutput!=null){
			//	numberOfGoodsInStock = saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods();
				//numberOfGoodsDelivered = saleStockOutput.getTangibleProductStockMovement().getQuantity().abs();
			//}
		}
		
		public ReceiptParameters(Sale sale) {
			super(null,null);
			//this(saleStockOutput,saleStockOutput.getSaleCashRegisterMovement());
		}
	}


	/**/
	
}
