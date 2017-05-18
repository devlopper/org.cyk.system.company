package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementCollectionDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.security.UserAccount;

public class SaleCashRegisterMovementCollectionBusinessImpl extends AbstractCollectionBusinessImpl<SaleCashRegisterMovementCollection,SaleCashRegisterMovement, SaleCashRegisterMovementCollectionDao,SaleCashRegisterMovementDao,SaleCashRegisterMovementBusiness> implements SaleCashRegisterMovementCollectionBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;
	
	@Inject
	public SaleCashRegisterMovementCollectionBusinessImpl(SaleCashRegisterMovementCollectionDao dao) {
		super(dao); 
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTION;
	}
	
	@Override
	protected Object[] getPropertyValueTokens(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE}, name))
			return new Object[]{saleCashRegisterMovementCollection.getCashRegisterMovement()};
		return super.getPropertyValueTokens(saleCashRegisterMovementCollection, name);
	}
	
	@Override
	public SaleCashRegisterMovementCollection instanciateOne(String code, String name) {
		SaleCashRegisterMovementCollection saleCashRegisterMovementCollection =  super.instanciateOne(code, name);
		saleCashRegisterMovementCollection.setAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent());
		return saleCashRegisterMovementCollection;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovementCollection instanciateOne(String code, String name, String cashRegisterCode,String cashRegisterMovementModeCode,Object[][] saleCashRegisterMovements) {
		SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = instanciateOne(code,name);
		saleCashRegisterMovementCollection.setCashRegisterMovement(inject(CashRegisterMovementBusiness.class).instanciateOne(code,name,BigDecimal.ZERO.toString(),cashRegisterCode,cashRegisterMovementModeCode));
		
		if(saleCashRegisterMovements!=null)
			for(Object[] saleCashRegisterMovement : saleCashRegisterMovements)
				inject(SaleCashRegisterMovementBusiness.class).instanciateOne(saleCashRegisterMovementCollection, (String)saleCashRegisterMovement[0], (String)saleCashRegisterMovement[1]);
		return saleCashRegisterMovementCollection;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovementCollection instanciateOne(String code, String name, String cashRegisterCode,Object[][] saleCashRegisterMovements) {
		return instanciateOne(code, name, cashRegisterCode, CompanyConstant.Code.CashRegisterMovementMode.CASH, saleCashRegisterMovements);
	}
	
	@Override
	public SaleCashRegisterMovementCollection instanciateOne(UserAccount userAccount) {
		return instanciateOne(null, null, CompanyConstant.Code.CashRegister.DEFAULT, null);
	}
	
	/*@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SaleCashRegisterMovement instanciateOne(UserAccount userAccount,Sale sale,CashRegister cashRegister) {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateOne();
		setSale(saleCashRegisterMovement, sale);
		saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement());
		setCashRegister(userAccount, saleCashRegisterMovement, cashRegister);
		/*for(SalableProductCollectionItem salableProductCollectionItem : inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()))
			saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().getCollection().add(
					new SalableProductCollectionItemSaleCashRegisterMovement(salableProductCollectionItem,saleCashRegisterMovement));
		
		return saleCashRegisterMovement;
	}*/
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void setCashRegister(UserAccount userAccount,SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,CashRegister cashRegister) {
		inject(CashRegisterMovementBusiness.class).setCashRegister(saleCashRegisterMovementCollection.getCashRegisterMovement(), cashRegister);
		/*saleCashRegisterMovementCollection.getCashRegisterMovement().setCashRegister(cashRegister);
		saleCashRegisterMovementCollection.getCashRegisterMovement().setMovement(cashRegister == null ? null 
				: inject(MovementBusiness.class).instanciateOne(cashRegister.getMovementCollection()));*/
	}
	/*
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void in(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().setValue(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public void out(SaleCashRegisterMovement saleCashRegisterMovement) {
		saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().setValue(saleCashRegisterMovement.getAmountIn().subtract(saleCashRegisterMovement.getAmountOut()));
	}
	*/
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void computeAmount(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,Collection<SaleCashRegisterMovement> saleCashRegisterMovements) {
		saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().setValue(BigDecimal.ZERO);
		for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovements)
			commonUtils.increment(BigDecimal.class, saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement(), Movement.FIELD_VALUE, saleCashRegisterMovement.getAmount());
	}
	
	@Override
	protected void beforeCreate(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
		createIfNotIdentified(saleCashRegisterMovementCollection.getCashRegisterMovement());
		super.beforeCreate(saleCashRegisterMovementCollection);
	}
	
	@Override
	public SaleCashRegisterMovement add(SaleCashRegisterMovementCollection collection, SaleCashRegisterMovement item) {
		SaleCashRegisterMovement saleCashRegisterMovement = super.add(collection, item);
		commonUtils.increment(BigDecimal.class, collection.getCashRegisterMovement().getMovement(), Movement.FIELD_VALUE, item.getAmount());
		return saleCashRegisterMovement;
	}
	
	@Override
	public SaleCashRegisterMovement remove(SaleCashRegisterMovementCollection collection,SaleCashRegisterMovement item) {
		SaleCashRegisterMovement saleCashRegisterMovement = super.remove(collection, item);
		commonUtils.increment(BigDecimal.class, collection.getCashRegisterMovement().getMovement(), Movement.FIELD_VALUE, item.getAmount().negate());
		return saleCashRegisterMovement;
	}
	
	@Override
	protected void beforeDelete(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
		super.beforeDelete(saleCashRegisterMovementCollection);
		CashRegisterMovement cashRegisterMovement = saleCashRegisterMovementCollection.getCashRegisterMovement();
		saleCashRegisterMovementCollection.setCashRegisterMovement(null);
		inject(CashRegisterMovementBusiness.class).delete(cashRegisterMovement);
	}
	
	@Override
	protected void afterCrud(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection,Crud crud) {
		super.afterCrud(saleCashRegisterMovementCollection,crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Boolean.TRUE.equals(CompanyConstant.Configuration.SaleCashRegisterMovementCollection.AUTOMATICALLY_GENERATE_REPORT_FILE)){
				createReportFile(saleCashRegisterMovementCollection, CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4, RootConstant.Configuration.ReportTemplate.LOCALE);
			}
		}
	}
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<SaleCashRegisterMovementCollection>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/
		
		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<SaleCashRegisterMovementCollection> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				public static class EnterpriseResourcePlanning extends Listener.Adapter.Default implements Serializable {
					private static final long serialVersionUID = -1625238619828187690L;
					
					/**/
					
					
				}
			}
		}
		
	}



	
	
}
