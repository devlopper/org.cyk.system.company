package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Binding;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleStockInputQueryResultFormModel extends AbstractFormModel<SaleStockInput> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText
	private String date;
	
	@Input @InputText @Binding(field="identificationNumber")
	private String number;
	
	@Input @InputText
	private String customer;
	
	@Input @InputText
	private String numberOfGoods;
	
	@Input @InputText
	private String cost;
	
	@Input @InputText
	private String balance;
	
	@Override
	public void read() {
		super.read();
		this.number = identifiable.getSale().getIdentificationNumber();
		this.cost = UIManager.getInstance().getNumberBusiness().format(identifiable.getSale().getCost());
		this.date = UIManager.getInstance().getTimeBusiness().formatDateTime(identifiable.getSale().getDate());
		this.balance = UIManager.getInstance().getNumberBusiness().format(identifiable.getSale().getBalance().getValue());
		this.numberOfGoods = UIManager.getInstance().getNumberBusiness().format(identifiable.getTangibleProductStockMovement().getQuantity());
		
		if(identifiable.getSale().getCustomer()!=null)
			customer = identifiable.getSale().getCustomer().getRegistration().getCode()+"/"+
				identifiable.getSale().getCustomer().getPerson().getNames();
	}
	
}
