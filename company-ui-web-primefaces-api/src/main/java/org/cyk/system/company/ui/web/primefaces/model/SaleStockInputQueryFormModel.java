package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleStockInputQueryFormModel extends DefaultQueryFormModel implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText
	private String externalIdentifier;
	
	
}
