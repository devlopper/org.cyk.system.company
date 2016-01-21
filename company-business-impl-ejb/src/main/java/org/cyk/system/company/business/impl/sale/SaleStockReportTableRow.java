package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.root.business.impl.file.report.AbstractReportTableRow;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.annotation.user.interfaces.Text.ValueType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockReportTableRow extends AbstractReportTableRow implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	public static Boolean FIELD_IDENTIFIER_VISIBLE = Boolean.TRUE;
	
	private SaleStockTangibleProductMovement saleStock;
	
	@Input @InputText @ReportColumn private String date;
	
	//@Input @ReportColumn private String number;
	@Input @ReportColumn private String identifier;
	@Input @ReportColumn private String saleStockInputExternalIdentifier;
	
	@Input @ReportColumn private String customer;
	
	@Input(label=@Text(value="dropped")) @ReportColumn private String numberOfGoods;
	@Input @ReportColumn private String takenNumberOfGoods;
	
	@Input @ReportColumn private String stockIn;
	@Input @ReportColumn private String stockOut;
	@Input(label=@Text(value="remaining")) @ReportColumn private String remainingNumberOfGoods;
	
	@Input(label=@Text(value="amount")) @ReportColumn private String amount;
	@Input @ReportColumn private String amountPaid;
	@Input @ReportColumn private String balance;
	@Input @ReportColumn private String cumulatedBalance;
	
	@Input(label=@Text(type=ValueType.ID,value="ui.form.salestockinput.field.comments")) @ReportColumn private String comments;
	
	public SaleStockReportTableRow() {
		constructor(null);
	}
	
	public SaleStockReportTableRow(SaleStockTangibleProductMovementOutput saleStock) {
		constructor(saleStock);
	}
	
	public SaleStockReportTableRow(SaleStockTangibleProductMovementInput saleStock) {
		constructor(saleStock);
	}
	
	public SaleStockReportTableRow(SaleStockTangibleProductMovement saleStock) {
		constructor(saleStock);
	}
	
	private void constructor(SaleStockTangibleProductMovement saleStock) {
		this.saleStock = saleStock;
		//TODO refactor same code in functions
		if(saleStock instanceof SaleStockTangibleProductMovementInput){
			saleStockIntput((SaleStockTangibleProductMovementInput) saleStock);
		}else if(saleStock instanceof SaleStockTangibleProductMovementOutput){
			saleStockOutput((SaleStockTangibleProductMovementOutput) saleStock);
		}
	}
	
	private void saleStockIntput(SaleStockTangibleProductMovementInput saleStockInput){
		Sale sale = saleStockInput.getSale();
		saleStockInputExternalIdentifier = saleStockInput.getExternalIdentifier();
		identifier = saleStockInput.getSale().getComputedIdentifier();
		//number = sale.getIdentifier().to;
		if(sale.getCustomer()!=null)
			customer = sale.getCustomer().getRegistration().getCode()+" - "+sale.getCustomer().getPerson().getNames();
		amount = formatNumber(sale.getCost().getValue());
		//balance = amount;
		balance = formatNumber(saleStockInput.getSale().getBalance().getValue()); 
		
		cumulatedBalance = formatNumber(saleStockInput.getSale().getBalance().getCumul());
		//cumulatedBalance = formatNumber(saleStockInput.getSale().getCost());
		//numberOfGoods = formatNumber(saleStockInput.getStockTangibleProductStockMovement().getQuantity());
		
		//remainingNumberOfGoods = formatNumber(saleStockInput.getStockTangibleProductStockMovement().getQuantity());
		//remainingNumberOfGoods = numberOfGoods;
		//remainingNumberOfGoods = formatNumber(saleStockInput.getRemainingNumberOfGoods());
		
		date = formatDate(saleStockInput.getSale().getDate());
		
		//stockIn = formatNumber(saleStockInput.getStockTangibleProductStockMovement().getQuantity().abs());
		comments = saleStockInput.getSale().getComments();
	}
	
	private void saleStockOutput(SaleStockTangibleProductMovementOutput saleStockOutput){
		SaleStockTangibleProductMovementInput saleStockInput = saleStockOutput.getSaleStockInput();
		Sale sale = saleStockInput.getSale();
		saleStockInputExternalIdentifier = saleStockInput.getExternalIdentifier();
		identifier = saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getComputedIdentifier();
		//number = sale.getIdentificationNumber();
		if(sale.getCustomer()!=null)
			customer = sale.getCustomer().getRegistration().getCode()+" - "+sale.getCustomer().getPerson().getNames();
		//amountPaid = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getAmount());
		balance = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getBalance().getValue());
		cumulatedBalance = formatNumber(saleStockOutput.getSaleCashRegisterMovement().getBalance().getCumul());
		//numberOfGoods = formatNumber(saleStockInput.getStockTangibleProductStockMovement().getQuantity());
		remainingNumberOfGoods = formatNumber(saleStockOutput.getRemainingNumberOfGoods());
		
		//takenNumberOfGoods = formatNumber(saleStockOutput.getStockTangibleProductStockMovement().getQuantity().abs());
		
		//date = formatDate(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		/*
		Integer sign = saleStockOutput.getStockTangibleProductStockMovement().getQuantity().signum();
		if(sign==-1)
			stockOut = formatNumber(saleStockOutput.getStockTangibleProductStockMovement().getQuantity().abs());
		else if(sign==1)
			stockIn = formatNumber(saleStockOutput.getStockTangibleProductStockMovement().getQuantity().abs());
		*/
		comments = saleStockOutput.getSaleStockInput().getSale().getComments();
	}
	
	/**/
	
	public static Boolean cashRegisterFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("saleStockInputExternalIdentifier") || field.getName().equals("identifier") || 
				field.getName().equals("takenNumberOfGoods") || field.getName().equals("amountPaid") || field.getName().equals("balance") 
				|| field.getName().equals("comments"));
	}
	
	public static Boolean inventoryFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("customer") || field.getName().equals("stockIn") || field.getName().equals("stockOut") 
				|| field.getName().equals("remainingNumberOfGoods") || field.getName().equals("comments"));
	}
	
	public static Boolean customerFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("customer") || field.getName().equals("saleStockInputExternalIdentifier") || 
				field.getName().equals("amount") || field.getName().equals("amountPaid") || field.getName().equals("cumulatedBalance") 
				|| field.getName().equals("comments"));
	}
	
	public static Boolean inputFieldIgnored(Field field){
		return !(field.getName().equals("date") || field.getName().equals("customer") 
				|| (Boolean.TRUE.equals(FIELD_IDENTIFIER_VISIBLE) && field.getName().equals("identifier")) 
				|| field.getName().equals("saleStockInputExternalIdentifier") || field.getName().equals("numberOfGoods") || field.getName().equals("remainingNumberOfGoods") 
				|| field.getName().equals("amount") || field.getName().equals("balance"));
	}
	
	/**/
	
	public static final String FIELD_REMAINING_NUMBER_OF_GOODS="remainingNumberOfGoods";
	public static final String FIELD_STOCK_IN="stockIn";
	public static final String FIELD_TAKEN_NUMBER_OF_GOODS="takenNumberOfGoods";
	public static final String FIELD_NUMBER_OF_GOODS="numberOfGoods";
	public static final String FIELD_STOCK_OUT="stockOut";
	public static final String FIELD_AMOUNT="amount";
	public static final String FIELD_AMOUNT_PAID="amountPaid";
	public static final String FIELD_BALANCE="balance";
	public static final String FIELD_CUMULATED_BALANCE="cumulatedBalance";
	
}
