package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductResults;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @MappedSuperclass
public abstract class AbstractAccountingPeriodResults<ENTITY extends AbstractIdentifiable> extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = -6857596944768525467L;

	@ManyToOne protected AccountingPeriod accountingPeriod;
	@ManyToOne protected ENTITY entity;
	
	@Embedded protected SaleResults saleResults = new SaleResults();
	
	@Embedded protected ProductResults productResults = new ProductResults();

	public AbstractAccountingPeriodResults(AccountingPeriod accountingPeriod,ENTITY entity) {
		super();
		this.accountingPeriod = accountingPeriod;
		this.entity = entity;
	}	
	
	public SaleResults getSaleResults(){
		if(saleResults==null)
			saleResults = new SaleResults();
		return saleResults;
	}
	
	public ProductResults getProductResults(){
		if(productResults==null)
			productResults = new ProductResults();
		return productResults;
	}
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_ENTITY = "entity";
	public static final String FIELD_SALE_RESULTS = "saleResults";
	public static final String FIELD_PRODUCT_RESULTS = "productResults";
	
}
