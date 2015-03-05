package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.MenuManager;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.UserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.IconType;
import org.cyk.ui.web.api.AbstractWebManager;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER) @Getter
public class CompanyWebManager extends AbstractWebManager implements Serializable {

	private static final long serialVersionUID = 7231721191071228908L;

	private static CompanyWebManager INSTANCE;
	
	private final String requestParameterBalanceType = "bt";
	private final String requestParameterNegativeBalance = BalanceType.NEGAITVE.name();
	private final String requestParameterZeroBalance = BalanceType.ZERO.name();
	private final String requestParameterPositiveBalance = BalanceType.POSITIVE.name();
	private final String requestParameterPaymentType = "pt";
	private final String requestParameterPay = "pay";
	private final String requestParameterPayback = "payback";
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();  
		//lan
	}
		
	public static CompanyWebManager getInstance() {
		return INSTANCE;
	}

	@Override
	public Collection<Class<? extends AbstractIdentifiable>> parameterMenuItemClasses(UserSession userSession) {
		Collection<Class<? extends AbstractIdentifiable>> collection = new ArrayList<>();
		collection.add(DivisionType.class);
		collection.add(Division.class);
		
		collection.add(IntangibleProduct.class);
		collection.add(TangibleProduct.class);
		collection.add(ProductCollection.class);
		
		return collection;
	}

	@Override
	public Collection<UICommandable> businessMenuItems(UserSession userSession) {
		Collection<UICommandable> collection = new ArrayList<>();
		UICommandable c;
		
		UICommandable hr = UIProvider.getInstance().createCommandable("command.humanresources", IconType.PERSON);
		hr.addChild(c = MenuManager.crudMany(Employee.class, null));
		hr.addChild(c = MenuManager.crudOne(Customer.class, null));
		c.setLabel(UIManager.getInstance().text("command.customer.new"));
		hr.addChild(c = MenuManager.crudMany(Customer.class, null));
		hr.addChild(c = MenuManager.crudMany(Cashier.class, null));
		collection.add(hr);
		
		UICommandable sale = UIProvider.getInstance().createCommandable("command.sale", null);
		sale.addChild(c = MenuManager.crudOne(Sale.class, IconType.ACTION_ADD));
		c.setLabel(UIManager.getInstance().text("command.sale.new"));
		sale.addChild("command.sale.listall", null, "saleListView", null);
		sale.addChild("command.sale.negativebalance", null, "saleNegativeBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterNegativeBalance)));
		sale.addChild("command.sale.zerobalance", null, "saleZeroBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterZeroBalance)));
		sale.addChild("command.sale.positivebalance", null, "salePositiveBalanceListView", Arrays.asList(new UICommandable.Parameter(requestParameterBalanceType, requestParameterPositiveBalance)));
		sale.addChild(c = MenuManager.crudMany(CashRegister.class, null));
		collection.add(sale);
		
		UICommandable statistics = UIProvider.getInstance().createCommandable("command.statistics", null);
		statistics.addChild("company.command.statistics.sale.daily", null, "saleStatisticsPeriodly", null);
		/*
		statistics.addChild("company.command.statistics.sale.weekly", null, "saleStatisticsPeriodly", null);
		statistics.addChild("company.command.statistics.sale.monthly", null, "saleStatisticsPeriodly", null);
		statistics.addChild("company.command.statistics.sale.productly", null, "saleStatisticsProductly", null);
		statistics.addChild("company.command.statistics.sale.cashierly", null, "saleStatisticsCashierly", null);
		*/
		collection.add(statistics);
		
		return collection;
	}
	
}
