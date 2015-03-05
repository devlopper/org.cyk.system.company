package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter
public class SaleCashRegisterMovementReport extends AbstractGeneratable<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String identifier,date,amountDue,amountIn,amountToOut,amountOut,balance,vatRate,vatAmount,amountDueNoTaxes;

	@Override
	public void generate() {
		identifier=RandomStringUtils.randomNumeric(8);
		date=new Date().toString();
		amountDue=provider.randomInt(1, 1000000)+"";
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		balance=provider.randomInt(1, 1000000)+"";
		vatRate="3.18";
		amountDueNoTaxes=provider.randomInt(1, 1000000)+"";
		vatAmount=provider.randomInt(1, 1000000)+"";
	}
	
}
