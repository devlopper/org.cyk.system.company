package org.cyk.system.company.model.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.event.AbstractIdentifiablePeriod;
import org.cyk.system.root.model.file.File;

@Getter @Setter @NoArgsConstructor @Entity
public class AccountingPeriod extends AbstractIdentifiablePeriod implements Serializable {

	private static final long serialVersionUID = 3174964099221813640L;

	@ManyToOne @NotNull private OwnedCompany ownedCompany;
	
	@ManyToOne private File pointOfSaleReportFile;
	
	/**
	 * Zero means no taxes are collected
	 */
	@Column(precision=PERCENT_PRECISION,scale=PERCENT_SCALE,nullable=false)
	@NotNull
	private BigDecimal valueAddedTaxRate = BigDecimal.ZERO;
	@Column(nullable=false) @NotNull private Boolean valueAddedTaxIncludedInCost = Boolean.TRUE;
	
	@Column(nullable=false) @NotNull private Boolean closed = Boolean.FALSE;
	
	@Embedded private SalesResults salesResults = new SalesResults();
	
}
