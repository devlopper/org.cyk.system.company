package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.FormatterBusiness;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.ui.api.data.collector.form.FormOneData;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementCollectionEditPage extends AbstractCollectionEditPage<SaleCashRegisterMovementCollection,SaleCashRegisterMovement,SaleCashRegisterMovementCollectionEditPage.Item> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	protected List<SelectItem> sales;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		itemCollection = createItemCollection(Item.class, SaleCashRegisterMovement.class,identifiable ,new ItemCollectionWebAdapter<Item,SaleCashRegisterMovement
				,SaleCashRegisterMovementCollection>(identifiable,crud){

			private static final long serialVersionUID = 1L;
			
			@Override
			public Collection<SaleCashRegisterMovement> load() {
				getCollection().getItems().setCollection(inject(SaleCashRegisterMovementBusiness.class).findByCollection(getCollection()));
				return getCollection().getItems().getCollection();
			}
			
			@Override
			public SaleCashRegisterMovement instanciate(AbstractItemCollection<Item, SaleCashRegisterMovement,SaleCashRegisterMovementCollection, SelectItem> itemCollection) {
				Sale sale = (Sale) itemCollection.getOneMasterSelected();
				SaleCashRegisterMovement item = inject(SaleCashRegisterMovementBusiness.class).instanciateOne(collection, sale, BigDecimal.ZERO);
				updateFormAmount(itemCollection.getContainerForm(),collection);
				return item;
			}
			
			@Override
			public void delete(AbstractItemCollection<Item, SaleCashRegisterMovement,SaleCashRegisterMovementCollection, SelectItem> itemCollection,Item item) {
				super.delete(itemCollection, item);
				inject(SaleCashRegisterMovementCollectionBusiness.class).remove(collection, item.getIdentifiable());
				updateFormAmount(itemCollection.getContainerForm(),collection);
			}
			
			@Override
			public Boolean isShowAddButton() {
				return Boolean.TRUE;
			}
			
			@Override
			public void read(Item item) {
				super.read(item);
				item.setSale(item.getIdentifiable().getSale());
				item.setCode(item.getIdentifiable().getSale().getCode());
				item.setName(item.getIdentifiable().getSale().getName());
				item.setCost(inject(FormatterBusiness.class).format(item.getIdentifiable().getSale().getSalableProductCollection().getCost().getValue()));
				item.setBalance(inject(FormatterBusiness.class).format(item.getIdentifiable().getBalance().getValue()));
				item.setToPay(item.getBalance());
				item.setAmount(item.getIdentifiable().getAmount());
			}
							
		});
		identifiable.getItems().setSynchonizationEnabled(Boolean.TRUE);
		sales = webManager.getSelectItems(Sale.class, inject(SaleBusiness.class).findAll(),Boolean.FALSE);
	}
	
	public void saleCashRegisterMovementAmountChanged(Item item){
		item.getIdentifiable().setAmount(item.getAmount());
		inject(SaleCashRegisterMovementBusiness.class).computeBalance(item.getIdentifiable());
		inject(SaleCashRegisterMovementCollectionBusiness.class).computeAmount(identifiable,identifiable.getItems().getCollection());
		itemCollection.read(item);
		updateFormAmount(form,identifiable);
	}
	
	@SuppressWarnings("unchecked")
	private static void updateFormAmount(FormOneData<?, ?, ?, ?, ?, ?> form,SaleCashRegisterMovementCollection saleCashRegisterMovementCollection){
		((Form)form.getData()).setAmount(saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().getValue());
		form.findInputByClassByFieldName(org.cyk.ui.api.data.collector.control.InputNumber.class, Form.FIELD_AMOUNT).setValue(((Form)form.getData()).getAmount());
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		createAjaxBuilder(Form.FIELD_CASH_REGISTER)
		.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
			@Override
			public void execute(CashRegister cashRegister) {
				if(Crud.CREATE.equals(crud))
					inject(SaleCashRegisterMovementCollectionBusiness.class).setCashRegister(userSession.getUserAccount(), identifiable, cashRegister);
				identifiable.setAmountIn(((Form)form.getData()).getAmount());
			}
		}).build();
		
	}
	
	@Override
	protected AbstractCollection<?> getCollection() {
		return identifiable;
	}
	
	/**/
	
	@Getter @Setter
	public static class Form extends AbstractBusinessIdentifiedEditFormModel<SaleCashRegisterMovementCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		@Input(disabled=true,readOnly=true) @InputNumber @NotNull protected BigDecimal amount;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegisterMovementMode mode;
		@Input @InputText protected String supportingDocumentProvider;
		@Input @InputText protected String supportingDocumentIdentifier;
		
		@Override
		public void read() {
			cashRegister = identifiable.getCashRegisterMovement().getCashRegister();
			mode = identifiable.getCashRegisterMovement().getMode();
			if(identifiable.getCashRegisterMovement().getMovement()!=null){
				amount = identifiable.getCashRegisterMovement().getMovement().getValue();
				supportingDocumentProvider = identifiable.getCashRegisterMovement().getMovement().getSupportingDocumentProvider();
				supportingDocumentIdentifier = identifiable.getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier();
			}
			super.read();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.setAmountIn(amount);
			identifiable.setAmountOut(BigDecimal.ZERO);
			identifiable.getCashRegisterMovement().setMode(mode);
			identifiable.getCashRegisterMovement().getMovement().setSupportingDocumentProvider(supportingDocumentProvider);
			identifiable.getCashRegisterMovement().getMovement().setSupportingDocumentIdentifier(supportingDocumentIdentifier);
		}
		
		/**/
		
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_AMOUNT = "amount";
		public static final String FIELD_MODE = "mode";
		public static final String FIELD_SUPPORTING_DOCUMENT_PROVIDER = "supportingDocumentProvider";
		public static final String FIELD_SUPPORTING_DOCUMENT_IDENTIFIER = "supportingDocumentIdentifier";
		
	}
	
	/**/
	
	@Getter @Setter
	public static class Item extends AbstractItemCollectionItem<SaleCashRegisterMovement> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		
		protected Sale sale;
		protected String code,name,cost,toPay,balance;
		protected BigDecimal amount;
		 		
	}

}