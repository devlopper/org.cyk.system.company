package org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.model.sale.ProFormaInvoice;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.company.model.structure.Vehicle;
import org.cyk.system.root.business.api.security.BusinessServiceCollectionBusiness;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = super.build(userSession);
		addBusinessMenu(userSession,systemMenu,getProductCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getPaymentCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getSaleCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getCompanyCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getCompanyCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable(Company.class, null);
		module.setLabel(inject(BusinessServiceCollectionBusiness.class).find(CompanyConstant.BUSINESS_SERVICE_COLLECTION_COMPANY).getName());
		//module.addChild(createListCommandable(Company.class, null));
		module.addChild(createListCommandable(EmploymentAgreementType.class, null));
		module.addChild(createListCommandable(EmploymentAgreement.class, null));
		module.addChild(createListCommandable(Employee.class, null));
		
		return module;
	}
	
	public Commandable getEmployeeCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("command.employee.management", null);
		module.addChild(createListCommandable(Employee.class, null));
		addReportCommandables(Employee.class,module, EmployeeBusinessImpl.Listener.COLLECTION);
		return module;
	}
	
	public Commandable getProductCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable(Product.class, null);
		module.setLabel(inject(BusinessServiceCollectionBusiness.class).find(CompanyConstant.BUSINESS_SERVICE_COLLECTION_PRODUCT).getName());
		module.addChild(createListCommandable(TangibleProduct.class, null));
		module.addChild(createListCommandable(IntangibleProduct.class, null));
		/*module.addChild(createListCommandable(ProductCategory.class, null));
		module.addChild(createListCommandable(ProductCollection.class, null));
		module.addChild(createListCommandable(ProductCollectionItem.class, null));*/
		//module.addChild(createListCommandable(TangibleProductInstance.class, null));
		/*module.addChild(createListCommandable(TangibleProductInventory.class, null));*/
		return module;
	}
	
	public Commandable getPaymentCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("payment", null);
		module.setLabel(inject(BusinessServiceCollectionBusiness.class).find(CompanyConstant.BUSINESS_SERVICE_COLLECTION_PAYMENT).getName());
		module.addChild(createListCommandable(CashRegister.class, null));
		module.addChild(createListCommandable(Cashier.class, null));
		module.addChild(createListCommandable(CashRegisterMovementMode.class, null));
		module.addChild(createListCommandable(CashRegisterMovement.class, null));
		module.addChild(createListCommandable(CashRegisterMovementTermCollection.class, null));
		module.addChild(createListCommandable(CashRegisterMovementTerm.class, null));
		return module;
	}
	
	public Commandable getSaleCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable(Sale.class, null);
		module.setLabel(inject(BusinessServiceCollectionBusiness.class).find(CompanyConstant.BUSINESS_SERVICE_COLLECTION_SALE).getName());
		module.addChild(createListCommandable(SalableProduct.class, null));
		module.addChild(createListCommandable(SalableProductCollection.class, null));
		module.addChild(createListCommandable(SalableProductCollectionItem.class, null));
		//module.addChild(createListCommandable(SalableProductInstance.class, null));
		
		module.addChild(createListCommandable(Customer.class, null));
		module.addChild(createListCommandable(CustomerSalableProduct.class, null));
		return module;
	}
	
	public Commandable getServiceCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("command.service.management", null);
		module.addChild(createListCommandable(Vehicle.class, null));
		
		return module;
	}
	
	public Commandable getFinanceCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("command.finance.management", null);
		module.addChild(createListCommandable(Sale.class, null));
		module.addChild(createListCommandable(SaleCashRegisterMovement.class, null));
		module.addChild(createListCommandable(ProFormaInvoice.class, null));
		
		module.addChild(createListCommandable(Customer.class, null));
		
		return module;
	}
	
	/**/
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}
}
