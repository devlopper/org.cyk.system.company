package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyReportProducer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractRootReportProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCompanyReportProducer extends AbstractRootReportProducer implements CompanyReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCompanyReportProducer.class);
	
	@Override
	public SaleReport produceInvoice(InvoiceParameters previousStateParameters,InvoiceParameters currentStateParameters) {
		logDebug("Prepare Sale report");
		SaleReport saleReport = prepare(currentStateParameters.getSale(), currentStateParameters.getSaleStockInput(), 
				currentStateParameters.getSaleCashRegisterMovement().getCashRegisterMovement(),previousStateParameters.getAmountToPay(),
				previousStateParameters.getAmountPaid(),previousStateParameters.getAmountToOut(),Boolean.FALSE
				,previousStateParameters.getSaleStockInput()==null?null:previousStateParameters.getSaleStockInput().getStockTangibleProductMovement().getMovement().getValue(),null);
		return saleReport;
	}
	
	@Override
	public SaleReport producePaymentReceipt(ReceiptParameters previousStateParameters,ReceiptParameters currentStateParameters) {
		logDebug("Prepare Payment report");
		SaleStockTangibleProductMovementOutput saleStockOutput = currentStateParameters.getSaleStockOutput();
		SaleCashRegisterMovement saleCashRegisterMovement = saleStockOutput==null?previousStateParameters.getSaleCashRegisterMovement():saleStockOutput.getSaleCashRegisterMovement();
		Sale sale = saleStockOutput==null?saleCashRegisterMovement.getSale():saleStockOutput.getSaleStockInput().getSale();
		SaleReport saleReport = prepare(sale, saleStockOutput==null?null:saleStockOutput.getSaleStockInput(), saleCashRegisterMovement.getCashRegisterMovement()
						,previousStateParameters.getAmountToPay(),previousStateParameters.getAmountPaid(),previousStateParameters.getAmountToOut(),Boolean.TRUE
						,previousStateParameters.getNumberOfGoodsInStock(),previousStateParameters.getNumberOfGoodsDelivered());
		return saleReport;
	}
	
	private SaleReport prepare(Sale sale, SaleStockTangibleProductMovementInput saleStockInput,CashRegisterMovement cashRegisterMovement,BigDecimal amountToPay,BigDecimal amountPaid,BigDecimal amountToOut,Boolean paymentOnly
			,BigDecimal previousNumberOfGoodsInStock,BigDecimal deliveredNumberOfGoodsInStock){
		Boolean paymentExist = cashRegisterMovement.getIdentifier()!=null;
		Company company = cashRegisterMovement.getCashRegister().getOwnedCompany().getCompany();
		BigDecimal numberOfProducts = BigDecimal.ZERO;
		for(SaleProduct sp : sale.getSaleProducts())
			numberOfProducts = numberOfProducts.add(sp.getQuantity());
		
		SaleReport saleReport = new SaleReport();
		saleReport.setTitle(languageBusiness.findText(Boolean.TRUE.equals(paymentOnly)?"company.report.pointofsale.paymentreceipt":"company.report.pointofsale.invoice"));
		saleReport.setIdentifier(sale.getComputedIdentifier());
		saleReport.setCashRegisterIdentifier(Boolean.TRUE.equals(sale.getAccountingPeriod().getSaleConfiguration().getShowPointOfSaleReportCashier())?cashRegisterMovement.getCashRegister().getCode():null);
		saleReport.setDate(timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN));
		saleReport.setNumberOfProducts(numberBusiness.format(numberOfProducts));
		saleReport.setCost(numberBusiness.format(sale.getCost().getValue()));
		saleReport.setWelcomeMessage(languageBusiness.findText("company.report.pointofsale.welcome"));
		saleReport.setGoodByeMessage(languageBusiness.findText("company.report.pointofsale.goodbye"));
		
		saleReport.getAccountingPeriod().getCompany().setName(company.getName());
		saleReport.getAccountingPeriod().getCompany().setImage(rootBusinessLayer.getFileBusiness().findInputStream(company.getImage()));

		contactCollectionBusiness.load(company.getContactCollection());
		saleReport.getAccountingPeriod().getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		
		labelValue(saleReport.getHeaderInfos(),"company.report.pointofsale.invoicenumber", saleReport.getIdentifier());
		labelValue(saleReport.getHeaderInfos(),"company.report.pointofsale.paymentreceiptnumber", cashRegisterMovement.getComputedIdentifier(),paymentExist);
		labelValue("cashier",cashRegisterMovement.getCashRegister().getCode(),Boolean.TRUE.equals(sale.getAccountingPeriod().getSaleConfiguration().getShowPointOfSaleReportCashier()));
		labelValue("date", timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN));
		if(sale.getCustomer()!=null)
			labelValue("customer", sale.getCustomer().getRegistration().getCode(),sale.getCustomer()!=null);
		if(saleStockInput!=null)
			labelValue("parcel", saleStockInput.getExternalIdentifier(),saleStockInput!=null);
		
		labelValue(saleReport.getPaymentInfos(),LABEL_AMOUNT_TO_PAY, format(amountToPay));
		
		if(saleStockInput!=null){
			if(previousNumberOfGoodsInStock!=null)
				labelValue("company.report.pointofsale.numberofgoodsinstock", format(previousNumberOfGoodsInStock));
			if(deliveredNumberOfGoodsInStock!=null)
				labelValue("company.report.pointofsale.numberofgoodsdelivered", format(deliveredNumberOfGoodsInStock));
			
		}
		
		labelValue(LABEL_CASH, format(amountPaid),amountPaid.signum()>0);
		if(paymentExist){
			//BigDecimal amountToOut = saleCashRegisterMovement.getAmountIn().subtract(sale.getCost());
			if(sale.getBalance().getValue().signum()>0)
				labelValue(LABEL_AMOUNT_DU, format(sale.getBalance().getValue()));
			else
				labelValue(LABEL_AMOUNT_TO_OUT, format(amountPaid.subtract(amountToPay)));	
		}
		
		if(Boolean.TRUE.equals(paymentOnly)){
			
		}else{
			if(sale.getCost().getTax().signum()>0)
				valueAddedTaxesPart(saleReport, sale);
		}
		
		
		
		/*if(Boolean.TRUE.equals(sale.getAccountingPeriod().getShowPointOfSaleReportCashier()))
			saleReport.getHeaderInfos().add("Caisse", cashRegisterMovement.getCashRegister().getCode());
		saleReport.getHeaderInfos().add("Date", timeBusiness.formatDate(sale.getDate(),TimeBusiness.DATE_TIME_LONG_PATTERN));
		if(sale.getCustomer()!=null)
			saleReport.getHeaderInfos().add("Client", sale.getCustomer().getRegistration().getCode());
		*/
		/*saleReport.getPaymentInfos().add("Montant a payer", format(sale.getCost()));
		saleReport.getPaymentInfos().add("Especes", format(saleCashRegisterMovement.getAmountIn()));
		saleReport.getPaymentInfos().add("A rendre", format(saleCashRegisterMovement.getAmountIn().subtract(sale.getCost())));
		
		saleReport.getTaxInfos().add("Taux TVA", format(sale.getAccountingPeriod().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
		saleReport.getTaxInfos().add("Montant HT", format(sale.getCost().subtract(sale.getValueAddedTax()).setScale(2)));
		saleReport.getTaxInfos().add("TVA", format(sale.getValueAddedTax().setScale(2)));*/
		/*
		saleReport.getAccountingPeriod().getCompany().getContact().setPhoneNumbers(StringUtils.join(company.getContactCollection().getPhoneNumbers()," - "));
		saleReport.getCustomer().setRegistrationCode(sale.getCustomer()==null?"":sale.getCustomer().getRegistration().getCode());
		saleReport.getSaleCashRegisterMovement().setAmountDue(numberBusiness.format(sale.getCost()));
		if(Boolean.TRUE.equals(sale.getDone())){
			saleReport.getSaleCashRegisterMovement().setAmountIn(numberBusiness.format(saleCashRegisterMovement.getAmountIn()));
			saleReport.getSaleCashRegisterMovement().setAmountToOut(numberBusiness.format(saleCashRegisterMovement.getAmountIn().subtract(sale.getCost())));
			saleReport.getSaleCashRegisterMovement().setAmountOut(numberBusiness.format(saleCashRegisterMovement.getAmountOut()));
			if(sale.getValueAddedTax()!=null && sale.getValueAddedTax().signum()!=0){
				saleReport.getSaleCashRegisterMovement().setVatRate(numberBusiness.format(sale.getAccountingPeriod().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
				saleReport.getSaleCashRegisterMovement().setVatAmount(numberBusiness.format(sale.getValueAddedTax().setScale(2)));
				saleReport.getSaleCashRegisterMovement().setAmountDueNoTaxes(numberBusiness.format(sale.getCost().subtract(sale.getValueAddedTax()).setScale(2)));
			}
		}
		*/
		if(Boolean.TRUE.equals(paymentOnly)){
			saleReport.setSaleProducts(null);
		}else{
			if(saleStockInput==null){
				/*for(SaleProduct sp : sale.getSaleProducts()){
					SaleProductReport spr = new SaleProductReport(saleReport,sp.getProduct().getCode(),sp.getProduct().getName(),
						sp.getProduct().getPrice()==null?"":format(sp.getProduct().getPrice()),format(sp.getQuantity()),format(sp.getPrice()));
					saleReport.getSaleProducts().add(spr);
				}
				*/
			}else{
				saleReport.setSaleProducts(null);
			}
		}
		
		
		return saleReport;
	}
	
	protected void valueAddedTaxesPart(SaleReport saleReport,Sale sale){
		labelValue(saleReport.getTaxInfos(),LABEL_VAT_RATE, format(sale.getAccountingPeriod().getSaleConfiguration().getValueAddedTaxRate().multiply(new BigDecimal("100")).setScale(2))+"%");
		labelValue(LABEL_AMOUNT_VAT_EXCLUDED, format(sale.getCost().getValue().subtract(sale.getCost().getTax()).setScale(2,RoundingMode.HALF_DOWN)));
		labelValue(LABEL_VAT_AMOUNT, format(sale.getCost().getTax().setScale(2,RoundingMode.HALF_DOWN)));
	}
		
	@Override
	protected Logger __logger__() {
		return LOGGER;
	}
	
	public static final String LABEL_AMOUNT_TO_PAY = "amount.to.pay";
	public static final String LABEL_CASH = "cash";
	public static final String LABEL_AMOUNT_PAID = "";
	public static final String LABEL_AMOUNT_TO_OUT = "amount.to.out";
	public static final String LABEL_AMOUNT_DU = "company.report.pointofsale.amount.du";
	
	public static final String LABEL_VAT_RATE = "company.report.pointofsale.vat.rate";
	public static final String LABEL_VAT_AMOUNT = "company.report.pointofsale.vat.amount";
	public static final String LABEL_AMOUNT_VAT_EXCLUDED = "company.report.pointofsale.amount.vat.excluded";

}
