package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;

@Getter @Setter @NoArgsConstructor @Entity
public class AccountingPeriodProduct extends AbstractAccountingPeriodResults<Product> implements Serializable {

	private static final long serialVersionUID = -6857596944768525467L;

	public AccountingPeriodProduct(AccountingPeriod accountingPeriod,Product product) {
		super(accountingPeriod,product);
	}
	
	public Product getProduct(){
		return entity;
	}
	
}
