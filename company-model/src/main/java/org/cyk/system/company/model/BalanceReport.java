package org.cyk.system.company.model;

import java.io.Serializable;

import org.cyk.utility.common.generator.AbstractGeneratable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BalanceReport extends AbstractGeneratable<BalanceReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String value;
	private String cumul;
	
	private String valueInWords;
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		_setValue(((Balance)source).getValue());
		cumul = format(((Balance)source).getCumul());
	}
	
	private void  _setValue(Number number){
		this.value = format(number);
		setValueInWords(formatNumberToWords(number));
	}
		
	@Override
	public void generate() {
		_setValue(provider.randomInt(1, 100));
		cumul=provider.randomInt(1, 100)+"";
	}
	
}
