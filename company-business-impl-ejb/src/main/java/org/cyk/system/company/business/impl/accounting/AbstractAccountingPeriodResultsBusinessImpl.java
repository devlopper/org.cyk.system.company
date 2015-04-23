package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.cyk.system.company.business.api.accounting.AbstractAccountingPeriodResultsBusiness;
import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.persistence.api.accounting.AbstractAccountingPeriodResultsDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.AbstractIdentifiable;

public class AbstractAccountingPeriodResultsBusinessImpl<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,DAO extends AbstractAccountingPeriodResultsDao<RESULTS,PRODUCT>,PRODUCT extends AbstractIdentifiable> extends AbstractTypedBusinessService<RESULTS, DAO> implements AbstractAccountingPeriodResultsBusiness<RESULTS,PRODUCT>, Serializable {

	private static final long serialVersionUID = -1843616492544404846L;
 
	public AbstractAccountingPeriodResultsBusinessImpl(DAO dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<RESULTS> findByAccountingPeriod(AccountingPeriod accountingPeriod) {
		return dao.readByAccountingPeriod(accountingPeriod);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public RESULTS findByAccountingPeriodByProduct(AccountingPeriod accountingPeriod, PRODUCT product) {
		return dao.readByAccountingPeriodByProduct(accountingPeriod, product);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<RESULTS> findHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products) {
		BigDecimal value = dao.readHighestNumberOfSales(accountingPeriod, products);
		return dao.readByAccountingPeriodByProductsByNumberOfSales(accountingPeriod, products, value);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<RESULTS> findLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<PRODUCT> products) {
		BigDecimal value = dao.readLowestNumberOfSales(accountingPeriod, products);
		return dao.readByAccountingPeriodByProductsByNumberOfSales(accountingPeriod, products, value);
	}
 
}
