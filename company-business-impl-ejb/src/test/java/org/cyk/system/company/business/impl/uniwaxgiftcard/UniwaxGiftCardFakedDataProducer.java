package org.cyk.system.company.business.impl.uniwaxgiftcard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.structure.Employee;

@Singleton @Getter
public class UniwaxGiftCardFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	public static final String SALABLE_PRODUCT_AMATEUR_CODE = "AMATEUR";
	public static final String SALABLE_PRODUCT_CLASSIC_CODE = "CLASSIC";
	public static final String SALABLE_PRODUCT_PRENIUM_CODE = "PRENIUM";
	
	public static final String CASH_REGISTER_PLATEAU_CODE = "PLATEAU";
	public static final String CASH_REGISTER_YOPOUGON_CODE = "YOP";
	public static final String CASH_REGISTER_ABOBO_CODE = "ABOBO";
	
	public static final String EMPLOYEE_PLATEAU_MANAGER_REGISTRATION_CODE = "EMP001";
	public static final String EMPLOYEE_YOP_MANAGER_REGISTRATION_CODE = "EMP002";
	public static final String EMPLOYEE_ABOBO_MANAGER_REGISTRATION_CODE = "EMP003";
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		
	}
	
	@Override
	protected void structure() {
		Collection<Product> products = new ArrayList<>();
		Collection<SalableProduct> salableProducts = new ArrayList<>();
		Collection<CashRegister> cashRegisters = new ArrayList<>();
		Collection<Cashier> cashiers = new ArrayList<>();
		
		for(Object[] values : new Object[][]{ 
				{SALABLE_PRODUCT_AMATEUR_CODE,"L'amateur","10000",new String[]{"1","2","3"} } 
				,{SALABLE_PRODUCT_CLASSIC_CODE,"Le classic","25000",new String[]{"A","B","C"}} 
				,{SALABLE_PRODUCT_PRENIUM_CODE,"Le prenium","100000",new String[]{"900","901","902"}} 
				} ){
			TangibleProduct tangibleProduct = companyBusinessLayer.getTangibleProductBusiness().instanciateOne((String)values[0], (String)values[1]);
			products.add(tangibleProduct);
			
			SalableProduct salableProduct = companyBusinessLayer.getSalableProductBusiness().instanciateOne(tangibleProduct.getCode(), tangibleProduct.getName(), (String[])values[3]);
			salableProduct.setProduct(tangibleProduct);
			salableProduct.setPrice(new BigDecimal((String)values[2]));
			salableProducts.add(salableProduct);
		}
		
		flush(Product.class, products);
		flush(SalableProduct.class, salableProducts);
		
		Collection<Employee> employees = rootBusinessLayer.getRootBusinessTestHelper().createActors(Employee.class, new String[]{EMPLOYEE_PLATEAU_MANAGER_REGISTRATION_CODE
			,EMPLOYEE_YOP_MANAGER_REGISTRATION_CODE,EMPLOYEE_ABOBO_MANAGER_REGISTRATION_CODE});
		
		String[] codes = new String[]{CASH_REGISTER_PLATEAU_CODE,CASH_REGISTER_YOPOUGON_CODE,CASH_REGISTER_ABOBO_CODE};
		int i =0;
		for(Employee employee : employees){
			CashRegister cashRegister = new CashRegister(codes[i++], null, null);
			cashRegisters.add(cashRegister);
			
			Cashier cashier = new Cashier(employee.getPerson(), cashRegister);
			cashiers.add(cashier);
		}
		flush(CashRegister.class, cashRegisters);
		flush(Cashier.class, cashiers);
		
		rootRandomDataProvider.createActor(Customer.class, 5);
		
		
	}

	@Override
	protected void doBusiness(Listener listener) {
		/*Collection<Sale> sales = companyBusinessLayer.getSaleBusiness().instanciateMany(new Object[][]{
				new Object[]{"sale001",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getRegistration().getCode(),"1/1/2000 05:00","false"
						,new String[][]{ new String[]{"TP2","2"} }}
				,new Object[]{"sale002",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getRegistration().getCode(),"1/1/2000 05:15","false"
						,new String[][]{ new String[]{"TP2","1"} }}
				,new Object[]{"sale003",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getRegistration().getCode(),"1/1/2000 08:00","false"
						,new String[][]{ new String[]{"TP2","1"},new String[]{"TP5","3"} }}
		});
		flush(Sale.class, sales);*/
	}

	
}
