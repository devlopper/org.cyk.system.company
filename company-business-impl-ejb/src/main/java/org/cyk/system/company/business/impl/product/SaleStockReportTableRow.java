package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.root.business.impl.file.report.AbstractReportTableRow;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.Text;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockReportTableRow extends AbstractReportTableRow implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText @ReportColumn private String date;
	
	@Input @ReportColumn private String number;
	@Input @ReportColumn private String identifier;
	@Input @ReportColumn private String saleStockInputExternalIdentifier;
	
	@Input @ReportColumn private String customer;
	
	@Input(label=@Text(value="dropped")) @ReportColumn private String numberOfGoods;
	@Input @ReportColumn private String takenNumberOfGoods;
	
	@Input @ReportColumn private String stockIn;
	@Input @ReportColumn private String stockOut;
	@Input(label=@Text(value="remaining")) @ReportColumn private String remainingNumberOfGoods;
	
	@Input(label=@Text(value="amount")) @ReportColumn private String amount;
	@Input(label=@Text(value="paid")) @ReportColumn private String amountPaid;
	@Input @ReportColumn private String balance;
	@Input @ReportColumn private String cumulatedBalance;
	
	public SaleStockReportTableRow(SaleStock saleStock) {
		if(saleStock instanceof SaleStockInput){
			saleStockIntput((SaleStockInput) saleStock);
		}else if(saleStock instanceof SaleStockOutput){
			saleStockOutput((SaleStockOutput) saleStock);
		}
	}
	
	private void saleStockIntput(SaleStockInput saleStockInput){
		Sale sale = saleStockInput.getSale();
		saleStockInputExternalIdentifier = saleStockInput.getExternalIdentifier();
		identifier = saleStockInput.getSale().getIdentificationNumber();
		number = sale.getIdentificationNumber();
		if(sale.getCustomer()!=null)
			customer = sale.getCustomer().getPerson().getNames();
		amount = formatNumber(sale.getCost());
		//balance = amount;
		balance = formatNumber(saleStockInput.getSale().getBalance().getValue()); 
		
		cumulatedBalance = formatNumber(saleStockInput.getSale().getBalance().getCumul());
		numberOfGoods = formatNumber(saleStockInput.getTangibleProductStockMovement().getQuantity());
		//remainingNumberOfGoods = numberOfGoods;
		remainingNumberOfGoods = formatNumber(saleStockInput.getRemainingNumberOfGoods());
		
		date = formatDate(saleStockInput.getSale().getDate());
		
		stockIn = formatNumber(saleStockInput.getTangibleProductStockMovement().getQuantity().abs());
	}
	
	private void saleStockOutput(SaleStockOutput saleStockOutput){
		SaleStockInput saleStockInput = saleStockOutput.getSaleStockInput();
		Sale sale = saleStockInput.getSale();
		saleStockInputExternalIdentifier = saleStockInput.getExternalIdentifier();
		identifier = saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getIdentificationNumber();
		number = sale.getIdentificationNumber();
		if(sale.getCustomer()!=null)
			customer = sale.getCustomer().getPerson().getNames();
		amountPaid = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getAmount());
		balance = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getBalance().getValue());
		cumulatedBalance = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getBalance().getCumul());
		numberOfGoods = formatNumber(saleStockInput.getTangibleProductStockMovement().getQuantity());
		remainingNumberOfGoods = formatNumber(saleStockOutput.getRemainingNumberOfGoods());
		
		takenNumberOfGoods = formatNumber(saleStockOutput.getTangibleProductStockMovement().getQuantity().abs());
		
		date = formatDate(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		
		Integer sign = saleStockOutput.getTangibleProductStockMovement().getQuantity().signum();
		if(sign==-1)
			stockOut = formatNumber(saleStockOutput.getTangibleProductStockMovement().getQuantity().abs());
		else if(sign==1)
			stockIn = formatNumber(saleStockOutput.getTangibleProductStockMovement().getQuantity().abs());
	}
	
	/**/
	
	public static Boolean cashRegisterFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("takenNumberOfGoods") || field.getName().equals("amountPaid") || field.getName().equals("balance"));
	}
	
	public static Boolean inventoryFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("customer") || field.getName().equals("stockIn") || field.getName().equals("stockOut") || field.getName().equals("remainingNumberOfGoods"));
	}
	
	public static Boolean customerFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("customer") || field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("amount") || field.getName().equals("amountPaid") || field.getName().equals("cumulatedBalance"));
	}
	
	public static Boolean inputFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("customer") || field.getName().equals("identifier") 
				||field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("numberOfGoods") || field.getName().equals("remainingNumberOfGoods") || field.getName().equals("amount") 
				|| field.getName().equals("balance"));
	}
	
}
