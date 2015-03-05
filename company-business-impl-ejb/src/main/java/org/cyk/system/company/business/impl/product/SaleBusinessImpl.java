package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyValueGenerator;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaledProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProductReport;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaledProductDao;
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
	@Inject private SaledProductBusiness saledProductBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private JasperReportBusinessImpl reportBusiness;
	@Inject private LanguageBusiness languageBusiness;
	@Inject private FileBusiness fileBusiness;
	@Inject private ContactCollectionBusiness contactCollectionBusiness;
	@Inject private TimeBusiness timeBusiness;
	
	@Inject private SaledProductDao saledProductDao;
	@Inject private SaleCashRegisterMovementDao saleCashRegisterMovementDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaledProduct selectProduct(Sale sale, Product product) {
		SaledProduct saledProduct = new SaledProduct(sale, product, new BigDecimal("1"));
		saledProductBusiness.process(saledProduct);
		sale.getSaledProducts().add(saledProduct);
		sale.setCost(sale.getCost().add(saledProduct.getPrice()));
		return saledProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void unselectProduct(Sale sale, SaledProduct saledProduct) {
		sale.getSaledProducts().remove(saledProduct);
		sale.setCost(sale.getCost().subtract(saledProduct.getPrice()));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void quantifyProduct(Sale sale, SaledProduct saledProduct) {
		saledProductBusiness.process(saledProduct);
		sale.setCost(BigDecimal.ZERO);
		for(SaledProduct saleProduct : sale.getSaledProducts())
			sale.setCost( sale.getCost().add(saleProduct.getPrice() ));
	}
	
	@Override
	public Sale create(Sale sale) {
		BigDecimal total = BigDecimal.ZERO;
		for(SaledProduct saleProduct : sale.getSaledProducts())
			total = total.add(saleProduct.getPrice());
		exceptionUtils().exception(!total.equals(sale.getCost()), "validation.sale.cost.sum");
		
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			sale.setDate(universalTimeCoordinated());
		sale.setIdentificationNumber(companyValueGenerator.saleIdentificationNumber(sale));
		sale.setBalance(sale.getCost());
		sale.setValueAddedTax(sale.getCost().multiply(sale.getCashier().getCashRegister().getCompany().getValueAddedTaxRate()));
		
		sale = super.create(sale);
		for(SaledProduct saledProduct : sale.getSaledProducts()){
			saledProduct.setSale(sale);
			genericDao.create(saledProduct);
		}
		return sale;
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		create(sale);
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
		//create report
		Company company = saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getCompany();
		BigDecimal numberOfProducts = BigDecimal.ZERO;
		for(SaledProduct sp : sale.getSaledProducts())
			numberOfProducts = numberOfProducts.add(sp.getQuantity());
		SaleReport saleReport = new SaleReport(sale.getIdentificationNumber(),saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getCode(),
				DATE_TIME_LONG_FORMAT.format(sale.getDate()),numberOfProducts.toString()+"",sale.getCost().toString(),
				languageBusiness.findText("company.report.pointofsale.welcome"),languageBusiness.findText("company.report.pointofsale.goodbye"));
		saleReport.getCompany().setName(company.getName());
		contactCollectionBusiness.load(company.getContactCollection());
		saleReport.getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		saleReport.getCustomer().setRegistrationCode(sale.getCustomer()==null?"":sale.getCustomer().getRegistration().getCode());
		saleReport.getSaleCashRegisterMovement().setAmountDue(sale.getCost().toString());
		saleReport.getSaleCashRegisterMovement().setAmountIn(saleCashRegisterMovement.getAmountIn().toString());
		saleReport.getSaleCashRegisterMovement().setAmountToOut(saleCashRegisterMovement.getAmountIn().subtract(sale.getCost()).toString());
		saleReport.getSaleCashRegisterMovement().setAmountOut(saleCashRegisterMovement.getAmountOut().toString());
		saleReport.getSaleCashRegisterMovement().setVatRate(company.getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2).toString()+"%");
		saleReport.getSaleCashRegisterMovement().setVatAmount(sale.getValueAddedTax().toString());
		saleReport.getSaleCashRegisterMovement().setAmountDueNoTaxes(sale.getCost().subtract(sale.getValueAddedTax()).toString());
		
		for(SaledProduct sp : sale.getSaledProducts()){
			SaleProductReport spr = new SaleProductReport(saleReport,sp.getProduct().getCode(),sp.getProduct().getName(),sp.getProduct().getPrice().toString(),
					sp.getQuantity().toString(),sp.getPrice().toString());
			saleReport.getSaledProducts().add(spr);
		}
		
		Report<SaleReport> report = createReport(sale, saleReport,saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getCompany().getPointOfSaleReportFile(),
				"pdf");
		
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
		sale.setSaledProducts(saledProductDao.readBySale(sale));
		sale.setSaleCashRegisterMovements(saleCashRegisterMovementDao.readBySale(sale));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Report<SaleReport> findReport(Collection<Sale> sales) {
		return createReport(sales.iterator().next(), null, null,DEFAULT_POINT_OF_SALE_REPORT_EXTENSION);//TODO many receipt print must be handled
	}
	
	/**/
	
	@Override
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
	
	@Override
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
