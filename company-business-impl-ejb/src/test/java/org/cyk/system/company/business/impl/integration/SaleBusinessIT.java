package org.cyk.system.company.business.impl.integration;

import static org.cyk.system.company.business.impl.__data__.FakedDataSet.TANGIBLE_PRODUCT_TP1;
import static org.cyk.system.root.model.RootConstant.Code.MovementCollectionType.SALE_BALANCE;
import static org.cyk.system.root.model.RootConstant.Code.MovementCollectionType.STOCK_REGISTER;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessTestHelper.TestCase;
import org.cyk.system.company.business.impl.__data__.FakedDataSet;
import org.cyk.system.company.business.impl.__data__.RealDataSet;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.business.api.information.IdentifiableCollectionBusiness;
import org.cyk.system.root.business.api.information.IdentifiableCollectionItemBusiness;
import org.cyk.system.root.business.api.mathematics.MovementBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.impl__data__.DataSet;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.information.IdentifiableCollection;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.MovementCollectionType;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionDao;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.RandomHelper;
import org.junit.Test;

public class SaleBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;

    static {
    	ClassHelper.getInstance().map(DataSet.Listener.class, Data.class);
    }
    
    @Test
    public void crudSale(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	testCase.create(sale);
    	assertNotNull(sale.getCode());
    	testCase.assertNotNull(MovementCollection.class, RootConstant.Code.generate(sale.getCode(),SALE_BALANCE));
    	testCase.assertSaleCost(sale.getCode(),null,null,null,null);
    	testCase.clean();
    	testCase.assertNull(MovementCollection.class, RootConstant.Code.generate(sale.getCode(),SALE_BALANCE));
    }
    
    @Test
    public void crudSaleWithSynchronisation(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertSalableProductCollection(saleCode,"0","0","0","0");
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItem(){
    	TestCase testCase = instanciateTestCase();
    	testCase.countAll(Movement.class);
    	testCase.addClasses(SalableProductCollection.class,SalableProductCollectionItem.class);
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "8");
    	//testCase.assertmovem
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "8");
    	
    	testCase.clean();
    	testCase.assertCountAll(Movement.class, 2);
    	testCase.deleteAll(Movement.class);
    }
    
    @Test
    public void crudSaleWithAtLeastTwoItems(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "2");
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class);
    }
    
    @Test
    public void crudSaleWithItemsUpdate(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	testCase.create(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "1");
    	testCase.assertSalableProductCollection(saleCode,"2","200","31","169");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "2");
    	testCase.assertSalableProductCollection(saleCode,"3","350","54","296");
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class);
    }
    
    @Test
    public void computeChanges(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "0", "0", "0", "0");
    	
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("2"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(), "2","200","31","169");
    	
    	salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,FakedDataSet.TANGIBLE_PRODUCT_TP3));
    	salableProductCollectionItem.setQuantity(new BigDecimal("1"));
    	
    	inject(SaleBusiness.class).computeChanges(sale);
    	testCase.assertCost(sale.getSalableProductCollection().getCost(),"3","350","54","296");
    	
    	testCase.clean();
    }
    
    @Test
    public void crudSaleWithItemWithProductQuantityVariation(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("4"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "6");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "6", "1");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().getElements().iterator().next().setQuantity(new BigDecimal("3"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "7");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "7", "2");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().removeAll();
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "10");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "10", "3");
    	
    	testCase.clean();
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "10", "3");
    	testCase.assertMovements(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER)
    		, new String[]{"0","-4","6","false"}
    		, new String[]{"1","1","7","true"}
    		, new String[]{"2","3","10","true"});
    	testCase.deleteAll(Movement.class);
    }
    
    @Test
    public void crudSaleWithItemWithBalanceVariation(){
    	TestCase testCase = instanciateTestCase();
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("4"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "6");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "400", "0");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().addMany(inject(SalableProductCollectionItemDao.class).readByCollection(sale.getSalableProductCollection()));
    	sale.getSalableProductCollection().getItems().getElements().iterator().next().setQuantity(new BigDecimal("3"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "7");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "7", "2");
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "300", "1");
    	
    	sale = testCase.read(Sale.class, saleCode);
    	sale.getSalableProductCollection().getItems().removeAll();
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.update(sale);
    	testCase.assertCollection(SalableProductCollection.class, SalableProductCollectionItem.class, saleCode, "0");
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "10");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "10", "3");
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "0", "2");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-100","300","false"}
        		, new String[]{"1","-300","0","false"});
    	testCase.clean();
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "10", "3");  	
    	testCase.deleteAll(Movement.class);
    	
    }
    
    //@Test
    public void crudSaleWithItemWithBalanceVariationPayment(){
    	TestCase testCase = instanciateTestCase();
    	String cashRegisterMovementCollectionCode = RandomHelper.getInstance().getAlphabetic(5);
    	testCase.create(inject(MovementCollectionBusiness.class).instanciateOne(cashRegisterMovementCollectionCode).setValue(new BigDecimal("0")));
    	
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	String saleCode = RandomHelper.getInstance().getAlphabetic(3);
    	sale.setCode(saleCode);
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).instanciateOne(sale.getSalableProductCollection());
    	salableProductCollectionItem.setSalableProduct(testCase.read(SalableProduct.class,TANGIBLE_PRODUCT_TP1));
    	salableProductCollectionItem.setQuantity(new BigDecimal("10"));
    	sale.getSalableProductCollection().getItems().setSynchonizationEnabled(Boolean.TRUE);
    	testCase.create(sale);
    	testCase.assertStockableTangibleProduct(TANGIBLE_PRODUCT_TP1, "0");    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "1000", "0");
    	
    	IdentifiableCollection identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	Movement movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("200"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "800", "1");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        	);
    	
    	//2nd pay
    	identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("500"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "300", "2");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        	);
    	
    	//3rd pay
    	identifiableCollection = inject(IdentifiableCollectionBusiness.class).instanciateOne();
    	identifiableCollection.getItems().setSynchonizationEnabled(Boolean.TRUE);
    	
    	movement = inject(MovementBusiness.class).instanciateOne( inject(MovementCollectionDao.class)
    			.readByTypeByJoin(testCase.read(MovementCollectionType.class, SALE_BALANCE), sale).iterator().next());
    	movement.setValueSettableFromAbsolute(Boolean.TRUE);
    	movement.setValueAbsolute(new BigDecimal("300"));
    	movement.setAction(movement.getCollection().getType().getDecrementAction());
    	movement.setDestinationMovementCollection(inject(MovementCollectionDao.class).read(cashRegisterMovementCollectionCode));
    	inject(IdentifiableCollectionItemBusiness.class).instanciateOne(identifiableCollection).addIdentifiables(movement);
    	
    	testCase.create(identifiableCollection);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(saleCode,SALE_BALANCE), "0", "3");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        		, new String[]{"2","-300","0","false"}
        	);
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "0", "1");
    	testCase.assertMovements(RootConstant.Code.generate(saleCode,SALE_BALANCE)
        		, new String[]{"0","-200","800","false"}
        		, new String[]{"1","-500","300","false"}
        		, new String[]{"2","-300","0","false"}
        	);
    	testCase.assertMovementCollection(cashRegisterMovementCollectionCode, "1000", "3");
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.deleteByCode(Sale.class, saleCode);

    	testCase.assertNull(MovementCollection.class, RootConstant.Code.generate(saleCode,SALE_BALANCE));
    	testCase.assertMovementCollection(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER), "10", "2");
    	testCase.assertMovements(RootConstant.Code.generate(TANGIBLE_PRODUCT_TP1,STOCK_REGISTER)
        		, new String[]{"0","-10","0","false"}
        		, new String[]{"1","10","10","true"}
        	);
    	testCase.assertMovementCollection(cashRegisterMovementCollectionCode, "1000", "3");
    	testCase.assertMovements(cashRegisterMovementCollectionCode
        		, new String[]{"0","200","200","true"}
        		, new String[]{"1","500","700","true"}
        		, new String[]{"2","300","1000","true"}
        	);
    	
    	testCase.clean();
    	testCase.deleteAll(Movement.class);
    	
    	
    }
    
    /**/
    
    @SuppressWarnings("unchecked")
	public static class Data extends RealDataSet.Adapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@SuppressWarnings({ "rawtypes" })
		@Override
		public Collection getClasses() {
			return Arrays.asList(Sale.class,Movement.class);
		}
		
    }
}
