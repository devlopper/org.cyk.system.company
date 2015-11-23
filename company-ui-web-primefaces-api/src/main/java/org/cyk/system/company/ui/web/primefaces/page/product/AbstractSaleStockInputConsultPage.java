package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.CommandRequestType;
import org.cyk.ui.api.command.UICommandable.Parameter;
import org.cyk.ui.api.command.UICommandable.ViewType;
import org.cyk.ui.api.model.AbstractOutputDetails;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public abstract class AbstractSaleStockInputConsultPage extends AbstractConsultPage<SaleStockInput> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static final String COMMANDABLE_WITHDRAW_IDENTIFIER = "withdraw";
	
	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
	
	@Inject protected SaleBusiness saleBusiness;
	@Inject protected CompanyBusinessLayer companyBusinessLayer;
	
	protected FormOneData<Details> details;
	protected Table<OutputDetails> outputsTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = (FormOneData<Details>) createDetailsForm(Details.class,identifiable, new DetailsConfigurationListener.Form.Adapter<>(SaleStockInput.class, Details.class));
		
		outputsTable = (Table<OutputDetails>) createDetailsTable(OutputDetails.class, new DetailsConfigurationListener.Table.Adapter<SaleStockOutput,OutputDetails>(SaleStockOutput.class, OutputDetails.class){
			private static final long serialVersionUID = -2147502075453340486L;
			@Override
			public Collection<SaleStockOutput> getIdentifiables() {
				return identifiable.getSaleStockOutputs();
			}
		});
		//configureDetailsTable(outputsTable, "model.entity.payment");
		/*
		outputsTable.setShowAddRemoveColumn(Boolean.TRUE);
		outputsTable.setShowOpenCommand(Boolean.TRUE);
		
		//outputsTable.getColumnListeners().add(new DefaultColumnAdapter());
		
		outputsTable.getRowListeners().add(new RowAdapter<OutputDetails>(){
			@Override
			public void added(Row<OutputDetails> row) {
				super.added(row);
				row.setOpenable(Boolean.TRUE);
			}
		});
		
		outputsTable.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 8640883295366346645L;
			@Override
			public void serve(UICommand command, Object parameter) {
				navigationManager.redirectToPrintData(
						navigationManager.reportParameters(((Row<OutputDetails>)parameter).getData().getSaleStockOutput().getSaleCashRegisterMovement(), 
						CompanyReportRepository.getInstance().getReportPointOfSaleReceipt(),Boolean.FALSE));
			}
		});
		*/
	}
	
	protected void afterInitialisation() {
		super.afterInitialisation();
		/*
		for(SaleStockOutput output : identifiable.getSaleStockOutputs())
			outputsTable.addRow(new OutputDetails(output));
		*/
		outputsTable.getColumn("amount").setFooter(numberBusiness.format(identifiable.getSale().getCost().subtract(identifiable.getSale().getBalance().getValue())));
		outputsTable.getColumn("numberOfStockGoods").setFooter(numberBusiness.format(identifiable.getTangibleProductStockMovement().getQuantity()
				.subtract(identifiable.getRemainingNumberOfGoods())));
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		Integer balance = identifiable.getSale().getBalance().getValue().compareTo(BigDecimal.ZERO);
		UICommandable contextualMenu = UIProvider.getInstance().createCommandable("button", null),c;
		contextualMenu.setLabel(contentTitle); 
		if(Boolean.TRUE.equals(identifiable.getSale().getDone()) && balance!=0){
			Collection<Parameter> parameters = Arrays.asList(new Parameter(uiManager.keyFromClass(SaleStockInput.class), identifiable.getIdentifier()),
					new Parameter(webManager.getRequestParameterPreviousUrl(), url));
			c = contextualMenu.addChild("command.widthdraw", null, "saleStockOutputEditView", parameters);	
			c.setIdentifier(COMMANDABLE_WITHDRAW_IDENTIFIER);
		}
		
		UICommandable printReceipt = UIProvider.getInstance().createCommandable("command.see.invoice", null);
		printReceipt.setCommandRequestType(CommandRequestType.UI_VIEW);
		printReceipt.setViewType(ViewType.TOOLS_REPORT);
		printReceipt.getParameters().addAll(navigationManager.reportParameters(identifiable.getSale(), 
				CompanyReportRepository.getInstance().getReportPointOfSale(),Boolean.FALSE));
		contextualMenu.getChildren().add(printReceipt);
		
		return Arrays.asList(contextualMenu);
	}

	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(SaleStockInput.class);
	}
	
	
	@Getter @Setter
	public static class Details implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String identifier,cost,balance,customer,date,numberOfStockGoods,remainingNumberOfGoods;
		
		public Details(SaleStockInput saleStockInput) {
			this.identifier = saleStockInput.getSale().getComputedIdentifier();
			this.cost = UIManager.getInstance().getNumberBusiness().format(saleStockInput.getSale().getCost());
			this.balance = UIManager.getInstance().getNumberBusiness().format(saleStockInput.getSale().getBalance().getValue().abs());
			this.customer = saleStockInput.getSale().getCustomer()==null?"":saleStockInput.getSale().getCustomer().getPerson().getNames();
			this.date = UIManager.getInstance().getTimeBusiness().formatDateTime(saleStockInput.getSale().getDate());
			this.numberOfStockGoods = UIManager.getInstance().getNumberBusiness().format(saleStockInput.getTangibleProductStockMovement().getQuantity());
			this.remainingNumberOfGoods = UIManager.getInstance().getNumberBusiness().format(saleStockInput.getRemainingNumberOfGoods());
		}
	}
	
	@Getter @Setter
	public static class OutputDetails extends AbstractOutputDetails<SaleStockOutput> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String identifier,date,amount,numberOfStockGoods;
		
		public OutputDetails(SaleStockOutput saleStockOutput) {
			super(saleStockOutput);
			this.identifier = saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getComputedIdentifier();
			this.amount = UIManager.getInstance().getNumberBusiness().format(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getAmount());
			this.numberOfStockGoods = UIManager.getInstance().getNumberBusiness().format(saleStockOutput.getTangibleProductStockMovement().getQuantity().abs());
			this.date = UIManager.getInstance().getTimeBusiness().formatDateTime(saleStockOutput.getSaleCashRegisterMovement().getCashRegisterMovement().getDate());
		}
	}

}