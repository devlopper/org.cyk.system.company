package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import lombok.Getter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.stock.StockableTangibleProductDetails;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.ProductCollectionFormModel;
import org.cyk.system.company.ui.web.primefaces.stock.StockableTangibleProductEditPage;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.IconType;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.api.config.IdentifiableConfiguration;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.HierarchyNode;
import org.primefaces.model.TreeNode;

@Getter
public class AbstractCompanyWebManager extends AbstractPrimefacesManager implements Serializable {

	private static final long serialVersionUID = 7231721191071228908L;
	
	public static final int DEPLOYMENT_ORDER = CompanyBusinessLayer.DEPLOYMENT_ORDER+1;
	
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
	private final String outcomeCustomerSaleStock = "customerSaleStockView";
	private final String outcomeSaleStockInStock = "saleStockInStockView";
	private final String outcomeSaleStockList = "saleStockListView";
	private final String outcomeSaleStockOutputList = "saleStockOutputListView";
		
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyReportRepository companyReportRepository;
	
	@Override
	protected void initialisation() {
		super.initialisation();  
		identifier = "company";
		languageBusiness.registerResourceBundle("org.cyk.system.company.ui.resources.ui", getClass().getClassLoader());
		
		//businessClassConfig(Customer.class,CustomerFormModel.class);
		//businessClassConfig(Employee.class,EmployeeFormModel.class);
		//businessClassConfig(TangibleProductStockMovement.class,TangibleProductStockMovementFormModel.class);
		
		businessClassConfig(ProductCollection.class,ProductCollectionFormModel.class,null);
		businessEntityInfos(ProductCollection.class).getUserInterface().setListViewId(null);
		
		businessEntityInfos(TangibleProductInventory.class).getUserInterface().setListViewId("tangibleProductInventoryListView");
		businessEntityInfos(TangibleProductInventory.class).getUserInterface().setConsultViewId("tangibleProductInventoryConsultView");
		
		businessEntityInfos(StockTangibleProductMovement.class).getUserInterface().setEditViewId("tangibleProductStockMovementCrudManyView");
		businessEntityInfos(StockTangibleProductMovement.class).getUserInterface().setListViewId(outcomeTangibleProductStockMovementList);
		
		//UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Employee.class, ActorConsultFormModel.class);
		//UIManager.DEFAULT_MANY_FORM_MODEL_MAP.put(Customer.class, ActorConsultFormModel.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(StockableTangibleProduct.class, StockableTangibleProductEditPage.Form.class, StockableTangibleProductDetails.class
				,null,null));
		uiManager.configBusinessIdentifiable(StockableTangibleProduct.class, null);
		//webNavigationManager.useDynamicSelectView(StockableTangibleProduct.class);
	}
	
	@Override
	public SystemMenu systemMenu(AbstractUserSession<TreeNode,HierarchyNode> userSession) {
		Cashier cashier = null;
		if(userSession.getUser() instanceof Person){
			cashier = companyBusinessLayer.getCashierBusiness().findByPerson((Person) userSession.getUser());
		}
		SystemMenu systemMenu = new SystemMenu();
		/*
		UICommandable group = uiProvider.createCommandable("department", null);

		group.addChild(menuManager.crudMany(DivisionType.class, null));	
		group.addChild(menuManager.crudMany(Division.class, null));	
		group.addChild(menuManager.crudMany(Division.class, null));	
		group.addChild("command.ownedcompany", null, "ownedCompanyCrudOne", null);
		systemMenu.getReferenceEntities().add(group);
		*/
		/*
		group = uiProvider.createCommandable(uiManager.businessEntityInfos(Product.class).getUserInterface().getLabelId(), null);
		//group.addChild(menuManager.crudMany(ProductCategory.class, null));
		group.addChild(menuManager.crudMany(IntangibleProduct.class, null));	
		group.addChild(menuManager.crudMany(TangibleProduct.class, null));	
		//group.addChild(menuManager.crudMany(ProductCollection.class, null));
		systemMenu.getReferenceEntities().add(group);
		
		group = uiProvider.createCommandable(uiManager.businessEntityInfos(Sale.class).getUserInterface().getLabelId(), null);
		group.addChild(menuManager.crudMany(SalableProduct.class, null));
		group.addChild(menuManager.crudMany(Customer.class, null));
		
		systemMenu.getReferenceEntities().add(group);
		*/
		addBusinessMenu(systemMenu,getProductCommandable(userSession,systemMenu.getMobileBusinesses())); 
		addBusinessMenu(systemMenu,getCustomerCommandable(userSession,systemMenu.getMobileBusinesses())); 
		addBusinessMenu(systemMenu,paymentCommandables(userSession,systemMenu.getMobileBusinesses(), cashier));
		addBusinessMenu(systemMenu,getSaleCommandable(userSession,systemMenu.getMobileBusinesses(), cashier)); 
		
		return systemMenu;
	}
	
	public UICommandable getProductCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = null;
		module = uiProvider.createCommandable(uiManager.businessEntityInfos(Product.class).getUserInterface().getLabelId(), null);
		module.addChild(menuManager.crudMany(TangibleProduct.class, null));
		module.addChild(menuManager.crudMany(IntangibleProduct.class, null));
		module.addChild(menuManager.crudMany(SalableProduct.class, null));
		return module;
	}
	
	public UICommandable humanResourcesManagerCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable hr = null;
		hr = uiProvider.createCommandable("command.humanresources", null);
		hr.addChild(menuManager.crudMany(Employee.class, null));
		hr.addChild(menuManager.crudMany(Cashier.class, null));
		return hr;
	}
	
	public UICommandable getCustomerCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = null;
		module = uiProvider.createCommandable(uiManager.businessEntityInfos(Customer.class).getUserInterface().getLabelId(), IconType.PERSON);
		module.addChild(menuManager.crudMany(Customer.class, null));
		
		UICommandable c;
		module.addChild(c = uiProvider.createCommandable("field.credence", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceCredence());
		
		module.addChild(c = uiProvider.createCommandable("company.command.customer.balance", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceAll());
		
		return module;
	}
	
	public UICommandable paymentCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable module = uiProvider.createCommandable(uiManager.businessEntityInfos(CashRegisterMovement.class).getUserInterface().getLabelId(), null);
		module.addChild(menuManager.crudMany(CashRegister.class, null));
		module.addChild(menuManager.crudMany(CashRegisterMovement.class, null));
		return module;
	}
	
	public UICommandable getSaleCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable module = uiProvider.createCommandable(uiManager.businessEntityInfos(Sale.class).getUserInterface().getLabelId(), null);
		module.addChild(menuManager.crudMany(Sale.class, null));
		module.addChild(menuManager.createSelectOne(Sale.class,CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementInput() ,null));
		module.addChild(menuManager.createSelectOne(Sale.class,CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementOutput() ,null));
		return module;
	}
	
	public void reportCommandables(Collection<UICommandable> collection){
		UICommandable c;
		/*collection.add(c = uiProvider.createCommandable("field.credence", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceCredence());
		
		collection.add(c = uiProvider.createCommandable("company.command.customer.balance", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceAll());
		*/
		collection.add(c = uiProvider.createCommandable("company.command.customer.salestock", null, outcomeCustomerSaleStock));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportSaleStock());
		
		collection.add(c = uiProvider.createCommandable("company.command.salestock.instock", null, outcomeSaleStockInStock));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportSaleStock());
		
		
	}
	
	public UICommandable stockCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		UICommandable stock = uiProvider.createCommandable("command.stock", null);
		stock.getChildren().addAll(stockContextCommandables(userSession));
		return stock;
	}
	
	public Collection<UICommandable> stockContextCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		commandables.add(uiProvider.createCommandable("dashboard", null, "stockDashBoardView"));
		commandables.add(menuManager.crudMany(StockableTangibleProduct.class, null));
		//commandables.add(menuManager.crudMany(StockTangibleProductMovement.class, null));
		//commandables.add(uiProvider.createCommandable("command.quantityinuse", null, "tangibleProductQuantityInUseUpdateManyView"));
		//commandables.add(menuManager.crudMany(TangibleProductInventory.class, null));
		return commandables;
	}
	
	public Collection<UICommandable> productionContextCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		//commandables.add(uiProvider.createCommandable("dashboard", null, "productionDashBoardView"));
		commandables.add(menuManager.crudOne(Production.class, null));
		return commandables;
	}
	
	public UICommandable goodsDepositCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable goods = null;
		goods = uiProvider.createCommandable("goods", null);
		goods.getChildren().add(uiProvider.createCommandable("deposits", null, "saleStockInputListView"));
		
		saleStockReportCommandables(userSession, goods.getChildren(), mobileCommandables);
		
		return goods;
	}
	
	public UICommandable productionCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable production = null;
		production = uiProvider.createCommandable("production", null);
		production.getChildren().add(menuManager.crudMany(Reseller.class, null));
		production.getChildren().add(menuManager.crudMany(ResellerProductionPlan.class, null));
		production.getChildren().add(menuManager.crudMany(ProductionUnit.class, null));
		//production.getChildren().add(menuManager.crudMany(Production.class, null));
		
		return production;
	}
	
	public void saleStockReportCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		UICommandable c;
			
		commandables.add(c = uiProvider.createCommandable("company.report.salestockoutput.cashregister.title", null, outcomeSaleStockOutputList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCashRegister());
		
		commandables.add(c = uiProvider.createCommandable("company.report.salestock.inventory.title", null, outcomeSaleStockList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportInventory());
		
		commandables.add(c = uiProvider.createCommandable("company.report.salestock.customer.title", null, outcomeSaleStockList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCustomer());
		
	}
	
	/**/
	
	public String javascriptShowPointOfSale(Sale sale){
		String url = webNavigationManager.reportUrl(sale, companyReportRepository.getReportPointOfSale(),uiManager.getPdfParameter(),Boolean.TRUE);
		return javaScriptHelper.openWindow("pointofsale"+sale.getIdentifier(), url, 400, 550);
	}
	
	public String javascriptShowPointOfSale(SaleCashRegisterMovement saleCashRegisterMovement){
		String url = webNavigationManager.reportUrl(saleCashRegisterMovement, companyReportRepository.getReportPointOfSaleReceipt(),uiManager.getPdfParameter(),Boolean.TRUE);
		return javaScriptHelper.openWindow("pointofsale"+saleCashRegisterMovement.getIdentifier(), url, 400, 550);
	}
	
	/**/
	
	
		
}
