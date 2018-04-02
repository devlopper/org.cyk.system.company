package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.stock.StockableTangibleProductDetails;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.ProductCollectionFormModel;
import org.cyk.system.company.ui.web.primefaces.stock.StockableTangibleProductEditPage;
import org.cyk.system.root.model.network.UniformResourceLocatorParameter;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.Icon;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.api.config.IdentifiableConfiguration;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.HierarchyNode;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.primefaces.model.TreeNode;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyWebManager.DEPLOYMENT_ORDER) @Getter
public class CompanyWebManager extends AbstractPrimefacesManager implements Serializable {

	private static final long serialVersionUID = 7231721191071228908L;
	private static CompanyWebManager INSTANCE;
	public static final int DEPLOYMENT_ORDER = CompanyBusinessLayer.DEPLOYMENT_ORDER+1;
	
	private final String requestParameterBalanceType = "bt";
	private final String requestParameterNegativeBalance = BalanceType.NEGAITVE.name();
	private final String requestParameterZeroBalance = BalanceType.ZERO.name();
	private final String requestParameterPositiveBalance = BalanceType.POSITIVE.name();
	private final String requestParameterPaymentType = "pt";
	private final String requestParameterPay = "pay";
	private final String requestParameterPayback = "payback";
	
	private final String outcomePrintEmployeeEmploymentContract = "employmentContractPrintView";
	private final String outcomeEditSaleDeliveryDetails = "saleDeliveryDetailsView";
	private final String outcomeStockDashBoard = "stockDashBoardView";
	private final String outcomeTangibleProductStockMovementList = "tangibleProductStockMovementList";
	private final String outcomeSaleDashBoard = "saleDashBoardView";
	private final String outcomeCustomerBalance = "customerBalanceView";
	private final String outcomeCustomerSaleStock = "customerSaleStockView";
	private final String outcomeSaleStockInStock = "saleStockInStockView";
	private final String outcomeSaleStockList = "saleStockListView";
	private final String outcomeSaleStockOutputList = "saleStockOutputListView";
	
	private final String outcomeSalableProductInstanceCashRegisterStateLogList = "salableProductInstanceCashRegisterStateLogListView";
		
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyReportRepository companyReportRepository;
	
