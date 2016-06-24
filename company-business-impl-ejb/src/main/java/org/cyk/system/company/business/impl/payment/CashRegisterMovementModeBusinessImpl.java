package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementModeBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementModeDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public class CashRegisterMovementModeBusinessImpl extends AbstractEnumerationBusinessImpl<CashRegisterMovementMode, CashRegisterMovementModeDao> implements CashRegisterMovementModeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public CashRegisterMovementModeBusinessImpl(CashRegisterMovementModeDao dao) {
		super(dao);
	}
	
	
}
