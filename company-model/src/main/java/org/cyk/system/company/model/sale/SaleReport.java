package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.root.model.party.person.ActorReport;

@Getter @Setter @NoArgsConstructor
public class SaleReport extends AbstractSaleReport<SaleReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String numberOfProducts,vatRate,vatAmount,amountDueNoTaxes,welcomeMessage,goodByeMessage;
	
	private AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	private ActorReport cashier = new ActorReport();
	private ActorReport customer = new ActorReport();
	
	private Collection<SaleProductReport> saleProducts = new ArrayList<>();

	@Override
	public void generate() {
		super.generate();
		title = "Facture";
		numberOfProducts=provider.randomInt(1, 100)+"";
		
		vatRate="18";
		amountDueNoTaxes=provider.randomInt(1, 1000000)+"";
		vatAmount=provider.randomInt(1, 1000000)+"";
		
		welcomeMessage="Welcome";
		goodByeMessage="Good bye";
		
		for(int i=0;i<6;i++){
			SaleProductReport sp = new SaleProductReport();
			sp.generate();
			sp.setSaleReport(this);
			saleProducts.add(sp);
		}
		
	}
	
	
	
}
