package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementReportTemplateFile extends AbstractReportTemplateFile<SaleCashRegisterMovementReportTemplateFile> implements Serializable {

	private static final long serialVersionUID = -6025941646465245555L;
	
	private SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();

	@Override
	public void generate() {
		super.generate();
		saleCashRegisterMovement.generate();
		
	}

	
}
