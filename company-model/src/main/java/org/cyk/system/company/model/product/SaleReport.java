package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.company.model.accounting.AccountingPeriodReport;
import org.cyk.system.root.model.file.report.LabelValueCollection;
import org.cyk.system.root.model.party.person.ActorReport;
import org.cyk.utility.common.generator.AbstractGeneratable;

@Getter @Setter @NoArgsConstructor
public class SaleReport extends AbstractGeneratable<SaleReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private LabelValueCollection headerInfos = new LabelValueCollection();
	private LabelValueCollection paymentInfos = new LabelValueCollection();
	private LabelValueCollection taxInfos = new LabelValueCollection();
	
	private String title,identifier,cashRegisterIdentifier,date,numberOfProducts,cost,welcomeMessage,goodByeMessage;
	private Boolean done;
	
	private AccountingPeriodReport accountingPeriod = new AccountingPeriodReport();
	private ActorReport cashier = new ActorReport();
	private ActorReport customer = new ActorReport();
	private SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();
	private SaleStockInputReport saleStockInputReport = new SaleStockInputReport();
	
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
		welcomeMessage="Welcome";
		goodByeMessage="Good bye";
		
		accountingPeriod.generate();
		cashier.generate();
		customer.generate();
		saleCashRegisterMovement.generate();
		saleStockInputReport.generate();
		for(int i=0;i<6;i++){
			SaleProductReport sp = new SaleProductReport();
			sp.generate();
			sp.setSaleReport(this);
			saleProducts.add(sp);
		}
		
		headerInfos.add(null,"Identifiant", identifier);
		headerInfos.add(null,"Caisse", cashier.getRegistrationCode());
		headerInfos.add(null,"Date", date);
		headerInfos.add(null,"Client", customer.getRegistrationCode());
		
		paymentInfos.add(null,"A payer", saleCashRegisterMovement.getAmountDue());
		paymentInfos.add(null,"Especes", saleCashRegisterMovement.getAmountIn());
		paymentInfos.add(null,"A rendre", saleCashRegisterMovement.getAmountToOut());
		
		taxInfos.add(null,"Taux TVA", saleCashRegisterMovement.getVatRate());
		taxInfos.add(null,"Montant Hors Taxe", saleCashRegisterMovement.getAmountDueNoTaxes());
		taxInfos.add(null,"TVA", saleCashRegisterMovement.getVatAmount());
		
	}
	
	
	
}
