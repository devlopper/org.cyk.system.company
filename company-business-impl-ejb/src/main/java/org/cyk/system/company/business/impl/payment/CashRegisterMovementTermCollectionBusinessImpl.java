package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterMovementTermBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementTermCollectionBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementTermCollectionDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementTermDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;

@Stateless
public class CashRegisterMovementTermCollectionBusinessImpl extends AbstractCollectionBusinessImpl<CashRegisterMovementTermCollection,CashRegisterMovementTerm, CashRegisterMovementTermCollectionDao,CashRegisterMovementTermDao,CashRegisterMovementTermBusiness> implements CashRegisterMovementTermCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject private CashRegisterMovementTermDao cashRegisterMovementTermDao;
	
	@Inject
	public CashRegisterMovementTermCollectionBusinessImpl(CashRegisterMovementTermCollectionDao dao) {
		super(dao); 
	}
	
	@Override
	protected CashRegisterMovementTermDao getItemDao() {
		return cashRegisterMovementTermDao;
	}
	@Override
	protected CashRegisterMovementTermBusiness getItemBusiness() { 
		return inject(CashRegisterMovementTermBusiness.class);
	}
	
	

}
