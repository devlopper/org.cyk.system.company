package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.generator.RandomDataProvider;

@Getter @Setter @Named @ViewScoped //TOFIX to be moved on GUI projects
public class SampleDataPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 3459311493291130244L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	
	private RandomDataProvider randomDataProvider = RandomDataProvider.getInstance();
	private List<Product> products;
	
	private Data data = new Data();
	private FormOneData<Data> form;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		form = (FormOneData<Data>) createFormOneData(data, Crud.CREATE);
		form.setDynamic(Boolean.TRUE);
		form.getSubmitCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = -4051260187568895766L;
			@Override
			public void serve(UICommand command, Object parameter) {
				createSales();
			}
		});
	}
	
	public void createSales(){
		if(products==null)
			products = new ArrayList<Product>(productBusiness.findAll());
		for(int i=0;i<data.getNumberOfSales();i++){
			Sale sale = new Sale();
			sale.setAccountingPeriod(accountingPeriodBusiness.findCurrent());
			sale.setCashier(cashierBusiness.find().one());
			sale.setDate(randomDataProvider.randomDate(sale.getAccountingPeriod().getPeriod().getFromDate(), sale.getAccountingPeriod().getPeriod().getToDate()));
			for(int pc=0;pc<RandomDataProvider.getInstance().randomInt(1, 10);pc++)
				saleBusiness.selectProduct(sale, (Product)randomDataProvider.randomFromList(products), new BigDecimal(randomDataProvider.randomInt(1, 10)));
				
			SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,new CashRegisterMovement(sale.getCashier().getCashRegister()));
			saleCashRegisterMovement.setAmountIn(sale.getCost());
			saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
			//debug(sale);
			saleBusiness.create(sale, saleCashRegisterMovement);
		}
	}
	
	@Getter @Setter
	public class Data implements Serializable{
		private static final long serialVersionUID = 7484994571511058040L;
		@Input @InputNumber
		private Integer numberOfSales=1;
	}
	
}
