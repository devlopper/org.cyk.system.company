package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;
import org.cyk.system.company.model.sale.SaleReport;

public class DefaultSaleReportProducer extends AbstractCompanyReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	@Override
	public SaleReport produceSaleReport(Sale sale) {
		SaleReport report = super.produceSaleReport(sale);

		report.addLabelValueCollection("company.report.sale.detailsblocktitle",new String[][]{
				{text("company.report.sale.identifier"), report.getIdentifier()}
				,{text("company.report.sale.cashregister"), report.getCashRegisterIdentifier()}
				,{text("company.report.sale.date"), report.getDate()}
				,{text("company.report.sale.customer"), report.getCustomer().getCommonActor().getPerson().getNames()}
				});
		
		report.addLabelValueCollection(text("company.report.sale.paymentblocktitle"),new String[][]{
				{text("company.report.sale.amount.du"), report.getAmountDue()}
				});
		
		if(sale.getCost().getTax().equals(BigDecimal.ZERO))
			report.addLabelValueCollection(text("company.report.sale.vatblocktitle"),new String[][]{});
		else{
			report.addLabelValueCollection(text("company.report.sale.vatblocktitle"),new String[][]{
					{text("company.report.sale.vat.rate"), report.getVatRate()}
					,{text("company.report.sale.amount.vat.excluded"), report.getAmountDueNoTaxes()}
					,{text("company.report.sale.vat.amount"), report.getVatAmount()}
					});
		}
		return report;
	}
	
	@Override
	public SaleCashRegisterMovementReport produceSaleCashRegisterMovementReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		SaleCashRegisterMovementReport report = super.produceSaleCashRegisterMovementReport(saleCashRegisterMovement);
		
		report.addLabelValueCollection("company.report.salecashregistermovement.detailsblocktitle",new String[][]{
				{text("company.report.salecashregistermovement.identifier"), report.getIdentifier()}
				,{text("company.report.salecashregistermovement.sale.identifier"), report.getSale().getIdentifier()}
				,{text("company.report.salecashregistermovement.cashregister"), report.getCashRegisterIdentifier()}
				,{text("company.report.salecashregistermovement.date"), report.getDate()}
				,{text("company.report.salecashregistermovement.customer"), report.getSale().getCustomer().getCommonActor().getPerson().getNames()}
				});
		
		report.addLabelValueCollection(text("company.report.salecashregistermovement.paymentblocktitle"),new String[][]{
				{text("company.report.salecashregistermovement.amount.du"), report.getAmountDue()}
				,{text("company.report.salecashregistermovement.amount.in"), report.getAmountIn()}
				,{text("company.report.salecashregistermovement.amount.out"), report.getAmountOut()}
				,{text("company.report.salecashregistermovement.amount.balance"), report.getBalance()}
				});
		
		return report;
	}
	
}
