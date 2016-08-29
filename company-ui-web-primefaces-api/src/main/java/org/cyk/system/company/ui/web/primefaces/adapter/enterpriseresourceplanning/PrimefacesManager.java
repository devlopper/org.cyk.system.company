package org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementModeDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementTermCollectionDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementTermDetails;
import org.cyk.system.company.business.impl.payment.CashierDetails;
import org.cyk.system.company.business.impl.product.IntangibleProductDetails;
import org.cyk.system.company.business.impl.product.TangibleProductDetails;
import org.cyk.system.company.business.impl.sale.CustomerSalableProductDetails;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionDetails;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.business.impl.sale.SalableProductDetails;
import org.cyk.system.company.business.impl.structure.EmployeeDetails;
import org.cyk.system.company.business.impl.structure.EmploymentAgreementDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementModeEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementTermCollectionEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementTermEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashierEditPage;
import org.cyk.system.company.ui.web.primefaces.product.IntangibleProductEditPage;
import org.cyk.system.company.ui.web.primefaces.product.TangibleProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.CustomerSalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductCollectionEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductCollectionItemEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.structure.EmployeeEditPage;
import org.cyk.system.company.ui.web.primefaces.structure.EmploymentAgreementEditPage;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.DetailsConfiguration;

public class PrimefacesManager extends org.cyk.ui.web.primefaces.adapter.enterpriseresourceplanning.PrimefacesManager implements Serializable {

	private static final long serialVersionUID = -8716834916609095637L;
	
	public PrimefacesManager() {
		configurePaymentModule();
		configureProductModule();
		configureSaleModule();
		configureCompanyModule();
	}
	
	@Override
	public SystemMenu getSystemMenu(UserSession userSession) {
		return SystemMenuBuilder.getInstance().build(userSession);
	}
	
