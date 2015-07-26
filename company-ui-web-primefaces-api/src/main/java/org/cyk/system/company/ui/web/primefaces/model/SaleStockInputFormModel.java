package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanButton;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.annotation.user.interfaces.Text.ValueType;

@Getter @Setter
public class SaleStockInputFormModel extends AbstractFormModel<SaleStockInput> implements Serializable {

	private static final long serialVersionUID = -7403076234556118486L;
	
	@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Customer customer;
	
	@Input(label=@Text(type=ValueType.ID,value="ui.form.editsalestockinput.field.external.identifier")) 
	@InputText @NotNull private String externalIdentifier;
	
	@Input @InputTextarea @NotNull
	private String comments;
	
	@Input @InputNumber @NotNull //@Size(min=1,max=Integer.MAX_VALUE) 
	private BigDecimal quantity;
	
	@Input @InputNumber @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
	private BigDecimal price;
	
	private Boolean commissionInPercentage = Boolean.TRUE;
	
	@Input @InputNumber @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
	private BigDecimal commission;
	
	@Input @InputBooleanButton @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
	private Boolean valueAddedTaxable;
	
	@Input(readOnly=true) @InputText @NotNull 
	private String totalCost; // used for read only value
	
	@Input(readOnly=true) @InputText @NotNull 
	private String valueAddedTax; // used for read only value
	
	private SaleProduct saleProduct;
	
	@Override
	public void read() {
		super.read();
		this.customer = identifiable.getSale().getCustomer();
		this.externalIdentifier = identifiable.getExternalIdentifier();
		this.price = identifiable.getSale().getCost();
		this.quantity = identifiable.getTangibleProductStockMovement().getQuantity();
		this.saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
		this.commission = saleProduct.getCommission();
		this.valueAddedTaxable = BigDecimal.ZERO.compareTo(identifiable.getSale().getValueAddedTax()) != 0;
		this.valueAddedTax = numberBusiness.format(identifiable.getSale().getValueAddedTax());
		this.comments = identifiable.getSale().getComments();
	}
	
	@Override
	public void write() {
		super.write();
		identifiable.getSale().setCustomer(customer);
		identifiable.setExternalIdentifier(externalIdentifier);
		identifiable.getSale().setAutoComputeValueAddedTax(valueAddedTaxable);
		identifiable.getSale().setComments(comments);
		if(Boolean.TRUE.equals(commissionInPercentage)){
			saleProduct.setCommission(numberBusiness.computePercentage(identifiable.getSale().getCost(),commission));
		}else
			saleProduct.setCommission(commission);
		identifiable.getTangibleProductStockMovement().setQuantity(quantity);
		SaleProduct saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
		saleProduct.setPrice(price);
	}
}