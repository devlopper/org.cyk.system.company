package org.cyk.system.company.business.impl.aibsbakery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.ManufacturedProduct;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResourceProduct;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.InputName;

import lombok.Getter;

@Singleton @Getter @Deprecated
public class AibsBakeryFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;

	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
	/* Products */
	private ManufacturedProduct manufacturedProductPain;
	
	/* Production */
	private ProductionEnergy productionEnergyGas,productionEnergyFuel,productionEnergyWood;
	private ProductionPlan productionPlanPain;
	private ProductionPlanResource productionPlanResourceFarine,productionPlanResourceAmeliorant;	
	private ProductionPlanMetric productionPlanMetricQuantity;
	private InputName inputNameQuantity;
	private ProductionUnit productionUnit1;
	private Collection<Reseller> resellers;
		
	@Override
	protected void structure(Listener listener){
		createEnumerations(ProductionEnergy.class, new Object[]{ProductionEnergy.GAS,ProductionEnergy.FUEL,ProductionEnergy.WOOD});
		createEnumerations(InputName.class, new Object[]{"Quantité"});
		
		Collection<Product> candidateResourceProducts = new ArrayList<>();
		Collection<Product> products = new ArrayList<>();
		candidateResourceProducts.add(createTangibleProduct("Farine", null, products));
		candidateResourceProducts.add(createTangibleProduct("Levure", null, products));
		candidateResourceProducts.add(createTangibleProduct("Ameliorant", null, products));
		candidateResourceProducts.add(createTangibleProduct("Glace alimentaire", null, products));
		candidateResourceProducts.add(createTangibleProduct("Energie", null, products));
		
		candidateResourceProducts.add(createIntangibleProduct("Main d'oeuvre", null, products));
		
		TangibleProduct tangibleProductPain = createTangibleProduct("Pain", null, products);
		flush(Product.class, productBusiness, products);
		
		Collection<ManufacturedProduct> manufacturedProducts = new ArrayList<>();
		manufacturedProductPain = createManufacturedProduct(tangibleProductPain, manufacturedProducts);
		flush(ManufacturedProduct.class, manufacturedProducts);
		
		Collection<ResourceProduct> resourceProducts = new ArrayList<>();
		createResourceProducts(candidateResourceProducts, resourceProducts);
		flush(ResourceProduct.class, resourceProducts);
		
		Collection<ProductionUnit> productionUnits = new ArrayList<>();
		productionUnit1 = createProductionUnit(getCompany(), productionUnits);
		//createProductionUnit(company, getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS), productionUnits);
		flush(ProductionUnit.class, productionUnitBusiness, productionUnits);
		
		Collection<ProductionPlan> productionPlans = new ArrayList<>();
		productionPlanPain = createProductionPlan("PP1", productionUnit1, manufacturedProductPain, getEnumeration(ProductionEnergy.class, ProductionEnergy.GAS),getEnumeration(TimeDivisionType.class, RootConstant.Code.TimeDivisionType.DAY), productionPlans);
		for(AbstractIdentifiable identifiable : genericBusiness.use(ResourceProduct.class).find().all())
			createProductionPlanResource(productionPlanPain,(ResourceProduct) identifiable);
		//for(AbstractIdentifiable identifiable : genericBusiness.use(InputName.class).find().all())
		//	createProductionPlanMetric(productionPlanPain,(InputName) identifiable);
		/*
		productionPlanResourceFarine = createProductionPlanResource(productionPlanPain,resourceProductFarine);
		productionPlanResourceAmeliorant = createProductionPlanResource(productionPlanPain,resourceProductAmeliorant);
		productionPlanMetricQuantity = createProductionPlanMetric(productionPlanPain,inputNameQuantity);
		*/
		flush(ProductionPlan.class, productionPlanBusiness, productionPlans);
		
	}
	
	@Override
	protected void doBusiness(Listener listener){
		/*
		rootRandomDataProvider.createActor(Reseller.class, 10);
		flush("Resellers");
		
		Collection<ResellerProductionPlan> resellerProductionPlans = new ArrayList<>();
		for(Reseller reseller : resellerBusiness.findAll()){
			createResellerProductionPlan(reseller, productionPlanPain, resellerProductionPlans);
		}
		flush(ResellerProductionPlan.class, resellerProductionPlanBusiness, resellerProductionPlans);
		
		/*
		Collection<Production> productions = new ArrayList<>();
		createProduction(productionPlanPain, productions);
		flush(Production.class, productionBusiness, productions);
		
		Collection<ResellerProduction> resellerProductions = new ArrayList<>();
		for(Production production : productionBusiness.findAll())
			for(Reseller reseller : resellerBusiness.findAll())
				createResellerProduction(reseller, production, resellerProductions);
		flush(ResellerProduction.class, resellerProductionBusiness, resellerProductions);
		*/	
	}
		
	/**/
	
	
		
}
