package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.payment.Cashier;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashierConsultPage extends AbstractConsultPage<Cashier> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/*@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		
		FiniteStateMachine finiteStateMachine = inject(AccountingPeriodBusiness.class).findCurrent()
				.getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
		
		commandable.addChild(Builder.createCreateMany(uiManager.businessEntityInfos(SalableProductInstanceCashRegister.class),null).addParameter(identifiable)
				.addParameter(finiteStateMachine.getInitialState()));
		
	}*/

}