	@Override
	protected void initialisation() {
		identifier = "company";
		INSTANCE = this;
		super.initialisation();  
		
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
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(StockableProduct.class, StockableTangibleProductEditPage.Form.class, StockableTangibleProductDetails.class
				,null,null,null));
		uiManager.configBusinessIdentifiable(StockableProduct.class, null);
		//webNavigationManager.useDynamicSelectView(StockableProduct.class);
	}
	
	@Override
	public SystemMenu systemMenu(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		return systemMenu;
	}
	
	public UICommandable getProductCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = null;
		module = Builder.create(uiManager.businessEntityInfos(Product.class).getUserInterface().getLabelId(), null);
		module.addChild(Builder.createList(SalableProduct.class, null));
		return module;
	}
	
	public UICommandable humanResourcesManagerCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable hr = null;
		hr = Builder.create("command.humanresources", null);
		hr.addChild(Builder.createList(Employee.class, null));
		hr.addChild(Builder.createList(Cashier.class, null));
		return hr;
	}
	
	public UICommandable getCustomerCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = null;
		module = Builder.create(uiManager.businessEntityInfos(Customer.class).getUserInterface().getLabelId(), Icon.PERSON);
		module.addChild(Builder.createList(Customer.class, null));
		module.addChild(Builder.createList(CustomerSalableProduct.class, null));
		
		UICommandable c;
		module.addChild(c = Builder.create("field.credence", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceCredence());
		
		module.addChild(c = Builder.create("company.command.customer.balance", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceAll());
		
		return module;
	}
	
	public UICommandable paymentCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable module = Builder.create(uiManager.businessEntityInfos(CashRegisterMovement.class).getUserInterface().getLabelId(), null);
		module.addChild(Builder.createList(CashRegister.class, null));
		module.addChild(Builder.createList(CashRegisterMovement.class, null));
		return module;
	}
	
	public UICommandable getSaleCommandable(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable module = Builder.create(uiManager.businessEntityInfos(Sale.class).getUserInterface().getLabelId(), null);
		module.addChild(Builder.createList(Sale.class, null));
		module.addChild(Builder.createSelectOne(Sale.class,CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementInput() ,null));
		module.addChild(Builder.createSelectOne(Sale.class,CompanyBusinessLayer.getInstance().getActionCreateSaleCashRegisterMovementOutput() ,null));
		return module;
	}
	
	public void reportCommandables(Collection<UICommandable> collection){
		UICommandable c;
		/*collection.add(c = Builder.create("field.credence", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceCredence());
		
		collection.add(c = Builder.create("company.command.customer.balance", null, outcomeCustomerBalance));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportBalance());
		c.addParameter(companyReportRepository.getParameterCustomerBalanceType(), companyReportRepository.getParameterCustomerBalanceAll());
		*/
		collection.add(c = Builder.create("company.command.customer.salestock", null, outcomeCustomerSaleStock));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportSaleStock());
		
		collection.add(c = Builder.create("company.command.salestock.instock", null, outcomeSaleStockInStock));
		c.addParameter(companyReportRepository.getParameterCustomerReportType(), companyReportRepository.getParameterCustomerReportSaleStock());
		
		
	}
	
	public UICommandable stockCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		UICommandable stock = Builder.create("command.stock", null);
		stock.getChildren().addAll(stockContextCommandables(userSession));
		return stock;
	}
	
	public Collection<UICommandable> stockContextCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		commandables.add(Builder.create("dashboard", null, "stockDashBoardView"));
		commandables.add(Builder.createList(StockableProduct.class, null));
		//commandables.add(Builder.createList(StockTangibleProductMovement.class, null));
		//commandables.add(Builder.create("command.quantityinuse", null, "tangibleProductQuantityInUseUpdateManyView"));
		//commandables.add(Builder.createList(TangibleProductInventory.class, null));
		return commandables;
	}
	
	public Collection<UICommandable> productionContextCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession){
		Collection<UICommandable> commandables = new ArrayList<>();
		//commandables.add(Builder.create("dashboard", null, "productionDashBoardView"));
		commandables.add(Builder.createCreate(Production.class));
		return commandables;
	}
	
	public UICommandable goodsDepositCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables,Cashier cashier){
		UICommandable goods = null;
		goods = Builder.create("goods", null);
		goods.getChildren().add(Builder.create("deposits", null, "saleStockInputListView"));
		
		saleStockReportCommandables(userSession, goods.getChildren(), mobileCommandables);
		
		return goods;
	}
	
	public UICommandable productionCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> mobileCommandables){
		UICommandable production = null;
		production = Builder.create("production", null);
		production.getChildren().add(Builder.createList(Reseller.class, null));
		production.getChildren().add(Builder.createList(ResellerProductionPlan.class, null));
		production.getChildren().add(Builder.createList(ProductionUnit.class, null));
		//production.getChildren().add(Builder.createList(Production.class, null));
		
		return production;
	}
	
	public void saleStockReportCommandables(AbstractUserSession<TreeNode,HierarchyNode> userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		UICommandable c;
			
		commandables.add(c = Builder.create("company.report.salestockoutput.cashregister.title", null, outcomeSaleStockOutputList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCashRegister());
		
		commandables.add(c = Builder.create("company.report.salestock.inventory.title", null, outcomeSaleStockList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportInventory());
		
		commandables.add(c = Builder.create("company.report.salestock.customer.title", null, outcomeSaleStockList));
		c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCustomer());
		
	}
	
	/**/
	
	public String javascriptShowPointOfSale(Sale sale){
		String url = webNavigationManager.reportUrl(sale, companyReportRepository.getReportPointOfSale(),UniformResourceLocatorParameter.PDF,Boolean.TRUE);
		return javaScriptHelper.openWindow("pointofsale"+sale.getIdentifier(), url, 400, 550);
	}
	
	public String javascriptShowPointOfSale(SaleCashRegisterMovement saleCashRegisterMovement){
		String url = webNavigationManager.reportUrl(saleCashRegisterMovement, companyReportRepository.getReportPointOfSaleReceipt(),UniformResourceLocatorParameter.PDF,Boolean.TRUE);
		return javaScriptHelper.openWindow("pointofsale"+saleCashRegisterMovement.getIdentifier(), url, 400, 550);
	}
	
	/**/
	
	public static CompanyWebManager getInstance() {
		return INSTANCE;
	}
		
}
