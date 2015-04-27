package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AbstractAccountingPeriodResultsBusiness;
import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.persistence.api.accounting.AbstractAccountingPeriodResultsDao;
import org.cyk.system.root.business.api.chart.PieModel;
import org.cyk.system.root.business.api.chart.SeriesItem;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.AbstractEnumeration;

public class AbstractAccountingPeriodResultsBusinessImpl<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,DAO extends AbstractAccountingPeriodResultsDao<RESULTS,PRODUCT>,PRODUCT extends AbstractEnumeration> extends AbstractTypedBusinessService<RESULTS, DAO> implements AbstractAccountingPeriodResultsBusiness<RESULTS,PRODUCT>, Serializable {

	private static final long serialVersionUID = -1843616492544404846L;
 
	@Inject private LanguageBusiness languageBusiness;
	
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
	public Collection<RESULTS> findByAccountingPeriodByProducts(AccountingPeriod accountingPeriod, Collection<PRODUCT> products) {
		return dao.readByAccountingPeriodByProducts(accountingPeriod, products);
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

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public PieModel findNumberOfSalesPieModel(AccountingPeriod accountingPeriod,Collection<PRODUCT> products) {
		return findNumberOfSalesPieModel(findByAccountingPeriodByProducts(accountingPeriod, products));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public PieModel findNumberOfSalesPieModel(Collection<RESULTS> resultsCollection) {
		PieModel pieModel = new PieModel(languageBusiness.findText("field.number.of.sales"));
		for(RESULTS results : resultsCollection)
			pieModel.getSeries().getItems().add(new SeriesItem(results.getEntity().getName(), results.getSalesResults().getCount()));
		return pieModel;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public PieModel findTurnoverPieModel(Collection<RESULTS> resultsCollection) {
		PieModel pieModel = new PieModel(languageBusiness.findText("field.turnover"));
		for(RESULTS results : resultsCollection)
			pieModel.getSeries().getItems().add(new SeriesItem(results.getEntity().getName(), results.getSalesResults().getTurnover()));
		return pieModel;
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal findHighestNumberOfSalesValue(Collection<RESULTS> resultsCollection) {
		List<BigDecimal> values = new ArrayList<>();
		for(RESULTS results : resultsCollection)
			values.add(results.getSalesResults().getCount());
		Collections.sort(values);
		return values.get(values.size()-1);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal findLowestNumberOfSalesValue(Collection<RESULTS> resultsCollection) {
		List<BigDecimal> values = new ArrayList<>();
		for(RESULTS results : resultsCollection)
			values.add(results.getSalesResults().getCount());
		Collections.sort(values);
		return values.get(0);
	}	
	
}
