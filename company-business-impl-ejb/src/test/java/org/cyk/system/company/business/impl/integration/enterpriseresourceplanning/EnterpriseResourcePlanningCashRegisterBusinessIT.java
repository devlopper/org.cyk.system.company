package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.impl.AbstractCollectionItemBusinessImpl;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;

public class EnterpriseResourcePlanningCashRegisterBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private String CASH_REGISTER_001 = "CR001";
    
    @Override
    protected void businesses() {
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	assertThat("Cash register "+CASH_REGISTER_001+" exists", inject(CashRegisterBusiness.class).find(CASH_REGISTER_001)!=null);
    	assertThat("Movement collection "+CASH_REGISTER_001+" exists", inject(MovementCollectionBusiness.class).find(CASH_REGISTER_001)!=null);
    	CashRegisterMovement cashRegisterMovement = inject(CashRegisterMovementBusiness.class).instanciateOne(userAccount, inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	cashRegisterMovement.getMovement().setValue(new BigDecimal("3"));
    	create(cashRegisterMovement);
    	Movement movement = inject(MovementBusiness.class).findByCollection(cashRegisterMovement.getCashRegister().getMovementCollection()).iterator().next();
    	assertEquals(AbstractCollectionItemBusinessImpl.getRelativeCode(movement.getCollection(), movement.getCode()),cashRegisterMovement.getCode());
    }
         
}
