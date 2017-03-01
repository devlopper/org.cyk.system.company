package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.BalanceFormModel;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputChoiceAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.cdi.BeanAdapter;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleEditPage extends AbstractSalableProductCollectionEditPage<Sale,SalableProductCollectionItem,SaleEditPage.Item> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initialisation() {
		super.initialisation();
		itemCollection = createItemCollection(Item.class, SalableProductCollectionItem.class,identifiable ,new AbstractSalableProductCollectionEditPage
			.ItemCollectionAdapter.SalableProductCollectionItemAdapter(identifiable.getSalableProductCollection(),crud));
	}
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable.getSalableProductCollection();
	}
	
	/**/
	
	public static class Form extends AbstractDefaultForm<Sale,SalableProductCollectionItem> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputChoiceAutoComplete @InputOneChoice @InputOneAutoComplete private Customer customer;
		@IncludeInputs(layout=IncludeInputs.Layout.VERTICAL) private BalanceFormModel balance = new BalanceFormModel();
		
		@Override
		public void read() {
			super.read();
			balance.set(identifiable.getBalance());
		}
		
		@Override
		public void write() {
			super.write();
			balance.write(identifiable.getBalance());
		}

		@Override
		protected SalableProductCollection getSalableProductCollection() {
			return identifiable.getSalableProductCollection();
		}
		
		public static final String FIELD_CUSTOMER = "customer";
		public static final String FIELD_BALANCE = "balance";
	}
	
	@Getter @Setter
	public static class Item extends AbstractSalableProductCollectionEditPage.AbstractItem<SalableProductCollectionItem> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;

		@Override
		protected SalableProductCollectionItem getSalableProductCollectionItem() {
			return identifiable;
		}
		
	}
	
	@Getter @Setter
	public static class FormOneSaleProduct extends AbstractFormModel<Sale> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputCalendar private Date date;
		@Input @InputText private String externalIdentifier;
		@Input @InputChoice @InputOneChoice @InputOneCombo private Cashier cashier;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private SalableProduct salableProduct;
		@Input @InputChoice(load=false) @InputOneChoice @InputOneCombo private SalableProductInstance salableProductInstance;
		@Input @InputChoice @InputOneChoice @InputOneCombo private Customer customer;
		@Input @InputNumber private BigDecimal cost;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo private CashRegisterMovementMode cashRegisterMovementMode;
		@Input @InputText private String supportingDocumentIdentifier;
		/*
		private SaleProduct saleProduct;
		private SaleProductInstance saleProductInstance;
		
		public void setSalableProduct(SalableProduct salableProduct){
			saleProduct = inject(SaleBusiness.class).selectProduct(identifiable, salableProduct);
		}
		
		@Override
		public void read() {
			super.read();
			if(identifiable.getSaleProducts().isEmpty()){
				
			}else{
				saleProduct = identifiable.getSaleProducts().iterator().next();
			}
		}
		*/
		@Override
		public void write() {
			super.write();
			//saleProduct.setSalableProduct(salableProduct);
			//saleProductInstance.setSalableProductInstance(salableProductInstance);
		}
		
		public static Boolean SHOW_DATE = Boolean.FALSE;
		
		public static final String FIELD_DATE = "date";
		public static final String FIELD_EXTERNAL_IDENTIFIER = "externalIdentifier";
		public static final String FIELD_CASHIER = "cashier";
		public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
		public static final String FIELD_SALABLE_PRODUCT_INSTANCE = "salableProductInstance";
		public static final String FIELD_CUSTOMER = "customer";
		public static final String FIELD_COST = "cost";
		public static final String FIELD_CASH_REGISTER_MOVEMENT_MODE = "cashRegisterMovementMode";
		public static final String FIELD_SUPPORTING_DOCUMENT_IDENTIFIER = "supportingDocumentIdentifier";
	}
	
	public static interface Listener {
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		Collection<SalableProductInstance> getSalableProductInstances(SalableProduct salableProduct,CashRegister cashRegister);
		BigDecimal getCost(Sale identifiable);
		BigDecimal getAmountIn(Sale sale);
		void processSaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement);
		
		public static class Adapter extends BeanAdapter implements Listener {
			private static final long serialVersionUID = -2307751181350506061L;

			@Override
			public Collection<SalableProductInstance> getSalableProductInstances(SalableProduct salableProduct,CashRegister cashRegister) {
				return null;
			}
			@Override
			public BigDecimal getAmountIn(Sale sale) {
				return null;
			}
			@Override
			public BigDecimal getCost(Sale sale) {
				return null;
			}
			@Override
			public void processSaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement) {}
			/**/
			
			public static class Default extends Adapter implements Serializable {
				private static final long serialVersionUID = 8303179056469661724L;
				
				@Override
				public Collection<SalableProductInstance> getSalableProductInstances(SalableProduct salableProduct,CashRegister cashRegister) {
					return inject(SalableProductInstanceBusiness.class).findByCollection(salableProduct);
				}
				@Override
				public BigDecimal getCost(Sale sale) {
					return null;//sale.getSaleProducts().iterator().next().getSalableProduct().getPrice();
				}
				@Override
				public BigDecimal getAmountIn(Sale sale) {
					return getCost(sale);
				}
				
			}
			
		}
	}

	
}