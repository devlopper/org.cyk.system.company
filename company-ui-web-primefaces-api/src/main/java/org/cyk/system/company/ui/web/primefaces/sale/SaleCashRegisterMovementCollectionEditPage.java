package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.system.company.ui.web.primefaces.sale.AbstractSalableProductCollectionEditPage.AbstractDefaultForm;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductCollectionEditPage.Item;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.ui.api.data.collector.form.FormOneData;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementCollectionEditPage extends AbstractCollectionEditPage<SaleCashRegisterMovementCollection,SaleCashRegisterMovement,SaleCashRegisterMovementCollectionEditPage.Item> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		itemCollection = createItemCollection(Item.class, SaleCashRegisterMovement.class,identifiable ,new ItemCollectionWebAdapter<Item,SaleCashRegisterMovement
				,SaleCashRegisterMovementCollection>(identifiable,crud){

			private static final long serialVersionUID = 1L;
			
			@Override
			public Collection<SaleCashRegisterMovement> load() {
				getCollection().setCollection(inject(SaleCashRegisterMovementBusiness.class).findByCollection(getCollection()));
				return getCollection().getCollection();
			}
			
			@Override
			public SaleCashRegisterMovement instanciate(AbstractItemCollection<Item, SaleCashRegisterMovement,SaleCashRegisterMovementCollection, SelectItem> itemCollection) {
				SaleCashRegisterMovement item = new SaleCashRegisterMovement();
				/*item.setSalableProduct((SalableProduct) itemCollection.getOneMasterSelected());
				item.setQuantity(BigDecimal.ONE);
				item.setCollection(collection);
				inject(SalableProductCollectionBusiness.class).add(collection, item);
				*/
				updateFormCost(itemCollection.getContainerForm(),collection);
				return item;
			}
			
			@Override
			public void delete(AbstractItemCollection<Item, SaleCashRegisterMovement,SaleCashRegisterMovementCollection, SelectItem> itemCollection,Item item) {
				super.delete(itemCollection, item);
				//inject(SalableProductCollectionBusiness.class).remove(collection, item.getIdentifiable());
				updateFormCost(itemCollection.getContainerForm(),collection);
			}
			
			/*@Override
			public Boolean isShowAddButton() {
				return Boolean.TRUE;
			}*/
			
			@Override
			public void read(Item item) {
				super.read(item);
				/*item.setCode(item.getIdentifiable().getSalableProduct().getProduct().getCode());
				item.setName(item.getIdentifiable().getSalableProduct().getProduct().getName());
				item.setUnitPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getSalableProduct().getPrice()));
				item.setQuantity(item.getIdentifiable().getQuantity());
				item.setQuantifiedPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getQuantifiedPrice()));
				item.setReduction(item.getIdentifiable().getReduction()==null?null:new BigDecimal(item.getIdentifiable().getReduction().intValue()));
				item.setTotalPrice(inject(NumberBusiness.class).format(item.getIdentifiable().getCost().getValue()));
				*/
				//item.setInstanceChoices(new ArrayList<>(inject(SalableProductInstanceBusiness.class).findByCollection(item.getIdentifiable().getSalableProduct())));
			}	
				
		});
	}
	
	@SuppressWarnings("unchecked")
	private static void updateFormCost(FormOneData<?, ?, ?, ?, ?, ?> form,SaleCashRegisterMovementCollection saleCashRegisterMovementCollection){
		//((AbstractDefaultForm<?,?>)form.getData()).getCost().setValue(salableProductCollection.getCost().getValue());
		//form.findInputByClassByFieldName(InputNumber.class, CostFormModel.FIELD_VALUE).setValue(((AbstractDefaultForm<?,?>)form.getData()).getCost().getValue());
		
	}
	
	/*	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		createAjaxBuilder(Form.FIELD_CASH_REGISTER)
		.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
			@Override
			public void execute(CashRegister cashRegister) {
				if(Crud.CREATE.equals(crud))
					inject(SaleCashRegisterMovementCollectionBusiness.class).setCashRegister(userSession.getUserAccount(), identifiable, cashRegister);
				identifiable.setAmountIn(((Form)form.getData()).getValue());
				((Form)form.getData()).setMovement(identifiable.getCashRegisterMovement().getMovement());
			}
		}).build();
		
	}
	
	@Override
	protected SaleCashRegisterMovementCollection instanciateIdentifiable(Sale sale) {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount(),sale,webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected SaleCashRegisterMovementCollection instanciateIdentifiable() {
		return inject(SaleCashRegisterMovementBusiness.class).instanciateOne(userSession.getUserAccount()
				, webManager.getIdentifiableFromRequestParameter(Sale.class,Boolean.TRUE)
				, webManager.getIdentifiableFromRequestParameter(CashRegister.class,Boolean.TRUE));
	}
	
	@Override
	protected Sale getCollection(SaleCashRegisterMovement saleCashRegisterMovement) {
		return saleCashRegisterMovement.getSale();
	}
	
	@Override
	protected void selectCollection(Sale sale) {
		super.selectCollection(sale);
		if(Crud.CREATE.equals(crud))
			inject(SaleCashRegisterMovementBusiness.class).setSale(identifiable, sale);
		updateCurrentTotal();
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return identifiable.getCashRegisterMovement();
	}
	
	@Override
	protected MovementCollection getMovementCollection(Sale sale) {
		if(((Form)form.getData()).getCashRegister()==null)
			return null;
		return ((Form)form.getData()).getCashRegister().getMovementCollection();
	}
	
	@Override
	protected BigDecimal getCurrentTotal() {
		if(identifiable.getSale()==null)
			return null;
		return identifiable.getSale().getBalance().getValue();
	}
	
	@Override
	protected BigDecimal getNextTotal(BigDecimal increment) {
		if(identifiable.getSale()==null)
			return null;
		return inject(SaleCashRegisterMovementBusiness.class).computeBalance(identifiable,(MovementAction) form.getInputByFieldName(Form.FIELD_ACTION).getValue()
				,increment == null ? BigDecimal.ZERO : increment);
	}
	*/	
	
	@Override
	protected AbstractCollection<?> getCollection() {
		return identifiable;
	}
	
	/**/
	
	@Getter @Setter
	public static class Form extends AbstractBusinessIdentifiedEditFormModel<SaleCashRegisterMovementCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		@Input @InputNumber @NotNull protected BigDecimal paid;
		
		@Override
		public void read() {
			cashRegister = identifiable.getCashRegisterMovement().getCashRegister();
			paid = identifiable.getCashRegisterMovement().getMovement().getValue();
			super.read();
		}
		
		@Override
		public void write() {
			identifiable.getCashRegisterMovement().setCashRegister(cashRegister);
			identifiable.getCashRegisterMovement().getMovement().setCollection(cashRegister.getMovementCollection());
			super.write();
			identifiable.setAmountIn(paid);
			identifiable.setAmountOut(BigDecimal.ZERO);
		}
		
		/**/
		
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_PAID = "paid";
		
	}
	
	/**/
	
	@Getter @Setter
	public static class Item extends AbstractItemCollectionItem<SaleCashRegisterMovement> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		
		protected String code,name,cost,toPay,balance;
		protected BigDecimal paid;
		 		
	}

	

}