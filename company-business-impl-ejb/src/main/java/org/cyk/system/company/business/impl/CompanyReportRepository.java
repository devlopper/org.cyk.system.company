package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.product.CustomerReportTableRow;
import org.cyk.system.company.business.impl.product.SaleReportTableDetail;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.business.impl.product.StockDashBoardReportTableDetails;
import org.cyk.system.company.business.impl.product.TangibleProductInventoryReportTableDetails;
import org.cyk.system.company.business.impl.product.TangibleProductStockMovementLineReport;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.product.TangibleProductStockMovementSearchCriteria;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.system.root.business.impl.file.report.DefaultReportBasedOnDynamicBuilder;
import org.cyk.system.root.business.impl.file.report.ReportBasedOnDynamicBuilderAdapter;
import org.cyk.system.root.business.impl.file.report.jasper.DefaultJasperReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilder;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderIdentifiableConfiguration;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFileConfiguration;
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
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private SaleStockBusiness saleStockBusiness;
	@Inject private SaleStockInputBusiness saleStockInputBusiness;
	@Inject private SaleStockOutputBusiness saleStockOutputBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private LanguageBusiness languageBusiness;
	
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
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
	    		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),TangibleProductStockMovement.class,TangibleProductStockMovementLineReport.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new TangibleProductStockMovementLineReport((TangibleProductStockMovement) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				//parameters.setTitle("Rapport de stock");
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				TangibleProductStockMovementSearchCriteria searchCriteria = new TangibleProductStockMovementSearchCriteria(fromDate,toDate);
				
				return tangibleProductStockMovementBusiness.findByCriteria(searchCriteria);
			}
		});
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
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
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				SaleSearchCriteria searchCriteria = new SaleSearchCriteria(fromDate,toDate);
				
				return saleBusiness.findByCriteria(searchCriteria);
			}
		});
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
        		RootBusinessLayer.getInstance().getParameterGenericReportBasedOnDynamicBuilder(),SaleStock.class,SaleStockReportTableRow.class) {
			private static final long serialVersionUID = -1966207854828857772L;
			@Override
			public Object model(AbstractIdentifiable identifiable) {
				return new SaleStockReportTableRow((SaleStock) identifiable);
			}
			@Override
			public Boolean useCustomIdentifiableCollection() {
				return Boolean.TRUE;
			}
			@Override
			public Collection<? extends AbstractIdentifiable> identifiables(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				String reportType = parameters.getExtendedParameterMap().get(parameterSaleStockReportType)[0];
				Boolean saleDone = null;
				try { saleDone = Boolean.parseBoolean(parameters.getExtendedParameterMap().get(parameterSaleDone)[0]); } 
				catch (Exception e) { saleDone = Boolean.TRUE;}
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				
				if(parameterSaleStockReportCashRegister.equals(reportType)){
					parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.salestockoutput.cashregister.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.cashRegisterFieldIgnored(field);};
			        });
					SaleStockOutputSearchCriteria searchCriteria = new SaleStockOutputSearchCriteria(fromDate,toDate,saleDone);
					return saleStockOutputBusiness.findByCriteria(searchCriteria);
				}else if(parameterSaleStockReportInventory.equals(reportType)){
					parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.salestock.inventory.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.inventoryFieldIgnored(field);};
			        });
					return saleStockBusiness.findByCriteria(new SaleStockSearchCriteria(fromDate,toDate,BigDecimal.ONE,saleDone));
				}else if(parameterSaleStockReportCustomer.equals(reportType)){
					parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.salestock.customer.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.customerFieldIgnored(field);};
			        });
					return saleStockBusiness.findByCriteria(new SaleStockSearchCriteria(fromDate,toDate,saleDone));
				}else if(parameterSaleStockReportInput.equals(reportType)){
					parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.salestockinput.list.title"));
					parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
						private static final long serialVersionUID = -1279948056976719107L;
						public Boolean ignoreField(Field field) {return SaleStockReportTableRow.inputFieldIgnored(field);};
			        });
					return saleStockInputBusiness.findByCriteria(new SaleStockInputSearchCriteria(fromDate,toDate,saleDone));
				}
				return null;
			}
			
			public void beforeBuild(ReportBasedOnDynamicBuilderParameters<Object> parameters) {
				String reportType = parameters.getExtendedParameterMap().get(parameterSaleStockReportType)[0];
				/*Boolean saleDone = null;
				try { saleDone = Boolean.parseBoolean(parameters.getExtendedParameterMap().get(parameterSaleDone)[0]); } 
				catch (Exception e) { saleDone = Boolean.TRUE;}*/
				Date fromDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterFromDate())[0]));
				Date toDate = new Date(Long.parseLong(parameters.getExtendedParameterMap().get(RootBusinessLayer.getInstance().getParameterToDate())[0]));
				if(parameterSaleStockReportCashRegister.equals(reportType)){
					BigDecimal output=BigDecimal.ZERO,paid=BigDecimal.ZERO,balance=BigDecimal.ZERO;
					for(Object object : parameters.getDatas()){
						SaleStockReportTableRow row = (SaleStockReportTableRow) object;
						if(row.getSaleStock() instanceof SaleStockOutput){
							output = output.add( ((SaleStockOutput)row.getSaleStock()).getTangibleProductStockMovement().getQuantity().abs());
							paid = paid.add( ((SaleStockOutput)row.getSaleStock()).getSaleCashRegisterMovement().getCashRegisterMovement().getAmount());
							//balance = saleBusiness.sumBalanceByCriteria(criteria) 
									//balance.add(((SaleStockOutput)row.getSaleStock()).getSaleStockInput().getS.getBalance().getValue());
						}
					}
					balance = saleBusiness.sumBalanceByCriteria(new SaleSearchCriteria(fromDate,toDate));
					SaleStockReportTableRow totalRow = new SaleStockReportTableRow(null);
					//totalRow.setCustomer(RootBusinessLayer.getInstance().getLanguageBusiness().findText("total"));
					totalRow.setTakenNumberOfGoods(RootBusinessLayer.getInstance().getNumberBusiness().format(output));
					totalRow.setAmountPaid(RootBusinessLayer.getInstance().getNumberBusiness().format(paid));	
					totalRow.setBalance(RootBusinessLayer.getInstance().getNumberBusiness().format(balance));
					parameters.getDatas().add(totalRow);
				}else if(parameterSaleStockReportInventory.equals(reportType)){
					Set<String> customerIds = new LinkedHashSet<>();
					List<Object> list = new LinkedList<>();
					//get the filters
					for(Object object : parameters.getDatas()){
						SaleStockReportTableRow row = (SaleStockReportTableRow)object;
						customerIds.add(row.getCustomer());
					}
					//for each filter
					for(String customerId : customerIds){
						//filter rows
						BigDecimal in=BigDecimal.ZERO,out=BigDecimal.ZERO;
						for(Object object : parameters.getDatas()){
							SaleStockReportTableRow row = (SaleStockReportTableRow) object;
							if(row.getCustomer().equals(customerId)){
								list.add(row);
								if(row.getSaleStock() instanceof SaleStockInput){
									in = in.add( ((SaleStockInput)row.getSaleStock()).getTangibleProductStockMovement().getQuantity());
								}else if(row.getSaleStock() instanceof SaleStockOutput){
									out = out.add( ((SaleStockOutput)row.getSaleStock()).getTangibleProductStockMovement().getQuantity().abs());
								}
							}
						}
						SaleStockReportTableRow totalRow = new SaleStockReportTableRow(null);
						totalRow.setCustomer(RootBusinessLayer.getInstance().getLanguageBusiness().findText("total"));
						totalRow.setStockIn(RootBusinessLayer.getInstance().getNumberBusiness().format(in));
						totalRow.setStockOut(RootBusinessLayer.getInstance().getNumberBusiness().format(out));	
						totalRow.setRemainingNumberOfGoods(RootBusinessLayer.getInstance().getNumberBusiness().format(in.subtract(out)));
						list.add(totalRow);
					}
					parameters.setDatas(list);
				}else if(parameterSaleStockReportCustomer.equals(reportType)){
					Set<String> customerIds = new LinkedHashSet<>();
					List<Object> list = new LinkedList<>();
					//get the filters
					for(Object object : parameters.getDatas()){
						SaleStockReportTableRow row = (SaleStockReportTableRow)object;
						customerIds.add(row.getCustomer());
					}
					//for each filter
					for(String customerId : customerIds){
						//filter rows
						BigDecimal amount=BigDecimal.ZERO,paid=BigDecimal.ZERO,balance=BigDecimal.ZERO;
						for(Object object : parameters.getDatas()){
							SaleStockReportTableRow row = (SaleStockReportTableRow) object;
							if(row.getCustomer().equals(customerId)){
								list.add(row);
								if(row.getSaleStock() instanceof SaleStockInput){
									amount = amount.add( ((SaleStockInput)row.getSaleStock()).getSale().getCost());
									balance = ((SaleStockInput)row.getSaleStock()).getSale().getBalance().getCumul();
								}else if(row.getSaleStock() instanceof SaleStockOutput){
									paid = paid.add( ((SaleStockOutput)row.getSaleStock()).getSaleCashRegisterMovement().getCashRegisterMovement().getAmount());
									balance = ((SaleStockOutput)row.getSaleStock()).getSaleCashRegisterMovement().getBalance().getCumul();
								}
							}
						}
						SaleStockReportTableRow totalRow = new SaleStockReportTableRow(null);
						totalRow.setCustomer(RootBusinessLayer.getInstance().getLanguageBusiness().findText("total"));
						totalRow.setAmount(RootBusinessLayer.getInstance().getNumberBusiness().format(amount));
						totalRow.setAmountPaid(RootBusinessLayer.getInstance().getNumberBusiness().format(paid));	
						totalRow.setCumulatedBalance(RootBusinessLayer.getInstance().getNumberBusiness().format(balance));
						list.add(totalRow);
					}
					parameters.setDatas(list);
				}else if(parameterSaleStockReportInput.equals(reportType)){
					
				}	
			}
		});
		
		addConfiguration(new ReportBasedOnDynamicBuilderIdentifiableConfiguration<AbstractIdentifiable, Object>(
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
				BusinessEntityInfos tangibleProductInventoryEntityInfos = RootBusinessLayer.getInstance().getApplicationBusiness().findBusinessEntityInfos(TangibleProductInventory.class);
				Long tangibleProductInventoryId = Long.parseLong(parameters.getExtendedParameterMap().get(tangibleProductInventoryEntityInfos.getIdentifier())[0]);
				TangibleProductInventory tangibleProductInventory = tangibleProductInventoryBusiness.load(tangibleProductInventoryId);
				parameters.setTitle(languageBusiness.findClassLabelText(TangibleProductInventory.class)+" "+ RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(tangibleProductInventory.getDate()));
				return tangibleProductInventory.getDetails();
			}
		});
        
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
				Collection<? extends AbstractIdentifiable> collection = null;
				String reportType = parameters.getExtendedParameterMap().get(parameterCustomerReportType)[0];
				if(parameterCustomerReportBalance.equals(reportType)){
					if(parameterCustomerBalanceAll.equals(parameters.getExtendedParameterMap().get(parameterCustomerBalanceType)[0])){
						parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.customer.balance.title"));
						parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
							private static final long serialVersionUID = -1279948056976719107L;
							public Boolean ignoreField(Field field) {return CustomerReportTableRow.balanceFieldIgnored(field);};
				        });
						collection = customerBusiness.findAll();
					}else{
						parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.credence.title"));
						parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder(){
							private static final long serialVersionUID = -1279948056976719107L;
							public Boolean ignoreField(Field field) {return CustomerReportTableRow.credenceFieldIgnored(field);};
				        });
						collection = customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO);
					}
				}else if(parameterCustomerReportSaleStock.equals(reportType)){
					parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.customer.salestock.title"));
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
			@SuppressWarnings("unchecked")
			@Override
			public <MODEL> ReportBasedOnTemplateFile<SaleReport> build(Class<MODEL> arg0, Collection<MODEL> sales, String arg2,Boolean arg3, Map<String, String[]> arg4) {
				return saleBusiness.findReport((Collection<Sale>) sales);
			}
		});
		
		registerConfiguration(new ReportBasedOnTemplateFileConfiguration<Sale, ReportBasedOnTemplateFile<SaleReport>>(reportPointOfSaleReceipt) {
			@SuppressWarnings("unchecked")
			@Override
			public <MODEL> ReportBasedOnTemplateFile<SaleReport> build(Class<MODEL> arg0, Collection<MODEL> sales, String arg2,Boolean arg3, Map<String, String[]> arg4) {
				return saleCashRegisterMovementBusiness.findReport((Collection<SaleCashRegisterMovement>) sales);
			}
		});
		
		registerConfiguration(new ReportBasedOnDynamicBuilderConfiguration<StockDashBoardReportTableDetails, ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>>(reportStockDashboard) {        	
			@Override
			public ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails> build(ReportBasedOnDynamicBuilderParameters<StockDashBoardReportTableDetails> parameters) {
				parameters.setDatas(new ArrayList<StockDashBoardReportTableDetails>());
				for(TangibleProduct tangibleProduct : tangibleProductBusiness.findAll())
					parameters.getDatas().add(new StockDashBoardReportTableDetails(tangibleProduct) ) ;
		        
				parameters.setTitle(RootBusinessLayer.getInstance().getLanguageBusiness().findText("company.report.dashboard.stock.title"));
				parameters.setModelClass(StockDashBoardReportTableDetails.class);
				parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultReportBasedOnDynamicBuilder());
		        parameters.getReportBasedOnDynamicBuilderListeners().add(new DefaultJasperReportBasedOnDynamicBuilder());
				return (ReportBasedOnDynamicBuilder<StockDashBoardReportTableDetails>) reportBusiness.build(parameters);
			}
		});	
	}
	
	/**/
	
	private static CompanyReportRepository INSTANCE;
	public static CompanyReportRepository getInstance() {
		return INSTANCE;
	}
}
