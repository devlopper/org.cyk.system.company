package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanButton;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionEditPage extends AbstractSalableProductCollectionEditPage<SalableProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static Boolean SHOW_QUANTITY_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_UNIT_PRICE_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_INSTANCE_COLUMN = Boolean.TRUE;
	
	private SaleConfiguration saleConfiguration;
	private List<SelectItem> salableProducts;
	private List<Customer> customers;

	private Cashier selectedCashier;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showUnitPriceColumn=SHOW_UNIT_PRICE_COLUMN,showQuantityColumn = SHOW_QUANTITY_COLUMN
			,showInstanceColumn = SHOW_INSTANCE_COLUMN,showProductTable=Boolean.TRUE;
	
	private SalableProduct selectedSalableProduct;
	private ItemCollection<Item,SalableProductCollectionItem> salableCollection;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		salableCollection = createItemCollection(Item.class, SalableProductCollectionItem.class ,new ItemCollectionWebAdapter<Item,SalableProductCollectionItem>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public SalableProductCollectionItem instanciate(AbstractItemCollection<Item, SalableProductCollectionItem, SelectItem> itemCollection) {
				SalableProductCollectionItem salableProductCollectionItem = new SalableProductCollectionItem();
				salableProductCollectionItem.setSalableProduct(selectedSalableProduct);
				salableProductCollectionItem.setQuantity(BigDecimal.ONE);
				salableProductCollectionItem.setCollection(identifiable);
				return inject(SalableProductCollectionBusiness.class).add(identifiable, salableProductCollectionItem);
			}
			
			@Override
			public void delete(AbstractItemCollection<Item, SalableProductCollectionItem, SelectItem> itemCollection,Item item) {
				super.delete(itemCollection, item);
				inject(SalableProductCollectionBusiness.class).remove(identifiable, item.getIdentifiable());
			}
			
			@Override
			public void instanciated(AbstractItemCollection<Item, SalableProductCollectionItem,SelectItem> itemCollection,Item item) {
				super.instanciated(itemCollection, item);
			}
			@Override
			public Crud getCrud() {
				return crud;
			}
			@Override
			public Boolean isShowAddButton() {
				return Boolean.TRUE;
			}
			
			@Override
			public void read(Item item) {
				super.read(item);
				item.setCode(item.getIdentifiable().getSalableProduct().getProduct().getCode());
				item.setName(item.getIdentifiable().getSalableProduct().getProduct().getName());
				item.setUnitPrice(numberBusiness.format(item.getIdentifiable().getSalableProduct().getPrice()));
				item.setQuantity(item.getIdentifiable().getQuantity());
				item.setTotalPrice(numberBusiness.format(item.getIdentifiable().getCost().getValue()));
				item.setInstanceChoices(new ArrayList<>(inject(SalableProductInstanceBusiness.class).findByCollection(item.getIdentifiable().getSalableProduct())));
			}
			
			@Override
			public void write(Item item) {
				super.write(item);
				SaleProductInstance saleProductInstance = new SaleProductInstance();
				saleProductInstance.setSalableProductInstance(item.getInstance());
				//saleProductInstance.setSaleProduct(item.getIdentifiable());
				saleProductInstance.getProcessing().setParty(identifiable.getProcessing().getParty());
				item.getIdentifiable().getInstances().add(saleProductInstance);
			}
			
			
		});
		salableCollection.setLabel(text(uiManager.businessEntityInfos(SalableProductCollectionItem.class).getUserInterface().getLabelId()));
		salableCollection.setShowAddCommandableAtBottom(Boolean.FALSE);
		
		identifiable.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		customers = new ArrayList<Customer>(inject(CustomerBusiness.class).findAll());
		
		/*if(Boolean.TRUE.equals(saleConfiguration.getAllowOnlySalableProductInstanceOfCashRegister())){
			
		}else{*/
			salableProducts = webManager.getSelectItems(SalableProduct.class, inject(SalableProductBusiness.class).findAll());
			//salableProducts = new ArrayList<SalableProduct>(inject(SalableProductBusiness.class).findAll());
			//intangibleProducts = new ArrayList<SalableProduct>();
			//tangibleProducts = new ArrayList<SalableProduct>();
			//for(SalableProduct salableProduct : salableProducts)
			//	( salableProduct.getProduct() instanceof TangibleProduct ? tangibleProducts : intangibleProducts ).add(salableProduct);		
		//}
		
		/*if(Boolean.TRUE.equals(getIsFormOneSaleProduct())){
			form.getControlSetListeners().add(new ControlSetAdapter<Object>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean build(Object data,Field field) {
					if(FormOneSaleProduct.FIELD_DATE.equals(field.getName()) && FormOneSaleProduct.SHOW_DATE!=null)
						return FormOneSaleProduct.SHOW_DATE;
					return super.build(data,field);
				}
			});
		}*/
	}
	
	public void productQuantityChanged(Item item){
		item.getIdentifiable().setQuantity(item.getQuantity());
		inject(SalableProductCollectionBusiness.class).computeCost(identifiable);
		salableCollection.read(item);	
	}
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable;
	}
	
	@Getter @Setter
	public static class Form extends AbstractDefaultForm<SalableProductCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo private AccountingPeriod accountingPeriod;
		
		@IncludeInputs(layout=IncludeInputs.Layout.VERTICAL) private CostFormModel cost = new CostFormModel();
		
		@Input @InputBooleanButton private Boolean autoComputeValueAddedTax = Boolean.TRUE;
		
		@Override
		public void read() {
			super.read();
			cost.set(identifiable.getCost());
		}
		
		@Override
		public void write() {
			super.write();
			cost.write(identifiable.getCost(),autoComputeValueAddedTax);
		}
		
		public static final String FIELD_ACCOUNTINGPERIOD = "accountingPeriod";
		public static final String FIELD_COST = "cost";
		public static final String FIELD_AUTO_COMPUTE_VALUE_ADDED_TAX = "autoComputeValueAddedTax";
		
	}
	
	/**/
	
	@Getter @Setter
	public static class Item extends AbstractItemCollectionItem<SalableProductCollectionItem> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		
		private String code,name,unitPrice,totalPrice;
		private BigDecimal quantity;
		private SalableProductInstance instance;
		
		private List<SalableProductInstance> instanceChoices;
		
	}

}
