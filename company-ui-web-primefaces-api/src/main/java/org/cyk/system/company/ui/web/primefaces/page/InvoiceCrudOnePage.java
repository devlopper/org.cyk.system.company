package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.service.CustomerBusiness;
import org.cyk.system.company.business.api.service.ServiceBusiness;
import org.cyk.system.company.business.api.service.ServiceCollectionBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.service.Customer;
import org.cyk.system.company.model.service.Invoice;
import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.company.model.service.ServiceDelivery;
import org.cyk.system.company.model.service.ServiceDeliveryEmployee;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.crud.CrudOnePage;

@Named @ViewScoped @Getter @Setter
public class InvoiceCrudOnePage extends CrudOnePage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private ServiceBusiness serviceBusiness;
	@Inject private ServiceCollectionBusiness serviceCollectionBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private CustomerBusiness customerBusiness;
	
	private List<Service> services;
	private List<ServiceCollection> serviceCollections;
	private List<Customer> customers;
	private List<Employee> employees;

	private Service selectedService;
	private ServiceCollection selectedServiceCollection;
	private Customer selectedCustomer;
	
	private List<Item> items = new ArrayList<>();
	//private Commandable addItemCommandable,addServiceCommandable,addServiceCollectionCommandable,deleteItemCommandable;
	
	private Boolean showQuantityColumn = Boolean.TRUE;
	private BigDecimal totalPrice = BigDecimal.ZERO;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		services = new ArrayList<Service>(serviceBusiness.findAll());
		serviceCollections = new ArrayList<ServiceCollection>(serviceCollectionBusiness.findAll());
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		employees = new ArrayList<Employee>(employeeBusiness.findAll());
		
		/*
		addItemCommandable = (Commandable) UIProvider.getInstance().createCommandable("command.add", IconType.ACTION_ADD);
		addServiceCommandable = (Commandable) UIProvider.getInstance().createCommandable("command.add", IconType.ACTION_ADD);
		addServiceCollectionCommandable = (Commandable) UIProvider.getInstance().createCommandable("command.add", IconType.ACTION_ADD);
		deleteItemCommandable = (Commandable) UIProvider.getInstance().createCommandable("command.delete", IconType.ACTION_REMOVE);
		
		addItemCommandable.getButton().setType("button");
		addItemCommandable.getButton().setOnclick("PF('serviceDialog').show();");
		*/
	}
	
	/**/
	
	@Override
	public void serve(UICommand command, Object parameter) {
		//super.serve(command, parameter);
		System.out.println("InvoiceCrudOnePage.serve()");
	}
	
	public void addService(){
		items.add(new Item(selectedService));
		updateTotalPrice();
	}
	
	public void addServiceCollection(){
		items.add(new Item(selectedServiceCollection));
		updateTotalPrice();
	}
	
	public void deleteItem(Integer index){
		items.remove(index.intValue());
	}
	
	public List<Service> autoCompleteService(String query){
		return new ArrayList<Service>(serviceBusiness.findAll());
	}
	
	public List<ServiceCollection> autoCompleteServiceCollection(String query){
		return new ArrayList<ServiceCollection>(serviceCollectionBusiness.findAll());
	}
	
	public void updateTotalPrice(){
		totalPrice = BigDecimal.ZERO;
		for(Item item : items)
			totalPrice = totalPrice.add(item.getTotalPrice());
	}
	
	@Getter @Setter
	public class Item implements Serializable{
		
		private static final long serialVersionUID = -19234217552006129L;
		
		private Service service;
		private ServiceCollection serviceCollection; 
		private List<ServiceDeliveryEmployee> serviceDeliveryEmployees = new ArrayList<>();
		
		private String code;
		private String name;
		private BigDecimal unitPrice;
		private Integer quantity;
		private BigDecimal totalPrice;
		
		public Item(Service service) {
			super();
			this.service = service;
			construct(service.getCode(),service.getName(),service.getPrice(),Arrays.asList(service));
		}
		
		public Item(ServiceCollection serviceCollection) {
			super();
			this.serviceCollection = serviceCollection;
			construct(serviceCollection.getCode(),serviceCollection.getName(),serviceCollection.getPrice(),serviceCollection.getCollection());	
		}
		
		private void construct(String code, String name, BigDecimal unitPrice,Collection<Service> services) {
			this.code = code;
			this.name = name;
			this.unitPrice = unitPrice;
			quantity = 1;
			for(Service service : services)
				serviceDeliveryEmployees.add(new ServiceDeliveryEmployee(new ServiceDelivery(service, (Invoice) identifiable),null));
			quantityChanged();
		}

		public void quantityChanged(){
			totalPrice = unitPrice.multiply(new BigDecimal(quantity));
		}

	}

}