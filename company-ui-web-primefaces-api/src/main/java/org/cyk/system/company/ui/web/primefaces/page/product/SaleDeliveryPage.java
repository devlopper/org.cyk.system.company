package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductEmployeeBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleDeliveryPage extends AbstractBusinessEntityFormOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 261926313336131365L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductEmployeeBusiness productEmployeeBusiness;
	
	private List<Assignment> assignments = new ArrayList<>();
	private List<SelectItem> employeesItems = new ArrayList<>();
	private List<Product> products;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle += " - "+identifiable.getComputedIdentifier()+" - "+text("command.edit.executiondetails");
		form.setDynamic(Boolean.TRUE);
		for(ProductEmployee productEmployee : productEmployeeBusiness.findPerformersBySale(identifiable))
			assignments.add(new Assignment(productEmployee));
		//employeesItems = webManager.buildSelectItems(Employee.class); 
		products = new ArrayList<>(productBusiness.findToDelivery(identifiable));
		
	}
	
	@Override
	public void serve(UICommand command, Object parameter) {
		Collection<ProductEmployee> productEmployees = new ArrayList<>();
		for(Assignment assignment : assignments)
			productEmployees.addAll(assignment.productEmployees());
		
		saleBusiness.updateDelivery(identifiable,productEmployees);
	}
	
	public void addProductEmployee(Integer index){
		assignments.add(new Assignment(new ProductEmployee(products.remove(index.intValue()), null)));
	}
	
	public void deleteProductEmployee(Integer index){
		products.add(assignments.remove(index.intValue()).getProductEmployee().getProduct());
		//debug(assignments.get(index.intValue()).getProductEmployee());
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(Sale.class);
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.UPDATE;
	}
	
	/**/
	
	@Getter @Setter
	public static class Assignment implements Serializable{
		private static final long serialVersionUID = -3392409169091400768L;
		
		private ProductEmployee productEmployee;
		private List<ProductEmployee> productEmployees;
		
		public Assignment(ProductEmployee productEmployee) {
			super();
			this.productEmployee = productEmployee;
		}
		
		public Collection<ProductEmployee> productEmployees(){
			if(productEmployee==null)
				return productEmployees;
			return Arrays.asList(productEmployee);
		}
	}
	
}
