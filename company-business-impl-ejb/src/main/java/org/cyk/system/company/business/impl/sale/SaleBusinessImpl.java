package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyBusinessLayerListener;
import org.cyk.system.company.business.api.CompanyReportProducer.InvoiceParameters;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleProductDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineFinalStateDao;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	public static final Collection<SaleBusinessImplListener> LISTENERS = new ArrayList<>();
	public static Boolean AUTO_SET_SALE_DATE = Boolean.TRUE;
	
	@Inject private SaleProductDao saleProductDao;
	@Inject private StockableTangibleProductDao stockableTangibleProductDao;
	@Inject private FiniteStateMachineFinalStateDao finiteStateMachineFinalStateDao;
	
	@Inject
	public SaleBusinessImpl(SaleDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Sale instanciate(Person person) {
		Sale sale = new Sale();
		sale.setAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent());
		sale.setCashier(CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson(person));
		sale.setAutoComputeValueAddedTax(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().signum()!=0);
		sale.setFiniteStateMachineState(sale.getAccountingPeriod().getSaleConfiguration().getFiniteStateMachine().getInitialState());
		logInstanceCreated(sale);
		return sale;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct,BigDecimal quantity) {
		SaleProduct saleProduct = new SaleProduct();
		saleProduct.setSale(sale);
		saleProduct.setSalableProduct(salableProduct);
		saleProduct.setQuantity(quantity);
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		sale.getSaleProducts().add(saleProduct);
		//if(saleProduct.getCost().getValue()!=null)
		//	sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_VALUE, saleProduct.getCost().getValue());
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE);
		logIdentifiable("Selected", saleProduct);
		return saleProduct;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProduct selectProduct(Sale sale, SalableProduct salableProduct) {
		return selectProduct(sale, salableProduct, BigDecimal.ONE);
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
		//sale.getCost().setValue(sale.getCost().getValue().subtract(saleProduct.getCost().getValue()));
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_VALUE, saleProduct.getCost().getValue().negate());
		commonUtils.increment(BigDecimal.class, sale.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, BigDecimal.ONE.negate());
		logIdentifiable("Unselected", saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void applyChange(Sale sale, SaleProduct saleProduct) {
		CompanyBusinessLayer.getInstance().getSaleProductBusiness().process(saleProduct);
		updateCost(sale);
		logIdentifiable("Change applied", saleProduct);
	}
	
	private void updateCost(Sale sale){
		sale.getCost().setValue(BigDecimal.ZERO);
		for(SaleProduct saleProduct : sale.getSaleProducts()){
			sale.getCost().setValue(sale.getCost().getValue().add(saleProduct.getCost().getValue()));
		}
	}
	
	@Override
	public Sale create(Sale sale) {
		//logIdentifiable("Create",sale);
		if(Boolean.TRUE.equals(AUTO_SET_SALE_DATE))
			if(sale.getDate()==null)
				sale.setDate(universalTimeCoordinated());
		sale = super.create(sale);
		for(SaleProduct saleProduct : sale.getSaleProducts())
			genericDao.create(saleProduct);
		
		consume(sale);
		
		if(sale.getComputedIdentifier()==null)
			sale.setComputedIdentifier(generateIdentifier(sale,CompanyBusinessLayerListener.SALE_IDENTIFIER,sale.getAccountingPeriod().getSaleConfiguration()
				.getIdentifierGenerator()));
		
		sale = dao.update(sale);
		logIdentifiable("Created",sale);
		return sale;
	}
		
	@Override
	public void create(Sale sale, SaleCashRegisterMovement saleCashRegisterMovement) {
		//Firstly we create the sale
		create(sale);
		exceptionUtils().exception(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())==null, "exception.sale.finitestatemachinestate.notfinal");
		//Secondly we pay
		InvoiceParameters previous = new InvoiceParameters(sale, null, saleCashRegisterMovement);
		//FIXME has be done to handled sale stock issue : 0 amount and X stock out. think another better way
		if(saleCashRegisterMovement.getAmountIn().equals(saleCashRegisterMovement.getAmountOut()) && saleCashRegisterMovement.getAmountIn().equals(BigDecimal.ZERO)){
			//logDebug("No sale cash register movement");
		}else if(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())!=null) {
			saleCashRegisterMovement.setSale(sale);
			CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().create(saleCashRegisterMovement);
			sale.getBalance().setCumul(sale.getBalance().getCumul().subtract(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getValue()));	 
		}
		
		Boolean updateReport = Boolean.TRUE;
		for(SaleBusinessImplListener listener : LISTENERS){
			Boolean v = listener.isReportUpdatable(this, sale);
			if(v!=null)
				updateReport = v;
		}
		
		if(Boolean.TRUE.equals(updateReport)){
			createReport(previous,new InvoiceParameters(sale, null, saleCashRegisterMovement));
		}
	}
	
	@Override
	public void update(Sale sale,FiniteStateMachineAlphabet finiteStateMachineAlphabet) {
		exceptionUtils().exception(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())!=null, "exception.sale_state_cannotbeupdated");
		sale.setFiniteStateMachineState(RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness().findByFromStateByAlphabet(sale.getFiniteStateMachineState(), finiteStateMachineAlphabet));
		consume(sale);
		dao.update(sale);
	}
	
	private void consume(Sale sale){
		if(finiteStateMachineFinalStateDao.readByState(sale.getFiniteStateMachineState())==null){
			
		}else{
			/*
			 * It is a final state so we can compute the derived values
			 */
			
			Collection<SaleProduct> saleProducts = saleProductDao.readBySale(sale);
			Collection<TangibleProduct> tangibleProducts = new LinkedHashSet<>();
			for(SaleProduct saleProduct : saleProducts)
				if(saleProduct.getSalableProduct().getProduct() instanceof TangibleProduct)
					tangibleProducts.add((TangibleProduct) saleProduct.getSalableProduct().getProduct());
				
			for(TangibleProduct tangibleProduct : tangibleProducts){
				StockableTangibleProduct stockableTangibleProduct = stockableTangibleProductDao.readByTangibleProduct(tangibleProduct);
				if(stockableTangibleProduct==null)
					;
				else{
					BigDecimal count = BigDecimal.ZERO;
					for(SaleProduct saleProduct : saleProducts)
						if(saleProduct.getSalableProduct().getProduct().equals(stockableTangibleProduct.getTangibleProduct()))
							count = count.add(saleProduct.getQuantity());
					StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement(stockableTangibleProduct
							,RootBusinessLayer.getInstance().getMovementBusiness().instanciate(stockableTangibleProduct.getMovementCollection(), Boolean.FALSE));
					stockTangibleProductMovement.getMovement().setValue(count.negate());
					CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(stockTangibleProductMovement);
					logIdentifiable("Updated",stockableTangibleProduct);
				}
			}
			
			if(Boolean.TRUE.equals(sale.getAutoComputeValueAddedTax()))
				sale.getCost().setTax(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(sale.getAccountingPeriod(), sale.getCost().getValue()));
			sale.getCost().setTurnover(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(sale.getAccountingPeriod(), sale.getCost().getValue(), sale.getCost().getTax()));
			sale.getBalance().setValue(sale.getCost().getValue());
			
			commonUtils.increment(BigDecimal.class, sale.getBalance(), Balance.FIELD_VALUE
					,Boolean.TRUE.equals(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxIncludedInCost()) ? BigDecimal.ZERO:sale.getCost().getTax());
			
			CompanyBusinessLayer.getInstance().getProductBusiness().consume(saleProductDao.readBySale(sale));
			if(sale.getCustomer()!=null){
				CompanyBusinessLayer.getInstance().getCustomerBusiness().consume(sale);
				sale.getBalance().setCumul(sale.getCustomer().getBalance());//to keep track of evolution
			}
			CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().consume(sale);
			CompanyBusinessLayer.getInstance().getAccountingPeriodProductBusiness().consume(sale);
			
			logIdentifiable("Derived values computed",sale);
		}
	}

	private void createReport(InvoiceParameters previous,InvoiceParameters current){
		SaleReport saleReport = CompanyBusinessLayer.getInstance().getSaleReportProducer().produceInvoice(previous,current);
		for(SaleBusinessImplListener listener : LISTENERS)
			listener.reportUpdated(this, saleReport, Boolean.TRUE);
		
		RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(current.getSale(), saleReport, current.getSale().getAccountingPeriod().getSaleConfiguration().getPointOfSaleReportTemplate().getTemplate(), Boolean.TRUE); 
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
	public SaleResults computeByCriteria(SaleSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Collection<Sale> sales) {
		return RootBusinessLayer.getInstance().getReportBusiness().buildBinaryContent(sales.iterator().next().getReport(),CompanyBusinessLayer.getInstance().getPointOfSaleInvoiceReportName());//TODO many receipt print must be handled
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale) {
		return findReport(Arrays.asList(sale));
	}
		
	/**/
	
}
