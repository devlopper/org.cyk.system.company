package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.structure.Division;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter @Deprecated
public abstract class AbstractServiceFormModel extends AbstractFormModel<Product> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText
	protected String code;
	
	@Input @InputText
	protected String name;
	
	@Input @InputNumber
	protected String price;
	
	//@Input @InputChoice @InputOneChoice @InputOneCombo
	protected Division division;
	
}
