package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.ui.api.model.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class ResellerProductionDetails extends AbstractOutputDetails<ResellerProduction> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String names,takenQuantity,soldQuantity,returnedQuantity,amountToPay,amountPaid,amountGap,amountGapCummul,
		discount,commission,payable,netPayable;
	
	public ResellerProductionDetails(ResellerProduction resellerProduction) {
		super(resellerProduction);
		names = resellerProduction.getResellerProductionPlan().getReseller().getPerson().getNames();
		takenQuantity = numberBusiness.format(resellerProduction.getTakenQuantity());
		soldQuantity = numberBusiness.format(resellerProduction.getSoldQuantity());
		returnedQuantity = numberBusiness.format(resellerProduction.getReturnedQuantity());
		amountPaid = numberBusiness.format(resellerProduction.getAmount().getUser());
		amountToPay = numberBusiness.format(resellerProduction.getAmount().getSystem());
		amountGap = numberBusiness.format(resellerProduction.getAmount().getGap());
		amountGapCummul = numberBusiness.format(resellerProduction.getAmountGapCumul());
		discount = numberBusiness.format(resellerProduction.getDiscount());
		commission = numberBusiness.format(resellerProduction.getCommission());
		payable = numberBusiness.format(resellerProduction.getPayable());
		netPayable = numberBusiness.format(resellerProduction.getPayableCumul());
	}
}