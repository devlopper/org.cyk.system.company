package org.cyk.system.company.business.impl.iesa;

import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.utility.common.generator.AbstractGeneratable;

public class IesaPopulate extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private static String CASH_REGISTER_001 = "CR001",CASH_REGISTER_002 = "CR002",CASH_REGISTER_003 = "CR003";
    //private static String SALE_001 = "S001",SALE_002 = "S002";
    private static String CUSTOMER_001 = "C001",CUSTOMER_002 = "C002";
   
    /*private SaleCashRegisterMovement saleCashRegisterMovement;
    private SalableProductCollection salableProductCollection;
    private SalableProductCollectionItem salableProductCollectionItem;
    */
    @Override
    protected void populate() {
    	AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
    	PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/salecashregistermovementlogo.png";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
    	super.populate();
    	create(inject(TangibleProductBusiness.class).instanciateMany(new String[][]{{"TP01","Books Package Primary"},{"TP02", "Polo shirt Primary"}
    		,{"TP03", "Sportswear Primary"},{"TP04","ID Card"},{"TP05","School Uniform (Up and Down) Primary"},{"TP06","Culottes Primary"}}));
    	create(inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{{"IP01","Re-registration"},{"IP02", "Tuition fees"},{"IP03", "Exam (STA)"}
    		,{"IP04","UCMAS Program"},{"IP05","Swimming (First, Second & Third Terms)"},{"IP06","Art and Craft (First, Second & Third Terms)"}
    		,{"IP07","Transportation (till June 2017)"}}));
    	create(inject(SalableProductBusiness.class).instanciateMany(new String[][]{{"","","","","","","","","","","TP01","60000"}
    		,{"","","","","","","","","","","TP02", "7000"},{"","","","","","","","","","","TP03", "7000"},{"","","","","","","","","","","TP04", "4000"}
    		,{"","","","","","","","","","","TP05", "14000"},{"","","","","","","","","","","TP06", "7000"},{"","","","","","","","","","","IP01", "65000"}
    		,{"","","","","","","","","","","IP02", "1450000"},{"","","","","","","","","","","IP03", "45000"},{"","","","","","","","","","","IP04", "40000"}
    		,{"","","","","","","","","","","IP05", "30000"},{"","","","","","","","","","","IP06", "30000"},{"","","","","","","","","","","IP07", "30000"}}));
    	
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
    	
    	create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001));
    	create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002));
    }
    
    @Override
    protected void _execute_() {
    	super._execute_();
    	companyBusinessTestHelper.create(inject(SaleBusiness.class).instanciateOne("SCHOOLFEES001",CUSTOMER_001, new Object[][]{{"TP05",2},{"TP06",1},{"IP04",3},{"IP05",3},{"IP06",3},{"IP07",10}}));    	
    	companyBusinessTestHelper.create(inject(SaleBusiness.class).instanciateOne("SCHOOLFEES002",CUSTOMER_001, new Object[][]{{"IP01",1},{"IP02",1,100000},{"TP01",1},{"TP02",1},{"TP03",1},{"TP04",1},{"IP03",1}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p001",null, CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","1000"},{"SCHOOLFEES002","500"}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p002",null, CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","1500"},{"SCHOOLFEES002","800"}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p003",null, CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","2100"},{"SCHOOLFEES002","900"}}));
    	
		System.exit(0);
    }
    
    
         
}
