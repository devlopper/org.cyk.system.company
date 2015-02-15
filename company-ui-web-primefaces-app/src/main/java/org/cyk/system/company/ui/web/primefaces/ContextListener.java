package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.api.service.CustomerBusiness;
import org.cyk.system.company.business.api.service.ServiceCollectionBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.service.Customer;
import org.cyk.system.company.model.service.Invoice;
import org.cyk.system.company.model.service.Payment;
import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.CustomerFormModel;
import org.cyk.system.company.ui.web.primefaces.model.EmployeeFormModel;
import org.cyk.system.root.business.api.BusinessAdapter;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.MenuManager;
import org.cyk.ui.api.MenuManager.Type;
import org.cyk.ui.api.UserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UIMenu;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.AbstractContextListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ServiceCollectionBusiness serviceCollectionBusiness;
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		businessClassConfig(Customer.class,CustomerFormModel.class);
		businessClassConfig(Employee.class,EmployeeFormModel.class);
		//businessClassConfig(ServiceCollection.class,ServiceCollectionFormModel.class);
		
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
				} else if(ServiceCollection.class.equals(dataClass)){
					return (Collection<T>) serviceCollectionBusiness.findAllWithService();
				} 
				return super.find(dataClass, first, pageSize, sortField, ascendingOrder, filter);
			}
			
			@Override
			public <T extends AbstractIdentifiable> Long count(Class<T> dataClass, String filter) {
				if(Customer.class.equals(dataClass)){
					return customerBusiness.countAll();
				}else if(Customer.class.equals(dataClass)){
					return employeeBusiness.countAll();
				}else if(ServiceCollection.class.equals(dataClass)){
					return serviceCollectionBusiness.countAll();
				}
				
				return super.count(dataClass, filter);
			}
		});
		
	}
	
	@Override
	public void menu(UserSession session, UIMenu menu, Type type) {
		switch(type){
		case APPLICATION:
			UICommandable commandable;
			
			menu.addCommandable(MenuManager.crudMenu(Employee.class));
			menu.addCommandable(MenuManager.crudMenu(Customer.class));
			/*
			commandable = menu.addCommandable("command.customer", null);
			commandable.getChildren().add(p=MenuManager.getInstance().crudOne(Customer.class, null));
			p.setLabel(UIManager.getInstance().text("command.item.add"));
			commandable.getChildren().add(p=MenuManager.getInstance().crudMany(Customer.class, null));
			p.setLabel(UIManager.getInstance().text("command.list"));
			
			commandable = menu.addCommandable("command.client", null);
			commandable.getChildren().add(p=MenuManager.getInstance().crudOne(Customer.class, null));
			p.setLabel(UIManager.getInstance().text("command.item.add"));
			commandable.getChildren().add(p=MenuManager.getInstance().crudMany(Customer.class, null));
			p.setLabel(UIManager.getInstance().text("command.list"));
			*/
			//commandable.addChild("command.list", null, "",null);
			/*commandable.addChild("command.search", null, "",null);
			*/
			commandable = menu.addCommandable("command.invoice", null);
			commandable.addChild("command.invoice.new", null, "newinvoice",WebNavigationManager.getInstance().crudOneParameters(Invoice.class));
			commandable.addChild("command.payment.do", null, "newpayment",WebNavigationManager.getInstance().crudOneParameters(Payment.class));
			
			//commandable.getChildren().add(p=MenuManager.getInstance().crudOne(Invoice.class, null));
			//p.setLabel(UIManager.getInstance().text("command.item.new"));
			
			//commandable.getChildren().add(p=MenuManager.getInstance().crudOne(Payment.class, null));
						
			commandable = menu.addCommandable("command.service", null);
			//commandable.getChildren().add(p=MenuManager.crudMany(Service.class, null));
			//p.getParameters().add(new Parameter(webManager.getRequestParameterFormModel(), CompanyWebManager.getInstance().getFormModelServiceUnit()));
			//p.setLabel(UIManager.getInstance().text("command.service.unit"));
			
			commandable.getChildren().add(MenuManager.crudMany(Service.class, null));
			commandable.getChildren().add(MenuManager.crudMany(ServiceCollection.class, null));
			
			//commandable.addChild("command.service.unit", null, "serviceunitmany",null);
			//commandable.addChild("command.service.package", null, "servicepackagemany",null);
			
			
			break;
		default:break;
		}	
	}
	
}
