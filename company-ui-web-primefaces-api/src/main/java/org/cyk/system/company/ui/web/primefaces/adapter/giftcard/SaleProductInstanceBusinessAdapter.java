package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import org.cyk.system.company.business.impl.sale.SaleProductInstanceBusinessImpl;
import org.cyk.system.company.model.sale.SaleProductInstance;

public class SaleProductInstanceBusinessAdapter extends SaleProductInstanceBusinessImpl.Listener.Adapter implements Serializable {
	private static final long serialVersionUID = -8980261961629960732L;

	@Override
	public void beforeCreate(SaleProductInstance saleProductInstance) {
		/*if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(saleProductInstance.getSalableProductCollectionItem().getCollection().getProcessing().getIdentifier())){
			SalableProductInstanceCashRegister salableProductInstanceCashRegister = inject(SalableProductInstanceCashRegisterDao.class)
					.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance()
							,saleProductInstance.getSaleProduct().getSale().getCashier().getCashRegister());
			
			salableProductInstanceCashRegister.setFiniteStateMachineState(inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD));
			salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(saleProductInstance.getProcessing().getParty());
			inject(SalableProductInstanceCashRegisterBusiness.class).update(salableProductInstanceCashRegister);	
		}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(saleProductInstance.getSaleProduct().getSale().getProcessing().getIdentifier())){
			
		}*/	
	}
}
