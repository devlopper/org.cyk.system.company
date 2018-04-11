package org.cyk.system.company.model.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;
import org.cyk.system.root.model.mathematics.IntervalReport;
import org.cyk.system.root.model.mathematics.movement.MovementReport;

@Getter @Setter @NoArgsConstructor @Deprecated
public class CashRegisterMovementReport extends AbstractIdentifiableReport<CashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private CashRegisterReport cashRegister = new CashRegisterReport();
	private MovementReport movement = new MovementReport();
	private CashRegisterMovementModeReport mode = new CashRegisterMovementModeReport();
	private IntervalReport stampDuty = new IntervalReport();
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		cashRegister.setSource( ((CashRegisterMovement)source).getCashRegister() ); 
		movement.setSource( ((CashRegisterMovement)source).getMovement() ); 
		mode.setSource( ((CashRegisterMovement)source).getMode());
		stampDuty.setSource(((CashRegisterMovement)source).getStampDutyInterval());
		String modeCode = ((CashRegisterMovement)source).getMode().getCode();
		if(CompanyConstant.Code.CashRegisterMovementMode.CASH.equals(modeCode)){
			text = "STAMP DUTY : "+stampDuty.getValue();
		}else if(CompanyConstant.Code.CashRegisterMovementMode.CHEQUE.equals(modeCode)){
			text = "BANK : "+((CashRegisterMovement)source).getSupportingDocument().getGenerator()+" - N° : "
					+((CashRegisterMovement)source).getSupportingDocument().getCode();
		}else if(CompanyConstant.Code.CashRegisterMovementMode.BANK_TRANSFER.equals(modeCode)){
			text = "REFERENCE : "+((CashRegisterMovement)source).getSupportingDocument().getCode();
		}else if(CompanyConstant.Code.CashRegisterMovementMode.MOBILE_PAYMENT.equals(modeCode)){
			text = "NETWORK : "+((CashRegisterMovement)source).getSupportingDocument().getGenerator()+" CEL N° : "
					+((CashRegisterMovement)source).getSupportingDocument().getContentWriter()+" ID : "+((CashRegisterMovement)source).getSupportingDocument().getCode();
		}
	}
	
	@Override
	public void generate() {
		super.generate();
		cashRegister.generate();
		movement.generate();
		mode.generate();
		stampDuty.generate();
	}
}
