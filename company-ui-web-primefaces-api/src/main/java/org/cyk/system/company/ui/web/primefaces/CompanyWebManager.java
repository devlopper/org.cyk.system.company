package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.CustomerFormModel;
import org.cyk.system.company.ui.web.primefaces.model.EmployeeFormModel;
import org.cyk.system.company.ui.web.primefaces.model.ProductCollectionFormModel;
import org.cyk.system.root.business.api.BusinessAdapter;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.IconType;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.api.model.party.ActorConsultFormModel;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter.SecuredUrlProvider;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyWebManager.DEPLOYMENT_ORDER) @Getter
public class CompanyWebManager extends AbstractPrimefacesManager implements Serializable {

	public static final int DEPLOYMENT_ORDER = CompanyBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = 7231721191071228908L;

	private static CompanyWebManager INSTANCE;
	
	private final String requestParameterBalanceType = "bt";
	private final String requestParameterNegativeBalance = BalanceType.NEGAITVE.name();
	private final String requestParameterZeroBalance = BalanceType.ZERO.name();
	private final String requestParameterPositiveBalance = BalanceType.POSITIVE.name();
	private final String requestParameterPaymentType = "pt";
	private final String requestParameterPay = "pay";
	private final String requestParameterPayback = "payback";
	
	private final String outcomeEditSaleDeliveryDetails = "saleDeliveryDetailsView";
	private final String outcomeStockDashBoard = "stockDashBoardView";
	private final String outcomeTangibleProductStockMovementList = "tangibleProductStockMovementList";
	private final String outcomeSaleDashBoard = "saleDashBoardView";
	private final String outcomeCustomerBalance = "customerBalanceView";
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private ProductionSpreadSheetBusiness productionBusiness;
		@Inject private ProductionSpreadSheetTemplateBusiness productionPlanModelBusiness;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();  
		identifier = "company";
		languageBusiness.registerResourceBundle("org.cyk.system.company.ui.resources.ui", getClass().getClassLoader());
		
		businessClassConfig(Customer.class,CustomerFormModel.class);
		businessClassConfig(Employee.class,EmployeeFormModel.class);
		//businessClassConfig(TangibleProductStockMovement.class,TangibleProductStockMovementFormModel.class);
		
		businessClassConfig(ProductCollection.class,ProductCollectionFormModel.class,null);
		businessEntityInfos(ProductCollection.class).setUiListViewId(null);
		
		businessEntityInfos(TangibleProductInventory.class).setUiListViewId("tangibleProductInventoryListView");
		businessEntityInfos(TangibleProductInventory.class).setUiConsultViewId("tangibleProductInventoryConsultView");
		
		businessEntityInfos(TangibleProductStockMovement.class).setUiEditViewId("tangibleProductStockMovementCrudManyView");
		businessEntityInfos(TangibleProductStockMovement.class).setUiListViewId(outcomeTangibleProductStockMovementList);
		
		UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Employee.class, ActorConsultFormModel.class);
		UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Customer.class, ActorConsultFormModel.class);
		
		uiManager.getBusinesslisteners().add(new BusinessAdapter(){
			private static final long serialVersionUID = 4605368263736933413L;
			@SuppressWarnings("unchecked")
			@Override
			public <T extends AbstractIdentifiable> Collection<T> find(Class<T> dataClass, Integer first, Integer pageSize, String sortField, Boolean ascendingOrder,
					String filter) {
				if(Customer.class.equals(dataClass)){
					return (Collection<T>) customerBusiness.findAll();
				}else if(Employee.class.equals(dataClass)){
					return (Collection<T>) employeeBusiness.findAll();
				} else if(ProductCollection.class.equals(dataClass)){
					return (Collection<T>) productCollectionBusiness.findAllWithProduct();
				} else if(TangibleProductInventory.class.equals(dataClass)){
					return (Collection<T>) tangibleProductInventoryBusiness.findAll();
				} else if(TangibleProductStockMovement.class.equals(dataClass)){
					return (Collection<T>) tangibleProductStockMovementBusiness.findAll();
				} else if(CashRegister.class.equals(dataClass)){
					return (Collection<T>) cashRegisterBusiness.findAll();
				} else if(ProductionSpreadSheetTemplate.class.equals(dataClass)){
					return (Collection<T>) productionPlanModelBusiness.findAll();
				} else if(ProductionSpreadSheet.class.equals(dataClass)){
					return (Collection<T>) productionBusiness.findAll();
				}               
				return super.find(dataClass, first, pageSize, sortField, ascendingOrder, filter);
			}
			
			@Override
			public <T extends AbstractIdentifiable> Long count(Class<T> dataClass, String filter) {
				if(Customer.class.equals(dataClass)){
					return customerBusiness.countAll();
				}else if(Employee.class.equals(dataClass)){
					return employeeBusiness.countAll();	
				}else if(ProductCollection.class.equals(dataClass)){
					return productCollectionBusiness.countAll();
				}else if(TangibleProductInventory.class.equals(dataClass)){
					return tangibleProductInventoryBusiness.countAll();
				}else if(TangibleProductStockMovement.class.equals(dataClass)){
					return tangibleProductStockMovementBusiness.countAll();
				} else if(CashRegister.class.equals(dataClass)){
					return cashRegisterBusiness.countAll();
				} else if(ProductionSpreadSheetTemplate.class.equals(dataClass)){
					return productionPlanModelBusiness.countAll();
				} else if(ProductionSpreadSheet.class.equals(dataClass)){
					return productionBusiness.countAll();
				} 
				
				return super.count(dataClass, filter);
			}
		});
		
		WebEnvironmentAdapter.SECURED_URL_PROVIDERS.add(new SecuredUrlProvider() {
			
			@Override
			public void provide() {
				roleFolder("__salemanager__", CompanyBusinessLayer.getInstance().getRoleSaleManagerCode());
				roleFolder("__stockmanager__", CompanyBusinessLayer.getInstance().getRoleStockManagerCode());
				roleFolder("__humanresourcesmanager__", CompanyBusinessLayer.getInstance().getRoleHumanResourcesManagerCode());
				roleFolder("__productionmanager__", CompanyBusinessLayer.getInstance().getRoleProductionManagerCode());
			}
		});
	}
		
	public static CompanyWebManager getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SystemMenu systemMenu(AbstractUserSession userSession) {
		Cashier cashier = null;
		if(userSession.getUser() instanceof Person){
			cashier = cashierBusiness.findByPerson((Person) userSession.getUser());
		}
		SystemMenu systemMenu = new SystemMenu();
		UICommandable group = uiProvider.createCommandable("department", null);
		group.addChild(menuManager.crudMany(DivisionType.class, null));	
		group.addChild(menuManager.crudMany(Division.class, null));	
		group.addChild(menuManager.crudMany(Division.class, null));	
		group.addChild("command.ownedcompany", null, "ownedCompanyCrudOne", null);
		systemMenu.getReferenceEntities().add(group);
		
		group = uiProvider.createCommandable("product", null);
		group.addChild(menuManager.crudMany(ProductCategory.class, null));
		group.addChild(menuManager.crudMany(IntangibleProduct.class, null));	
		group.addChild(menuManager.crudMany(TangibleProduct.class, null));	
		group.addChild(menuManager.crudMany(ProductCollection.class, null));
		systemMenu.getReferenceEntities().add(group);
		
		group = uiProvider.createCommandable("sale", null);
		group.addChild(menuManager.crudMany(CashRegister.class, null));	
		systemMenu.getReferenceEntities().add(group);
		
		/**/
		
		addBusinessMenu(systemMenu,humanResourcesManagerCommandables(userSession,systemMenu.getMobileBusinesses())); 
		addBusinessMenu(systemMenu,customerManagerCommandables(userSession,systemMenu.getMobileBusinesses())); 
		addBusinessMenu(systemMenu,saleCommandables(userSession,systemMenu.getMobileBusinesses(), cashier)); 
		addBusinessMenu(systemMenu,stockCommandables(userSession));
		addBusinessMenu(systemMenu,goodsDepositCommandables(userSession, systemMenu.getMobileBusinesses(), cashier));
		
		return systemMenu;
	}
	
	public UICommandable humanResourcesManagerCommandables(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable hr = null;
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleCustomerManagerCode())){
			hr = uiProvider.createCommandable("command.humanresources", null);
			hr.addChild(menuManager.crudMany(Employee.class, null));
			hr.addChild(menuManager.crudMany(Cashier.class, null));
		}
		return hr;
	}
	
	public UICommandable customerManagerCommandables(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable customer = null;
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleCustomerManagerCode())){
			customer = uiProvider.createCommandable("customer", IconType.PERSON);
			customer.addChild(menuManager.crudMany(Customer.class, null));
		}
		return customer;
	}
	
	public UICommandable saleCommandables(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable sale = uiProvider.createCommandable("command.sale", null);
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleSaleManagerCode())){
			UICommandable c;
			if(cashier!=null){
				sale.getChildren().add(c = menuManager.crudOne(Sale.class, IconType.ACTION_ADD));
				c.setLabel(uiManager.text("command.sale.new"));
				
			}
			
			sale.getChildren().add(c = uiProvider.createCommandable("dashboard", null, outcomeSaleDashBoard));
			mobileCommandables.add(c); 
			sale.getChildren().add(uiProvider.createCommandable("command.sale.listall", null, "saleListView"));
			sale.getChildren().add(c = uiProvider.createCommandable("field.credence", null, outcomeCustomerBalance));
			c.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), CompanyBusinessLayer.getInstance().getParameterCustomerBalanceCredence());
			sale.getChildren().add(c = uiProvider.createCommandable("company.command.customer.balance", null, outcomeCustomerBalance));
			c.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), CompanyBusinessLayer.getInstance().getParameterCustomerBalanceAll());
			sale.getChildren().add(uiProvider.createCommandable("command.list", null, "saleStockInputListView"));
			
			sale.getChildren().add(uiProvider.createCommandable("prod", null, "productionConsultView"));
			//sale.getChildren().add(uiProvider.createCommandable("prodPlan", null, "productionPlanModelListView"));
			
			/*
			sale.addChild("command.sale.negativebalance", null, "saleNegativeBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterNegativeBalance)));
			sale.addChild("command.sale.zerobalance", null, "saleZeroBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterZeroBalance)));
			sale.addChild("command.sale.positivebalance", null, "salePositiveBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterPositiveBalance)));
			*/
		}
		
		return sale;
	}
	
	public UICommandable stockCommandables(AbstractUserSession userSession){
		UICommandable stock = uiProvider.createCommandable("command.stock", null);
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleStockManagerCode())){
			stock.getChildren().addAll(stockContextCommandables(userSession));
		}
		return stock;
	}
	
	public Collection<UICommandable> stockContextCommandables(AbstractUserSession userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		commandables.add(uiProvider.createCommandable("dashboard", null, "stockDashBoardView"));
		commandables.add(menuManager.crudMany(TangibleProductStockMovement.class, null));
		commandables.add(uiProvider.createCommandable("command.quantityinuse", null, "tangibleProductQuantityInUseUpdateManyView"));
		commandables.add(menuManager.crudMany(TangibleProductInventory.class, null));
		return commandables;
	}
	
	public Collection<UICommandable> productionContextCommandables(AbstractUserSession userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		//commandables.add(uiProvider.createCommandable("dashboard", null, "productionDashBoardView"));
		commandables.add(menuManager.crudOne(ProductionSpreadSheet.class, null));
		return commandables;
	}
	
	public UICommandable goodsDepositCommandables(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable goods = null;
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleSaleManagerCode())){
			goods = uiProvider.createCommandable("goods", null);
			if(cashier!=null){
				goods.getChildren().add(uiProvider.createCommandable("command.deposit", null, "saleStockInputEditView"));
			}
			goods.getChildren().add(uiProvider.createCommandable("command.list", null, "saleStockInputListView"));	
		}
		return goods;
	}
	
	/**/
		
}
