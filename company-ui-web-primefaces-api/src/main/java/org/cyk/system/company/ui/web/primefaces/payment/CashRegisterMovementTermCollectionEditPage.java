package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementTermCollectionEditPage extends AbstractCollectionEditPage.Extends<CashRegisterMovementTermCollection,CashRegisterMovementTerm,AbstractItemCollectionItem<CashRegisterMovementTerm>> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected AbstractCollection<?> getCollection() {
		return identifiable;
	}
	
	@Getter @Setter
	public static class Form extends AbstractForm.Extends<CashRegisterMovementTermCollection,CashRegisterMovementTerm> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputNumber @NotNull private BigDecimal amount;
		
		public static final String FIELD_AMOUNT = "amount";

	}

	

}
