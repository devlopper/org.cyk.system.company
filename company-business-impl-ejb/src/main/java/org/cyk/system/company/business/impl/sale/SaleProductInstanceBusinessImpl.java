package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleProductInstanceBusiness;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.utility.common.ListenerUtils;

@Stateless
public class SaleProductInstanceBusinessImpl extends AbstractTypedBusinessService<SaleProductInstance, SaleProductInstanceDao> implements SaleProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaleProductInstanceBusinessImpl(SaleProductInstanceDao dao) {
		super(dao);
	}
	
	@Override
	public SaleProductInstance create(final SaleProductInstance saleProductInstance) {
		/*exceptionUtils().exception(Boolean.TRUE.equals(inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration()
				.getAllowOnlySalableProductInstanceOfCashRegister()) 
				&& salableProductInstanceCashRegisterDao.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance()
					, saleProductInstance.getSaleProduct().getSale().getCashier().getCashRegister())==null, "AllowOnlySalableProductInstanceOfCashRegister");
		*/
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeCreate(saleProductInstance);
			}});
		
		/*Sale sale = saleProductInstance.getSaleProduct().getSale();
		if(sale.getAccountingPeriod().getSaleConfiguration().getSalableProductInstanceCashRegisterSaleConsumeState()==null){
			
		}else{
			SalableProductInstanceCashRegister salableProductInstanceCashRegister = salableProductInstanceCashRegisterDao
					.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance(),sale.getCashier().getCashRegister());
			salableProductInstanceCashRegister.setFiniteStateMachineState(sale.getAccountingPeriod().getSaleConfiguration()
					.getSalableProductInstanceCashRegisterSaleConsumeState());
			salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(saleProductInstance.getProcessing().getParty());
			inject(SalableProductInstanceCashRegisterBusiness.class).update(salableProductInstanceCashRegister);
			
		}
		*/
		return super.create(saleProductInstance);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProductInstance> findBySaleProduct(SalableProductCollectionItem saleProduct) {
		return dao.readBySaleProduct(saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProductInstance findBySalableProductInstanceCode(String code) {
		return dao.readBySalableProductInstanceCode(code);
	}
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<SaleProductInstance> {
		
		Collection<Listener> COLLECTION = new ArrayList<>();

		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<SaleProductInstance> implements Listener,Serializable{
			private static final long serialVersionUID = 8213436661982661753L;
			
		}
		
	}
	
}
