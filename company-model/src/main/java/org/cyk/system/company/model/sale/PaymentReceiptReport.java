package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;

@Getter @Setter @NoArgsConstructor
public class PaymentReceiptReport extends AbstractReportTemplateFile<PaymentReceiptReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();

	@Override
	public void generate() {
		super.generate();
		saleCashRegisterMovement.generate();
	}
	
	public SaleReport getSale(){
		return saleCashRegisterMovement.getSale();
	}
}