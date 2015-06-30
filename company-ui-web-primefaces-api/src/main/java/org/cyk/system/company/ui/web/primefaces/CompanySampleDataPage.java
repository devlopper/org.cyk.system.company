package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyRandomDataProvider;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.generator.RandomDataProvider;

@Getter @Setter @Named @ViewScoped //TOFIX to be moved on GUI projects
public class CompanySampleDataPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 3459311493291130244L;

	@Inject private RootRandomDataProvider rootRandomDataProvider;
    @Inject private CompanyRandomDataProvider companyRandomDataProvider;
	
	private RootBusinessLayer rootBusinessLayer = RootBusinessLayer.getInstance();
	private RandomDataProvider randomDataProvider = RandomDataProvider.getInstance();
	private List<Product> products;
	private List<TangibleProduct> tangibleProducts;
	
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
				run();
			}
		});
	}
	
	public void run(){
		switch(data.getAction()){
		case CREATE_CUSTOMERS:rootRandomDataProvider.createActor(Customer.class, data.getNumberOfCustomers());break;
		case CREATE_EMPLOYEES:rootRandomDataProvider.createActor(Employee.class, data.getNumberOfEmployees());break;
		case CREATE_CASHIERS:/*createCashiers();*/break;
		case CREATE_SALES:companyRandomDataProvider.createSale(data.getNumberOfSales());break;
		}
	}
	
	@Getter @Setter
	public class Data implements Serializable{
		private static final long serialVersionUID = 7484994571511058040L;
		@Input @InputNumber private Integer numberOfCustomers=0;
		@Input @InputNumber private Integer numberOfEmployees=0;
		@Input @InputNumber private Integer numberOfSales=0;
		@Input @InputNumber private Integer numberOfTangibleProductStockMovement=0;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull
		private Action action;
		
	}
	
	
	
}
