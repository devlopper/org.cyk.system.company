package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.web.primefaces.AbstractContextListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	/*
	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductCollectionBusiness productCollectionBusiness;
	*/	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		/*
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
		*/
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		
		
	}
	
}
