package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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

	@ManyToOne @JoinColumn(name=COLUMN_OWNED_COMPANY) @NotNull private OwnedCompany ownedCompany;
	
	@Embedded private SaleConfiguration saleConfiguration = new SaleConfiguration();
	@Embedded private SaleResults saleResults = new SaleResults();
	
	@Embedded private StockConfiguration stockConfiguration = new StockConfiguration();
	@Embedded private StockResults stockResults = new StockResults();
	
	public static final String FIELD_OWNED_COMPANY = "ownedCompany";
	public static final String FIELD_SALE_CONFIGURATION = "saleConfiguration";
	public static final String FIELD_SALE_RESULTS = "saleResults";
	public static final String FIELD_STOCK_CONFIGURATION = "stockConfiguration";
	public static final String FIELD_STOCK_RESULTS = "stockResults";
	
	public static final String COLUMN_OWNED_COMPANY = FIELD_OWNED_COMPANY;
}
