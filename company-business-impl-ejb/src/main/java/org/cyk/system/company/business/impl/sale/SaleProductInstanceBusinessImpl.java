package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleProductInstanceBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class SaleProductInstanceBusinessImpl extends AbstractTypedBusinessService<SaleProductInstance, SaleProductInstanceDao> implements SaleProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SalableProductInstanceCashRegisterDao salableProductInstanceCashRegisterDao;
	
	@Inject
	public SaleProductInstanceBusinessImpl(SaleProductInstanceDao dao) {
		super(dao);
	}
	
	@Override
	public SaleProductInstance create(SaleProductInstance saleProductInstance) {
		exceptionUtils().exception(Boolean.TRUE.equals(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration()
				.getAllowOnlySalableProductInstanceOfCashRegister()) 
				&& salableProductInstanceCashRegisterDao.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance()
					, saleProductInstance.getSaleProduct().getSale().getCashier().getCashRegister())==null, "AllowOnlySalableProductInstanceOfCashRegister");
		
		Sale sale = saleProductInstance.getSaleProduct().getSale();
		if(sale.getAccountingPeriod().getSaleConfiguration().getSalableProductInstanceCashRegisterSaleConsumeState()==null){
			
		}else{
			SalableProductInstanceCashRegister salableProductInstanceCashRegister = salableProductInstanceCashRegisterDao
					.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance(),sale.getCashier().getCashRegister());
			salableProductInstanceCashRegister.setFiniteStateMachineState(sale.getAccountingPeriod().getSaleConfiguration()
					.getSalableProductInstanceCashRegisterSaleConsumeState());
			salableProductInstanceCashRegister.getFiniteStateMachineState().setProcessingUser(saleProductInstance.getProcessingUser());
			CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterBusiness().update(salableProductInstanceCashRegister);
		}
		
		return super.create(saleProductInstance);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProductInstance> findBySaleProduct(SaleProduct saleProduct) {
		return dao.readBySaleProduct(saleProduct);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SaleProductInstance findBySalableProductInstanceCode(String code) {
		return dao.readBySalableProductInstanceCode(code);
	}
	
}
