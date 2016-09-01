package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReport;

public class DefaultSaleReportProducer extends AbstractCompanyReportProducer implements Serializable {

	private static final long serialVersionUID = 7126711234011563710L;
	
	@Override
	public SaleCashRegisterMovementReport produceSaleCashRegisterMovementReport(SaleCashRegisterMovement saleCashRegisterMovement) {
		SaleCashRegisterMovementReport report = super.produceSaleCashRegisterMovementReport(saleCashRegisterMovement);
		/*
		report.addLabelValueCollection("company.report.salecashregistermovement.detailsblocktitle",new String[][]{
				{text("company.report.salecashregistermovement.identifier"), report.getIdentifier()}
				//,{text("company.report.salecashregistermovement.sale.identifier"), report.getSale().getIdentifier()}
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
		*/
		return report;
	}
	
}
