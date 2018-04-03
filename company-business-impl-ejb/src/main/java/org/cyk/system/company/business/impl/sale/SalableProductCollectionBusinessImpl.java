package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.CostBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.ConditionHelper;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;
import org.cyk.utility.common.helper.MethodHelper;
import org.cyk.utility.common.helper.NumberHelper;

public class SalableProductCollectionBusinessImpl extends AbstractCollectionBusinessImpl<SalableProductCollection,SalableProductCollectionItem, SalableProductCollectionDao,SalableProductCollectionItemDao,SalableProductCollectionItemBusiness> implements SalableProductCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public SalableProductCollectionBusinessImpl(SalableProductCollectionDao dao) {
		super(dao); 
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTION;
	}
	
	@Override
	public SalableProductCollection instanciateOne() {
		return super.instanciateOne().addCascadeOperationToMasterFieldNames(SalableProductCollection.FIELD_PROPERTIES);
	}
	
	@Override
	protected void afterCrud(SalableProductCollection salableProductCollection, Crud crud) {
		super.afterCrud(salableProductCollection, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(salableProductCollection.getItems().getSynchonizationEnabled())){
				/*Cost cost = new Cost();
				Collection<Cost> costs = MethodHelper.getInstance().callGet(salableProductCollection.getItems().getElements(), Cost.class, SalableProductCollectionItem.FIELD_COST);
				inject(CostBusiness.class).add(cost, costs);
				throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("costvalue")
				.setDomainNameIdentifier("salableProductCollection").setNumber1(salableProductCollection.getCost().getValue())
				.setNumber2(cost.getValue()).setEqual(Boolean.FALSE), BusinessException.class);
				*/
				
				// sum of items cost must be equal to cost
				Collection<SalableProductCollectionItem> salableProductCollectionItems = inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection);
				Cost cost = new Cost();
				Collection<Cost> costs = MethodHelper.getInstance().callGet(salableProductCollectionItems, Cost.class, SalableProductCollectionItem.FIELD_COST);
				inject(CostBusiness.class).add(cost, costs);
				
				throw__(new ConditionHelper.Condition.Builder.Comparison.Adapter.Default().setValueNameIdentifier("costvalue")
						.setDomainNameIdentifier("salableProductCollection").setValue1(salableProductCollection.getCost().getValue())
						.setValue2(cost.getValue()).setEqual(Boolean.FALSE));
			}
		}
	}
	
	/*@Override
	protected void afterCrud(SalableProductCollection salableProductCollection, Crud crud) {
		super.afterCrud(salableProductCollection, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(salableProductCollection.isItemAggregationApplied()){
				computeCost(salableProductCollection);
				BigDecimal v1 = salableProductCollection.getCost().getValue();
				Cost cost =inject(SalableProductCollectionItemDao.class).sumCostBySalableProductCollection(salableProductCollection);
				exceptionUtils().exception(v1.compareTo(commonUtils.getValueIfNotNullElseDefault(cost.getValue(),BigDecimal.ZERO))!=0
						, v1+"costdoesnotmatch"+commonUtils.getValueIfNotNullElseDefault(cost.getValue(),BigDecimal.ZERO)); 	
				dao.update(salableProductCollection);
			}
		}
	}*/
	
	@Override
	public SalableProductCollection instanciateOne(String code,String name) {
		SalableProductCollection salableProductCollection = super.instanciateOne(code,name);
		return salableProductCollection;
	}
	
	@Override
	public SalableProductCollection instanciateOne(String code,String name,Cost cost,Object[][] salableProducts) {
		SalableProductCollection salableProductCollection = instanciateOne(code,name);
		salableProductCollection.getCost()._set(cost);
		if(salableProducts!=null)
			for(Object[] salableProduct : salableProducts){
				inject(SalableProductCollectionItemBusiness.class)
					.instanciateOne(salableProductCollection, inject(SalableProductDao.class).read((String)salableProduct[0])
						, commonUtils.getBigDecimal(salableProduct[1].toString()), new BigDecimal(commonUtils.getValueAt(salableProduct, 2, "0").toString()) , BigDecimal.ZERO);
			}
		//computeCost(salableProductCollection);
		return salableProductCollection;
	}
	
	@Override
	public SalableProductCollection instanciateOne(String code,Object[][] salableProducts) {
		return instanciateOne(code,code,null,salableProducts);
	}
	
	@Override
	protected SalableProductCollectionItem addOrRemove(SalableProductCollection salableProductCollection, SalableProductCollectionItem salableProductCollectionItem,Boolean add) {
		BigDecimal factor = (add == null || Boolean.TRUE.equals(add)) ? BigDecimal.ONE : BigDecimal.ONE.negate();
		super.addOrRemove(salableProductCollection, salableProductCollectionItem, add);
		NumberHelper numberHelper = NumberHelper.getInstance();
		inject(CostBusiness.class).add(salableProductCollection.getCost()
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductCollectionItem.getCost().getValue(),factor))
				, BigDecimal.ONE.multiply(factor)
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductCollectionItem.getCost().getTax(),factor))
				, numberHelper.get(BigDecimal.class, numberHelper.multiply(salableProductCollectionItem.getCost().getTurnover(),factor)));
		/*commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_VALUE, salableProductCollectionItem.getCost().getValue().multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_TAX, salableProductCollectionItem.getCost().getTax().multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_TURNOVER, salableProductCollectionItem.getCost().getTurnover().multiply(factor));*/		
		return salableProductCollectionItem;
	}
	
	/*@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductCollection salableProductCollection,Collection<SalableProductCollectionItem> salableProductCollectionItems) {
		LogMessage.Builder logMessageBuilder = new LogMessage.Builder("Compute", "cost");
		addLogMessageBuilderParameters(logMessageBuilder,"items", salableProductCollectionItems);
		Cost cost = new Cost();
		for(SalableProductCollectionItem salableProductCollectionItem : salableProductCollectionItems){
			inject(SalableProductCollectionItemBusiness.class).computeCost(salableProductCollectionItem);
			inject(CostBusiness.class).add(cost, salableProductCollectionItem.getCost());
		}
		setCost(salableProductCollection, cost,Boolean.FALSE,logMessageBuilder);
		logTrace(logMessageBuilder);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductCollection salableProductCollection) {
		LogMessage.Builder logMessageBuilder = new LogMessage.Builder("Compute", "cost");
		Cost cost = inject(SalableProductCollectionItemDao.class).sumCostBySalableProductCollection(salableProductCollection);
		setCost(salableProductCollection,cost,Boolean.TRUE,logMessageBuilder);
		logTrace(logMessageBuilder);
	}
	
	private void setCost(SalableProductCollection salableProductCollection,Cost cost,Boolean database,LogMessage.Builder logMessageBuilder){
		addLogMessageBuilderParameters(logMessageBuilder,"salable product collection",salableProductCollection.getCode(),"database",database,"cost",cost
				,"Aggregation applied",salableProductCollection.isItemAggregationApplied());
		if(salableProductCollection.isItemAggregationApplied()){
			addLogMessageBuilderParameters(logMessageBuilder, "current cost",cost);
			salableProductCollection.getCost()._set(cost);
		}
		addLogMessageBuilderParameters(logMessageBuilder, "new cost",salableProductCollection.getCost());
	}*/
	
	@Override
	public SalableProductCollection instanciateOneRandomly(String code) {
		SalableProductCollection salableProductCollection = super.instanciateOneRandomly(code);
		salableProductCollection.getCost().setValue(new BigDecimal("100"));
		return salableProductCollection;
	}
	
	@Override
	protected void computeChanges(final SalableProductCollection salableProductCollection, Builder logMessageBuilder) {
		super.computeChanges(salableProductCollection, logMessageBuilder);
		logMessageBuilder.addManyParameters("cost");
		logMessageBuilder.addNamedParameters("before",salableProductCollection.getCost().toString());
		if(Boolean.TRUE.equals(salableProductCollection.getItems().isSynchonizationEnabled())){
			salableProductCollection.getCost().setValue(BigDecimal.ZERO);
			salableProductCollection.getCost().setTax(BigDecimal.ZERO);
			salableProductCollection.getCost().setTurnover(BigDecimal.ZERO);
			salableProductCollection.getCost().setNumberOfProceedElements(BigDecimal.ZERO);
			new CollectionHelper.Iterator.Adapter.Default<SalableProductCollectionItem>(salableProductCollection.getItems().getElements()){
				private static final long serialVersionUID = 1L;
				protected void __executeForEach__(SalableProductCollectionItem salableProductCollectionItem) {
					inject(CostBusiness.class).add(salableProductCollection.getCost(), salableProductCollectionItem.getCost());
				}
			}.execute();	
		}
		logMessageBuilder.addNamedParameters("after",salableProductCollection.getCost().toString());
	}
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<SalableProductCollection>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<SalableProductCollection> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					
				}
			}
		}
		
	}
	
}
