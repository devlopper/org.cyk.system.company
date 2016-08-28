package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;

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
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;

@Stateless
public class SalableProductCollectionBusinessImpl extends AbstractCollectionBusinessImpl<SalableProductCollection,SalableProductCollectionItem, SalableProductCollectionDao,SalableProductCollectionItemDao,SalableProductCollectionItemBusiness> implements SalableProductCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject private SalableProductCollectionItemDao salableProductCollectionItemDao;
	
	@Inject
	public SalableProductCollectionBusinessImpl(SalableProductCollectionDao dao) {
		super(dao); 
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
	protected SalableProductCollectionItemDao getItemDao() {
		return salableProductCollectionItemDao;
	}
	@Override
	protected SalableProductCollectionItemBusiness getItemBusiness() { 
		return inject(SalableProductCollectionItemBusiness.class);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SalableProductCollectionItem add(SalableProductCollection salableProductCollection, SalableProductCollectionItem salableProductCollectionItem) {
		inject(SalableProductCollectionItemBusiness.class).process(salableProductCollectionItem);
		Boolean found = Boolean.FALSE;
		if(salableProductCollection.getCollection()!=null)
			for(SalableProductCollectionItem index : salableProductCollection.getCollection())
				if(index == salableProductCollectionItem){
					found = Boolean.TRUE;
					break;
				}
		if(Boolean.FALSE.equals(found))
			salableProductCollection.add(salableProductCollectionItem);
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_VALUE, salableProductCollectionItem.getCost().getValue());
		commonUtils.increment(BigDecimal.class, salableProductCollection.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE);
		logIdentifiable("Selected", salableProductCollectionItem);
		return salableProductCollectionItem;
	}
	/*
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SalableProductCollectionItem selectProduct(Sale sale, SalableProduct salableProduct) {
		return selectProduct(sale, salableProduct, BigDecimal.ONE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void unselectProduct(Sale sale, SalableProductCollectionItem saleProduct) {
		if(sale.getSaleProducts() instanceof List<?>){
			List<SaleProduct> list = (List<SaleProduct>) sale.getSaleProducts();
			for(int i=0;i<list.size();){
				if(list.get(i)==saleProduct){
					list.remove(i);
					break;
				}else
					i++;
			}
		}
		//sale.getCost().setValue(sale.getCost().getValue().subtract(saleProduct.getCost().getValue()));
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_VALUE, saleProduct.getCost().getValue().negate());
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.negate());
		logIdentifiable("Unselected", saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void applyChange(Sale sale, SalableProductCollectionItem saleProduct) {
		inject(SaleProductBusiness.class).process(saleProduct);
		updateCost(sale);
		logIdentifiable("Change applied", saleProduct);
	}
	
	private void updateCost(Sale sale){
		sale.getCost().setValue(BigDecimal.ZERO);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		}
	}
	*/

}
