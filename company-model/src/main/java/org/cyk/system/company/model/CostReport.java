package org.cyk.system.company.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor
public class CostReport extends AbstractGeneratable<CostReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String numberOfProceedElements;
	private String value;
	private String tax;
	private String turnover;
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		numberOfProceedElements = format(((Cost)source).getNumberOfProceedElements());
		value = format(((Cost)source).getValue());
		tax = format(((Cost)source).getTax());
		turnover = format(((Cost)source).getTurnover());
	}
	
	@Override
	public void generate() {
		numberOfProceedElements=provider.randomInt(1, 100)+"";
		value=provider.randomInt(1, 100)+"";
		tax=provider.randomInt(1, 100)+"";
		turnover=provider.randomInt(1, 100)+"";
	}
	
}
