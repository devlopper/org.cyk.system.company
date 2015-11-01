package org.cyk.system.company.business.impl.aibsbakery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.root.model.userinterface.InputName;

import lombok.Getter;

@Singleton @Getter
public class AibsBakeryFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;

	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
	private ProductionEnergy productionEnergyGas,productionEnergyFuel,productionEnergyWood;
	private TangibleProduct tangibleProductFarine,tangibleProductAmeliorant,tangibleProductPain;
	private ProductionPlanResource productionPlanResourceFarine,productionPlanResourceAmeliorant;
	private ProductionPlanMetric productionPlanMetricQuantity;
	private InputName inputNameQuantity;
	private ProductionUnit productionUnit1;
	
	private void parameters(){
		productionEnergyGas = createEnumeration(ProductionEnergy.class, ProductionEnergy.GAS);
		productionEnergyFuel = createEnumeration(ProductionEnergy.class, ProductionEnergy.FUEL);
		productionEnergyWood = createEnumeration(ProductionEnergy.class, ProductionEnergy.WOOD);
		inputNameQuantity = createEnumeration(InputName.class, "Quantity");
		
		Collection<Product> products = new ArrayList<>();
		tangibleProductFarine = createTangibleProduct("Farine", null, products);
		tangibleProductAmeliorant = createTangibleProduct("Ameliorant", null, products);
		
		Collection<ProductionPlanResource> productionPlanResources = new ArrayList<>();
		productionPlanResourceFarine = createProductionPlanResource(tangibleProductFarine, productionPlanResources);
		productionPlanResourceAmeliorant = createProductionPlanResource(tangibleProductAmeliorant, productionPlanResources);
		
		Collection<ProductionPlanMetric> productionPlanMetrics = new ArrayList<>();
		productionPlanMetricQuantity = createProductionPlanMetric(inputNameQuantity, productionPlanMetrics);
		
		flush(Product.class, productBusiness, products);
		flush(ProductionPlanResource.class, productionPlanResourceBusiness, productionPlanResources);
		flush(ProductionPlanMetric.class, productionPlanMetricBusiness, productionPlanMetrics);
	}
	
	private void structure(){
		Collection<ProductionUnit> productionUnits = new ArrayList<>();
		productionUnit1 = createProductionUnit(getCompany(), getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS), productionUnits);
		//createProductionUnit(company, getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS), productionUnits);
		
		Collection<ProductionPlan> productionPlans = new ArrayList<>();
		ProductionPlan productionPlan = createProductionPlan("PP1", productionUnit1, tangibleProductPain, rootBusinessLayer.getTimeDivisionTypeDay(), productionPlans);
		productionPlan.getRows().add(productionPlanResourceFarine);
		productionPlan.getRows().add(productionPlanResourceAmeliorant);
		productionPlan.getColumns().add(productionPlanMetricQuantity);
		
		Collection<Production> productions = new ArrayList<>();
		createProduction(productionPlan,new Object[][]{
			{productionPlanResourceFarine, productionPlanMetricQuantity,"1"},{productionPlanResourceAmeliorant, productionPlanMetricQuantity,"2"}
		}, productions);
		
		flush(ProductionUnit.class, productionUnitBusiness, productionUnits);
		flush(ProductionPlan.class, productionPlanBusiness, productionPlans);
		flush(Production.class, productionBusiness, productions);
	}
	
	private void business(){
		
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
