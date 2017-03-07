package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.sale.CustomerReportTableRow;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterStateLogDetails;
import org.cyk.system.company.business.impl.sale.StockDashBoardReportTableDetails;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatSequenceArguments;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.business.impl.file.report.DefaultReportBasedOnDynamicBuilder;
import org.cyk.system.root.business.impl.file.report.ReportBasedOnDynamicBuilderAdapter;
import org.cyk.system.root.business.impl.file.report.jasper.DefaultJasperReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderIdentifiableConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFileConfiguration;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifier;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=CompanyBusinessLayer.DEPLOYMENT_ORDER+1)
public class CompanyReportRepository extends AbstractReportRepository implements Serializable {

	private static final long serialVersionUID = 6917567891985885124L;

	@Getter private final String reportPointOfSale = "pos";
	@Getter private final String reportPointOfSaleReceipt = "posr";
	@Getter private final String reportStockDashboard = "rsdb";
	@Getter private final String parameterCustomerReportType = "crt";
	@Getter private final String parameterCustomerReportBalance = "crb";
	@Getter private final String parameterCustomerReportSaleStock = "crss";
	@Getter private final String parameterCustomerBalanceType = "cbt";
	@Getter private final String parameterCustomerBalanceAll = "cball";
	@Getter private final String parameterCustomerBalanceCredence = "cbcred";
	@Getter private final String parameterMinimumRemainingNumberOfGoods = "mrnog";
	@Getter private final String parameterBalanceType = "bt";
	@Getter private final String parameterSaleStockReportType = "crt";
	@Getter private final String parameterSaleStockReportCashRegister = "ssorcr";
	@Getter private final String parameterSaleStockReportInventory = "ssori";
	@Getter private final String parameterSaleStockReportCustomer = "ssorc";
	@Getter private final String parameterSaleStockReportInput = "ssiri";
	@Getter private final String parameterSaleDone = "saledone";
	
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		INSTANCE = this;
	}
	
	@Override
	public void build() {
		addListener(new ReportBasedOnDynamicBuilderAdapter(){
        	@Override
        	public void report(ReportBasedOnDynamicBuilder<?> report,ReportBasedOnDynamicBuilderParameters<?> parameters) {
        		parameters.setOwner(ownedCompanyBusiness.findDefaultOwnedCompany().getCompany());
        	}
        });
		
		/*addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
	    		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),StockTangibleProductMovement.class,TangibleProductStockMovementLineReport.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new TangibleProductStockMovementLineReport((StockTangibleProductMovement) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				StockTangibleProductMovementSearchCriteria searchCriteria = new StockTangibleProductMovementSearchCriteria(getParameterFromDate(parameters)
						,getParameterToDate(parameters),BigDecimal.ZERO);
				return tangibleProductStockMovementBusiness.findByCriteria(searchCriteria);
			}
		});*/
		
		/*addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),Sale.class,SaleReportTableDetail.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new SaleReportTableDetail((Sale) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				SaleSearchCriteria searchCriteria = new SaleSearchCriteria(getParameterFromDate(parameters)
						,getParameterToDate(parameters));
				return saleBusiness.findByCriteria(searchCriteria);
			}
		});*/
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),SaleStockTangibleProductMovement.class,Object.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return null;//new SaleStockReportTableRow((SaleStockTangibleProductMovement) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				/*String reportType = parameters.getExtendedParameterMap().get(parameterSaleStockReportType)[0];
				Boolean saleDone = null;
				try { saleDone = Boolean.parseBoolean(parameters.getExtendedParameterMap().get(parameterSaleDone)[0]); } 
				catch (Exception e) { saleDone = Boolean.TRUE;}
				Date fromDate = getParameterFromDate(parameters);
				Date toDate = getParameterToDate(parameters);
				*/
				/*if(parameterSaleStockReportCashRegister.equals(reportType)){
					parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.salestockoutput.cashregister.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.cashRegisterFieldIgnored(field);};
			        });
					SaleStockOutputSearchCriteria searchCriteria = new SaleStockOutputSearchCriteria(fromDate,toDate,saleDone);
					return saleStockOutputBusiness.findByCriteria(searchCriteria);
				}else if(parameterSaleStockReportInventory.equals(reportType)){
					parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.salestock.inventory.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.inventoryFieldIgnored(field);};
			        });
					return saleStockBusiness.findByCriteria(new SaleStockSearchCriteria(fromDate,toDate,BigDecimal.ONE,saleDone));
				}else if(parameterSaleStockReportCustomer.equals(reportType)){
					parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.salestock.customer.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.customerFieldIgnored(field);};
			        });
					return saleStockBusiness.findByCriteria(new SaleStockSearchCriteria(fromDate,toDate,saleDone));
				}else if(parameterSaleStockReportInput.equals(reportType)){
					parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.salestockinput.list.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.inputFieldIgnored(field);};
			        });
					return saleStockInputBusiness.findByCriteria(new SaleStockInputSearchCriteria(fromDate,toDate,saleDone));
				}*/
				return null;
			}
			
			@SuppressWarnings("unchecked")
			public void beforeBuild(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				String reportType = parameters.getExtendedParameterMap().get(parameterSaleStockReportType)[0];
				Date fromDate = getParameterFromDate(parameters);
				Date toDate = getParameterToDate(parameters);
				
				parameters.setDatas((Collection<Object>) processSaleStockReportRows(reportType, fromDate, toDate, parameters.getDatas(),Boolean.TRUE));
			}
		});
		
		/*addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),TangibleProductInventory.class,TangibleProductInventoryReportTableDetails.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new TangibleProductInventoryReportTableDetails((TangibleProductInventoryDetail) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				BusinessEntityInfos tangibleProductInventoryEntityInfos = inject(ApplicationBusiness.class).findBusinessEntityInfos(TangibleProductInventory.class);
				Long tangibleProductInventoryId = Long.parseLong(parameters.getExtendedParameterMap().get(tangibleProductInventoryEntityInfos.getIdentifier())[0]);
				TangibleProductInventory tangibleProductInventory = tangibleProductInventoryBusiness.load(tangibleProductInventoryId);
				parameters.setTitle(languageBusiness.findClassLabelText(TangibleProductInventory.class)+" "+ inject(TimeBusiness.class).formatDateTime(tangibleProductInventory.getDate()));
				return tangibleProductInventory.getDetails();
			}
		});*/
        
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),Customer.class,CustomerReportTableRow.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new CustomerReportTableRow((Customer) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				if(parameters.getExtendedParameterMap().get(parameterCustomerReportType)==null){
					parameters.getColumnNamesToExclude().add(CustomerReportTableRow.FIELD_BALANCE);
					parameters.getColumnNamesToExclude().add(CustomerReportTableRow.FIELD_PAID);
					parameters.getColumnNamesToExclude().add(CustomerReportTableRow.FIELD_SALE_STOCK_INPUT_COUNT);
					parameters.getColumnNamesToExclude().add(CustomerReportTableRow.FIELD_SALE_STOCK_OUTPUT_COUNT);
					parameters.getColumnNamesToExclude().add(CustomerReportTableRow.FIELD_TURNOVER);
					return customerBusiness.findAll();
				}
				Collection<? extends AbstractIdentifiable> collection = null;
				String reportType = parameters.getExtendedParameterMap().get(parameterCustomerReportType)[0];
				if(parameterCustomerReportBalance.equals(reportType)){
					if(parameterCustomerBalanceAll.equals(parameters.getExtendedParameterMap().get(parameterCustomerBalanceType)[0])){
						parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.customer.balance.title"));
						parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
							private static final long serialVersionUID = -1279948056976719107L;
							public Boolean ignoreField(Field field) {return CustomerReportTableRow.balanceFieldIgnored(field);};
				        });
						collection = customerBusiness.findAll();
					}else{
						parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.credence.title"));
						parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
							private static final long serialVersionUID = -1279948056976719107L;
							public Boolean ignoreField(Field field) {return CustomerReportTableRow.credenceFieldIgnored(field);};
				        });
						collection = customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
					}
				}else if(parameterCustomerReportSaleStock.equals(reportType)){
					parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.customer.salestock.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return CustomerReportTableRow.saleStockFieldIgnored(field);};
			        });
					collection = customerBusiness.findAll();
				}
				return collection;
			}
		});
	
		registerConfiguration(new ReportBasedOnTemplateFileConfiguration<Sale, ReportBasedOnTemplateFile<SaleReport>>(reportPointOfSale) {
			@Override
			public <MODEL> ReportBasedOnTemplateFile<SaleReport> build(Class<MODEL> arg0, Collection<MODEL> sales, String arg2,Boolean arg3, Map<String, String[]> arg4) {
				if(sales.size()>1)
					;
				else
					return saleBusiness.findReport((Sale) sales.iterator().next());
				return null;
			}
		});
		
		registerConfiguration(new ReportBasedOnTemplateFileConfiguration<Sale, ReportBasedOnTemplateFile<SaleReport>>(reportPointOfSaleReceipt) {
			@Override
			public <MODEL> ReportBasedOnTemplateFile<SaleReport> build(Class<MODEL> arg0, Collection<MODEL> saleCashRegisterMovements, String arg2,Boolean arg3, Map<String, String[]> arg4) {
				if(saleCashRegisterMovements.size()>1)
					;
				else
					return null;//saleCashRegisterMovementBusiness.findReport((SaleCashRegisterMovement) saleCashRegisterMovements.iterator().next());
				return null;
			}
		});
		
		registerConfiguration(new ReportBasedOnDynamicBuilderConfiguration<StockDashBoardReportTableDetails, ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>>(reportStockDashboard) {        	
			@Override
			public ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails> build(ReportBasedOnDynamicBuilderParameters<StockDashBoardReportTableDetails> parameters) {
				parameters.setDatas(new ArrayList<StockDashBoardReportTableDetails>());
				for(TangibleProduct tangibleProduct : tangibleProductBusiness.findAll())
					parameters.getDatas().add(new StockDashBoardReportTableDetails(tangibleProduct) ) ;
		        
				parameters.setTitle(inject(LanguageBusiness.class).findText("company.report.dashboard.stock.title"));
				parameters.setModelClass(StockDashBoardReportTableDetails.class);
				parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder());
		        parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultJasperReportBasedOnDynamicBuilder());
				return (ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>) reportBusiness.build(parameters);
			}
		});
		
		/**/
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),FiniteStateMachineStateIdentifiableGlobalIdentifier.class
        		,SalableProductInstanceCashRegisterStateLogDetails.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new SalableProductInstanceCashRegisterStateLogDetails((FiniteStateMachineStateIdentifiableGlobalIdentifier) identifiable);
			}
			/*@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}*/
			
			@Override
			public void beforeBuild(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				for(Object object : parameters.getDatas()){
					((SalableProductInstanceCashRegisterStateLogDetails)object).setCode("456");
				}
			}
			
		});
	}
	
	/**/
	
	public Collection<?> processSaleStockReportRows(String reportType,Date fromDate,Date toDate,Collection<Object> initialRows,Boolean addGrandTotalRow) {
		if(parameterSaleStockReportCashRegister.equals(reportType)){
			//BigDecimal output=BigDecimal.ZERO,paid=BigDecimal.ZERO,balance=BigDecimal.ZERO;
			/*for(Object object : initialRows){
				SaleStockReportTableRow row = (SaleStockReportTableRow) object;
				if(row.getSaleStock() instanceof SaleStockTangibleProductMovementOutput){
					//output = output.add( ((SaleStockOutput)row.getSaleStock()).getStockTangibleProductStockMovement().getQuantity().abs());
					paid = paid.add( ((SaleStockTangibleProductMovementOutput)row.getSaleStock()).getSaleCashRegisterMovement().getCashRegisterMovement().getMovement().getValue());
					//balance = saleBusiness.sumBalanceByCriteria(criteria) 
							//balance.add(((SaleStockOutput)row.getSaleStock()).getSaleStockInput().getS.getBalance().getValue());
				}
			}*/
			//SaleResults results = saleBusiness.computeByCriteria(new SaleSearchCriteria(fromDate,toDate));
			//balance = results.getBalance();
			//SaleStockReportTableRow totalRow = new SaleStockReportTableRow();
			//totalRow.setCustomer(inject(LanguageBusiness.class).findText("total"));
			/*totalRow.setTakenNumberOfGoods(inject(NumberBusiness.class).format(output));
			totalRow.setAmountPaid(inject(NumberBusiness.class).format(paid));	
			totalRow.setBalance(inject(NumberBusiness.class).format(balance));
			initialRows.add(totalRow);*/
			return initialRows;
		}/*else if(parameterSaleStockReportInventory.equals(reportType)){
			return groupByCustomer(initialRows,Boolean.TRUE.equals(addGrandTotalRow)?saleStockBusiness.computeByCriteria(new SaleStockSearchCriteria(fromDate, toDate)):null
					,addGrandTotalRow);
		}else if(parameterSaleStockReportCustomer.equals(reportType)){
			return groupByCustomer(initialRows,Boolean.TRUE.equals(addGrandTotalRow)?saleStockBusiness.computeByCriteria(new SaleStockSearchCriteria(fromDate, toDate)):null
					,addGrandTotalRow);
		}*/else if(parameterSaleStockReportInput.equals(reportType)){
			return initialRows;
		}	
		return null;
	}
	
	//protected Collection<Object> groupByCustomer(Collection<Object> initialRows,SaleStocksDetails saleStocksDetails,Boolean addGrandTotalRow){
		//Map<String,Collection<SaleStockReportTableRow>> map = new LinkedHashMap<>();
		//group by
		/*for(Object object : initialRows){
			SaleStockReportTableRow row = (SaleStockReportTableRow)object;
			Collection<SaleStockReportTableRow> collection = map.get(row.getCustomer());
			if(collection==null){
				map.put(row.getCustomer(), collection = new ArrayList<SaleStockReportTableRow>());
			}
			collection.add(row);
		}
		*/
		//total
		/*Collection<SaleStockReportTableRow> rows = new ArrayList<>();
		SaleStockReportTableRow totalRow;*/
		//BigDecimal[][] totals = new BigDecimal[5][2];
		//for(Entry<String, Collection<SaleStockReportTableRow>> entry : map.entrySet()){
		//	set(totals, BigDecimal.ZERO);
			/*for(SaleStockReportTableRow row : entry.getValue()){
				if(row.getSaleStock() instanceof SaleStockTangibleProductMovementInput){
					//incrementTotal(totals, STOCK_IN, ((SaleStockInput)row.getSaleStock()).getStockTangibleProductStockMovement().getQuantity());
					incrementTotal(totals, AMOUNT, ((SaleStockTangibleProductMovementInput)row.getSaleStock()).getSale().getCost().getValue());
					set(totals, CUMUL,0, ((SaleStockTangibleProductMovementInput)row.getSaleStock()).getSale().getBalance().getCumul());
				}else if(row.getSaleStock() instanceof SaleStockTangibleProductMovementOutput){
					//incrementTotal(totals, STOCK_OUT, ((SaleStockOutput)row.getSaleStock()).getStockTangibleProductStockMovement().getQuantity().abs());
					incrementTotal(totals, PAID, ((SaleStockTangibleProductMovementOutput)row.getSaleStock()).getSaleCashRegisterMovement().getCashRegisterMovement().getMovement().getValue());
					set(totals, CUMUL,0, ((SaleStockTangibleProductMovementOutput)row.getSaleStock()).getSaleCashRegisterMovement().getBalance().getCumul());
				}
			} */
			
			/*
			totalRow = new SaleStockReportTableRow();
			totalRow.setCustomer(inject(LanguageBusiness.class).findText("total"));
			totalRow.setStockIn(getTotal(totals, STOCK_IN));
			totalRow.setStockOut(getTotal(totals, STOCK_OUT));	
			totalRow.setRemainingNumberOfGoods(inject(NumberBusiness.class).format(totals[STOCK_IN][0].subtract(totals[STOCK_OUT][0])));
			
			totalRow.setAmount(getTotal(totals, AMOUNT));
			totalRow.setAmountPaid(getTotal(totals, PAID));	
			totalRow.setCumulatedBalance(getTotal(totals, CUMUL));
			
			entry.getValue().add(totalRow);
			*/
		//	rows.addAll(entry.getValue());
		//}
		//grand total
		//if(Boolean.TRUE.equals(addGrandTotalRow)){
			/*totalRow = new SaleStockReportTableRow();
			totalRow.setCustomer(inject(LanguageBusiness.class).findText("total.grand"));
			totalRow.setStockIn(format(saleStocksDetails.getIn()));
			totalRow.setStockOut(format(saleStocksDetails.getOut()));	
			totalRow.setRemainingNumberOfGoods(format(saleStocksDetails.getRemaining()));
			totalRow.setAmount(format(saleStocksDetails.getSaleResults().getCost().getValue()));
			totalRow.setAmountPaid(format(saleStocksDetails.getSaleResults().getPaid()));	
			totalRow.setCumulatedBalance(format(saleStocksDetails.getSaleResults().getBalance()));
			rows.add(totalRow);*/
		//}
		//return null;//rows;
	//}
	
	public Collection<?> processSaleStockReportRowsI(String reportType,Date fromDate,Date toDate,Collection<AbstractIdentifiable> initialRows,Boolean addGrandTotalRow) {
		Collection<Object> c = new ArrayList<>();
		for(AbstractIdentifiable i : initialRows)
			c.add(i);
		return processSaleStockReportRows(reportType, fromDate, toDate, c,addGrandTotalRow);
	}
	
	/**/
	
	public Collection<SalableProductInstanceCashRegisterStateLogDetails> format(Collection<SalableProductInstanceCashRegisterStateLogDetails> collection,Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters,String timeDivisionTypeCode){
		//NumberBusiness numberBusiness = inject(NumberBusiness.class);
		Map<String, SalableProductInstanceCashRegisterStateLogDetails> map = new LinkedHashMap<>();
		for(SalableProductInstanceCashRegisterStateLogDetails salableProductInstanceCashRegisterStateLogDetails : collection){
			for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters){
				if(salableProductInstanceCashRegisterStateLogDetails.getMaster().getIdentifiableGlobalIdentifier().getIdentifier().equals(salableProductInstanceCashRegister.getGlobalIdentifier().getIdentifier())){
					salableProductInstanceCashRegisterStateLogDetails.setSalableProductInstanceCashRegister(salableProductInstanceCashRegister);
					salableProductInstanceCashRegisterStateLogDetails.setCode(salableProductInstanceCashRegister.getSalableProductInstance().getCode());
					salableProductInstanceCashRegisterStateLogDetails.setQuantity("1");
					salableProductInstanceCashRegisterStateLogDetails.setUnitPrice(format(salableProductInstanceCashRegister.getSalableProductInstance().getCollection().getPrice()));
					salableProductInstanceCashRegisterStateLogDetails.setTotalPrice( 
							format(new BigDecimal(salableProductInstanceCashRegisterStateLogDetails.getQuantity()).multiply(salableProductInstanceCashRegisterStateLogDetails.getSalableProductInstanceCashRegister().getSalableProductInstance().getCollection().getPrice())) );
					salableProductInstanceCashRegisterStateLogDetails.setCashRegister(salableProductInstanceCashRegister.getCashRegister().getCode());
					break;
				}
			}
			/*
			String key = ""+salableProductInstanceCashRegisterStateLogDetails.getDate()+salableProductInstanceCashRegisterStateLogDetails
					.getSalableProductInstanceCashRegister().getCashRegister().getCode()+salableProductInstanceCashRegisterStateLogDetails.getMaster().getState().getCode();
			SalableProductInstanceCashRegisterStateLogDetails spicrsld = map.get(key);
			if(spicrsld==null){
				spicrsld = map.put(key, salableProductInstanceCashRegisterStateLogDetails);
				
			}else{
				spicrsld.setCode(spicrsld.getCode()+Constant.CHARACTER_COMA+salableProductInstanceCashRegisterStateLogDetails.getCode());
				spicrsld.setQuantity(String.valueOf(Integer.parseInt(spicrsld.getQuantity())+Integer.parseInt(salableProductInstanceCashRegisterStateLogDetails.getQuantity())));
				spicrsld.setTotalPrice(format(numberBusiness.parse(BigDecimal.class,spicrsld.getTotalPrice())
						.add(numberBusiness.parse(BigDecimal.class, salableProductInstanceCashRegisterStateLogDetails.getTotalPrice())) ));
			}
			*/
		}
		
		for(SalableProductInstanceCashRegisterStateLogDetails salableProductInstanceCashRegisterStateLogDetails : map.values()){
			FormatSequenceArguments<Long> arguments = new FormatSequenceArguments<>(1l);
			Collection<Long> identifiers = new ArrayList<>();
			for(String value : StringUtils.split(salableProductInstanceCashRegisterStateLogDetails.getCode(), Constant.CHARACTER_COMA))
				identifiers.add(Long.parseLong(value));
			salableProductInstanceCashRegisterStateLogDetails.setCode(inject(NumberBusiness.class).formatSequences(identifiers, arguments));
			if(RootConstant.Code.TimeDivisionType.DAY.equals(timeDivisionTypeCode))
				;
				//salableProductInstanceCashRegisterStateLogDetails.setDate(formatDate(salableProductInstanceCashRegisterStateLogDetails.getMaster().getGlobalIdentifier()
				//		.getCreationDate()));
		}
		
		return map.values();
	}
	
	private static CompanyReportRepository INSTANCE;
	public static CompanyReportRepository getInstance() {
		return INSTANCE;
	}
}
