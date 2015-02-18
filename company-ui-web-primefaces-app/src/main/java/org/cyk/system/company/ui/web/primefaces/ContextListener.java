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
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.model.CustomerFormModel;
import org.cyk.system.company.ui.web.primefaces.model.EmployeeFormModel;
import org.cyk.system.company.ui.web.primefaces.model.ProductCollectionFormModel;
import org.cyk.system.root.business.api.BusinessAdapter;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.MenuManager;
import org.cyk.ui.api.MenuManager.Type;
import org.cyk.ui.api.UserSession;
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
		switch(type){
		case APPLICATION:
			
			menu.addCommandable(MenuManager.crudMenu(Employee.class));
			menu.addCommandable(MenuManager.crudMenu(Customer.class));
			menu.addCommandable(MenuManager.crudMenu(ProductCollection.class));
			menu.addCommandable(MenuManager.crudMenu(Sale.class));
			menu.addCommandable(MenuManager.crudMenu(Payment.class));
			
			//commandable = menu.addCommandable("command.sale", null);
			//commandable.addChild("command.product.collection.new", null, "crudoneproductcollection",WebNavigationManager.getInstance().crudOneParameters(ProductCollection.class));
			//commandable.addChild("command.sale.new", null, "crudoneinvoice",WebNavigationManager.getInstance().crudOneParameters(Sale.class));
			//commandable.addChild("command.payment.new", null, "crudonepayment",WebNavigationManager.getInstance().crudOneParameters(Payment.class));
						
			//commandable = menu.addCommandable("command.service", null);
			
			//commandable.getChildren().add(MenuManager.crudMany(Product.class, null));
			//commandable.getChildren().add(MenuManager.crudMany(ProductCollection.class, null));
			break;
		default:break;
		}	
	}
	
}
