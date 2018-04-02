package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.CostBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreCollectionItemDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.ConditionHelper;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;
import org.cyk.utility.common.helper.MethodHelper;
import org.cyk.utility.common.helper.NumberHelper;

public class SalableProductStoreCollectionBusinessImpl extends AbstractCollectionBusinessImpl<SalableProductStoreCollection,SalableProductStoreCollectionItem, SalableProductStoreCollectionDao,SalableProductStoreCollectionItemDao,SalableProductStoreCollectionItemBusiness> implements SalableProductStoreCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public SalableProductStoreCollectionBusinessImpl(SalableProductStoreCollectionDao dao) {
		super(dao); 
	}
	
	@Override
	protected void afterCrud(SalableProductStoreCollection salableProductStoreCollection, Crud crud) {
		super.afterCrud(salableProductStoreCollection, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(salableProductStoreCollection.getItems().getSynchonizationEnabled())){
				/*Cost cost = new Cost();
				Collection<Cost> costs = MethodHelper.getInstance().callGet(salableProductStoreCollection.getItems().getElements(), Cost.class, SalableProductStoreCollectionItem.FIELD_COST);
				inject(CostBusiness.class).add(cost, costs);
				throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("costvalue")
				.setDomainNameIdentifier("salableProductStoreCollection").setNumber1(salableProductStoreCollection.getCost().getValue())
				.setNumber2(cost.getValue()).setEqual(Boolean.FALSE), BusinessException.class);
				*/
				
				// sum of items cost must be equal to cost
				Collection<SalableProductStoreCollectionItem> salableProductStoreCollectionItems = inject(SalableProductStoreCollectionItemDao.class).readByCollection(salableProductStoreCollection);
				Cost cost = new Cost();
				Collection<Cost> costs = MethodHelper.getInstance().callGet(salableProductStoreCollectionItems, Cost.class, SalableProductStoreCollectionItem.FIELD_COST);
				inject(CostBusiness.class).add(cost, costs);
				
				throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("costvalue")
						.setDomainNameIdentifier("salableProductStoreCollection").setValue1(salableProductStoreCollection.getCost().getValue())
						.setValue2(cost.getValue()).setEqual(Boolean.FALSE));
			}
		}
	}
	
	/*@Override
	protected void afterCrud(SalableProductStoreCollection salableProductStoreCollection, Crud crud) {
		super.afterCrud(salableProductStoreCollection, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(salableProductStoreCollection.isItemAggregationApplied()){
				computeCost(salableProductStoreCollection);
				BigDecimal v1 = salableProductStoreCollection.getCost().getValue();
				Cost cost =inject(SalableProductStoreCollectionItemDao.class).sumCostBySalableProductStoreCollection(salableProductStoreCollection);
				exceptionUtils().exception(v1.compareTo(commonUtils.getValueIfNotNullElseDefault(cost.getValue(),BigDecimal.ZERO))!=0
						, v1+"costdoesnotmatch"+commonUtils.getValueIfNotNullElseDefault(cost.getValue(),BigDecimal.ZERO)); 	
				dao.update(salableProductStoreCollection);
			}
		}
	}*/
	
	@Override
	public SalableProductStoreCollection instanciateOne(String code,String name) {
		SalableProductStoreCollection salableProductStoreCollection = super.instanciateOne(code,name);
		//salableProductStoreCollection.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		return salableProductStoreCollection;
	}
	
	@Override
	protected SalableProductStoreCollectionItem addOrRemove(SalableProductStoreCollection salableProductStoreCollection, SalableProductStoreCollectionItem salableProductStoreCollectionItem,Boolean add) {
		BigDecimal factor = (add == null || Boolean.TRUE.equals(add)) ? BigDecimal.ONE : BigDecimal.ONE.negate();
		super.addOrRemove(salableProductStoreCollection, salableProductStoreCollectionItem, add);
		NumberHelper numberHelper = NumberHelper.getInstance();
		inject(CostBusiness.class).add(salableProductStoreCollection.getCost()
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductStoreCollectionItem.getCost().getValue(),factor))
				, BigDecimal.ONE.multiply(factor)
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductStoreCollectionItem.getCost().getTax(),factor))
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductStoreCollectionItem.getCost().getTurnover(),factor)));
		/*commonUtils.increment(BigDecimal.class, salableProductStoreCollection.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductStoreCollection.getCost(), Cost.FIELD_VALUE, salableProductStoreCollectionItem.getCost().getValue().multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductStoreCollection.getCost(), Cost.FIELD_TAX, salableProductStoreCollectionItem.getCost().getTax().multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductStoreCollection.getCost(), Cost.FIELD_TURNOVER, salableProductStoreCollectionItem.getCost().getTurnover().multiply(factor));*/		
		return salableProductStoreCollectionItem;
	}
	
	/*@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductStoreCollection salableProductStoreCollection,Collection<SalableProductStoreCollectionItem> salableProductStoreCollectionItems) {
		LogMessage.Builder logMessageBuilder = new LogMessage.Builder("Compute", "cost");
		addLogMessageBuilderParameters(logMessageBuilder,"items", salableProductStoreCollectionItems);
		Cost cost = new Cost();
		for(SalableProductStoreCollectionItem salableProductStoreCollectionItem : salableProductStoreCollectionItems){
			inject(SalableProductStoreCollectionItemBusiness.class).computeCost(salableProductStoreCollectionItem);
			inject(CostBusiness.class).add(cost, salableProductStoreCollectionItem.getCost());
		}
		setCost(salableProductStoreCollection, cost,Boolean.FALSE,logMessageBuilder);
		logTrace(logMessageBuilder);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductStoreCollection salableProductStoreCollection) {
		LogMessage.Builder logMessageBuilder = new LogMessage.Builder("Compute", "cost");
		Cost cost = inject(SalableProductStoreCollectionItemDao.class).sumCostBySalableProductStoreCollection(salableProductStoreCollection);
		setCost(salableProductStoreCollection,cost,Boolean.TRUE,logMessageBuilder);
		logTrace(logMessageBuilder);
	}
	
	private void setCost(SalableProductStoreCollection salableProductStoreCollection,Cost cost,Boolean database,LogMessage.Builder logMessageBuilder){
		addLogMessageBuilderParameters(logMessageBuilder,"salable product collection",salableProductStoreCollection.getCode(),"database",database,"cost",cost
				,"Aggregation applied",salableProductStoreCollection.isItemAggregationApplied());
		if(salableProductStoreCollection.isItemAggregationApplied()){
			addLogMessageBuilderParameters(logMessageBuilder, "current cost",cost);
			salableProductStoreCollection.getCost()._set(cost);
		}
		addLogMessageBuilderParameters(logMessageBuilder, "new cost",salableProductStoreCollection.getCost());
	}*/
	
	@Override
	public SalableProductStoreCollection instanciateOneRandomly(String code) {
		SalableProductStoreCollection salableProductStoreCollection = super.instanciateOneRandomly(code);
		salableProductStoreCollection.getCost().setValue(new BigDecimal("100"));
		return salableProductStoreCollection;
	}
	
	@Override
	protected void computeChanges(final SalableProductStoreCollection salableProductStoreCollection, Builder logMessageBuilder) {
		super.computeChanges(salableProductStoreCollection, logMessageBuilder);
		logMessageBuilder.addManyParameters("cost");
		logMessageBuilder.addNamedParameters("before",salableProductStoreCollection.getCost().toString());
		if(Boolean.TRUE.equals(salableProductStoreCollection.getItems().isSynchonizationEnabled())){
			salableProductStoreCollection.getCost().setValue(BigDecimal.ZERO);
			salableProductStoreCollection.getCost().setTax(BigDecimal.ZERO);
			salableProductStoreCollection.getCost().setTurnover(BigDecimal.ZERO);
			salableProductStoreCollection.getCost().setNumberOfProceedElements(BigDecimal.ZERO);
			new CollectionHelper.Iterator.Adapter.Default<SalableProductStoreCollectionItem>(salableProductStoreCollection.getItems().getElements()){
				private static final long serialVersionUID = 1L;
				protected void __executeForEach__(SalableProductStoreCollectionItem salableProductStoreCollectionItem) {
					inject(CostBusiness.class).add(salableProductStoreCollection.getCost(), salableProductStoreCollectionItem.getCost());
				}
			}.execute();	
		}
		logMessageBuilder.addNamedParameters("after",salableProductStoreCollection.getCost().toString());
	}
	
	/**/
	
}
