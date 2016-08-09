package org.cyk.system.company.persistence.impl.accounting;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.root.persistence.impl.event.AbstractIdentifiablePeriodDaoImpl;

public class AccountingPeriodDaoImpl extends AbstractIdentifiablePeriodDaoImpl<AccountingPeriod> implements AccountingPeriodDao, Serializable {

	private static final long serialVersionUID = -763318561920858308L;
	
	private static final String READ_BY_COMPANY_FORMAT = "SELECT accountingPeriod FROM AccountingPeriod accountingPeriod WHERE %s ORDER BY accountingPeriod.globalIdentifier.existencePeriod.fromDate DESC";
	
	private String readWhereToDateLessThanByDateByCompany,countWhereToDateLessThanByDateByCompany,readWhereDateBetweenPeriodByCompany,countWhereDateBetweenPeriodByCompany;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readWhereToDateLessThanByDateByCompany,String.format(READ_BY_COMPANY_FORMAT, 
				"accountingPeriod.ownedCompany = :ownedCompany AND accountingPeriod.globalIdentifier.existencePeriod.toDate < :thedate"));
        registerNamedQuery(readWhereDateBetweenPeriodByCompany,String.format(READ_BY_COMPANY_FORMAT, 
        		"accountingPeriod.ownedCompany = :ownedCompany AND :thedate BETWEEN accountingPeriod.globalIdentifier.existencePeriod.fromDate AND accountingPeriod.globalIdentifier.existencePeriod.toDate"));
	}
	
	@Override
	public Collection<AccountingPeriod> readWhereToDateLessThanByDateByOwnedCompany(Date date, OwnedCompany ownedCompany) {
		return namedQuery(readWhereToDateLessThanByDateByCompany).parameter("thedate", date).parameter("ownedCompany", ownedCompany).resultMany();
	}

	@Override
	public Long countWhereToDateLessThanByDateByOwnedCompany(Date date,OwnedCompany ownedCompany) {
		return countNamedQuery(countWhereToDateLessThanByDateByCompany).parameter("thedate", date).parameter("ownedCompany", ownedCompany).resultOne();
	}

	@Override
	public AccountingPeriod readWhereDateBetweenPeriodByOwnedCompany(Date date,OwnedCompany ownedCompany) {
		return namedQuery(readWhereDateBetweenPeriodByCompany).ignoreThrowable(NoResultException.class).parameter("thedate", date).parameter("ownedCompany", ownedCompany).resultOne();
	}

	@Override
	public Long countWhereDateBetweenPeriodByOwnedCompany(Date date, OwnedCompany ownedCompany) {
		return countNamedQuery(countWhereDateBetweenPeriodByCompany).parameter("thedate", date).parameter("ownedCompany", ownedCompany).resultOne();
	}

	
	
}
