package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterConsultPage extends AbstractConsultPage<CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CashRegisterDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(CashRegisterDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<CashRegister,CashRegisterDetails>(CashRegister.class, CashRegisterDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
		
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		
		FiniteStateMachine finiteStateMachine = CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent()
				.getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
		
		commandable.addChild(Builder.createCreateMany(uiManager.businessEntityInfos(SalableProductInstanceCashRegister.class),null).addParameter(identifiable)
				.addParameter(finiteStateMachine.getInitialState()));
		
		/*for(FiniteStateMachineAlphabet finiteStateMachineAlphabet : RootBusinessLayer.getInstance().getFiniteStateMachineAlphabetBusiness().findByMachine(finiteStateMachine)){
			commandable.addChild(Builder.create(null, null,"salableProductInstanceCashRegisterProcessWorkflowView").setLabel(finiteStateMachineAlphabet.getName()));
		}*/
			
	
	}

}
