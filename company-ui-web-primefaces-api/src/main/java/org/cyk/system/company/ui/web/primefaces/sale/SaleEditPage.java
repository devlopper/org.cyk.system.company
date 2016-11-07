package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
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
import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
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

@Named @ViewScoped @Getter @Setter
public class SaleEditPage extends AbstractSalableProductCollectionEditPage<Sale,SalableProductCollectionItem> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static Class<? extends AbstractFormModel<?>> FORM_EDIT_CLASS = Form.class;
	public static Boolean SHOW_QUANTITY_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_UNIT_PRICE_COLUMN = Boolean.TRUE;
	public static Boolean SHOW_INSTANCE_COLUMN = Boolean.TRUE;
	
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	
	private SaleConfiguration saleConfiguration;
	private List<SelectItem> salableProducts,intangibleProducts,tangibleProducts;
	private List<Customer> customers;
	private List<SelectItem> cashiers;

	private SalableProduct selectedProduct,selectedIntangibleProduct,selectedTangibleProduct;
	private Cashier selectedCashier;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showUnitPriceColumn=SHOW_UNIT_PRICE_COLUMN,showQuantityColumn = SHOW_QUANTITY_COLUMN
			,showInstanceColumn = SHOW_INSTANCE_COLUMN,showProductTable=Boolean.TRUE;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	/*
	private ItemCollection<SaleProductItem,SaleProduct> saleProductCollection;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		saleConfiguration = inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration();
		saleProductCollection = createItemCollection(SaleProductItem.class, SaleProduct.class 
				,new ItemCollectionWebAdapter<SaleProductItem,SaleProduct>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public SaleProduct instanciate(AbstractItemCollection<SaleProductItem, SaleProduct, SelectItem> itemCollection) {
				return inject(SaleBusiness.class).selectProduct(identifiable, selectedProduct);
			}
			
			@Override
			public void instanciated(AbstractItemCollection<SaleProductItem, SaleProduct,SelectItem> itemCollection,SaleProductItem item) {
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
			public Boolean instanciatable(AbstractItemCollection<SaleProductItem, SaleProduct, SelectItem> itemCollection) {
				return saleConfiguration.getMaximalNumberOfProductBySale()==null || identifiable.getSaleProducts().size() < saleConfiguration.getMaximalNumberOfProductBySale();
			}
			@Override
			public void read(SaleProductItem item) {
				super.read(item);
				item.setCode(item.getIdentifiable().getSalableProduct().getProduct().getCode());
				item.setName(item.getIdentifiable().getSalableProduct().getProduct().getName());
				item.setUnitPrice(numberBusiness.format(item.getIdentifiable().getSalableProduct().getPrice()));
				item.setQuantity(item.getIdentifiable().getQuantity());
				item.setTotalPrice(numberBusiness.format(item.getIdentifiable().getCost().getValue()));
				item.setInstanceChoices(new ArrayList<>(inject(SalableProductInstanceBusiness.class).findByCollection(item.getIdentifiable().getSalableProduct())));
			}
			
			@Override
			public void write(SaleProductItem item) {
				super.write(item);
				SaleProductInstance saleProductInstance = new SaleProductInstance();
				saleProductInstance.setSalableProductInstance(item.getInstance());
				saleProductInstance.setSaleProduct(item.getIdentifiable());
				saleProductInstance.getProcessing().setParty(identifiable.getProcessing().getParty());
				item.getIdentifiable().getInstances().add(saleProductInstance);
			}
			
			@Override
			public void delete(AbstractItemCollection<SaleProductItem, SaleProduct, SelectItem> itemCollection,SaleProductItem item) {
				super.delete(itemCollection, item);
				inject(SaleBusiness.class).unselectProduct(identifiable, item.getIdentifiable());
			}
		});
		saleProductCollection.setLabel(text(uiManager.businessEntityInfos(SaleProduct.class).getUserInterface().getLabelId()));
		saleProductCollection.setShowAddCommandableAtBottom(Boolean.FALSE);
	
		identifiable.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		if(roleManager.isAdministrator(Faces.getRequest())){
			
		}else
			identifiable.setCashier(selectedCashier = inject(CashierBusiness.class).findByPerson((Person) getUserSession().getUser()));
		if(identifiable.getCashier()==null && !Boolean.TRUE.equals(roleManager.isAdministrator(Faces.getRequest()))){
			renderViewErrorMessage("View Init Error!!!", "View Init Error Details!!!");
			return;
		}
		
		cashiers = webManager.getSelectItems(Cashier.class,inject(CashierBusiness.class).findAll());
		customers = new ArrayList<Customer>(inject(CustomerBusiness.class).findAll());
		
		if(Boolean.TRUE.equals(saleConfiguration.getAllowOnlySalableProductInstanceOfCashRegister())){
			
		}else{
			salableProducts = webManager.getSelectItems(SalableProduct.class, inject(SalableProductBusiness.class).findAll());
			//salableProducts = new ArrayList<SalableProduct>(inject(SalableProductBusiness.class).findAll());
			//intangibleProducts = new ArrayList<SalableProduct>();
			//tangibleProducts = new ArrayList<SalableProduct>();
			//for(SalableProduct salableProduct : salableProducts)
			//	( salableProduct.getProduct() instanceof TangibleProduct ? tangibleProducts : intangibleProducts ).add(salableProduct);		
		}
		
		cashierChanged(identifiable.getCashier());
		
		if(Boolean.TRUE.equals(getIsFormOneSaleProduct())){
			form.getControlSetListeners().add(new ControlSetAdapter<Object>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean build(Object data,Field field) {
					if(FormOneSaleProduct.FIELD_DATE.equals(field.getName()) && FormOneSaleProduct.SHOW_DATE!=null)
						return FormOneSaleProduct.SHOW_DATE;
					return super.build(data,field);
				}
			});
		}
		//System.out.println(form.get);
		sell();
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		createAjaxBuilder(FormOneSaleProduct.FIELD_CASHIER).updatedFieldNames(FormOneSaleProduct.FIELD_SALABLE_PRODUCT,FormOneSaleProduct.FIELD_SALABLE_PRODUCT_INSTANCE
				,FormOneSaleProduct.FIELD_COST)
		.method(Cashier.class,new ListenValueMethod<Cashier>() {
			@Override
			public void execute(Cashier cashRegister) {
				selectCashier(cashRegister);
			}
		}).build();
		
		createAjaxBuilder(FormOneSaleProduct.FIELD_SALABLE_PRODUCT).crossedFieldNames(FormOneSaleProduct.FIELD_CASHIER).updatedFieldNames(FormOneSaleProduct.FIELD_SALABLE_PRODUCT_INSTANCE,FormOneSaleProduct.FIELD_COST)
		.method(SalableProduct.class,new ListenValueMethod<SalableProduct>() {
			@Override
			public void execute(SalableProduct salableProduct) {
				selectSalableProduct((Cashier) form.findInputByFieldName(FormOneSaleProduct.FIELD_CASHIER).getValue(),salableProduct);
			}
		}).build();
		
		createAjaxBuilder(FormOneSaleProduct.FIELD_SALABLE_PRODUCT_INSTANCE)
		.method(SalableProductInstance.class,new ListenValueMethod<SalableProductInstance>() {
			@Override
			public void execute(SalableProductInstance salableProductInstance) {
				selectSalableProductInstance(salableProductInstance);
			}
		}).build();
	}
	
	private void selectCashier(Cashier cashier){
		Collection<SalableProduct> salableProducts = inject(SalableProductBusiness.class).findByCashRegister(cashier.getCashRegister());
		SalableProduct salableProduct = (SalableProduct) setChoicesAndGetAutoSelected(FormOneSaleProduct.FIELD_SALABLE_PRODUCT,salableProducts);
		if(salableProduct!=null){
			selectSalableProduct(cashier,salableProduct);
		}
	}
	private void selectSalableProduct(final Cashier cashier,final SalableProduct salableProduct){
		if(identifiable.getSaleProducts().isEmpty())
			;
		else
			inject(SaleBusiness.class).unselectProduct(identifiable, identifiable.getSaleProducts().iterator().next());
		inject(SaleBusiness.class).selectProduct(identifiable, salableProduct);
		setFieldValue(FormOneSaleProduct.FIELD_COST, salableProduct.getPrice());
		
		Collection<SalableProductInstance> salableProductInstances = listenerUtils.getCollection(Listener.COLLECTION, new ListenerUtils.CollectionMethod<Listener, SalableProductInstance>() {
			@Override
			public Collection<SalableProductInstance> execute(Listener listener) {
				return listener.getSalableProductInstances(salableProduct,cashier.getCashRegister());
			}
		});
		SalableProductInstance salableProductInstance = (SalableProductInstance) setChoicesAndGetAutoSelected(FormOneSaleProduct.FIELD_SALABLE_PRODUCT_INSTANCE, salableProductInstances);
		if(salableProductInstance!=null)
			selectSalableProductInstance(salableProductInstance);
	}
	
	private void selectSalableProductInstance(SalableProductInstance salableProductInstance){
		identifiable.getSaleProducts().iterator().next().getInstances().clear();
		SaleProductInstance saleProductInstance = new SaleProductInstance();
		saleProductInstance.setSalableProductInstance(salableProductInstance);
		saleProductInstance.setSaleProduct(identifiable.getSaleProducts().iterator().next());
		identifiable.getSaleProducts().iterator().next().getInstances().add(saleProductInstance);
		
		saleProductInstance.getProcessing().setParty(userSession.getUser());
		saleProductInstance.getProcessing().setDate(null);
	}
	
	
	
	@Override
	protected Sale instanciateIdentifiable() {
		return roleManager.isAdministrator(Faces.getRequest()) ? inject(SaleBusiness.class).instanciateOne() : inject(SaleBusiness.class).instanciateOne((Person) getUserSession().getUser());
	}
	
	@Override
	public void transfer(UICommand command, Object object) throws Exception {
		super.transfer(command, object);
		if(form.getSubmitCommandable().getCommand() == command){
			//saleProductCollection.write();
		}
	}
	
	@Override
	protected void create() {
		if(Boolean.TRUE.equals(getIsFormOneSaleProduct())){
			//debug(identifiable);
			//debug(identifiable.getSaleProducts().iterator().next());
			//debug(identifiable.getSaleProducts().iterator().next().getInstances().iterator().next());
			
			//SaleProductInstance saleProductInstance = identifiable.getSaleProducts().iterator().next().getInstances().iterator().next();
			//saleProductInstance.setProcessingUser(userSession.getUser());
			SaleCashRegisterMovement saleCashRegisterMovement = null;
			if(((FormOneSaleProduct)form.getData()).cashRegisterMovementMode==null){
				
			}else{
				identifiable.setDate(((FormOneSaleProduct)form.getData()).date);
				saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(identifiable, identifiable.getCashier().getPerson(), Boolean.TRUE);
				identifiable.getSaleCashRegisterMovements().clear();
				identifiable.getSaleCashRegisterMovements().add(saleCashRegisterMovement);
				saleCashRegisterMovement.getCashRegisterMovement().setMode(((FormOneSaleProduct)form.getData()).cashRegisterMovementMode);
				saleCashRegisterMovement.getCashRegisterMovement().getMovement().setSupportingDocumentIdentifier(((FormOneSaleProduct)form.getData()).supportingDocumentIdentifier);
				
				identifiable.getCost().setValue(listenerUtils.getBigDecimal(Listener.COLLECTION, new ListenerUtils.BigDecimalMethod<Listener>() {
					@Override
					public BigDecimal execute(Listener listener) {
						return listener.getCost(identifiable);
					}
				}));
				
				saleCashRegisterMovement.setAmountIn(listenerUtils.getBigDecimal(Listener.COLLECTION, new ListenerUtils.BigDecimalMethod<Listener>() {
					@Override
					public BigDecimal execute(Listener listener) {
						return listener.getAmountIn(identifiable);
					}
				}));
				inject(SaleCashRegisterMovementBusiness.class).in(saleCashRegisterMovement);
			}
			if(saleCashRegisterMovement!=null){
				final SaleCashRegisterMovement saleCashRegisterMovementFinal = saleCashRegisterMovement;
				listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>() {
					@Override
					public void execute(Listener listener) {
						listener.processSaleCashRegisterMovement(saleCashRegisterMovementFinal);
					}
				});
			}
			inject(SaleBusiness.class).create(identifiable);
		}else{
			identifiable.setCustomer(selectedCustomer);
			identifiable.getSaleCashRegisterMovements().clear();
			identifiable.getSaleCashRegisterMovements().add(cashRegisterController.getSaleCashRegisterMovement());
			inject(SaleBusiness.class).create(identifiable);
		}
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		if( ListenerUtils.getInstance().getBoolean(SaleBusinessImpl.Listener.COLLECTION, new ListenerUtils.BooleanMethod<SaleBusinessImpl.Listener>() {
			@Override
			public Boolean execute(SaleBusinessImpl.Listener listener) {
				return listener.isReportUpdatable(identifiable);
			}
		}) ){
			String url = null;
			url = navigationManager.reportUrl(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale(),UniformResourceLocatorParameter.PDF,Boolean.TRUE);
			messageDialogOkButtonOnClick += "window.open('"+url+"', 'pointofsale"+identifiable.getIdentifier()+"', 'location=no,menubar=no,titlebar=no,toolbar=no,width=400, height=550');";
		}
		return null;
	}
	
	public void addProduct(Integer type){
		if(saleConfiguration.getMaximalNumberOfProductBySale()!=null && identifiable.getSaleProducts().size() < saleConfiguration.getMaximalNumberOfProductBySale()){
			if(type==0)
				inject(SaleBusiness.class).selectProduct(identifiable, selectedProduct);
			else if(type==1)
				inject(SaleBusiness.class).selectProduct(identifiable, selectedIntangibleProduct);
			else if(type==2)
				inject(SaleBusiness.class).selectProduct(identifiable, selectedTangibleProduct);
			//saleProductCollection.add(selectedProduct);
		}
	}
		
	public void cash(){
		collectProduct = Boolean.FALSE;
		((Commandable)saleProductCollection.getAddCommandable()).getButton().setDisabled(Boolean.TRUE);
		collectMoney = Boolean.TRUE;
	}
	
	public void sell(){
		collectProduct = Boolean.TRUE;
		((Commandable)saleProductCollection.getAddCommandable()).getButton().setDisabled(Boolean.FALSE);
		collectMoney = Boolean.FALSE;
	}
	
	public void cashierChangedListener(ValueChangeEvent event){
		cashierChanged((Cashier) event.getNewValue());
	}
	
	public void cashierChanged(Cashier cashier){
		if(cashier==null)
			;
		else{
			identifiable.setCashier(cashier);
			salableProducts = webManager.getSelectItems(SalableProduct.class, Boolean.TRUE.equals(saleConfiguration.getAllowOnlySalableProductInstanceOfCashRegister())
					?inject(SalableProductBusiness.class).findByCashRegister(cashier.getCashRegister()) : inject(SalableProductBusiness.class).findAll());	
			cashRegisterController.init(inject(SaleCashRegisterMovementBusiness.class).instanciateOne(identifiable, cashier.getPerson(), Boolean.TRUE),Boolean.TRUE);
		}
	}
		
	public void productQuantityChanged(SaleProductItem saleProductItem){
		saleProductItem.getIdentifiable().setQuantity(saleProductItem.getQuantity());
		inject(SaleBusiness.class).applyChange(identifiable, saleProductItem.getIdentifiable());
		saleProductCollection.read(saleProductItem);	
	}
	
	public Boolean getIsFormOneSaleProduct(){
		return FormOneSaleProduct.class.equals(FORM_EDIT_CLASS);
	}
	*/
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable.getSalableProductCollection();
	}
	
	@Getter @Setter
	public static class SaleProductItem extends AbstractItemCollectionItem<SalableProductCollectionItem> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private String code,name,unitPrice,totalPrice;
		private BigDecimal quantity;
		private SalableProductInstance instance;
		
		private List<SalableProductInstance> instanceChoices;
		
	}
	
	/**/
	
	public static class Form extends AbstractBusinessIdentifiedEditFormModel<Sale> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputChoiceAutoComplete @InputOneChoice @InputOneAutoComplete private Customer customer;
		@Input @InputChoice @InputChoiceAutoComplete @InputOneChoice @InputOneAutoComplete private SalableProductCollection salableProductCollection;
		
		public static final String FIELD_SALABLE_PRODUCT_COLLECTION = "salableProductCollection";
		public static final String FIELD_CUSTOMER = "customer";
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