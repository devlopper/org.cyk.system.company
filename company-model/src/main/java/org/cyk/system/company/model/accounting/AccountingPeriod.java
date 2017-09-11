package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.sale.SaleConfiguration;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.stock.StockConfiguration;
import org.cyk.system.company.model.stock.StockResults;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.time.AbstractIdentifiablePeriod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class AccountingPeriod extends AbstractIdentifiablePeriod implements Serializable {

	private static final long serialVersionUID = 3174964099221813640L;

	@ManyToOne @NotNull private OwnedCompany ownedCompany;
	
	@Embedded private SaleConfiguration saleConfiguration = new SaleConfiguration();
	@Embedded private SaleResults saleResults = new SaleResults();
	
	@Embedded private StockConfiguration stockConfiguration = new StockConfiguration();
	@Embedded private StockResults stockResults = new StockResults();
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,saleConfiguration.getLogMessage(),saleResults.getLogMessage());
	}
	
	private static final String LOG_FORMAT = AccountingPeriod.class.getSimpleName()+"(%s %s)";

	public static final String FIELD_OWNED_COMPANY = "ownedCompany";
	public static final String FIELD_SALE_CONFIGURATION = "saleConfiguration";
	public static final String FIELD_SALE_RESULTS = "saleResults";
	public static final String FIELD_STOCK_CONFIGURATION = "stockConfiguration";
	public static final String FIELD_STOCK_RESULTS = "stockResults";
	
}
