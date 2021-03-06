package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineStateBusiness;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

public class SaleEditPageAdapter extends SaleEditPage.Listener.Adapter.Default implements Serializable {
	private static final long serialVersionUID = -1473884331127075090L;
	@Override
	public Collection<SalableProductInstance> getSalableProductInstances(SalableProduct salableProduct,CashRegister cashRegister) {
		FiniteStateMachineState finiteStateMachineState = inject(FiniteStateMachineStateBusiness.class).find(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_RECEIVED);
		return inject(SalableProductInstanceBusiness.class).findByCollectionByCashRegisterByFiniteStateMachineState(salableProduct,cashRegister, finiteStateMachineState);
	}
	/*
	@Override
	public BigDecimal getCost(Sale sale) {
		if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
			return super.getCost(sale);
		}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
			SalableProductInstance salableProductInstance = inject(SalableProductInstanceBusiness.class)
					.find(sale.getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier());
			if(salableProductInstance==null)
				return BigDecimal.ZERO;
			return salableProductInstance.getCollection().getPrice();
		}else
			return super.getCost(sale);
	}
	
	@Override
	public void processSaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().setValue(saleCashRegisterMovement.getAmountIn());
	}*/
	
	
	
	
}
