package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyValueGenerator;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleProductBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleProductReport;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.business.api.chart.Series;
import org.cyk.system.root.business.api.chart.SeriesItem;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.geography.ContactCollectionBusiness;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.file.JasperReportBusinessImpl;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.Report;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.model.time.TimeDivisionType;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public static String DEFAULT_POINT_OF_SALE_REPORT_NAME = "SaleReport";
	public static String DEFAULT_POINT_OF_SALE_REPORT_EXTENSION = "pdf";
	public static Boolean AUTO_SET_SALE_DATE = Boolean.TRUE;
	
	@Inject private CompanyValueGenerator companyValueGenerator;
	@Inject private SaleProductBusiness saleProductBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private JasperReportBusinessImpl reportBusiness;
	@Inject private LanguageBusiness languageBusiness;
	@Inject private FileBusiness fileBusiness;
	@Inject private ContactCollectionBusiness contactCollectionBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	
	@Inject private AccountingPeriodDao accountingPeriodDao;
	@Inject private SaleProductDao saleProductDao;
	@Inject private SaleCashRegisterMovementDao saleCashRegisterMovementDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, Product product) {
		SaleProduct saleProduct = new SaleProduct(sale, product, new BigDecimal("1"));
		saleProductBusiness.process(saleProduct);
		sale.getSaleProducts().add(saleProduct);
		sale.setCost(sale.getCost().add(saleProduct.getPrice()));
		return saleProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void unselectProduct(Sale sale, SaleProduct saleProduct) {
		if(sale.getSaleProducts() instanceof List<?>){
			List<SaleProduct> list = (List<SaleProduct>) sale.getSaleProducts();
			for(int i=0;i<list.size();){
				if(list.get(i)==saleProduct){
					list.remove(i);
					break;
				}else
					i++;
			}
		}
		sale.setCost(sale.getCost().subtract(saleProduct.getPrice()));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void quantifyProduct(Sale sale, SaleProduct saleProduct) {
		saleProductBusiness.process(saleProduct);
		sale.setCost(BigDecimal.ZERO);
		for(SaleProduct lSaleProduct : sale.getSaleProducts())
			sale.setCost( sale.getCost().add(lSaleProduct.getPrice() ));
	}
	
	@Override
	public Sale create(Sale sale) {
		exceptionUtils().exception(sale.getAccountingPeriod()==null, "exception.sale.accountingperiodmissing");
		productBusiness.consume(sale.getSaleProducts());
		
		BigDecimal total = BigDecimal.ZERO;
		for(SaleProduct saleProduct : sale.getSaleProducts())
			total = total.add(saleProduct.getPrice());
		exceptionUtils().exception(!total.equals(sale.getCost()), "validation.sale.cost.sum");
		
		AccountingPeriod accountingPeriod = sale.getAccountingPeriod();
		accountingPeriod.getSalesResults().setCount(sale.getAccountingPeriod().getSalesResults().getCount().add(BigDecimal.ONE));
		
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		sale.setIdentificationNumber(companyValueGenerator.saleIdentificationNumber(sale));
		
		for(SaleProduct saleProduct : sale.getSaleProducts()){	
			sale.setValueAddedTax(sale.getValueAddedTax().add(saleProduct.getValueAddedTax()));
			sale.setTurnover(sale.getTurnover().add(saleProduct.getTurnover()));
			sale.setBalance(sale.getBalance().add(saleProduct.getPrice()));
		}
		
		accountingPeriod.getSalesResults().setTurnover(accountingPeriod.getSalesResults().getTurnover().add(sale.getTurnover()));
		
		sale = super.create(sale);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			saleProduct.setSale(sale);
			genericDao.create(saleProduct);
		}
		
		accountingPeriodProductBusiness.consume(sale.getAccountingPeriod(), sale.getSaleProducts());
		accountingPeriodDao.update(accountingPeriod);
		return sale;
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		create(sale);
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
		//create report
		Company company = saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getOwnedCompany().getCompany();
		BigDecimal numberOfProducts = BigDecimal.ZERO;
		for(SaleProduct sp : sale.getSaleProducts())
			numberOfProducts = numberOfProducts.add(sp.getQuantity());
		SaleReport saleReport = new SaleReport(sale.getIdentificationNumber(),saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getCode(),
				timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN),numberBusiness.format(numberOfProducts)+"",numberBusiness.format(sale.getCost()),
				languageBusiness.findText("company.report.pointofsale.welcome"),languageBusiness.findText("company.report.pointofsale.goodbye"));
		saleReport.getAccountingPeriod().getCompany().setName(company.getName());
		contactCollectionBusiness.load(company.getContactCollection());
		saleReport.getAccountingPeriod().getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		saleReport.getCustomer().setRegistrationCode(sale.getCustomer()==null?"":sale.getCustomer().getRegistration().getCode());
		saleReport.getSaleCashRegisterMovement().setAmountDue(numberBusiness.format(sale.getCost()));
		saleReport.getSaleCashRegisterMovement().setAmountIn(numberBusiness.format(saleCashRegisterMovement.getAmountIn()));
		saleReport.getSaleCashRegisterMovement().setAmountToOut(numberBusiness.format(saleCashRegisterMovement.getAmountIn().subtract(sale.getCost())));
		saleReport.getSaleCashRegisterMovement().setAmountOut(numberBusiness.format(saleCashRegisterMovement.getAmountOut()));
		saleReport.getSaleCashRegisterMovement().setVatRate(numberBusiness.format(sale.getAccountingPeriod().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
		saleReport.getSaleCashRegisterMovement().setVatAmount(numberBusiness.format(sale.getValueAddedTax().setScale(2)));
		saleReport.getSaleCashRegisterMovement().setAmountDueNoTaxes(numberBusiness.format(sale.getCost().subtract(sale.getValueAddedTax()).setScale(2)));
		
		for(SaleProduct sp : sale.getSaleProducts()){
			SaleProductReport spr = new SaleProductReport(saleReport,sp.getProduct().getCode(),sp.getProduct().getName(),numberBusiness.format(sp.getProduct().getPrice()),
					numberBusiness.format(sp.getQuantity()),numberBusiness.format(sp.getPrice()));
			saleReport.getSaleProducts().add(spr);
		}
		
		Report<SaleReport> report = createReport(sale, saleReport,sale.getAccountingPeriod().getPointOfSaleReportFile(),"pdf");
		
		sale.setReport(new File());
		sale.getReport().setBytes(report.getBytes());
		sale.getReport().setExtension(report.getFileExtension());
		fileBusiness.create(sale.getReport());
		update(sale);
	}
	
	private Report<SaleReport> createReport(Sale sale,SaleReport saleReport,File template,String fileExtension){
		Report<SaleReport> report = new Report<SaleReport>();
		report.setFileName(DEFAULT_POINT_OF_SALE_REPORT_NAME);
		report.setFileExtension(StringUtils.isBlank(fileExtension)?DEFAULT_POINT_OF_SALE_REPORT_EXTENSION:fileExtension);
		if(saleReport==null){
			report.setBytes(sale.getReport().getBytes());
		}else{
			report.getDataSource().add(saleReport);
			report.setTemplateFile(template);
			reportBusiness.build(report, Boolean.FALSE);
		}
		return report;
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Sale> findByCriteria(SaleSearchCriteria criteria) {
		//Collection<Sale> sales = null;
		/*if(criteria.getFromDateSearchCriteria().getValue()==null || criteria.getToDateSearchCriteria().getValue()==null)
			sales = findAll();
		*/
		
		criteriaDefaultValues(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SaleSearchCriteria criteria) {
		/*
		if(criteria.getFromDateSearchCriteria().getValue()==null || criteria.getToDateSearchCriteria().getValue()==null)
    		return countAll();
    		*/
		criteriaDefaultValues(criteria);
		return dao.countByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal sumCostByCriteria(SaleSearchCriteria criteria) {
		criteriaDefaultValues(criteria);
		return dao.sumCostByCriteria(criteria);
	}
	
	@Override
	public BigDecimal sumValueAddedTaxByCriteria(SaleSearchCriteria criteria) {
		criteriaDefaultValues(criteria);
		return dao.sumValueAddedTaxByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria) {
		criteriaDefaultValues(criteria);
		return dao.sumBalanceByCriteria(criteria);
	}
	
	/**/
	
	private void criteriaDefaultValues(SaleSearchCriteria criteria){
		//do better
		if(criteria.getFromDateSearchCriteria().getValue()==null)
			criteria.getFromDateSearchCriteria().setValue(DATE_MOST_PAST);
		if(criteria.getToDateSearchCriteria().getValue()==null)
			criteria.getToDateSearchCriteria().setValue(DATE_MOST_FUTURE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void load(Sale sale) {
		sale.setSaleProducts(saleProductDao.readBySale(sale));
		sale.setSaleCashRegisterMovements(saleCashRegisterMovementDao.readBySale(sale));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Report<SaleReport> findReport(Collection<Sale> sales) {
		return createReport(sales.iterator().next(), null, null,DEFAULT_POINT_OF_SALE_REPORT_EXTENSION);//TODO many receipt print must be handled
	}
	
	@Override
	public void updateDelivery(Sale sale,Collection<ProductEmployee> productEmployees) {
		for(ProductEmployee productEmployee : productEmployees)
			sale.getPerformers().add(productEmployee);
		
		Set<ProductEmployee> delete = new HashSet<>();
		for(ProductEmployee productEmployee : sale.getPerformers())
			if( !productEmployees.contains(productEmployee) )
				delete.add(productEmployee);
		
		for(ProductEmployee productEmployee : delete)
			sale.getPerformers().remove(productEmployee);
		
		update(sale);	
	}
	
	/**/
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType) {
		Collection<Sale> sales = findByCriteria(saleSearchCriteria);
		if(sales.isEmpty())
			return null;
		Period searchPeriod = new Period(saleSearchCriteria.getFromDateSearchCriteria().getValue(), 
				saleSearchCriteria.getToDateSearchCriteria().getValue());
		
		String spl = languageBusiness.findText("field.from.date")+" "+DATE_SHORT_FORMAT.format(searchPeriod.getFromDate())+" "
				+languageBusiness.findText("field.to.date")+"  "+DATE_SHORT_FORMAT.format(searchPeriod.getToDate());
		
		CartesianModel cartesianModel = new CartesianModel(languageBusiness.findText("turnover")+" - "+spl,languageBusiness.findText("date"),
				languageBusiness.findText("amount"));
		Series amountOfSaleSeries = cartesianModel.addSeries(languageBusiness.findText("turnover"));
		
		
		if(sales.isEmpty())
			return cartesianModel;
		Collection<Period> periods = timeBusiness.findPeriods(searchPeriod, timeDivisionType,Boolean.TRUE) ;
		
		for(Period period : periods){
			BigDecimal amountOfSale = BigDecimal.ZERO;
			String x = timeBusiness.formatPeriod(period, timeDivisionType);
			for(Sale sale : sales)
				if(timeBusiness.between(period, sale.getDate(), Boolean.FALSE, Boolean.FALSE)){
					amountOfSale = amountOfSale.add(sale.getCost());
				}
			if(amountOfSale.compareTo(BigDecimal.ZERO)>0){
				amountOfSaleSeries.getItems().add(new SeriesItem(x, amountOfSale));
			}
		}
		return cartesianModel;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType) {
		Collection<Sale> sales = findByCriteria(saleSearchCriteria);
		if(sales.isEmpty())
			return null;
		Period searchPeriod = new Period(saleSearchCriteria.getFromDateSearchCriteria().getValue(), 
				saleSearchCriteria.getToDateSearchCriteria().getValue());
		String spl = languageBusiness.findText("field.from.date")+" "+DATE_SHORT_FORMAT.format(searchPeriod.getFromDate())+" "
				+languageBusiness.findText("field.to.date")+"  "+DATE_SHORT_FORMAT.format(searchPeriod.getToDate());
		
		CartesianModel cartesianModel = new CartesianModel(languageBusiness.findText("salecount")+" - "+spl,languageBusiness.findText("date"),
				languageBusiness.findText("count"));
		Series numberOfSaleSeries = cartesianModel.addSeries(languageBusiness.findText("salecount"));	
		
		
		Collection<Period> periods = timeBusiness.findPeriods(new Period(saleSearchCriteria.getFromDateSearchCriteria().getValue(), 
				saleSearchCriteria.getToDateSearchCriteria().getValue()), timeDivisionType,Boolean.TRUE) ;
		
		for(Period period : periods){
			Integer numberOfSale = 0;
			String x = timeBusiness.formatPeriod(period, timeDivisionType);
			for(Sale sale : sales)
				if(timeBusiness.between(period, sale.getDate(), Boolean.FALSE, Boolean.FALSE)){
					numberOfSale++;
				}
			if(numberOfSale>0){
				numberOfSaleSeries.getItems().add(new SeriesItem(x, numberOfSale));
			}
		}
		
		return cartesianModel;
	}
	
}
