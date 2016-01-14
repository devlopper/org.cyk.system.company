package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.stock.StockResults;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.event.AbstractIdentifiablePeriod;

@Getter @Setter @NoArgsConstructor @Entity
public class AccountingPeriod extends AbstractIdentifiablePeriod implements Serializable {

	private static final long serialVersionUID = 3174964099221813640L;

	@ManyToOne @NotNull private OwnedCompany ownedCompany;
	
	@Embedded private SaleConfiguration saleConfiguration = new SaleConfiguration();
	@Embedded private SaleResults saleResults = new SaleResults();
	
	@Embedded private StockConfiguration stockConfiguration = new StockConfiguration();
	@Embedded private StockResults stockResults = new StockResults();
	
	@Column(nullable=false) @NotNull private Boolean closed = Boolean.FALSE;
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,saleConfiguration,saleResults);
	}
	
	private static final String LOG_FORMAT = AccountingPeriod.class.getSimpleName()+"(%s %s)";

}
