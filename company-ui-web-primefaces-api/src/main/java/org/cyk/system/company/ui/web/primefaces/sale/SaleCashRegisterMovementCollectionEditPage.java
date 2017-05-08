package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.FormatterBusiness;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.FormOneData;
import org.cyk.ui.api.model.AbstractItemCollection;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.api.ItemCollectionWebAdapter;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.FieldOverrides;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementCollectionEditPage extends AbstractCollectionEditPage<SaleCashRegisterMovementCollection,SaleCashRegisterMovement,SaleCashRegisterMovementCollectionEditPage.Item> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	private Boolean showSupportingDocumentCode=null,showSupportingDocumentGenerator=null,showSupportingDocumentContentWriter=null;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		
		identifiable.getItems().setSynchonizationEnabled(Boolean.TRUE);

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
		//form.findInputByClassByFieldName(org.cyk.ui.api.data.collector.control.InputNumber.class, Form.FIELD_AMOUNT).setValue(((Form)form.getData()).getAmount());
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		
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
				Sale sale = (Sale) itemCollection.getInputChoice().getValue();
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
			
			@Override
			public AbstractIdentifiable getMasterSelected(AbstractItemCollection<Item, SaleCashRegisterMovement, SaleCashRegisterMovementCollection, SelectItem> itemCollection,
					SaleCashRegisterMovement saleCashRegisterMovement) {
				return saleCashRegisterMovement.getSale();
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public org.cyk.ui.api.data.collector.control.InputChoice<AbstractIdentifiable, ?, ?, ?, ?, ?> getInputChoice() {
				return (org.cyk.ui.api.data.collector.control.InputChoice<AbstractIdentifiable, ?, ?, ?, ?, SelectItem>) form.getInputByFieldName(AbstractForm.FIELD_ONE_ITEM_MASTER_SELECTED);
			}
							
		});
		
		itemCollection.getAddCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public void serve(UICommand command, Object parameter) {
				super.serve(command, parameter);
				onCompleteSetShowSupportingDocumentProperties(Boolean.TRUE);
			}
		});
		
		itemCollection.getDeleteCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public void serve(UICommand command, Object parameter) {
				super.serve(command, parameter);
				onCompleteSetShowSupportingDocumentProperties(Boolean.TRUE);
			}
		});
		
		itemCollection.getInputChoice().setIsAutomaticallyRemoveSelected(Boolean.TRUE);
		
		
		createAjaxBuilder(Form.FIELD_CASH_REGISTER)
		.method(CashRegister.class,new ListenValueMethod<CashRegister>() {
			@Override
			public void execute(CashRegister cashRegister) {
				if(Crud.CREATE.equals(crud))
					inject(SaleCashRegisterMovementCollectionBusiness.class).setCashRegister(userSession.getUserAccount(), identifiable, cashRegister);
				identifiable.setAmountIn(((Form)form.getData()).getAmount());
			}
		}).build();
		
		createAjaxBuilder(Form.FIELD_MODE)
		.method(CashRegisterMovementMode.class,new ListenValueMethod<CashRegisterMovementMode>() {
			@Override
			public void execute(CashRegisterMovementMode cashRegisterMovementMode) {
				listenCashRegisterMovementModeChange(cashRegisterMovementMode,Boolean.TRUE);
			}
		}).build();
		
		listenCashRegisterMovementModeChange(identifiable.getCashRegisterMovement().getMode(),Boolean.FALSE);
		
	}
	
	private void listenCashRegisterMovementModeChange(CashRegisterMovementMode cashRegisterMovementMode,Boolean ajax){
		//((SaleCashRegisterMovementCollection)form.getData()).getCashRegisterMovement().setMode(cashRegisterMovementMode);
		if(CompanyConstant.Code.CashRegisterMovementMode.CASH.equals(cashRegisterMovementMode.getCode())){
			
		}else{ 
			if(identifiable.getCashRegisterMovement().getSupportingDocument()==null)
				identifiable.getCashRegisterMovement().setSupportingDocument(inject(FileBusiness.class).instanciateOne());
			
		}
		
		setShowSupportingDocumentProperties(cashRegisterMovementMode,ajax);
		
	}
	
	private void setShowSupportingDocumentProperties(CashRegisterMovementMode cashRegisterMovementMode,Boolean ajax){
		if(CompanyConstant.Code.CashRegisterMovementMode.CASH.equals(cashRegisterMovementMode.getCode())){
			showSupportingDocumentCode = Boolean.FALSE;
			showSupportingDocumentGenerator = Boolean.FALSE;
			showSupportingDocumentContentWriter = Boolean.FALSE;
		}else{ 
			if(CompanyConstant.Code.CashRegisterMovementMode.BANK_TRANSFER.equals(cashRegisterMovementMode.getCode())){
				showSupportingDocumentCode = Boolean.TRUE;
				showSupportingDocumentGenerator = Boolean.FALSE;
				showSupportingDocumentContentWriter = Boolean.FALSE;
			}else if(CompanyConstant.Code.CashRegisterMovementMode.CHEQUE.equals(cashRegisterMovementMode.getCode())){
				showSupportingDocumentCode = Boolean.TRUE;
				showSupportingDocumentGenerator = Boolean.TRUE;
				showSupportingDocumentContentWriter = Boolean.TRUE;
			}else if(CompanyConstant.Code.CashRegisterMovementMode.MOBILE_PAYMENT.equals(cashRegisterMovementMode.getCode())){
				showSupportingDocumentCode = Boolean.TRUE;
				showSupportingDocumentGenerator = Boolean.TRUE;
				showSupportingDocumentContentWriter = Boolean.TRUE;
			}
		}
		
		onCompleteSetShowSupportingDocumentProperties(ajax);
	}
	
	private void onCompleteSetShowSupportingDocumentProperties(Boolean ajax){
		if(Boolean.TRUE.equals(ajax)){
			onComplete(inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_CODE,showSupportingDocumentCode)
					,inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_GENRATOR,showSupportingDocumentGenerator)
					,inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_CONTENT_WRITER,showSupportingDocumentContentWriter));
		}else{
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_CODE,showSupportingDocumentCode));
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_GENRATOR,showSupportingDocumentGenerator));
			onDocumentLoadJavaScript = javaScriptHelper.add(onDocumentLoadJavaScript, inputRowVisibility(form,Form.FIELD_SUPPORTING_DOCUMENT_CONTENT_WRITER,showSupportingDocumentContentWriter));
		}
			
	}
	
	@Override
	protected AbstractCollection<?> getCollection() {
		return identifiable;
	}
	
	/**/
	
	@Getter @Setter
	@FieldOverrides(value = {
			@FieldOverride(name=AbstractForm.FIELD_ONE_ITEM_MASTER_SELECTED,type=Sale.class)
			})
	public static class Form extends AbstractForm<SaleCashRegisterMovementCollection,SaleCashRegisterMovement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegister cashRegister;
		@Input(disabled=true,readOnly=true) @InputNumber @NotNull protected BigDecimal amount;
		@Input @InputText protected String receivedFrom;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private CashRegisterMovementMode mode;
		@Input @InputText protected String supportingDocumentCode;
		@Input @InputText protected String supportingDocumentGenerator;
		@Input @InputText protected String supportingDocumentContentWriter;
		
		
		@Override
		public void read() {
			cashRegister = identifiable.getCashRegisterMovement().getCashRegister();
			mode = identifiable.getCashRegisterMovement().getMode();
			if(identifiable.getCashRegisterMovement().getMovement()!=null){
				amount = identifiable.getCashRegisterMovement().getMovement().getValue();
				receivedFrom = identifiable.getCashRegisterMovement().getMovement().getSenderOrReceiverPersonAsString();
				
				if(identifiable.getCashRegisterMovement().getSupportingDocument()!=null){
					supportingDocumentCode = identifiable.getCashRegisterMovement().getSupportingDocument().getCode();
					supportingDocumentGenerator = identifiable.getCashRegisterMovement().getSupportingDocument().getGenerator();
					supportingDocumentContentWriter = identifiable.getCashRegisterMovement().getSupportingDocument().getContentWriter();	
				}
				
			}
			super.read();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.setAmountIn(amount);
			identifiable.setAmountOut(BigDecimal.ZERO);
			identifiable.getCashRegisterMovement().setMode(mode);
			identifiable.getCashRegisterMovement().getMovement().setSenderOrReceiverPersonAsString(receivedFrom);
			
			if(identifiable.getCashRegisterMovement().getSupportingDocument()!=null){
				identifiable.getCashRegisterMovement().getSupportingDocument().setCode(supportingDocumentCode);
				identifiable.getCashRegisterMovement().getSupportingDocument().setGenerator(supportingDocumentGenerator);
				identifiable.getCashRegisterMovement().getSupportingDocument().setContentWriter(supportingDocumentContentWriter);	
			}
			
		}
		
		/**/
		
		public static final String FIELD_CASH_REGISTER = "cashRegister";
		public static final String FIELD_AMOUNT = "amount";
		public static final String FIELD_MODE = "mode";
		public static final String FIELD_SUPPORTING_DOCUMENT_CODE = "supportingDocumentCode";
		public static final String FIELD_SUPPORTING_DOCUMENT_GENRATOR = "supportingDocumentGenerator";
		public static final String FIELD_SUPPORTING_DOCUMENT_CONTENT_WRITER = "supportingDocumentContentWriter";
		public static final String FIELD_RECEIVED_FROM = "receivedFrom";


		@Override
		protected AbstractCollection<?> getCollection() {
			return identifiable;
		}
		
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