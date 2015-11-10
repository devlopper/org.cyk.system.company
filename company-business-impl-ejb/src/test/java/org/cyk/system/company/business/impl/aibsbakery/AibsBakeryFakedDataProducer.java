package org.cyk.system.company.business.impl.aibsbakery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.ManufacturedProduct;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.model.production.ResourceProduct;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.InputName;

@Singleton @Getter
public class AibsBakeryFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;

	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
	/* Products */
	private TangibleProduct tangibleProductFarine,tangibleProductAmeliorant,tangibleProductPain;
	private ManufacturedProduct manufacturedProductPain;
	private ResourceProduct resourceProductFarine,resourceProductAmeliorant;
	
	/* Production */
	private ProductionEnergy productionEnergyGas,productionEnergyFuel,productionEnergyWood;
	private ProductionPlan productionPlanPain;
	private ProductionPlanResource productionPlanResourceFarine,productionPlanResourceAmeliorant;	
	private ProductionPlanMetric productionPlanMetricQuantity;
	private InputName inputNameQuantity;
	private ProductionUnit productionUnit1;
	private Collection<Reseller> resellers;
	
	private void parameters(){
		productionEnergyGas = createEnumeration(ProductionEnergy.class, ProductionEnergy.GAS);
		productionEnergyFuel = createEnumeration(ProductionEnergy.class, ProductionEnergy.FUEL);
		productionEnergyWood = createEnumeration(ProductionEnergy.class, ProductionEnergy.WOOD);
		inputNameQuantity = createEnumeration(InputName.class, "Quantity");
		
		Collection<Product> products = new ArrayList<>();
		tangibleProductFarine = createTangibleProduct("Farine", null, products);
		tangibleProductAmeliorant = createTangibleProduct("Ameliorant", null, products);
		tangibleProductPain = createTangibleProduct("Pain", null, products);
		flush(Product.class, productBusiness, products);
		
		Collection<ManufacturedProduct> manufacturedProducts = new ArrayList<>();
		manufacturedProductPain = createManufacturedProduct(tangibleProductPain, manufacturedProducts);
		flush(ManufacturedProduct.class, manufacturedProducts);
		
		Collection<ResourceProduct> resourceProducts = new ArrayList<>();
		resourceProductFarine = createResourceProduct(tangibleProductFarine, resourceProducts);
		resourceProductAmeliorant = createResourceProduct(tangibleProductAmeliorant, resourceProducts);
		flush(ResourceProduct.class, resourceProducts);
		
		
	}
	
	private void structure(){
		Collection<ProductionUnit> productionUnits = new ArrayList<>();
		productionUnit1 = createProductionUnit(getCompany(), productionUnits);
		//createProductionUnit(company, getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS), productionUnits);
		flush(ProductionUnit.class, productionUnitBusiness, productionUnits);
		
		Collection<ProductionPlan> productionPlans = new ArrayList<>();
		productionPlanPain = createProductionPlan("PP1", productionUnit1, manufacturedProductPain, getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS),getEnumeration(TimeDivisionType.class, TimeDivisionType.DAY), productionPlans);
		productionPlanResourceFarine = createProductionPlanResource(productionPlanPain,resourceProductFarine);
		productionPlanResourceAmeliorant = createProductionPlanResource(productionPlanPain,resourceProductAmeliorant);
		productionPlanMetricQuantity = createProductionPlanMetric(productionPlanPain,inputNameQuantity);
		flush(ProductionPlan.class, productionPlanBusiness, productionPlans);
		
	}
	
	private void business(){
	
		rootRandomDataProvider.createActor(Reseller.class, 10);
		flush("Resellers");
		
		Collection<ResellerProductionPlan> resellerProductionPlans = new ArrayList<>();
		for(Reseller reseller : resellerBusiness.findAll()){
			createResellerProductionPlan(reseller, productionPlanPain, resellerProductionPlans);
		}
		flush(ResellerProductionPlan.class, resellerProductionPlanBusiness, resellerProductionPlans);
		
		Collection<Production> productions = new ArrayList<>();
		createProduction(productionPlanPain,new Object[][]{
			{productionPlanResourceFarine, productionPlanMetricQuantity,"1"},{productionPlanResourceAmeliorant, productionPlanMetricQuantity,"2"}
		}, productions);
		
		createProduction(productionPlanPain,new Object[][]{
			{productionPlanResourceFarine, productionPlanMetricQuantity,"56"},{productionPlanResourceAmeliorant, productionPlanMetricQuantity,"102"}
		}, productions);
		flush(Production.class, productionBusiness, productions);
		
		Collection<ResellerProduction> resellerProductions = new ArrayList<>();
		for(Production production : productionBusiness.findAll())
			for(Reseller reseller : resellerBusiness.findAll())
				createResellerProduction(reseller, production, resellerProductions);
		flush(ResellerProduction.class, resellerProductionBusiness, resellerProductions);
			
	}
	
	@Override
	public void produce(FakedDataProducerListener listener) {
		this.listener =listener;
		rootDataProducerHelper.setBasePackage(CompanyBusinessLayer.class.getPackage());
		parameters();
    	structure();
    	business();
    	
	}
	
	/**/
	
	
		
}
