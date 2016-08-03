package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.file.report.AbstractReportTableRow;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerReportTableRow extends AbstractOutputDetails<Customer> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String registrationCode;
	@Input @InputText private String names;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockInputCount;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockOutputCount;
	//@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleCount;
	//@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String paymentCount;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String turnover;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String paid;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String balance;
	
	public CustomerReportTableRow(Customer customer) {
		super(customer);
		System.out.println("CustomerReportTableRow.CustomerReportTableRow()");
		this.registrationCode = customer.getCode();
		this.names = customer.getPerson().getNames();
		this.saleStockInputCount = formatNumber(customer.getSaleStockInputCount());
		this.saleStockOutputCount = formatNumber(customer.getSaleStockOutputCount());
		//this.saleCount = RootBusinessLayer.getInstance().getNumberBusiness().format(customer.getSaleCount());
		//this.paymentCount = RootBusinessLayer.getInstance().getNumberBusiness().format(customer.getPaymentCount());
		this.turnover = formatNumber(customer.getTurnover());
		this.paid = formatNumber(customer.getPaid());
		this.balance = formatNumber(customer.getBalance());
	}
	
	public static Boolean balanceFieldIgnored(Field field){
		return !(field.getName().equals("registrationCode") || field.getName().equals("names") || 
				field.getName().equals("paid") || field.getName().equals("turnover") || field.getName().equals("balance"));
	}
	
	public static Boolean credenceFieldIgnored(Field field){
		return !(field.getName().equals("registrationCode") || field.getName().equals("names") || field.getName().equals("balance"));
	}
	
	public static Boolean saleStockFieldIgnored(Field field){
		return !(field.getName().equals("registrationCode") || field.getName().equals("names") || 
				field.getName().equals("saleStockInputCount") || field.getName().equals("saleStockOutputCount"));
	}
	
	public static final String FIELD_REGISTRATION_CODE = "registrationCode";
	public static final String FIELD_NAMES = "names";
	public static final String FIELD_SALE_STOCK_INPUT_COUNT = "saleStockInputCount";
	public static final String FIELD_SALE_STOCK_OUTPUT_COUNT = "saleStockOutputCount";
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_PAID = "paid";
	public static final String FIELD_BALANCE = "balance";
	
}