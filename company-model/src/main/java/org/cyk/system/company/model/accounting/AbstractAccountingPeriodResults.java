package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @MappedSuperclass
public abstract class AbstractAccountingPeriodResults<ENTITY extends AbstractIdentifiable> extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = -6857596944768525467L;

	@ManyToOne protected AccountingPeriod accountingPeriod;
	@ManyToOne protected ENTITY entity;
	
	@Embedded protected SaleResults saleResults = new SaleResults();
	
	@Embedded protected ProductResults results;

	public AbstractAccountingPeriodResults(AccountingPeriod accountingPeriod,ENTITY entity) {
		super();
		this.accountingPeriod = accountingPeriod;
		this.entity = entity;
	}	
	
	@Override
	public String toString() {
		return entity+"/"+saleResults;
	}
	
	@Override
	public String getUiString() {
		return toString();
	}
	
}
