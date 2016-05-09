package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;

public class DefaultSaleReportProducer extends AbstractCompanyReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	@Override
	public SaleReport produceInvoice(Sale sale) {
		SaleReport report = super.produceInvoice(sale);

		report.addLabelValueCollection("company.report.invoice.detailsblocktitle",new String[][]{
				{text("company.report.invoice.identifier"), report.getIdentifier()}
				,{text("company.report.invoice.cashregister"), report.getCashRegisterIdentifier()}
				,{text("company.report.invoice.date"), report.getDate()}
				,{text("company.report.invoice.customer"), report.getCustomer().getPerson().getNames()}
				});
		
		report.addLabelValueCollection(text("company.report.invoice.paymentblocktitle"),new String[][]{
				{text("company.report.invoice.amount.du"), report.getCost()}
				});
		
		report.addLabelValueCollection(text("company.report.invoice.vatblocktitle"),new String[][]{
				{text("company.report.invoice.vat.rate"), report.getVatRate()}
				,{text("company.report.invoice.amount.vat.excluded"), report.getAmountDueNoTaxes()}
				,{text("company.report.invoice.vat.amount"), report.getVatAmount()}
				});
		return report;
	}
	
}
