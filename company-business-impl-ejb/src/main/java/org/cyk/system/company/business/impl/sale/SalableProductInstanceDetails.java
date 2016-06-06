package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductInstanceDetails extends AbstractOutputDetails<SalableProductInstance> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String /*product,unit*/code;
	
	public SalableProductInstanceDetails(SalableProductInstance salableProductInstance) {
		super(salableProductInstance);
		/*product = salableProductInstance.getSalableProduct().getProduct().getCode() +Constant.CHARACTER_SLASH 
				+ formatUsingBusiness(salableProductInstance.getSalableProduct().getProduct());
		unit = salableProductInstance.getIdentificationCode();
		*/
		code = salableProductInstance.getCode();
	}
	
	/**/
	
	
}