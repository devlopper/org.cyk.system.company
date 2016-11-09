package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;

public class EnterpriseResourcePlanningSaleBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private String CASH_REGISTER_001 = "CR001";
    private String SALE_001 = "S001";
    
    @Override
    protected void businesses() {
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	
    	create(inject(SaleBusiness.class).instanciateOneRandomly(SALE_001));
    	assertThat("Sale "+SALE_001+" exists", inject(SaleBusiness.class).find(SALE_001)!=null);
    	assertThat("Salable product collection "+SALE_001+" exists", inject(SalableProductCollectionBusiness.class).find(SALE_001)!=null);
    	
    	SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(SALE_001), inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCashRegisterMovement().getCode());
    }
         
}
