package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.data.collector.form.FormOneData;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.FieldOverrides;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanButton;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSalableProductCollectionEditPage<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable,TYPE extends AbstractSalableProductCollectionEditPage.AbstractItem<ITEM>> extends AbstractCollectionEditPage<COLLECTION,ITEM,TYPE> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean SHOW_QUANTITY_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_UNIT_PRICE_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_INSTANCE_COLUMN = Boolean.TRUE;
	
	protected SaleConfiguration saleConfiguration;
	//protected List<SelectItem> salableProducts;
	
	protected Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showUnitPriceColumn=SHOW_UNIT_PRICE_COLUMN,showQuantityColumn = SHOW_QUANTITY_COLUMN
			,showInstanceColumn = SHOW_INSTANCE_COLUMN,showProductTable=Boolean.TRUE;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		//salableProducts = webManager.getSelectItems(SalableProduct.class, inject(SalableProductBusiness.class).findAll(),Boolean.FALSE);
		getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		itemCollection.getInputChoice().setIsAutomaticallyRemoveSelected(Boolean.TRUE);
		//itemCollection.setChoices(salableProducts);
		//System.out.println("AbstractSalableProductCollectionEditPage.afterInitialisation() : "+itemCollection.getInputChoice().getUserDeviceType());
		
	}
	
	protected abstract SalableProductCollection getSalableProductCollection();
	
	@Override
	protected SalableProductCollection getCollection() {
		return getSalableProductCollection();
	}
	
	private void itemPropertyChanged(TYPE item){
		inject(SalableProductCollectionBusiness.class).computeCost(getSalableProductCollection(),getSalableProductCollection().getItems().getCollection());
		itemCollection.read(item);
		updateFormCost(form,getSalableProductCollection());
	}
	
	private static void updateFormCost(FormOneData<?, ?, ?, ?, ?, ?> form,SalableProductCollection salableProductCollection){
		form.setDataFieldValue(CostFormModel.FIELD_VALUE,CommonUtils.getInstance().attributePath(AbstractDefaultForm.FIELD_COST,CostFormModel.FIELD_VALUE)
				,AbstractDefaultForm.FIELD_COST, salableProductCollection.getCost());
	}
	
	public void productQuantityChanged(TYPE item){
		SalableProductCollectionItem salableProductCollectionItem = item.getSalableProductCollectionItem();
		salableProductCollectionItem.setQuantity(item.getQuantity());
		itemPropertyChanged(item);		
	}
	
	public void productReductionChanged(TYPE item){
		SalableProductCollectionItem salableProductCollectionItem = item.getSalableProductCollectionItem();
		salableProductCollectionItem.setReduction(item.getReduction());
		itemPropertyChanged(item);		
	}
	
	
	@Getter @Setter
	public static abstract class AbstractgetSalableProductCollectionForm<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable> extends AbstractCollectionEditPage.AbstractForm<COLLECTION,ITEM> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		protected abstract SalableProductCollection getSalableProductCollection();
		
		@Override
		protected AbstractCollection<?> getCollection() {
			return getSalableProductCollection();
		}
		
		/**/
		
		@Getter @Setter
		public static abstract class Default<COLLECTION extends AbstractCollection<ITEM>,ITEM extends AbstractCollectionItem<COLLECTION>> extends AbstractgetSalableProductCollectionForm<COLLECTION,ITEM> implements Serializable{
			private static final long serialVersionUID = -4741435164709063863L;
			
		}
		
	}
	
	@Getter @Setter
	@FieldOverrides(value = {
			@FieldOverride(name=AbstractForm.FIELD_ONE_ITEM_MASTER_SELECTED,type=SalableProduct.class)
			})
	public static abstract class AbstractDefaultForm<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractCollectionItem<?>> extends AbstractForm<COLLECTION,ITEM> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;

		@Input @InputChoice @InputOneChoice @InputOneCombo protected AccountingPeriod accountingPeriod;
		@IncludeInputs(layout=IncludeInputs.Layout.VERTICAL) protected CostFormModel cost = new CostFormModel();
		@Input @InputBooleanButton protected Boolean autoComputeValueAddedTax = Boolean.TRUE;
		
		//@Input @InputChoice @InputOneChoice @InputOneCombo protected SalableProduct salableProductChoice;
		
		protected abstract SalableProductCollection getSalableProductCollection();
		
		@Override
		protected AbstractCollection<?> getCollection() {
			return getSalableProductCollection();
		}
		
		@Override
		public void read() {
			super.read();
			accountingPeriod = getSalableProductCollection().getAccountingPeriod();
			cost.set(getSalableProductCollection().getCost());
			autoComputeValueAddedTax = getSalableProductCollection().getAutoComputeValueAddedTax();
		}
		
		@Override
		public void write() {
			super.write();
			getSalableProductCollection().setAccountingPeriod(accountingPeriod);
			getSalableProductCollection().setAutoComputeValueAddedTax(autoComputeValueAddedTax);
			cost.write(getSalableProductCollection().getCost(),getSalableProductCollection().getAccountingPeriod(),autoComputeValueAddedTax);
		}
		
		public static final String FIELD_ACCOUNTINGPERIOD = "accountingPeriod";
		public static final String FIELD_COST = "cost";
		public static final String FIELD_AUTO_COMPUTE_VALUE_ADDED_TAX = "autoComputeValueAddedTax";
		//public static final String FIELD_SALABLE_PRODUCT_CHOICE = "salableProductChoice";
	}

	@Getter @Setter
	public static abstract class AbstractItem<ITEM extends AbstractIdentifiable> extends AbstractItemCollectionItem<ITEM> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		
		protected String code,name,unitPrice,quantifiedPrice,totalPrice;
		protected BigDecimal quantity,reduction,commission;
		protected SalableProductInstance instance;
		 
		protected List<SalableProductInstance> instanceChoices;
		
		protected abstract SalableProductCollectionItem getSalableProductCollectionItem();
		
		
	}
	
	/**/
	
	public static class ItemCollection<TYPE extends AbstractItemCollectionItem<IDENTIFIABLE>,IDENTIFIABLE extends AbstractIdentifiable,COLLECTION extends AbstractIdentifiable> 
		extends org.cyk.ui.web.primefaces.ItemCollection<TYPE, IDENTIFIABLE,COLLECTION> {

		private static final long serialVersionUID = 1L;

		public ItemCollection(String identifier, Class<TYPE> itemClass, Class<IDENTIFIABLE> identifiableClass, COLLECTION collection,Crud crud) {
			super(identifier, itemClass, identifiableClass,collection,crud);
		}
		
		@Override
		protected AbstractIdentifiable getMasterSelected(IDENTIFIABLE identifiable) {
			return ((SalableProductCollectionItem)identifiable).getSalableProduct();
		}
		
	}
	
	/**/
	
	public static class ItemCollectionAdapter<TYPE extends AbstractItemCollectionItem<IDENTIFIABLE>,IDENTIFIABLE extends AbstractIdentifiable,COLLECTION extends AbstractIdentifiable> extends ItemCollectionWebAdapter<TYPE,IDENTIFIABLE,COLLECTION> implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public ItemCollectionAdapter(COLLECTION collection, Crud crud) {
			super(collection,crud);
		}
		
		public static class SalableProductCollectionItemAdapter<TYPE extends AbstractItem<SalableProductCollectionItem>> extends ItemCollectionAdapter<TYPE,SalableProductCollectionItem,SalableProductCollection> implements Serializable{
			private static final long serialVersionUID = 1L;

			public SalableProductCollectionItemAdapter(SalableProductCollection salableProductCollection, Crud crud) {
				super(salableProductCollection, crud);
			}
			
			@Override
			public Collection<SalableProductCollectionItem> load() {
				getCollection().getItems().setCollection(inject(SalableProductCollectionItemBusiness.class).findByCollection(getCollection()));
				return getCollection().getItems().getCollection();
			}
			
			@Override
			public SalableProductCollectionItem instanciate(AbstractItemCollection<TYPE, SalableProductCollectionItem,SalableProductCollection, SelectItem> itemCollection) {
				SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class)
						.instanciateOne(collection, (SalableProduct) itemCollection.getInputChoice().getValue(), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO);
				salableProductCollectionItem.setSalableProduct((SalableProduct) itemCollection.getInputChoice().getValue());
				updateFormCost(itemCollection.getContainerForm(),collection);
				return salableProductCollectionItem;
			}
			
			@Override
			public void delete(AbstractItemCollection<TYPE, SalableProductCollectionItem,SalableProductCollection, SelectItem> itemCollection,TYPE item) {
				super.delete(itemCollection, item);
				inject(SalableProductCollectionBusiness.class).remove(collection, item.getIdentifiable());
				updateFormCost(itemCollection.getContainerForm(),collection);
			}
			
			@Override
			public Boolean isShowAddButton() {
				return Boolean.TRUE;
			}
			
			@Override
			public void read(TYPE item) {
				super.read(item);
				item.setCode(item.getIdentifiable().getSalableProduct().getProduct().getCode());
				item.setName(item.getIdentifiable().getSalableProduct().getProduct().getName());
				item.setUnitPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getSalableProduct().getPrice()));
				item.setQuantity(item.getIdentifiable().getQuantity());
				item.setQuantifiedPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getQuantifiedPrice()));
				item.setReduction(item.getIdentifiable().getReduction()==null?null:new BigDecimal(item.getIdentifiable().getReduction().intValue()));
				item.setTotalPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getCost().getValue()));
				//item.setInstanceChoices(new ArrayList<>(inject(SalableProductInstanceBusiness.class).findByCollection(item.getIdentifiable().getSalableProduct())));
			}
		}
	}
}
