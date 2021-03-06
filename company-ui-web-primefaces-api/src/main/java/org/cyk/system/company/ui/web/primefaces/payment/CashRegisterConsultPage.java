package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementCollectionConsultPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterConsultPage extends AbstractMovementCollectionConsultPage<CashRegister,CashRegisterMovement,CashRegisterMovementDetails> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected Collection<CashRegisterMovement> findByCollection(CashRegister cashRegister) {
		return inject(CashRegisterMovementBusiness.class).findByCashRegister(cashRegister);
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		/*
		FiniteStateMachine finiteStateMachine = inject(AccountingPeriodBusiness.class).findCurrent()
				.getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
		
		commandable.addChild(Builder.createCreateMany(uiManager.businessEntityInfos(SalableProductInstanceCashRegister.class),null).addParameter(identifiable)
				.addParameter(finiteStateMachine.getInitialState()));
		*/
		/*for(FiniteStateMachineAlphabet finiteStateMachineAlphabet : RootBusinessLayer.getInstance().getFiniteStateMachineAlphabetBusiness().findByMachine(finiteStateMachine)){
			commandable.addChild(Builder.create(null, null,"salableProductInstanceCashRegisterProcessWorkflowView").setLabel(finiteStateMachineAlphabet.getName()));
		}*/
			
	
	}

}