	protected void configurePaymentModule() {
		getFormConfiguration(Cashier.class, Crud.CREATE).addRequiredFieldNames(CashierEditPage.Form.FIELD_CASH_REGISTER,CashierEditPage.Form.FIELD_PERSON);
		registerDetailsConfiguration(CashierDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashierDetails.FIELD_CASH_REGISTER,CashierDetails.FIELD_PERSON);
					}
				};
			}
		});
		
		getFormConfiguration(CashRegister.class, Crud.CREATE).addRequiredFieldNames(CashRegisterEditPage.Form.FIELD_MOVEMENT_COLLECTION,CashRegisterEditPage.Form.FIELD_CODE,CashRegisterEditPage.Form.FIELD_NAME);
		registerDetailsConfiguration(CashRegisterDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashRegisterDetails.FIELD_MOVEMENT_COLLECTION,CashRegisterDetails.FIELD_CODE,CashRegisterDetails.FIELD_NAME);
					}
				};
			}
		});
		
		getFormConfiguration(CashRegisterMovement.class, Crud.CREATE).addRequiredFieldNames(CashRegisterMovementEditPage.Form.FIELD_CASH_REGISTER
				,CashRegisterMovementEditPage.Form.FIELD_MOVEMENT,CashRegisterMovementEditPage.Form.FIELD_COMPUTED_IDENTIFIER,CashRegisterMovementEditPage.Form.FIELD_MODE);
		registerDetailsConfiguration(CashRegisterMovementDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashRegisterMovementDetails.FIELD_CASH_REGISTER,CashRegisterMovementDetails.FIELD_MOVEMENT
								,CashRegisterMovementDetails.FIELD_COMPUTED_IDENTIFIER,CashRegisterMovementDetails.FIELD_MODE);
					}
				};
			}
		});
		
		getFormConfiguration(CashRegisterMovementMode.class, Crud.CREATE).addRequiredFieldNames(CashRegisterMovementModeEditPage.Form.FIELD_CODE
				,CashRegisterMovementModeEditPage.Form.FIELD_NAME);
		registerDetailsConfiguration(CashRegisterMovementModeDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashRegisterMovementModeDetails.FIELD_CODE,CashRegisterMovementModeDetails.FIELD_NAME);
					}
				};
			}
		});
		
		getFormConfiguration(CashRegisterMovementTerm.class, Crud.CREATE).addRequiredFieldNames(CashRegisterMovementTerm.FIELD_COLLECTION,CashRegisterMovementTermEditPage.Form.FIELD_AMOUNT
				).addFieldNames(CashRegisterMovementTermEditPage.Form.FIELD_EVENT);
		registerDetailsConfiguration(CashRegisterMovementTermDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashRegisterMovementTermDetails.FIELD_AMOUNT,CashRegisterMovementTermDetails.FIELD_EVENT);
					}
				};
			}
		});
		
		getFormConfiguration(CashRegisterMovementTermCollection.class, Crud.CREATE).addRequiredFieldNames(CashRegisterMovementTermCollectionEditPage.Form.FIELD_AMOUNT);
		registerDetailsConfiguration(CashRegisterMovementTermCollectionDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CashRegisterMovementTermCollectionDetails.FIELD_AMOUNT);
					}
				};
			}
		});
	}

	protected void configureProductModule() {
		getFormConfiguration(TangibleProduct.class, Crud.CREATE).addRequiredFieldNames(TangibleProductEditPage.Form.FIELD_CODE
				,TangibleProductEditPage.Form.FIELD_NAME);
		registerDetailsConfiguration(TangibleProductDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,TangibleProductDetails.FIELD_CODE,TangibleProductDetails.FIELD_NAME);
					}
				};
			}
		});
		
		getFormConfiguration(IntangibleProduct.class, Crud.CREATE).addRequiredFieldNames(IntangibleProductEditPage.Form.FIELD_CODE
				,IntangibleProductEditPage.Form.FIELD_NAME);
		registerDetailsConfiguration(IntangibleProductDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,IntangibleProductDetails.FIELD_CODE,IntangibleProductDetails.FIELD_NAME);
					}
				};
			}
		});
	}
	
	protected void configureSaleModule() {
		getFormConfiguration(SalableProduct.class, Crud.CREATE).addRequiredFieldNames(SalableProductEditPage.Form.FIELD_CODE
				,SalableProductEditPage.Form.FIELD_NAME);
		registerDetailsConfiguration(SalableProductDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,SalableProductDetails.FIELD_CODE,SalableProductDetails.FIELD_NAME);
					}
				};
			}
		});
		
		getFormConfiguration(SalableProductCollection.class, Crud.CREATE).addRequiredFieldNames(SalableProductCollectionEditPage.Form.FIELD_ACCOUNTINGPERIOD
				,SalableProductCollectionEditPage.Form.FIELD_COST,CostFormModel.FIELD_VALUE);
		registerDetailsConfiguration(SalableProductCollectionDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,SalableProductCollectionDetails.FIELD_ACCOUNTING_PERIOD,SalableProductCollectionDetails.FIELD_COST
								,CostDetails.FIELD_VALUE);
					}
				};
			}
		});
		
		getFormConfiguration(SalableProductCollectionItem.class, Crud.CREATE).addRequiredFieldNames(SalableProductCollectionItemEditPage.Form.FIELD_COLLECTION
				,SalableProductCollectionItemEditPage.Form.FIELD_SALABLE_PRODUCT,SalableProductCollectionItemEditPage.Form.FIELD_QUANTITY
				//,SalableProductCollectionItemEditPage.Form.FIELD_REDUCTION,SalableProductCollectionItemEditPage.Form.FIELD_COMMISSION
				,SalableProductCollectionItemEditPage.Form.FIELD_COST,CostFormModel.FIELD_VALUE);
		registerDetailsConfiguration(SalableProductCollectionItemDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,SalableProductCollectionItemDetails.FIELD_CODE,SalableProductCollectionItemDetails.FIELD_QUANTITY
								,SalableProductCollectionItemDetails.FIELD_UNIT_PRICE,SalableProductCollectionItemDetails.FIELD_COST,CostDetails.FIELD_VALUE);
					}
				};
			}
		});
		
		getFormConfiguration(CustomerSalableProduct.class, Crud.CREATE).addRequiredFieldNames(CustomerSalableProductEditPage.Form.FIELD_CUSTOMER
				,CustomerSalableProductEditPage.Form.FIELD_SALABLE_PRODUCT,CustomerSalableProductEditPage.Form.FIELD_PRICE);
		registerDetailsConfiguration(CustomerSalableProductDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,CustomerSalableProductDetails.FIELD_CUSTOMER,CustomerSalableProductDetails.FIELD_SALABLE_PRODUCT
								,CustomerSalableProductDetails.FIELD_PRICE);
					}
				};
			}
		});
		
	}
	
	protected void configureCompanyModule() {
		configurePersonFormConfiguration(Employee.class,null,new String[]{EmployeeEditPage.Form.FIELD_JOB_FUNCTION,EmployeeEditPage.Form.FIELD_EMPLOYMENT_AGREEMENT_TYPE});
		registerDetailsConfiguration(EmployeeDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new PersonDetailsControlSetAdapter(new String[]{EmployeeDetails.FIELD_JOB_FUNCTION});
			}
		});
		
		getFormConfiguration(EmploymentAgreement.class, Crud.CREATE).addRequiredFieldNames(EmploymentAgreementEditPage.Form.FIELD_TYPE
				,EmploymentAgreementEditPage.Form.FIELD_EMPLOYEE);
		registerDetailsConfiguration(EmploymentAgreementDetails.class, new DetailsConfiguration(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			@Override
			public ControlSetAdapter getFormControlSetAdapter(Class clazz) {
				return new DetailsConfiguration.DefaultControlSetAdapter(){ 
					private static final long serialVersionUID = 1L;
					@Override
					public Boolean build(Object data,Field field) {
						return isFieldNameIn(field,EmploymentAgreementDetails.FIELD_TYPE,EmploymentAgreementDetails.FIELD_EMPLOYEE);
					}
				};
			}
		});
		
	}
	
	@Override
	protected Boolean isAutoConfigureClass(Class<?> aClass) {
		return super.isAutoConfigureClass(aClass) && !ArrayUtils.contains(new Class<?>[]{Employee.class}, aClass);
	}
}
