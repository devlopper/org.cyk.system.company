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
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleProductBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.Customer;
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
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.geography.ContactCollectionBusiness;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.file.report.jasper.JasperReportBusinessImpl;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;

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
	
	@Inject private AccountingPeriodBusiness accountingPeriodBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private AccountingPeriodDao accountingPeriodDao;
	@Inject private SaleProductDao saleProductDao;
	@Inject private SaleCashRegisterMovementDao saleCashRegisterMovementDao;
	@Inject private CustomerDao customerDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Sale newInstance(Person person) {
		logDebug("Instanciate sale.");
		Sale sale = new Sale();
		sale.setAccountingPeriod(accountingPeriodBusiness.findCurrent());
		sale.setCashier(cashierBusiness.findByPerson(person));
		sale.setAutoComputeValueAddedTax(sale.getAccountingPeriod().getValueAddedTaxRate().signum()!=0);
		exceptionUtils().exception(sale.getCashier()==null, "exception.sale.cashier.null");
		logDebug("Sale instanciated. Cashier={} VAT in={}.",sale.getCashier().getIdentifier(),Boolean.TRUE.equals(sale.getAccountingPeriod().getValueAddedTaxIncludedInCost()));
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, Product product,BigDecimal quantity) {
		logDebug("Select Sale product {}",product.getCode());
		SaleProduct saleProduct = new SaleProduct(sale, product, quantity);
		saleProductBusiness.process(saleProduct);
		sale.getSaleProducts().add(saleProduct);
		if(saleProduct.getPrice()!=null)
			sale.setCost(sale.getCost().add(saleProduct.getPrice()));
		return saleProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, Product product) {
		return selectProduct(sale, product, BigDecimal.ONE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void unselectProduct(Sale sale, SaleProduct saleProduct) {
		logDebug("Unselect Sale product {}",saleProduct.getProduct().getCode());
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
	public void applyChange(Sale sale, SaleProduct saleProduct) {
		logDebug("Apply changes to sale product {}. UP={} Q={} P={} VAT={} T={}",saleProduct.getProduct().getCode(),
				saleProduct.getProduct().getPrice(),saleProduct.getQuantity(),saleProduct.getPrice(),saleProduct.getValueAddedTax(),saleProduct.getTurnover());
		saleProductBusiness.process(saleProduct);
		updateCost(sale);
	}
	
	private void updateCost(Sale sale){
		sale.setCost(BigDecimal.ZERO);
		for(SaleProduct lSaleProduct : sale.getSaleProducts())
			sale.setCost( sale.getCost().add(lSaleProduct.getPrice() ));
	}
	
	@Override
	public Sale create(Sale sale) {
		logDebug("Create Sale");
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		sale.setIdentificationNumber(companyValueGenerator.saleIdentificationNumber(sale));
		
		exceptionUtils().exception(sale.getAccountingPeriod()==null, "exception.sale.accountingperiodmissing");
		
		if(Boolean.TRUE.equals(sale.getCompleted())){
			save(sale,Boolean.TRUE);
		}else{
			logDebug("Sale not yet completed");
			sale = super.create(sale);
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				saleProduct.setSale(sale);
				genericDao.create(saleProduct);
			}
		}
		//notifyCrudDone(Crud.CREATE, sale);
		logDebugSale("Sale created succesfully",sale);
		return sale;
	}
	
	private void save(Sale sale,Boolean creation){
		logDebug("Computing sale data");
		exceptionUtils().exception(Boolean.TRUE.equals(sale.getDone()), "exception.sale.done.already");
		for(SaleProduct saleProduct : sale.getSaleProducts())
			exceptionUtils().exception(saleProduct.getPrice()==null, "exception.sale.product.price.null");
		
		if(Boolean.TRUE.equals(creation)){
			
		}else{
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				saleProductBusiness.process(saleProduct);				
			}
			updateCost(sale);
		}
		
		/*
		BigDecimal total = BigDecimal.ZERO;
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			total = total.add(saleProduct.getPrice());
		}
		exceptionUtils().exception(!total.equals(sale.getCost()), "validation.sale.cost.sum");
		 */
		
		if(Boolean.TRUE.equals(sale.getCompleted())){
			productBusiness.consume(sale.getSaleProducts());
			AccountingPeriod accountingPeriod = sale.getAccountingPeriod();
			accountingPeriod.getSalesResults().setCount(sale.getAccountingPeriod().getSalesResults().getCount().add(BigDecimal.ONE));
			accountingPeriod.getSalesResults().setTurnover(accountingPeriod.getSalesResults().getTurnover().add(sale.getTurnover()));
			accountingPeriodDao.update(accountingPeriod);
			
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				sale.setValueAddedTax(sale.getValueAddedTax().add(saleProduct.getValueAddedTax()));
				//FIXME is it necessary since it is updated later???
				sale.setTurnover(sale.getTurnover().add(saleProduct.getTurnover()));
				sale.setBalance(sale.getBalance().add(saleProduct.getPrice()));
			}
			sale.setCost(sale.getCost().add(sale.getCommission()));
			sale.setBalance(sale.getCost());
			sale.setTurnover(sale.getCost());
			
			Customer customer = sale.getCustomer();
			if(customer!=null){
				customer.setSaleCount(customer.getSaleCount().add(BigDecimal.ONE));
				customer.setTurnover(customer.getTurnover().add(sale.getTurnover()));
				customer.setBalance(customer.getBalance().add(sale.getBalance()));
				customerDao.update(customer);
			}
		}else{
			sale.setValueAddedTax(BigDecimal.ZERO);
			sale.setTurnover(BigDecimal.ZERO);
			sale.setBalance(BigDecimal.ZERO);
		}

		sale.setDone(sale.getCompleted());
		if(Boolean.TRUE.equals(creation))
			sale = super.create(sale);
		else
			sale = super.update(sale);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			saleProduct.setSale(sale);
			if(Boolean.TRUE.equals(creation)){
				genericDao.create(saleProduct);
			}else
				genericDao.update(saleProduct);
		}
		
		if(Boolean.TRUE.equals(sale.getCompleted())){
			accountingPeriodProductBusiness.consume(sale.getAccountingPeriod(), sale.getSaleProducts());
		}else{
			
		}
		logDebugSale("Sale data computed successfully", sale);
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		create(sale);
		if(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO)){
			logDebug("No sale cash register movement");
		}else{
			saleCashRegisterMovement.setSale(sale);
			if(Boolean.TRUE.equals(sale.getDone())){
				saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
			}	
		}
		
		createReport(sale, saleCashRegisterMovement);
	}
	
	@Override
	public void complete(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement) {
		logDebug("Complete Sale");
		save(sale, Boolean.FALSE);
		createReport(sale, saleCashRegisterMovement);
		update(sale);
		logDebugSale("Sale completed succesfully",sale);
	}
	
	private void createReport(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement){
		logDebug("Prepare report");
		Company company = saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getOwnedCompany().getCompany();
		BigDecimal numberOfProducts = BigDecimal.ZERO;
		for(SaleProduct sp : sale.getSaleProducts())
			numberOfProducts = numberOfProducts.add(sp.getQuantity());
		SaleReport saleReport = new SaleReport(sale.getIdentificationNumber(),
				Boolean.TRUE.equals(sale.getAccountingPeriod().getShowPointOfSaleReportCashier())?saleCashRegisterMovement.getCashRegisterMovement().getCashRegister().getCode():null,
				timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN),numberBusiness.format(numberOfProducts)+"",numberBusiness.format(sale.getCost()),
				languageBusiness.findText("company.report.pointofsale.welcome"),languageBusiness.findText("company.report.pointofsale.goodbye"));
		saleReport.getAccountingPeriod().getCompany().setName(company.getName());
		saleReport.setDone(sale.getDone());
		contactCollectionBusiness.load(company.getContactCollection());
		
		saleReport.getAccountingPeriod().getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		saleReport.getCustomer().setRegistrationCode(sale.getCustomer()==null?"":sale.getCustomer().getRegistration().getCode());
		saleReport.getSaleCashRegisterMovement().setAmountDue(numberBusiness.format(sale.getCost()));
		if(Boolean.TRUE.equals(sale.getDone())){
			saleReport.getSaleCashRegisterMovement().setAmountIn(numberBusiness.format(saleCashRegisterMovement.getAmountIn()));
			saleReport.getSaleCashRegisterMovement().setAmountToOut(numberBusiness.format(saleCashRegisterMovement.getAmountIn().subtract(sale.getCost())));
			saleReport.getSaleCashRegisterMovement().setAmountOut(numberBusiness.format(saleCashRegisterMovement.getAmountOut()));
			if(sale.getValueAddedTax()!=null && sale.getValueAddedTax().signum()!=0){
				saleReport.getSaleCashRegisterMovement().setVatRate(numberBusiness.format(sale.getAccountingPeriod().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
				saleReport.getSaleCashRegisterMovement().setVatAmount(numberBusiness.format(sale.getValueAddedTax().setScale(2)));
				saleReport.getSaleCashRegisterMovement().setAmountDueNoTaxes(numberBusiness.format(sale.getCost().subtract(sale.getValueAddedTax()).setScale(2)));
			}
		}
		for(SaleProduct sp : sale.getSaleProducts()){
			SaleProductReport spr = new SaleProductReport(saleReport,sp.getProduct().getCode(),sp.getProduct().getName(),
					sp.getProduct().getPrice()==null?"":numberBusiness.format(sp.getProduct().getPrice()),
					numberBusiness.format(sp.getQuantity()),numberBusiness.format(sp.getPrice()));
			saleReport.getSaleProducts().add(spr);
		}
		
		ReportBasedOnTemplateFile<SaleReport> report = createReport(sale, saleReport,sale.getAccountingPeriod().getPointOfSaleReportFile(),"pdf");
		if(sale.getReport()==null)
			sale.setReport(new File());
		sale.getReport().setBytes(report.getBytes());
		sale.getReport().setExtension(report.getFileExtension());
		if(sale.getReport().getIdentifier()==null){
			fileBusiness.create(sale.getReport());
			logDebug("Report created");
		}else{
			fileBusiness.update(sale.getReport());
			logDebug("Report updated");
		}
		
		update(sale);
	}
	
	private ReportBasedOnTemplateFile<SaleReport> createReport(Sale sale,SaleReport saleReport,File template,String fileExtension){
		ReportBasedOnTemplateFile<SaleReport> report = new ReportBasedOnTemplateFile<SaleReport>();
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
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SaleSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal sumCostByCriteria(SaleSearchCriteria criteria) {
		return dao.sumCostByCriteria(criteria);
	}
	
	@Override
	public BigDecimal sumValueAddedTaxByCriteria(SaleSearchCriteria criteria) {
		return dao.sumValueAddedTaxByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria) {
		return dao.sumBalanceByCriteria(criteria);
	}
	
	/**/
	
	 
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void load(Sale sale) {
		sale.setSaleProducts(saleProductDao.readBySale(sale));
		for(SaleProduct saleProduct : sale.getSaleProducts())
			saleProduct.setSale(sale);
		sale.setSaleCashRegisterMovements(saleCashRegisterMovementDao.readBySale(sale));
		for(SaleCashRegisterMovement saleCashRegisterMovement : sale.getSaleCashRegisterMovements())
			saleCashRegisterMovement.setSale(sale);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Collection<Sale> sales) {
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

	private void logDebugSale(String message,Sale sale){
		logDebug("{}. (Done={}) C={} B={} C={} VAT={} T={}",message,sale.getDone(),sale.getCost(),sale.getBalance(),sale.getCommission(),
				sale.getValueAddedTax(),sale.getTurnover());
	}
}
