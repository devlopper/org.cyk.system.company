package org.cyk.system.company.model.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SaleProductReport extends AbstractGeneratable<SaleProductReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport saleReport;
	
	private String identifier,name,price,quantity,totalPrice;

	@Override
	public void generate() {
		identifier = RandomStringUtils.randomNumeric(8);
		name = RandomStringUtils.randomAlphabetic(100);
		price = provider.randomInt(1, 100000)+"";
		quantity = provider.randomInt(1, 10)+"";
		totalPrice = provider.randomInt(1, 1000000)+"";
	}
	
}
