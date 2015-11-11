package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanMetricBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanResourceBusiness;
import org.cyk.system.company.business.api.production.ProductionUnitBusiness;
import org.cyk.system.company.business.api.production.ResellerBusiness;
import org.cyk.system.company.business.api.production.ResellerProductionPlanBusiness;
import org.cyk.system.company.business.api.production.ResellerProductionBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.ManufacturedProduct;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.model.production.ResourceProduct;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.InputName;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.generator.RandomDataProvider;

@Getter
public abstract class AbstractCompanyFakedDataProducer extends AbstractFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;

	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	@Inject protected OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject protected ProductBusiness productBusiness;
	@Inject protected ProductionPlanResourceBusiness productionPlanResourceBusiness;
	@Inject protected ProductionPlanMetricBusiness productionPlanMetricBusiness;
	@Inject protected ProductionPlanBusiness productionPlanBusiness;
	@Inject protected ProductionUnitBusiness productionUnitBusiness;
	@Inject protected ProductionBusiness productionBusiness;
	@Inject protected ResellerProductionBusiness resellerProductionBusiness;
	@Inject protected ResellerProductionPlanBusiness resellerProductionPlanBusiness;
	@Inject protected ResellerBusiness resellerBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		rootRandomDataProvider.getRandomDataProviderListeners().add(new RootRandomDataProvider.RootRandomDataProviderAdapter(){
			private static final long serialVersionUID = -4292999908835323092L;

			@Override
			public void set(Object object) {
				super.set(object);
				if(object instanceof Reseller){
					((Reseller)object).setProductionUnit(rootRandomDataProvider.oneFromDatabase(ProductionUnit.class));
					((Reseller)object).setSalary(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 100000)));
					((Reseller)object).setAmountGap(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 100000)));
					((Reseller)object).setPayable(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 100000)));
				}
			}
		});
	}
	
	protected Company getCompany(){
		return ownedCompanyBusiness.findDefaultOwnedCompany().getCompany();
	}
	
	protected TangibleProduct createTangibleProduct(String name,String price,Collection<Product> products){
		TangibleProduct product = new TangibleProduct(StringUtils.remove(name, Constant.CHARACTER_SPACE),name,null,null,price==null?null:new BigDecimal(price));
		products.add(product);
		return product;
	}
	protected IntangibleProduct createIntangibleProduct(String name,String price,Collection<Product> products){
		IntangibleProduct product = new IntangibleProduct(StringUtils.remove(name, Constant.CHARACTER_SPACE),name,null,null,price==null?null:new BigDecimal(price));
		products.add(product);
		return product;
	}
	
	protected ManufacturedProduct createManufacturedProduct(Product product,Collection<ManufacturedProduct> manufacturedProducts){
		ManufacturedProduct manufacturedProduct = new ManufacturedProduct(product);
		manufacturedProducts.add(manufacturedProduct);
		return manufacturedProduct;
	}
	
	protected ResourceProduct createResourceProduct(Product product,Collection<ResourceProduct> resourceProducts){
		ResourceProduct resourceProduct = new ResourceProduct(product);
		resourceProducts.add(resourceProduct);
		return resourceProduct;
	}
	protected void createResourceProducts(Collection<Product> products,Collection<ResourceProduct> resourceProducts){
		for(Product product : products)
			createResourceProduct(product, resourceProducts);
	}
	
	protected ProductionPlanResource createProductionPlanResource(ProductionPlan productionPlan,ResourceProduct resourceProduct){
		ProductionPlanResource productionPlanResource = new ProductionPlanResource(productionPlan,resourceProduct);
		productionPlan.getRows().add(productionPlanResource);
		return productionPlanResource;
	}
	
	protected ProductionPlanMetric createProductionPlanMetric(ProductionPlan productionPlan,InputName inputName){
		ProductionPlanMetric productionPlanMetric = new ProductionPlanMetric(productionPlan,inputName);
		productionPlan.getColumns().add(productionPlanMetric);
		return productionPlanMetric;
	}

	protected ProductionUnit createProductionUnit(Company company,Collection<ProductionUnit> productionUnits){
		ProductionUnit productionUnit = new ProductionUnit();
		productionUnits.add(productionUnit);
		productionUnit.setCompany(company);
		return productionUnit;
	}
	
	protected ProductionPlan createProductionPlan(String code,ProductionUnit productionUnit,ManufacturedProduct manufacturedProduct,ProductionEnergy energy,TimeDivisionType reportIntervaltTimeDivisionType,Collection<ProductionPlan> productionPlans){
		ProductionPlan productionPlan = new ProductionPlan(code, code, productionUnit, manufacturedProduct, reportIntervaltTimeDivisionType);
		productionPlans.add(productionPlan);
		productionPlan.setEnergy(energy);
		productionPlan.setNextReportDate(new Date());
		return productionPlan;
	}
	
	protected ResellerProductionPlan createResellerProductionPlan(Reseller reseller,ProductionPlan productionPlan,Collection<ResellerProductionPlan> resellerProductionPlans){
		ResellerProductionPlan resellerProductionPlan = new ResellerProductionPlan(reseller,productionPlan);
		resellerProductionPlans.add(resellerProductionPlan);
		resellerProductionPlan.setSaleUnitPrice(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProductionPlan.setTakingUnitPrice(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProductionPlan.setCommissionRate(new BigDecimal("0."+RandomDataProvider.getInstance().randomInt(0, 100)));
		return resellerProductionPlan;
	}
	
	protected Production createProduction(ProductionPlan productionPlan,Collection<Object[]> collection,Collection<Production> productions){
		Production production = new Production();
		production.setPeriod(new Period(new Date(), new Date()));
		production.setTemplate(productionPlan);
		productions.add(production);
		for(Object[] object : collection){
			ProductionValue value = new ProductionValue((ProductionPlanResource)object[0], (ProductionPlanMetric)object[1],new BigDecimal((String)object[2]));
			//debug(value);
			production.getCells().add(value);
		}
		return production;
	}
	
	protected Production createProduction(ProductionPlan productionPlan,Collection<Production> productions){
		Collection<ProductionPlanResource> resources = productionPlanResourceBusiness.findByTemplate(productionPlan);
		Collection<ProductionPlanMetric> metrics = productionPlanMetricBusiness.findByTemplate(productionPlan);
		Collection<Object[]> collection = new ArrayList<>();
		for(ProductionPlanResource productionPlanResource : resources)
			for(ProductionPlanMetric productionPlanMetric : metrics)
				collection.add(new Object[]{productionPlanResource,productionPlanMetric,RandomDataProvider.getInstance().randomInt(1, 10)+""});
		return createProduction(productionPlan, collection, productions);
	}
	
	protected ResellerProduction createResellerProduction(ResellerProductionPlan resellerProductionPlan,Production production,Collection<ResellerProduction> resellerProductions){
		ResellerProduction resellerProduction = new ResellerProduction(resellerProductionPlan,production);
		resellerProductions.add(resellerProduction);
		resellerProduction.setTakenQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.setSoldQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.setReturnedQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.getAmount().setUser(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.getAmount().setSystem(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		return resellerProduction;
	}
		
}
