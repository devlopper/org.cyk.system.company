package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementTermBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementTermDao;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;

public class CashRegisterMovementTermBusinessImpl extends AbstractCollectionItemBusinessImpl<CashRegisterMovementTerm, CashRegisterMovementTermDao,CashRegisterMovementTermCollection> implements CashRegisterMovementTermBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public CashRegisterMovementTermBusinessImpl(CashRegisterMovementTermDao dao) {
		super(dao); 
	}
		
}
