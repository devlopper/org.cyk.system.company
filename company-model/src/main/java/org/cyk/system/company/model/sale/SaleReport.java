package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.system.root.model.party.person.ActorReport;

@Getter @Setter @NoArgsConstructor
public class SaleReport extends AbstractReportTemplateFile<SaleReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private String title,identifier,cashRegisterIdentifier,date,numberOfProducts,cost,vatRate,vatAmount,amountDueNoTaxes,welcomeMessage,goodByeMessage;
	
	private AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	private ActorReport cashier = new ActorReport();
	private ActorReport customer = new ActorReport();
	
	private Collection<SaleProductReport> saleProducts = new ArrayList<>();

	@Override
	public void generate() {
		accountingPeriod.getCompany().setGenerateImage(Boolean.TRUE);
		title = "Facture";
		identifier=RandomStringUtils.randomNumeric(8);
		cashRegisterIdentifier = RandomStringUtils.randomNumeric(8);
		date=new Date().toString();
		numberOfProducts=provider.randomInt(1, 100)+"";
		cost=provider.randomInt(1, 2000000)+"";
		
		vatRate="18";
		amountDueNoTaxes=provider.randomInt(1, 1000000)+"";
		vatAmount=provider.randomInt(1, 1000000)+"";
		
		welcomeMessage="Welcome";
		goodByeMessage="Good bye";
		
		accountingPeriod.generate();
		cashier.generate();
		customer.generate();
		
		for(int i=0;i<6;i++){
			SaleProductReport sp = new SaleProductReport();
			sp.generate();
			sp.setSaleReport(this);
			saleProducts.add(sp);
		}
		
	}
	
	
	
}
