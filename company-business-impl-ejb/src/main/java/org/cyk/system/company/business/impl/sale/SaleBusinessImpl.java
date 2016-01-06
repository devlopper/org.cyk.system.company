package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.CompanyReportProducer.InvoiceParameters;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SalesDetails;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public static Boolean AUTO_SET_SALE_DATE = Boolean.TRUE;
	
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
		//logInstanciate();
		Sale sale = new Sale();
		sale.setAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent());
		sale.setCashier(CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson(person));
		sale.setAutoComputeValueAddedTax(sale.getAccountingPeriod().getValueAddedTaxRate().signum()!=0);
		logInstanceCreated(sale);
		//logDebug("Sale instanciated. Cashier={} VAT in={}.",sale.getCashier().getIdentifier(),Boolean.TRUE.equals(sale.getAccountingPeriod().getValueAddedTaxIncludedInCost()));
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct,BigDecimal quantity) {
		//logTrace("Select Sale product {}",salableProduct.getProduct().getCode());
		SaleProduct saleProduct = new SaleProduct();
		saleProduct.setSale(sale);
		saleProduct.setSalableProduct(salableProduct);
		saleProduct.setQuantity(quantity);
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		sale.getSaleProducts().add(saleProduct);
		if(saleProduct.getCost().getValue()!=null)
			sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		return saleProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct) {
		return selectProduct(sale, salableProduct, BigDecimal.ONE);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void unselectProduct(Sale sale, SaleProduct saleProduct) {
		//logTrace("Unselect Sale product {}",saleProduct.getSalableProduct().getProduct().getCode());
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
		sale.getCost().setValue(sale.getCost().getValue().subtract(saleProduct.getCost().getValue()));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void applyChange(Sale sale, SaleProduct saleProduct) {
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		updateCost(sale);
	}
	
	private void updateCost(Sale sale){
		sale.getCost().setValue(BigDecimal.ZERO);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		}
	}
	
	@Override
	public Sale create(Sale sale) {
		logIdentifiable("Create",sale);
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		
		if(sale.getAccountingPeriod()==null)
			sale.setAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent());
		
		if(Boolean.TRUE.equals(sale.getCompleted())){
			save(sale,Boolean.TRUE);
		}else{
			sale = super.create(sale);
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				saleProduct.setSale(sale);
				genericDao.create(saleProduct);
			}
		}
		if(sale.getComputedIdentifier()==null)
			sale.setComputedIdentifier(generateIdentifier(sale,CompanyBusinessLayerListener.SALE_IDENTIFIER,CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent()
				.getSaleConfiguration().getSaleIdentifierGenerator()));
		sale = dao.update(sale);
		//notifyCrudDone(Crud.CREATE, sale);
		logIdentifiable("Created",sale);
		return sale;
	}
	
	private void save(Sale sale,Boolean creation){
		//logDebug("Saving {}",sale.getLogMessage());
		exceptionUtils().exception(Boolean.TRUE.equals(sale.getDone()), "exception.sale.done.already");
		
		if(Boolean.TRUE.equals(creation)){
			
		}else{
			for(SaleProduct saleProduct : sale.getSaleProducts()){
				CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);				
			}
			updateCost(sale);
		}
				
		if(Boolean.TRUE.equals(sale.getCompleted())){
			CompanyBusinessLayer.getInstance().getProductBusiness().consume(sale.getSaleProducts());
			AccountingPeriod accountingPeriod = sale.getAccountingPeriod();
			accountingPeriod.getSalesResults().setCount(sale.getAccountingPeriod().getSalesResults().getCount().add(BigDecimal.ONE));
			accountingPeriod.getSalesResults().setTurnover(accountingPeriod.getSalesResults().getTurnover().add(sale.getCost().getTurnover()));
			accountingPeriodDao.update(accountingPeriod);
			
			/*for(SaleProduct saleProduct : sale.getSaleProducts()){
				sale.getCost().setTax(sale.getCost().getTax().add(saleProduct.getCost().getTax()));
				//FIXME is it necessary since it is updated later???
				sale.getCost().setTurnover(sale.getCost().getTurnover().add(saleProduct.getCost().getTurnover()));
				sale.getBalance().setValue(sale.getBalance().getValue().add(saleProduct.getCost().getValue()));
			}*/
			
			if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
				sale.getCost().setTax(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(accountingPeriod, sale.getCost().getValue()));
				
			sale.getCost().setTurnover(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(accountingPeriod, sale.getCost().getValue(), sale.getCost().getTax()));
			
			sale.getBalance().setValue(sale.getCost().getValue().add( Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost()) ? BigDecimal.ZERO
					:sale.getCost().getTax()));
			
			Customer customer = sale.getCustomer();
			if(customer!=null){
				customer.setSaleCount(customer.getSaleCount().add(BigDecimal.ONE));
				customer.setTurnover(customer.getTurnover().add(sale.getCost().getTurnover()));
				customer.setBalance(customer.getBalance().add(sale.getBalance().getValue()));
				sale.getBalance().setCumul(customer.getBalance());//to keep track of evolution
				customerDao.update(customer);
			}
		}else{
			sale.getCost().setTax(BigDecimal.ZERO);
			sale.getCost().setTurnover(BigDecimal.ZERO);
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
			CompanyBusinessLayer.getInstance().getAccountingPeriodProductBusiness().consume(sale.getAccountingPeriod(), sale.getSaleProducts());
		}else{
			
		}
		//logIdentifiable("Sale data computed successfully", sale);
	}
	
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement,Boolean produceReport) {
		//Firstly we create the sale
		create(sale);
		//Secondly we pay
		InvoiceParameters previous = new InvoiceParameters(sale, null, saleCashRegisterMovement);
		//FIXME has be done to handled sale stock issue : 0 amount and X stock out. think another better way
		if(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO)){
			//logDebug("No sale cash register movement");
		}else{
			saleCashRegisterMovement.setSale(sale);
			if(Boolean.TRUE.equals(sale.getDone())){
				CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().create(saleCashRegisterMovement);
				sale.getBalance().setCumul(sale.getBalance().getCumul().subtract(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));
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
		logIdentifiable("Completing",sale);
		InvoiceParameters previous = new InvoiceParameters(sale, null, saleCashRegisterMovement);
		save(sale, Boolean.FALSE);
		if(Boolean.TRUE.equals(produceReport))
			createReport(previous,new InvoiceParameters(sale, null, saleCashRegisterMovement));
		update(sale);
		logIdentifiable("Completed",sale);
	}
	
	@Override
	public void complete(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		complete(sale, saleCashRegisterMovement, Boolean.TRUE);
	}
	
	private void createReport(InvoiceParameters previous,InvoiceParameters current){
		SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().produceInvoice(previous,current);
		for(SaleBusinessListener listener : LISTENERS)
			listener.reportCreated(this, saleReport, Boolean.TRUE);
		
		RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(current.getSale(), saleReport, current.getSale().getAccountingPeriod().getPointOfSaleReportFile(), Boolean.TRUE); 
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
		return RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(sales.iterator().next().getReport(),CompanyBusinessLayer.getInstance().getPointOfSaleInvoiceReportName());//TODO many receipt print must be handled
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		return findReport(Arrays.asList(sale));
	}
	
	@Override
	public void updateDelivery(Sale sale,Collection<ProductEmployee> productEmployees) {
		/*for(ProductEmployee productEmployee : productEmployees)
			sale.getPerformers().add(productEmployee);
		
		Set<ProductEmployee> delete = new HashSet<>();
		for(ProductEmployee productEmployee : sale.getPerformers())
			if( !productEmployees.contains(productEmployee) )
				delete.add(productEmployee);
		
		for(ProductEmployee productEmployee : delete)
			sale.getPerformers().remove(productEmployee);
		
		update(sale);
		*/	
	}
	
	/**/
	
}
