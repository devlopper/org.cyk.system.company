package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.utility.common.LogMessage;

@Stateless
public class SalableProductCollectionBusinessImpl extends AbstractCollectionBusinessImpl<SalableProductCollection,SalableProductCollectionItem, SalableProductCollectionDao,SalableProductCollectionItemDao,SalableProductCollectionItemBusiness> implements SalableProductCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject private SalableProductCollectionItemDao salableProductCollectionItemDao;
	
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
		SalableProductCollection salableProductCollection = super.instanciateOne();
		salableProductCollection.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		return salableProductCollection;
	}
	
	@Override
	public SalableProductCollection instanciateOne(String name) {
		SalableProductCollection salableProductCollection = super.instanciateOne(name);
		salableProductCollection.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		return salableProductCollection;
	}
	
	@Override
	public SalableProductCollection instanciateOne(String code,Object[][] salableProducts) {
		SalableProductCollection salableProductCollection = instanciateOne(code);
		System.out.println("#####################################");
		for(Object[] salableProduct : salableProducts){
			SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class)
					.instanciateOne(salableProductCollection, inject(SalableProductDao.class).read((String)salableProduct[0])
							, commonUtils.getBigDecimal(salableProduct[1].toString()), BigDecimal.ZERO, BigDecimal.ZERO);
			add(salableProductCollection, salableProductCollectionItem);
		}
		return salableProductCollection;
	}
		
	@Override
	protected SalableProductCollectionItemDao getItemDao() {
		return salableProductCollectionItemDao;
	}
	
	@Override
	protected SalableProductCollectionItemBusiness getItemBusiness() { 
		return inject(SalableProductCollectionItemBusiness.class);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SalableProductCollectionItem add(SalableProductCollection salableProductCollection, SalableProductCollectionItem salableProductCollectionItem) {
		return addOrRemove(salableProductCollection, salableProductCollectionItem, Boolean.TRUE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SalableProductCollectionItem remove(SalableProductCollection salableProductCollection,SalableProductCollectionItem salableProductCollectionItem) {
		return addOrRemove(salableProductCollection, salableProductCollectionItem, Boolean.FALSE);
	}
	
	private SalableProductCollectionItem addOrRemove(SalableProductCollection salableProductCollection, SalableProductCollectionItem salableProductCollectionItem,Boolean add) {
		System.out
				.println("SalableProductCollectionBusinessImpl.addOrRemove()");
		String action = Boolean.TRUE.equals(add) ? "ADD":"REMOVE";
		LogMessage.Builder logMessageBuilder = new LogMessage.Builder(action,SalableProductCollectionItem.class);
		logMessageBuilder.setAction("ADD/REMOVE");
		logMessageBuilder.setSubject("S");
		inject(SalableProductCollectionItemBusiness.class).computeCost(salableProductCollectionItem);
		logMessageBuilder.addParameters("salableProductCollection.cost.value",salableProductCollection.getCost().getValue()
				,"salableProductCollectionItem.cost.value",salableProductCollectionItem.getCost().getValue());
		BigDecimal factor;
		if(Boolean.TRUE.equals(add)){
			Boolean found = Boolean.FALSE;
			if(salableProductCollection.getCollection()!=null)
				for(SalableProductCollectionItem index : salableProductCollection.getCollection())
					if(index == salableProductCollectionItem){
						found = Boolean.TRUE;
						break;
					}
			if(Boolean.FALSE.equals(found))
				salableProductCollection.add(salableProductCollectionItem);	
			factor = BigDecimal.ONE;
		}else{
			if(salableProductCollection.getCollection()!=null)
				salableProductCollection.getCollection().remove(salableProductCollectionItem);
			salableProductCollection.addToDelete(salableProductCollectionItem);
			factor = BigDecimal.ONE.negate();
		}
		
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_VALUE, salableProductCollectionItem.getCost().getValue().multiply(factor));
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.multiply(factor));
		logMessageBuilder.addParameters("salableProductCollection.cost.newValue",salableProductCollection.getCost().getValue());
		logTrace(logMessageBuilder.build());
		System.out
				.println("SalableProductCollectionBusinessImpl.addOrRemove() : DONE");
		return salableProductCollectionItem;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductCollection salableProductCollection,Collection<SalableProductCollectionItem> salableProductCollectionItems) {
		logTrace("cost {} with items {}", salableProductCollection,salableProductCollectionItems);
		salableProductCollection.getCost().setValue(BigDecimal.ZERO);
		for(SalableProductCollectionItem salableProductCollectionItem : salableProductCollectionItems){
			inject(SalableProductCollectionItemBusiness.class).computeCost(salableProductCollectionItem);
			commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_VALUE, salableProductCollectionItem.getCost().getValue());
		}
		logTrace("costed {} with items {}", salableProductCollection,salableProductCollectionItems);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeCost(SalableProductCollection salableProductCollection) {
		computeCost(salableProductCollection,inject(SalableProductCollectionItemDao.class).readByCollection(salableProductCollection));
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
