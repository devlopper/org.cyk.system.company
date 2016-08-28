package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class SalableProductCollection extends AbstractCollection<SalableProductCollectionItem> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne private AccountingPeriod accountingPeriod;
	
	@Embedded private Cost cost = new Cost();
	
	/**/
	
	@Transient protected Boolean autoComputeValueAddedTax = Boolean.TRUE;
	
	
}
