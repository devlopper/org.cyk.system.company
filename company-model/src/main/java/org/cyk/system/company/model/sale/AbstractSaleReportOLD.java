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
public abstract class AbstractSaleReportOLD<MODEL> extends AbstractReportTemplateFile<MODEL> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	protected String identifier,cashRegisterIdentifier,date,amountDue;

	protected AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	protected ActorReport cashier = new ActorReport();
	
	private Collection<SalableProductCollectionItemReport> saleProducts = new ArrayList<>();

	@Override
	public void generate() {
		accountingPeriod.getCompany().getGlobalIdentifier().setGenerateImage(Boolean.TRUE);
		title = "Facture";
		identifier=RandomStringUtils.randomNumeric(8);
		cashRegisterIdentifier = RandomStringUtils.randomNumeric(8);
		date=new Date().toString();
		amountDue=provider.randomInt(1, 2000000)+"";
		
		accountingPeriod.generate();
		cashier.generate();
	
	}
	
	
	
}
