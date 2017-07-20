package org.cyk.system.company.business.impl.uniwaxgiftcard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.structure.Employee;

import lombok.Getter;

@Singleton @Getter
public class UniwaxGiftCardFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	public static final Object[][] GIFT_CARDS = {{"5000",1,5},{"10000",100,105},{"20000",200,205}};
	
	public static final String[] SHOPS = {"CAP NORD","CAP SUD","PLTX PR ST-C2D","PLTX  P.A.A","PLTX (BOLLORE)","PLTX","PLATEAU","WOODIN 2PLTX","CASH DJIBI"
		,"PRESIDENCE DE LA REPUBLIQUE","ANAC","Mme DICKOH"};
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		
	}
	
	@Override
	protected void structure(Listener listener) {
		Collection<Product> products = new ArrayList<>();
		Collection<SalableProduct> salableProducts = new ArrayList<>();
		Collection<CashRegister> cashRegisters = new ArrayList<>();
		Collection<Employee> employees = new ArrayList<>();
		Collection<Cashier> cashiers = new ArrayList<>();
		
		for(Object[] values : GIFT_CARDS ){
			TangibleProduct tangibleProduct = inject(TangibleProductBusiness.class).instanciateOne((String)values[0], (String)values[0]);
			products.add(tangibleProduct);
			List<String> instances = new ArrayList<>();
			for(int i = (Integer)values[1]; i <= (Integer)values[2]; i++ )
				instances.add(i+"");
			
			SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne(tangibleProduct.getCode(), tangibleProduct.getName(), instances.toArray(new String[]{}));
			salableProduct.setItemCodeSeparator(null);
			salableProduct.setProduct(tangibleProduct);
			salableProduct.setPrice(new BigDecimal((String)values[0]));
			salableProducts.add(salableProduct);
		}
		
		flush(Product.class, products);
		flush(SalableProduct.class, salableProducts);
		
		for(String shop : SHOPS){
			CashRegister cashRegister = new CashRegister(shop, null, null);
			cashRegisters.add(cashRegister);
			
			Employee employee = inject(EmployeeBusiness.class).instanciateOne();
			employees.add(employee);
			
			Cashier cashier = new Cashier(employee.getPerson(), cashRegister);
			cashiers.add(cashier);
		}
		
		flush(Employee.class, employees);
		flush(CashRegister.class, cashRegisters);
		flush(Cashier.class, cashiers);
		/*
		Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = new ArrayList<>();
		SaleConfiguration saleConfiguration = inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration();
		cashRegisters = cashRegisterDao.readAll();
		List<SalableProductInstance> salableProductInstances = new ArrayList<>(salableProductInstanceDao.readAll());
		int i = 0 , size = salableProductInstances.size() / cashRegisters.size();
		Application application = applicationDao.readOneRandomly();
		for(CashRegister cashRegister : cashRegisters){
			for(int j = i; j < i+size && j < salableProductInstances.size() ; j++){				
				SalableProductInstanceCashRegister salableProductInstanceCashRegister = new SalableProductInstanceCashRegister(salableProductInstances.get(j)
						, cashRegister, saleConfiguration.getSalableProductInstanceCashRegisterFiniteStateMachine().getInitialState());
				salableProductInstanceCashRegister.getProcessing().setParty(application);
				salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(application);
				salableProductInstanceCashRegisters.add(salableProductInstanceCashRegister);
			}
			i += size;
		}
		flush(SalableProductInstanceCashRegister.class, salableProductInstanceCashRegisters);
		*/
		inject(CustomerBusiness.class).create(inject(CustomerBusiness.class).instanciateManyRandomly(5));
		
	}

	@Override
	protected void doBusiness(Listener listener) {
		/*Collection<Sale> sales = inject(SaleBusiness.class).instanciateMany(new Object[][]{
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
