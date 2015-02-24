package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.CompanyValueGenerator;
import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaledProductBusiness;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.persistence.api.product.PaymentDao;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaledProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class SaleBusinessImpl extends AbstractTypedBusinessService<Sale, SaleDao> implements SaleBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CompanyValueGenerator companyValueGenerator;
	@Inject private SaledProductBusiness saledProductBusiness;
	@Inject private PaymentBusiness paymentBusiness;
	
	@Inject private SaledProductDao saledProductDao;
	@Inject private PaymentDao paymentDao;
	
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
		
		sale.setDate(universalTimeCoordinated());
		sale.setIdentificationNumber(companyValueGenerator.saleIdentificationNumber(sale));
		sale.setBalance(sale.getCost());
		sale = super.create(sale);
		for(SaledProduct saledProduct : sale.getSaledProducts()){
			saledProduct.setSale(sale);
			genericDao.create(saledProduct);
		}
		return sale;
	}
	
	@Override
	public void create(Sale sale, Payment payment) {
		create(sale);
		payment.setSale(sale);
		paymentBusiness.create(payment);
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
		sale.setPayments(paymentDao.readBySale(sale));
	}
}
