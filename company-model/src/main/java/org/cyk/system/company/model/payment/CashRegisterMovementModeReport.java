package org.cyk.system.company.model.payment;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CashRegisterMovementModeReport extends AbstractIdentifiableReport<CashRegisterMovementModeReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String supportDocumentIdentifier;
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		supportDocumentIdentifier = format(((CashRegisterMovementMode)source).getSupportDocumentIdentifier()); 
	}
	
	@Override
	public void generate() {
		super.generate();
		supportDocumentIdentifier = RandomStringUtils.randomAlphanumeric(6);
	}
	
	public static final String FIELD_SUPPORT_DOCUMENT_IDENTIFIER = "supportDocumentIdentifier";
}
