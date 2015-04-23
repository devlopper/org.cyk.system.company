package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductCategoryBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.accounting.SalesResults;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.MathematicsBusiness;
import org.cyk.system.root.business.api.mathematics.SortDecorator;
import org.cyk.system.root.business.api.time.TimeDivisionTypeBusiness;
import org.cyk.system.root.model.mathematics.Sort;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.Tree;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractDashboardPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.primefaces.model.chart.BarChartModel;

@Named @ViewScoped @Getter @Setter
public class SaleDashBoardPage extends AbstractDashboardPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private TimeDivisionTypeBusiness timeDivisionTypeBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	@Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
	@Inject private MathematicsBusiness mathematicsBusiness;
	
	private FormOneData<FilterForm> filterForm;
	private FormOneData<SalesResultsDetails> salesResultsDetails;
	private Table<ProductDetails> productTable;
	private BarChartModel turnoverBarChartModel,countBarChartModel;
	private AccountingPeriod accountingPeriod;
	private ProductCategory productCategory;
	private Boolean taxesCollected;
	private String credence,debt;
	private Collection<ProductCategory> productCategories;
	private Collection<Product> products;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		Long productCategoryId = requestParameterLong(uiManager.businessEntityInfos(ProductCategory.class).getIdentifier());
		if(productCategoryId!=null)
			productCategory = productCategoryBusiness.find(productCategoryId);
		
		accountingPeriod = accountingPeriodBusiness.findCurrent();
		taxesCollected = accountingPeriod.getValueAddedTaxRate().compareTo(BigDecimal.ZERO)!=0;
		TimeDivisionType selectedTimeDivisionType = timeDivisionTypeBusiness.find(TimeDivisionType.DAY);
		Date exerciceBegin = accountingPeriod.getPeriod().getFromDate();
		Date exerciceEnd = accountingPeriod.getPeriod().getToDate();
		SaleSearchCriteria saleSearchCriteria = null;
		
		contentTitle = text("sale")+" - "+text("dashboard")+" - "+timeBusiness.formatDate(exerciceBegin,exerciceEnd);
		
		SalesResults salesResults = null;
		saleSearchCriteria = new SaleSearchCriteria(exerciceBegin,exerciceEnd);
		credence = numberBusiness.format(saleBusiness.sumBalanceByCriteria(new SaleSearchCriteria(exerciceBegin,exerciceEnd,BalanceType.POSITIVE)));
		debt = numberBusiness.format(saleBusiness.sumBalanceByCriteria(new SaleSearchCriteria(exerciceBegin,exerciceEnd,BalanceType.NEGAITVE)).abs());
		
		if(productCategory==null){
			productCategories = productCategoryBusiness.findHierarchies();
			salesResults = accountingPeriod.getSalesResults();
			turnoverBarChartModel = chartManager.barModel(saleBusiness.findTurnOverStatistics(saleSearchCriteria, selectedTimeDivisionType));
	        countBarChartModel = chartManager.barModel(saleBusiness.findCountStatistics(saleSearchCriteria, selectedTimeDivisionType));
		}else{
			productCategoryBusiness.findHierarchy(productCategory);
			AccountingPeriodProductCategory accountingPeriodProductCategory = accountingPeriodProductCategoryBusiness.findByAccountingPeriodByProduct(accountingPeriod, productCategory);
			salesResults = accountingPeriodProductCategory.getSalesResults();
			contentTitle = productCategory.getName()+" / "+contentTitle;
			if(productCategory.getChildren()==null){
				products = productBusiness.findByCategory(productCategory);
			}else{
				productCategories = new ArrayList<>();
				for(Object o : productCategory.getChildren())
					productCategories.add((ProductCategory) o);
			}
		}

		salesResultsDetails = (FormOneData<SalesResultsDetails>) createFormOneData(new SalesResultsDetails(salesResults), Crud.READ);
		configureDetailsForm(salesResultsDetails);
		salesResultsDetails.setControlSetListener(new ControlSetAdapter<SalesResultsDetails>(){
			@Override
			public Boolean build(Field field) {
				if(field.getName().equals("sales") || field.getName().equals("valueAddedTaxes"))
					return taxesCollected;
				return super.build(field);
			}
		}); 
		
		filterForm = (FormOneData<FilterForm>) createFormOneData(new FilterForm(), Crud.CREATE);
		filterForm.setShowCommands(Boolean.FALSE);
		filterForm.getSubmitCommandable().setLabel(uiManager.text("command.apply"));
		filterForm.getSubmitCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = -3427079410254809553L;
			@Override
			public void serve(UICommand command, Object parameter) {
				System.out.println(((FilterForm)parameter).getCategory());
				super.serve(command, parameter);
			}
		});
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		List<ProductDetails> productDetailsCollection = new ArrayList<>();
		Set<ProductDetails> set = new LinkedHashSet<>();
		if(productCategories!=null && !productCategories.isEmpty()){
			for(AccountingPeriodProductCategory accountingPeriodProductCategory : accountingPeriodProductCategoryBusiness.findHighestNumberOfSales(accountingPeriod, productCategories))
				set.add(new ProductDetails(accountingPeriodProductCategory));
			for(AccountingPeriodProductCategory accountingPeriodProductCategory : accountingPeriodProductCategoryBusiness.findLowestNumberOfSales(accountingPeriod, productCategories))
				set.add(new ProductDetails(accountingPeriodProductCategory));	
		}
		
		if(products!=null && !products.isEmpty()){
			for(AccountingPeriodProduct accountingPeriodProduct : accountingPeriodProductBusiness.findHighestNumberOfSales(accountingPeriod, products))
				set.add(new ProductDetails(accountingPeriodProduct));
			for(AccountingPeriodProduct accountingPeriodProduct : accountingPeriodProductBusiness.findLowestNumberOfSales(accountingPeriod, products))
				set.add(new ProductDetails(accountingPeriodProduct));	
		}
		
		productDetailsCollection = new ArrayList<>(set);
		
		productTable = createDetailsTable(ProductDetails.class, productDetailsCollection, "model.entity.product");	
		productTable.setTree(new Tree());
		((Tree)productTable.getTree()).build(ProductCategory.class,productCategoryBusiness.findHierarchies(),CompanyWebManager.getInstance().getOutcomeSaleDashBoard());
		
	}
		
	/**/
	
	@Getter @Setter @AllArgsConstructor
	private class SalesResultsDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		@Input @InputText private String turnover,valueAddedTaxes;
		public SalesResultsDetails(SalesResults salesResults) {
			turnover = numberBusiness.format(salesResults.getTurnover());
			valueAddedTaxes = numberBusiness.format(salesResults.getValueAddedTaxes());
		}
	}
	
	@Getter @Setter @EqualsAndHashCode(callSuper=false,of={"code"})
	private class ProductDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		@Input @InputText private String code,name,numberOfSales,turnover;
		public ProductDetails(AbstractAccountingPeriodResults<?> product) {
			if(product instanceof AccountingPeriodProductCategory){
				this.code = ((AccountingPeriodProductCategory)product).getEntity().getCode();
				this.name = ((AccountingPeriodProductCategory)product).getEntity().getName();
			}else{
				this.code = ((AccountingPeriodProduct)product).getEntity().getCode();
				this.name = ((AccountingPeriodProduct)product).getEntity().getName();
			}
			this.numberOfSales = numberBusiness.format(product.getSalesResults().getCount());
			this.turnover = numberBusiness.format(product.getSalesResults().getTurnover());
		}
	}
		
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public class FilterForm implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		@Input @InputChoice @InputOneChoice @InputOneCombo 
		private ProductCategory category;
		
	}

}