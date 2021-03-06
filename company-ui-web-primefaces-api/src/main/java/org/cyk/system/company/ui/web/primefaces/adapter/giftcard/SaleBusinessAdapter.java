package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.sale.Sale;

public class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
	private static final long serialVersionUID = 6995530308698881653L;
	
	@Override
	public void beforeCreate(Sale sale) {
		if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
			
		}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
			/*
			ExceptionUtils.getInstance().exception(sale.getSaleCashRegisterMovements().isEmpty(), "exception.giftacrdinstancerequired");
			String giftCardIdentifier = sale.getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier();
			SalableProductInstance salableProductInstance = inject(SalableProductInstanceDao.class).read(giftCardIdentifier);
			ExceptionUtils.getInstance().exception(salableProductInstance ==null, "exception.giftacrdinstancedoesnotexist");
			Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = inject(SalableProductInstanceCashRegisterDao.class).readBySalableProductInstanceByFiniteStateMachineState(salableProductInstance,
							inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_SOLD));
			*/
			//No product selling while using gift card
			//while( !sale.getSaleProducts().isEmpty()  )
			//	inject(SaleBusiness.class).unselectProduct(sale, sale.getSaleProducts().iterator().next());
			//sale.getSalableProductCollection().getItems().getElements().clear();
			
			//change state to used
			
			/*
			for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters){
				salableProductInstanceCashRegister.setFiniteStateMachineState(inject(FiniteStateMachineStateDao.class).read(CompanyConstant.GIFT_CARD_WORKFLOW_STATE_USED));
				salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(sale.getProcessing().getParty());
			}
			inject(SalableProductInstanceCashRegisterBusiness.class).update(salableProductInstanceCashRegisters);
			*/
		}
	}
	
}
