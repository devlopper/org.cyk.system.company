package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AbstractAccountingPeriodResultsBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductCategoryBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness.SalesResultsCartesianModelParameters;
import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.chart.AbstractChartModel.LegendPosition;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.business.api.chart.PieModel;
import org.cyk.system.root.business.api.time.TimeDivisionTypeBusiness;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.Tree;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractDashboardPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;

@Named @ViewScoped @Getter @Setter
public class SaleDashBoardPage extends AbstractDashboardPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CashRegisterBusiness cashRegisterBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleProductBusiness saleProductBusiness;
	@Inject private TimeDivisionTypeBusiness timeDivisionTypeBusiness;
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	@Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
	
	private FormOneData<SalesResultsDetails> salesResultsDetails;
	private Table<ProductDetails> productTable;
	private BarChartModel turnoverBarChartModel,countBarChartModel;
	private PieChartModel countPieChartModel,turnoverPieChartModel;
	private AccountingPeriod accountingPeriod;
	private ProductCategory productCategory;
	private Boolean taxesCollected;
	private String credence,debt;
	private Collection<ProductCategory> productCategories;
	private Collection<Product> products;
	private Boolean salesFound;
	
	private List<ProductDetails> productDetailsCollection = new ArrayList<>();
		
	@Override
	protected void initialisation() {
		super.initialisation();
		Long productCategoryId = requestParameterLong(uiManager.businessEntityInfos(ProductCategory.class).getIdentifier());
		if(productCategoryId!=null)
			productCategory = productCategoryBusiness.find(productCategoryId);
		
		accountingPeriod = accountingPeriodBusiness.findCurrent();
		taxesCollected = accountingPeriod.getSaleConfiguration().getValueAddedTaxRate().compareTo(BigDecimal.ZERO)!=0;
		TimeDivisionType selectedTimeDivisionType = timeDivisionTypeBusiness.find(TimeDivisionType.MONTH);
		Date exerciceBegin = accountingPeriod.getExistencePeriod().getFromDate();
		Date exerciceEnd = accountingPeriod.getExistencePeriod().getToDate();
		SaleSearchCriteria saleSearchCriteria = null;
		
		contentTitle = text("sale")+" - "+text("dashboard")+" - "+timeBusiness.formatDate(exerciceBegin,exerciceEnd);
		
		SaleResults salesResults = null;
		saleSearchCriteria = new SaleSearchCriteria(exerciceBegin,exerciceEnd);
		
		credence = numberBusiness.format(saleBusiness.computeByCriteria(new SaleSearchCriteria(exerciceBegin,exerciceEnd,BalanceType.POSITIVE)).getBalance());
		debt = numberBusiness.format(saleBusiness.computeByCriteria(new SaleSearchCriteria(exerciceBegin,exerciceEnd,BalanceType.NEGAITVE)).getBalance().abs());
		
		if(productCategory==null){
			productCategories = productCategoryBusiness.findHierarchies();
			salesResults = accountingPeriod.getSaleResults();
		}else{
			productCategoryBusiness.findHierarchy(productCategory);
			AccountingPeriodProductCategory accountingPeriodProductCategory = accountingPeriodProductCategoryBusiness.findByAccountingPeriodByProduct(accountingPeriod, productCategory);
			salesResults = accountingPeriodProductCategory.getSaleResults();
			contentTitle = productCategory.getName()+" / "+contentTitle;
			if(productCategory.getChildren()==null){
				products = productBusiness.findByCategory(productCategory);
			}else{
				productCategories = new ArrayList<>();
				for(Object o : productCategory.getChildren())
					productCategories.add((ProductCategory) o);
			}
		}
		Collection<Sale> sales = saleBusiness.findByCriteria(saleSearchCriteria);
		
		Collection<SaleProduct> saleProducts = saleProductBusiness.findBySalesByCategories(sales, productCategory==null?productCategories:Arrays.asList(productCategory));
		
		if(saleProducts.isEmpty()){
			
		}else{
			salesFound = Boolean.TRUE;
			SalesResultsCartesianModelParameters salesResultsCartesianModelParameters = new SalesResultsCartesianModelParameters(accountingPeriod.getExistencePeriod(), 
					saleProducts, productCategory==null?productCategories:Arrays.asList(productCategory), selectedTimeDivisionType);
			turnoverBarChartModel = chartManager.barModel(configureCartesianModel(saleProductBusiness.findCartesianModelTurnOver(salesResultsCartesianModelParameters)));
	        countBarChartModel = chartManager.barModel(configureCartesianModel(saleProductBusiness.findCartesianModelCount(salesResultsCartesianModelParameters)));
	        
			//salesResultsDetails = (FormOneData<SalesResultsDetails>) createFormOneData(new SaleResultsDetails(salesResults), Crud.READ);
			configureDetailsForm(salesResultsDetails);
			salesResultsDetails.getControlSetListeners().add(new ControlSetAdapter<SalesResultsDetails>(){
				@Override
				public Boolean build(Field field) {
					if(field.getName().equals("sales") || field.getName().equals("valueAddedTaxes"))
						return taxesCollected;
					return super.build(field);
				}
			}); 
		}
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		if(productCategories!=null && !productCategories.isEmpty())
			productDetails(ProductCategory.class, AccountingPeriodProductCategory.class, accountingPeriodProductCategoryBusiness, productCategories);
		else
			productDetails(Product.class, AccountingPeriodProduct.class, accountingPeriodProductBusiness, products);
	}
	
	private <PRODUCT extends AbstractEnumeration,RESULTS extends AbstractAccountingPeriodResults<PRODUCT>> void productDetails(Class<PRODUCT> productClass,Class<RESULTS> resultsClass,AbstractAccountingPeriodResultsBusiness<RESULTS, PRODUCT> business
			,Collection<PRODUCT> products){
		PieModel countPieModel,turnoverPieModel;
		String highestNumberOfSales=null,lowestNumberOfSales=null;
		Collection<RESULTS> resultsCollection = business.findByAccountingPeriodByProducts(accountingPeriod, products);
		lowestNumberOfSales = numberBusiness.format(business.findLowestNumberOfSalesValue(resultsCollection));
		highestNumberOfSales = numberBusiness.format(business.findHighestNumberOfSalesValue(resultsCollection));
		
		/*
		for(AccountingPeriodProduct accountingPeriodProduct : accountingPeriodProductBusiness.findHighestNumberOfSales(accountingPeriod, products))
			set.add(new ProductDetails(accountingPeriodProduct));
		for(AccountingPeriodProduct accountingPeriodProduct : accountingPeriodProductBusiness.findLowestNumberOfSales(accountingPeriod, products))
			set.add(new ProductDetails(accountingPeriodProduct));
		*/
		
		for(RESULTS results : resultsCollection)
			productDetailsCollection.add(new ProductDetails(results));
		//System.out.println("SaleDashBoardPage.productDetails() "+business.findNumberOfSalesPieModel(resultsCollection));
		countPieModel = business.findNumberOfSalesPieModel(resultsCollection);
		turnoverPieModel = business.findTurnoverPieModel(resultsCollection);
		
		//productTable = createDetailsTable(ProductDetails.class, productDetailsCollection, "model.entity.product");	
		for(Row<ProductDetails> row : productTable.getRows())
			if(row.getData().getNumberOfSales().equals(highestNumberOfSales))
				row.getCascadeStyleSheet().addClass("highestNumberOfSales");
			else if(row.getData().getNumberOfSales().equals(lowestNumberOfSales))
				row.getCascadeStyleSheet().addClass("lowestNumberOfSales");
				
		productTable.setTree(new Tree());
		productTable.getTree().setDynamic(Boolean.FALSE);
		((Tree)productTable.getTree()).build(ProductCategory.class,productCategoryBusiness.findHierarchies(),productCategory,CompanyWebManager.getInstance().getOutcomeSaleDashBoard());
		
		countPieChartModel = chartManager.pieModel(configurePieModel(countPieModel));
		turnoverPieChartModel = chartManager.pieModel(configurePieModel(turnoverPieModel));
	}
	
	private CartesianModel configureCartesianModel(CartesianModel model){
		model.setLegendPosition(LegendPosition.NORTH_EAST);
		model.setShowTitle(Boolean.FALSE);
		return model;
	}
	
	private PieModel configurePieModel(PieModel model){
		model.setShowTitle(Boolean.FALSE);
		return model;
	}
		
	/**/
	
	public PieChartModel getCountPieChartModel(){
		return countPieChartModel;
	}
	public PieChartModel getTurnoverPieChartModel(){
		return turnoverPieChartModel;
	}
	public BarChartModel getCountBarChartModel(){
		return countBarChartModel;
	}
	public BarChartModel getTurnoverBarChartModel(){
		return turnoverBarChartModel;
	}
	
	/**/
	
	@Getter @Setter @AllArgsConstructor
	public class SalesResultsDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		@Input @InputText private String turnover,valueAddedTaxes;
		public SalesResultsDetails(SaleResults salesResults) {
			turnover = numberBusiness.format(salesResults.getCost().getTurnover());
			valueAddedTaxes = numberBusiness.format(salesResults.getCost().getTax());
		}
	}
	
	@Getter @Setter @EqualsAndHashCode(callSuper=false,of={"code"})
	public class ProductDetails implements Serializable {
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
			this.numberOfSales = numberBusiness.format(product.getSaleResults().getCost().getValue());
			this.turnover = numberBusiness.format(product.getSaleResults().getCost().getTurnover());
		}
	}
		
}