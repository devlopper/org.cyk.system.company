package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemSaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemSaleCashRegisterMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SalableProductCollectionItemSaleCashRegisterMovementBusinessImpl extends AbstractTypedBusinessService<SalableProductCollectionItemSaleCashRegisterMovement, SalableProductCollectionItemSaleCashRegisterMovementDao> implements SalableProductCollectionItemSaleCashRegisterMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductCollectionItemSaleCashRegisterMovementBusinessImpl(SalableProductCollectionItemSaleCashRegisterMovementDao dao) {
		super(dao);
	}
	
	@Override
	protected void beforeCreate(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement) {
		super.beforeCreate(salableProductCollectionItemSaleCashRegisterMovement);
		salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().setValue(
				salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().getValue()
					.subtract(salableProductCollectionItemSaleCashRegisterMovement.getAmount())	
				);
		inject(SalableProductCollectionItemDao.class).update(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem());
		
		salableProductCollectionItemSaleCashRegisterMovement.getBalance().setValue(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem()
				.getBalance().getValue());
		
	}
	
	@Override
	protected void beforeUpdate(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement) {
		super.beforeUpdate(salableProductCollectionItemSaleCashRegisterMovement);
		
		SalableProductCollectionItemSaleCashRegisterMovement db = dao.read(salableProductCollectionItemSaleCashRegisterMovement.getIdentifier());
		
		salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().setValue(
			salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().getValue().add(db.getAmount())
			.subtract(salableProductCollectionItemSaleCashRegisterMovement.getAmount())	);
	}
	
	
	@Override
	protected void afterDelete(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement) {
		super.afterCreate(salableProductCollectionItemSaleCashRegisterMovement);
		salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().setValue(
				salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem().getBalance().getValue()
					.add(salableProductCollectionItemSaleCashRegisterMovement.getAmount())	
				);
		inject(SalableProductCollectionItemDao.class).update(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem());
	}

	@Override
	public Collection<SalableProductCollectionItemSaleCashRegisterMovement> findBySaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement) {
		return dao.readBySaleCashRegisterMovement(saleCashRegisterMovement);
	}
	
	/*@Override
	protected void afterCrud(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement, Crud crud) {
		super.afterCrud(salableProductCollectionItemSaleCashRegisterMovement, crud);
		if(ArrayUtils.contains(new Crud[]{Crud.CREATE,Crud.UPDATE,Crud.DELETE}, crud))
			if(Boolean.TRUE.equals(salableProductCollectionItemSaleCashRegisterMovement.getCascadeOperationToMaster()))
				cascadeUpdateSalableProductCollectionItemBalance(salableProductCollectionItemSaleCashRegisterMovement);
	}
		
	private void cascadeUpdateSalableProductCollectionItemBalance(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement){
		inject(SalableProductCollectionItemBusiness.class).computeBalance(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem());
		inject(SalableProductCollectionItemDao.class).update(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem());
	}*/
	
	
}
