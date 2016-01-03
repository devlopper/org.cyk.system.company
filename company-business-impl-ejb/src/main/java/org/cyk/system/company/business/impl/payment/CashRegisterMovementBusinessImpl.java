package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

@Stateless
public class CashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<CashRegisterMovement, CashRegisterMovementDao> implements CashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	
	@Inject
	public CashRegisterMovementBusinessImpl(CashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegisterMovement create(CashRegisterMovement cashRegisterMovement) {
		RootBusinessLayer.getInstance().getMovementBusiness().create(cashRegisterMovement.getMovement());
		super.create(cashRegisterMovement);
		cashRegisterMovement.setComputedIdentifier(generateIdentifier(cashRegisterMovement,CompanyBusinessLayerListener.CASH_MOVEMENT_IDENTIFIER,accountingPeriodBusiness.findCurrent()
				.getSaleConfiguration().getCashRegisterMovementIdentifierGenerator()));
		dao.update(cashRegisterMovement);
		return cashRegisterMovement;
	}

}
