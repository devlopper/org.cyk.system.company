package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.CompanyReportProducer.InvoiceParameters;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SalesDetails;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.product.CustomerDao;
import org.cyk.system.company.persistence.api.product.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.file.report.ReportManager;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public static Boolean AUTO_SET_SALE_DATE = Boolean.TRUE;
	
	@Inject private SaleProductBusiness saleProductBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
	@Inject private ReportManager reportManager;
	
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
		logInstanciate();
		Sale sale = new Sale();
		sale.setAccountingPeriod(accountingPeriodBusiness.findCurrent());
		sale.setCashier(cashierBusiness.findByPerson(person));
		sale.setAutoComputeValueAddedTax(sale.getAccountingPeriod().getValueAddedTaxRate().signum()!=0);
		exceptionUtils().exception(sale.getCashier()==null, "exception.sale.cashier.null");
		logInstanceCreated(sale);
		//logDebug("Sale instanciated. Cashier={} VAT in={}.",sale.getCashier().getIdentifier(),Boolean.TRUE.equals(sale.getAccountingPeriod().getValueAddedTaxIncludedInCost()));
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
		logIdentifiable("Create Sale",sale);
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		
		exceptionUtils().exception(sale.getAccountingPeriod()==null, "exception.sale.accountingperiodmissing");
		
		if(Boolean.TRUE.equals(sale.getCompleted())){
			save(sale,Boolean.TRUE);
		}else{
			sale = super.create(sale);
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				saleProduct.setSale(sale);
				genericDao.create(saleProduct);
			}
		}
		
		sale.setComputedIdentifier(generateIdentifier(sale,CompanyBusinessLayerListener.SALE_IDENTIFIER,accountingPeriodBusiness.findCurrent()
				.getSaleConfiguration().getSaleIdentifierGenerator()));
		sale = dao.update(sale);
		//notifyCrudDone(Crud.CREATE, sale);
		logIdentifiable("Sale created succesfully",sale);
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
				sale.getBalance().setValue(sale.getBalance().getValue().add(saleProduct.getPrice()));
			}
			sale.getBalance().setValue(sale.getCost());
			sale.setTurnover(sale.getCost());
			
			Customer customer = sale.getCustomer();
			if(customer!=null){
				customer.setSaleCount(customer.getSaleCount().add(BigDecimal.ONE));
				customer.setTurnover(customer.getTurnover().add(sale.getTurnover()));
				customer.setBalance(customer.getBalance().add(sale.getBalance().getValue()));
				sale.getBalance().setCumul(customer.getBalance());//to keep track of evolution
				customerDao.update(customer);
			}
		}else{
			sale.setValueAddedTax(BigDecimal.ZERO);
			sale.setTurnover(BigDecimal.ZERO);
			sale.getBalance().setValue(BigDecimal.ZERO);
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
		logIdentifiable("Sale data computed successfully", sale);
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement,Boolean produceReport) {
		create(sale);
		InvoiceParameters previous = new InvoiceParameters(sale, null, saleCashRegisterMovement);
		if(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO)){
			logDebug("No sale cash register movement");
		}else{
			saleCashRegisterMovement.setSale(sale);
			if(Boolean.TRUE.equals(sale.getDone())){
				saleCashRegisterMovement.getCashRegisterMovement().setDate(sale.getDate());
				saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
			}	 
		}
		if(Boolean.TRUE.equals(produceReport)){
			createReport(previous,new InvoiceParameters(sale, null, saleCashRegisterMovement));
		}
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		create(sale, saleCashRegisterMovement,Boolean.TRUE);
	}
	
	@Override
	public void complete(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement,Boolean produceReport) {
		logDebug("Complete Sale");
		InvoiceParameters previous = new InvoiceParameters(sale, null, saleCashRegisterMovement);
		save(sale, Boolean.FALSE);
		if(Boolean.TRUE.equals(produceReport))
			createReport(previous,new InvoiceParameters(sale, null, saleCashRegisterMovement));
		update(sale);
		logIdentifiable("Sale completed succesfully",sale);
	}
	
	@Override
	public void complete(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		complete(sale, saleCashRegisterMovement, Boolean.TRUE);
	}
	
	private void createReport(InvoiceParameters previous,InvoiceParameters current){
		SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().produceInvoice(previous,current);
		for(SaleBusinessListener listener : LISTENERS)
			listener.reportCreated(this, saleReport, Boolean.TRUE);
		
		reportManager.buildBinaryContent(current.getSale(), saleReport, current.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE); 
		update(current.getSale());
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
	public SalesDetails computeByCriteria(SaleSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
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
		return reportManager.buildBinaryContent(sales.iterator().next().getReport(),CompanyBusinessLayer.getInstance().getPointOfSaleInvoiceReportName());//TODO many receipt print must be handled
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		return findReport(Arrays.asList(sale));
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
	
}
