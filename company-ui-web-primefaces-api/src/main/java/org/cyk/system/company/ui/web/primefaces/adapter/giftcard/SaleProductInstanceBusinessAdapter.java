package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.business.impl.sale.SaleProductInstanceBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;

public class SaleProductInstanceBusinessAdapter extends SaleProductInstanceBusinessImpl.Listener.Adapter implements Serializable {
	private static final long serialVersionUID = -8980261961629960732L;

	@Override
	public void beforeCreate(SaleProductInstance saleProductInstance) {
		if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(saleProductInstance.getSaleProduct().getSale().getProcessing().getIdentifier())){
			SalableProductInstanceCashRegister salableProductInstanceCashRegister = inject(SalableProductInstanceCashRegisterDao.class)
					.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance()
							,saleProductInstance.getSaleProduct().getSale().getCashier().getCashRegister());
			
			salableProductInstanceCashRegister.setFiniteStateMachineState(inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD));
			salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(saleProductInstance.getProcessing().getParty());
			inject(SalableProductInstanceCashRegisterBusiness.class).update(salableProductInstanceCashRegister);	
		}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(saleProductInstance.getSaleProduct().getSale().getProcessing().getIdentifier())){
			
		}	
	}
}
