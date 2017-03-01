package org.cyk.system.company.model.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;
import org.cyk.system.root.model.mathematics.MovementReport;

@Getter @Setter @NoArgsConstructor
public class CashRegisterMovementReport extends AbstractIdentifiableReport<CashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private CashRegisterReport cashRegister = new CashRegisterReport();
	private MovementReport movement = new MovementReport();
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		cashRegister.setSource( ((CashRegisterMovement)source).getCashRegister() ); 
		movement.setSource( ((CashRegisterMovement)source).getMovement() ); 
	}
	
	@Override
	public void generate() {
		super.generate();
		cashRegister.generate();
		movement.generate();
	}
}
