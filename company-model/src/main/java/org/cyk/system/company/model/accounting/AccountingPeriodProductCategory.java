package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductCategory;

@Getter @Setter @NoArgsConstructor @Entity
public class AccountingPeriodProductCategory extends AbstractAccountingPeriodResults<ProductCategory> implements Serializable {

	private static final long serialVersionUID = -6857596944768525467L;
	
	public AccountingPeriodProductCategory(AccountingPeriod accountingPeriod,ProductCategory productCategory) {
		super(accountingPeriod,productCategory);
	}

}
