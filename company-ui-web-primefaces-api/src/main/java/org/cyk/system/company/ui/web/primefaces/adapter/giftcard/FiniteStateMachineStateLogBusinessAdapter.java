package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import org.cyk.system.root.business.impl.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifierBusinessImpl;

public class FiniteStateMachineStateLogBusinessAdapter extends FiniteStateMachineStateIdentifiableGlobalIdentifierBusinessImpl.Listener.Adapter implements Serializable {
	private static final long serialVersionUID = -4637937513750895770L;
	/*@Override
	public <T extends AbstractIdentifiable> void afterSearchIdentifiablesFind(IdentifiablesSearchCriteria<T> searchCriteria,Collection<T> identifiables) {
		if(SalableProductInstanceCashRegister.class.equals(searchCriteria.getIdentifiableClass())){
			Collection<SalableProductInstanceCashRegister> utilise = new ArrayList<>();
			Collection<String> supportingDocumentIdentifiers = new LinkedHashSet<>();
			FiniteStateMachineState utiliseFiniteStateMachineState = inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED);
			if(searchCriteria.getFiniteStateMachineStateLog().getFiniteStateMachineStates().contains(utiliseFiniteStateMachineState)){
				for(T identifiable : identifiables){
					if(((SalableProductInstanceCashRegister)identifiable).getFiniteStateMachineState().getCode().equals(utiliseFiniteStateMachineState.getCode()) ){
						utilise.add((SalableProductInstanceCashRegister) identifiable);
						supportingDocumentIdentifiers.add( ((SalableProductInstanceCashRegister)identifiable).getSalableProductInstance().getCode() );
					}
				}
				Collection<SaleCashRegisterMovement> saleCashRegisterMovements = inject(SaleCashRegisterMovementDao.class)
						.readBySupportingDocumentIdentifiers(supportingDocumentIdentifiers);
				
				for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : utilise)
					for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovements)
						if(salableProductInstanceCashRegister.getSalableProductInstance().getCode().equals(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier())){
							;//salableProductInstanceCashRegister.setCashRegister(saleCashRegisterMovement.getSale().getCashier().getCashRegister());
						}				
			}	
		}	
	}*/
}
