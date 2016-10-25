package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.event.Event;
import org.cyk.ui.web.primefaces.page.AbstractCollectionItemEditPage;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementTermEditPage extends AbstractCollectionItemEditPage.AbstractDefault<CashRegisterMovementTerm,CashRegisterMovementTermCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected AbstractCollectionItem<?> getItem() {
		return identifiable;
	}
	
	@Getter @Setter @FieldOverride(name=AbstractForm.FIELD_COLLECTION,type=CashRegisterMovementTermCollection.class)
	public static class Form extends AbstractForm.AbstractDefault<CashRegisterMovementTerm,CashRegisterMovementTermCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputNumber @NotNull private BigDecimal amount;
		@Input @InputChoice @InputOneChoice @InputOneCombo private Event event;
		
		public static final String FIELD_EVENT = "event";
		public static final String FIELD_AMOUNT = "amount";
		
		
	}

	

}
