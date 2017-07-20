package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanCheck;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementModeEditPage extends AbstractCrudOnePage<CashRegisterMovementMode> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Getter @Setter
	public static class Form extends AbstractBusinessIdentifiedEditFormModel<CashRegisterMovementMode> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputBooleanCheck private Boolean supportDocumentIdentifier;
		
		public static final String FIELD_SUPPORT_DOCUMENT_IDENTIFIER = "supportDocumentIdentifier";
		
	}

}
