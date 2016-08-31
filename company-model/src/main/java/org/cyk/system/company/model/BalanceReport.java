package org.cyk.system.company.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor
public class BalanceReport extends AbstractGeneratable<BalanceReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String value;
	private String cumul;
	
	@Override
	public void generate() {
		value=provider.randomInt(1, 100)+"";
		cumul=provider.randomInt(1, 100)+"";
	}
	
}
