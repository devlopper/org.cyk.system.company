package org.cyk.system.company.business.impl;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.company.business.api.CompanyValueGenerator;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Sale;

public class CompanyValueGeneratorImpl implements CompanyValueGenerator,Serializable {

	private static final long serialVersionUID = -4852641723010830142L;

	@Override
	public String saleIdentificationNumber(Sale sale) {
		return RandomStringUtils.randomNumeric(8);
	}

	@Override
	public String cashRegisterMovementIdentificationNumber(CashRegisterMovement cashRegisterMovement) {
		return RandomStringUtils.randomNumeric(8);
	}

	

}
