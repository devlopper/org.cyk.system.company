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

	/*
	public SaleReport(String identifier, String cashRegisterIdentifier,
			String date, String numberOfProducts, String cost,
			String welcomeMessage, String goodByeMessage) {
		super();
		this.identifier = identifier;
		this.cashRegisterIdentifier = cashRegisterIdentifier;
		this.date = date;
		this.numberOfProducts = numberOfProducts;
		this.cost = cost;
		this.welcomeMessage = welcomeMessage;
		this.goodByeMessage = goodByeMessage;
	}*/
	
	@Override
	public void generate() {
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
		
		headerInfos.add("Identifiant", identifier);
		headerInfos.add("Caisse", cashier.getRegistrationCode());
		headerInfos.add("Date", date);
		headerInfos.add("Client", customer.getRegistrationCode());
		
		paymentInfos.add("A payer", saleCashRegisterMovement.getAmountDue());
		paymentInfos.add("Especes", saleCashRegisterMovement.getAmountIn());
		paymentInfos.add("A rendre", saleCashRegisterMovement.getAmountToOut());
		
		taxInfos.add("Taux TVA", saleCashRegisterMovement.getVatRate());
		taxInfos.add("Montant Hors Taxe", saleCashRegisterMovement.getAmountDueNoTaxes());
		taxInfos.add("TVA", saleCashRegisterMovement.getVatAmount());
		
	}
	
	
	
}
