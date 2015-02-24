package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.CustomerFormModel;
import org.cyk.system.company.ui.web.primefaces.model.EmployeeFormModel;
import org.cyk.system.company.ui.web.primefaces.model.ProductCollectionFormModel;
import org.cyk.system.root.business.api.BusinessAdapter;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.MenuManager;
import org.cyk.ui.api.MenuManager.Type;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.UserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UIMenu;
import org.cyk.ui.web.primefaces.AbstractContextListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		businessClassConfig(Customer.class,CustomerFormModel.class);
		businessClassConfig(Employee.class,EmployeeFormModel.class);
		businessClassConfig(ProductCollection.class,ProductCollectionFormModel.class,null);
		businessEntityInfos(ProductCollection.class).setUiListViewId(null);
		
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
				}
				
				return super.count(dataClass, filter);
			}
		});
		
	}
	
	@Override
	public void menu(UserSession session, UIMenu menu, Type type) {
		menu.getCommandables().clear();
		switch(type){
		case APPLICATION:
			UICommandable parametersCommandable = UIProvider.getInstance().createCommandable("command.parameters", null);
			for(Class<? extends AbstractIdentifiable> aClass : CompanyWebManager.getInstance().parameterMenuItemClasses(session))
				parametersCommandable.addChild(MenuManager.crudMany(aClass, null));
			
			menu.addCommandable(parametersCommandable);
			
			for(UICommandable commandable : CompanyWebManager.getInstance().businessMenuItems(session))
				menu.addCommandable(commandable);
			
			menu.addCommandable(MenuManager.menuItemUserAccount());
			break;
		default:break;
		}	
	}
	
}
