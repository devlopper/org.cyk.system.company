package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;
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
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
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
import org.cyk.ui.api.model.ActorConsultFormModel;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter.SecuredUrlProvider;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER) @Getter
public class CompanyWebManager extends AbstractPrimefacesManager implements Serializable {

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
	private final String outcomeSaleDashBoard = "saleDashBoardView";
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private CashierBusiness cashierBusiness;
	
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
		
		businessEntityInfos(TangibleProductInventory.class).setUiListViewId(null);
		businessEntityInfos(TangibleProductInventory.class).setUiConsultViewId("tangibleProductInventoryEditView");
		
		businessEntityInfos(TangibleProductStockMovement.class).setUiEditViewId(null);
		businessEntityInfos(TangibleProductStockMovement.class).setUiListViewId(null);
		
		UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Employee.class, ActorConsultFormModel.class);
		UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Customer.class, ActorConsultFormModel.class);
		//UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Actor.class, ActorConsultFormModel.class);
		
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
		SystemMenu systemMenu = new SystemMenu();UICommandable c;
		UICommandable group = uiProvider.createCommandable("department", null);
		group.addChild(menuManager.crudMany(DivisionType.class, null));	
		group.addChild(menuManager.crudMany(Division.class, null));	
		systemMenu.getReferenceEntities().add(group);
		
		group = uiProvider.createCommandable("product", null);
		group.addChild(menuManager.crudMany(IntangibleProduct.class, null));	
		group.addChild(menuManager.crudMany(TangibleProduct.class, null));	
		group.addChild(menuManager.crudMany(ProductCollection.class, null));
		systemMenu.getReferenceEntities().add(group);
		
		group = uiProvider.createCommandable("sale", null);
		group.addChild(menuManager.crudMany(CashRegister.class, null));	
		systemMenu.getReferenceEntities().add(group);
		
		/**/
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleHumanResourcesManagerCode())){
			UICommandable hr = uiProvider.createCommandable("command.humanresources", IconType.PERSON);
			hr.addChild(c = menuManager.crudMany(Employee.class, null));
			//hr.addChild(c = menuManager.crudOne(Customer.class, null));
			//c.setLabel(uiManager.text("command.customer.new"));
			hr.addChild(c = menuManager.crudMany(Customer.class, null));
			hr.addChild(c = menuManager.crudMany(Cashier.class, null));
			systemMenu.getBusinesses().add(hr);
		}
		
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleSaleManagerCode())){
			UICommandable sale = uiProvider.createCommandable("command.sale", null);
			if(cashier!=null){
				sale.addChild(c = menuManager.crudOne(Sale.class, IconType.ACTION_ADD));
				c.setLabel(uiManager.text("command.sale.new"));
			}
			UICommandable dashboard = sale.addChild("dashboard", null, outcomeSaleDashBoard, null);
			sale.addChild("command.sale.listall", null, "saleListView", null);
			sale.addChild("command.sale.negativebalance", null, "saleNegativeBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterNegativeBalance)));
			sale.addChild("command.sale.zerobalance", null, "saleZeroBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterZeroBalance)));
			sale.addChild("command.sale.positivebalance", null, "salePositiveBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterPositiveBalance)));
			systemMenu.getBusinesses().add(sale); 
			
			systemMenu.getMobileBusinesses().add(dashboard); 
		}
		
		if(userSession.hasRole(CompanyBusinessLayer.getInstance().getRoleStockManagerCode())){
			UICommandable stock = uiProvider.createCommandable("command.stock", null);
			//stock.addChild("command.provision", null, "tangibleProductStockInView", null);
			//stock.addChild(menuManager.crudMany(TangibleProductStockMovement.class, null));
			stock.addChild("dashboard", null, "stockDashBoardView", null);
			stock.addChild("model.entity.tangibleProductStockMovement", null, "tangibleProductStockMovementCrudManyView", null);
			stock.addChild("command.quantityinuse", null, "tangibleProductQuantityInUseUpdateManyView", null);
			//stock.addChild("command.inventory", null, "tangibleProductInventoryView", null);
			stock.addChild(menuManager.crudMany(TangibleProductInventory.class, null));
			systemMenu.getBusinesses().add(stock);
		}
			
		return systemMenu;
	}

	
}
