package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.cyk.system.company.business.api.production.ResellerProductBusiness;
import org.cyk.system.company.business.api.production.ResellerProductionBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
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
	@Inject protected ResellerProductBusiness resellerProductBusiness;
	@Inject protected ResellerBusiness resellerBusiness;
	
	protected Company getCompany(){
		return ownedCompanyBusiness.findDefaultOwnedCompany().getCompany();
	}
	
	protected TangibleProduct createTangibleProduct(String name,String price,Collection<Product> products){
		TangibleProduct product = new TangibleProduct(StringUtils.remove(name, Constant.CHARACTER_SPACE),name,null,null,price==null?null:new BigDecimal(price));
		products.add(product);
		return product;
	}
	
	protected ProductionPlanResource createProductionPlanResource(ProductionPlan productionPlan,Product product){
		ProductionPlanResource productionPlanResource = new ProductionPlanResource(productionPlan,product);
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
	
	protected ProductionPlan createProductionPlan(String code,ProductionUnit productionUnit,Product product,ProductionEnergy energy,TimeDivisionType reportIntervaltTimeDivisionType,Collection<ProductionPlan> productionPlans){
		ProductionPlan productionPlan = new ProductionPlan(code, code, productionUnit, product, reportIntervaltTimeDivisionType);
		productionPlans.add(productionPlan);
		productionPlan.setEnergy(energy);
		productionPlan.setNextReportDate(new Date());
		return productionPlan;
	}
	
	protected ResellerProduct createResellerProduct(Reseller reseller,Product product,Collection<ResellerProduct> resellerProducts){
		ResellerProduct resellerProduct = new ResellerProduct(reseller,product);
		resellerProducts.add(resellerProduct);
		resellerProduct.setSaleUnitPrice(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduct.setTakingUnitPrice(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduct.setCommissionRate(new BigDecimal("0."+RandomDataProvider.getInstance().randomInt(0, 100)));
		return resellerProduct;
	}
	
	protected Production createProduction(ProductionPlan productionPlan,Object[][] objects,Collection<Production> productions){
		Production production = new Production();
		production.setPeriod(new Period(new Date(), new Date()));
		production.setTemplate(productionPlan);
		productions.add(production);
		for(Object[] object : objects){
			ProductionValue value = new ProductionValue((ProductionPlanResource)object[0], (ProductionPlanMetric)object[1],new BigDecimal((String)object[2]));
			//debug(value);
			production.getCells().add(value);
		}
		return production;
	}
	
	protected ResellerProduction createResellerProduction(Reseller reseller,Production production,Collection<ResellerProduction> resellerProductions){
		ResellerProduction resellerProduction = new ResellerProduction(reseller,production);
		resellerProductions.add(resellerProduction);
		resellerProduction.setTakenQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.setSoldQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.setReturnedQuantity(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.getAmount().setUser(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		resellerProduction.getAmount().setSystem(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 1000)));
		return resellerProduction;
	}
		
}
