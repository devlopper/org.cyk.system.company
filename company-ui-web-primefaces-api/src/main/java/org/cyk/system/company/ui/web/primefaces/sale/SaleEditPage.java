package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleEditPage extends AbstractCrudOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	
	private List<SalableProduct> salableProducts,intangibleProducts,tangibleProducts;
	private List<Customer> customers;

	private SalableProduct selectedProduct,selectedIntangibleProduct,selectedTangibleProduct;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showQuantityColumn = Boolean.TRUE;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	
	private ItemCollection<SaleProductItem,SaleProduct> saleProductCollection;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		saleProductCollection = createItemCollection(SaleProductItem.class, SaleProduct.class 
				,new ItemCollectionWebAdapter<SaleProductItem,SaleProduct>(){
			private static final long serialVersionUID = -3872058204105902514L;
			@Override
			public SaleProduct instanciate(AbstractItemCollection<SaleProductItem, SaleProduct, SelectItem> itemCollection) {
				return saleBusiness.selectProduct(identifiable, selectedProduct);
			}
			
			@Override
			public void instanciated(AbstractItemCollection<SaleProductItem, SaleProduct,SelectItem> itemCollection,SaleProductItem item) {
				super.instanciated(itemCollection, item);
				
			}
			
			@Override
			public void read(SaleProductItem item) {
				super.read(item);
				item.setCode(item.getIdentifiable().getSalableProduct().getProduct().getCode());
				item.setName(item.getIdentifiable().getSalableProduct().getProduct().getName());
				item.setUnitPrice(numberBusiness.format(item.getIdentifiable().getSalableProduct().getPrice()));
				item.setQuantity(item.getIdentifiable().getQuantity());
				item.setTotalPrice(numberBusiness.format(item.getIdentifiable().getCost().getValue()));
			}
			
			@Override
			public void delete(AbstractItemCollection<SaleProductItem, SaleProduct, SelectItem> itemCollection,SaleProductItem item) {
				super.delete(itemCollection, item);
				saleBusiness.unselectProduct(identifiable, item.getIdentifiable());
			}
		});
		saleProductCollection.setLabel(null);
		
		identifiable.setAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent());
		identifiable.setCashier(CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson((Person) getUserSession().getUser()));
		if(identifiable.getCashier()==null){
			renderViewErrorMessage("View Init Error!!!", "View Init Error Details!!!");
			return;
		}
		
		salableProducts = new ArrayList<SalableProduct>(CompanyBusinessLayer.getInstance().getSalableProductBusiness().findAll());
		intangibleProducts = new ArrayList<SalableProduct>();
		tangibleProducts = new ArrayList<SalableProduct>();
		for(SalableProduct salableProduct : salableProducts)
			( salableProduct.getProduct() instanceof TangibleProduct ? tangibleProducts : intangibleProducts ).add(salableProduct);	
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		cashRegisterController.init(CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().instanciateOne(identifiable, identifiable.getCashier().getPerson(), Boolean.TRUE),Boolean.TRUE);
		sell();
	}
	
	/**/
	
	@Override
	protected Sale instanciateIdentifiable() {
		return saleBusiness.instanciateOne((Person) getUserSession().getUser());
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		return (T) saleBusiness.instanciate((Person) getUserSession().getUser()); //super.identifiableFromRequestParameter(aClass);
	}*/
	
	@Override
	protected void create() {
		identifiable.setCustomer(selectedCustomer);
		saleBusiness.create(identifiable, cashRegisterController.getSaleCashRegisterMovement());
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		String url = null;
		//url = "http://localhost:8080/company/private/__tools__/report.jsf?clazz=Sale&identifiable=151&fileExtensionParam=pdf&ridp=pos&windowmode=windowmodedialog";
		url = navigationManager.reportUrl(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale(),uiManager.getPdfParameter(),Boolean.TRUE);
		messageDialogOkButtonOnClick += "window.open('"+url+"', 'pointofsale"+identifiable.getIdentifier()+"', 'location=no,menubar=no,titlebar=no,toolbar=no,width=400, height=550');";
		//System.out.println(messageDialogOkButtonOnClick);
		return null;
	}
	
	public void addProduct(Integer type){
		if(type==0)
			saleBusiness.selectProduct(identifiable, selectedProduct);
		else if(type==1)
			saleBusiness.selectProduct(identifiable, selectedIntangibleProduct);
		else if(type==2)
			saleBusiness.selectProduct(identifiable, selectedTangibleProduct);
		//saleProductCollection.add(selectedProduct);
	}
		
	public void cash(){
		collectProduct = Boolean.FALSE;
		((Commandable)saleProductCollection.getAddCommandable()).getButton().setDisabled(Boolean.TRUE);
		collectMoney = Boolean.TRUE;
	}
	
	public void sell(){
		collectProduct = Boolean.TRUE;
		((Commandable)saleProductCollection.getAddCommandable()).getButton().setDisabled(Boolean.FALSE);
		collectMoney = Boolean.FALSE;
	}
		
	public void productQuantityChanged(SaleProductItem saleProductItem){
		saleProductItem.getIdentifiable().setQuantity(saleProductItem.getQuantity());
		saleBusiness.applyChange(identifiable, saleProductItem.getIdentifiable());
		saleProductCollection.read(saleProductItem);	
	}
			
	@Getter @Setter
	public static class SaleProductItem extends AbstractItemCollectionItem<SaleProduct> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private String code,name,unitPrice,totalPrice;
		private BigDecimal quantity;
	}
	
	/**/
	
	public static class Form extends AbstractFormModel<Sale> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
}