package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class CashierBusinessImpl extends AbstractTypedBusinessService<Cashier, CashierDao> implements CashierBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CashierBusinessImpl(CashierDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Cashier findByEmployee(Employee employee) {
		return dao.readByEmployee(employee);
	}
	
}
