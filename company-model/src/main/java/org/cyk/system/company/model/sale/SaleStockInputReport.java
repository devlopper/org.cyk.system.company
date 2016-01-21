package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.utility.common.generator.AbstractGeneratable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockInputReport extends AbstractGeneratable<SaleStockTangibleProductMovementInput> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String identifier,numberOfGoods,numberOfGoodsTaken,amount,amountPaid;

	@Override
	public void generate() {
		identifier=RandomStringUtils.randomNumeric(8);
		numberOfGoods=provider.randomInt(1, 100)+"";
		numberOfGoodsTaken=provider.randomInt(1, 100)+"";
		amount=provider.randomInt(1, 1000000)+"";
		amountPaid=provider.randomInt(1, 1000000)+"";
	}
	
}
