package org.cyk.system.company.persistence.api.accounting;

import java.util.Collection;
import java.util.Date;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.persistence.api.event.AbstractIdentifiablePeriodDao;

public interface AccountingPeriodDao extends AbstractIdentifiablePeriodDao<AccountingPeriod> {

	//AccountingPeriod readMostRecentByCompany();
	
	Collection<AccountingPeriod> readWhereToDateLessThanByDateByOwnedCompany(Date date, OwnedCompany ownedCompany);
	Long countWhereToDateLessThanByDateByOwnedCompany(Date date,OwnedCompany ownedCompany);
	
	AccountingPeriod readWhereDateBetweenPeriodByOwnedCompany(Date date, OwnedCompany ownedCompany);
	Long countWhereDateBetweenPeriodByOwnedCompany(Date date,OwnedCompany ownedCompany);

}
