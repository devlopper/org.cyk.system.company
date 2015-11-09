package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.InputName;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyPickList;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;

@Named @ViewScoped @Getter @Setter
public class ProductionPlanEditPage extends AbstractCrudOnePage<ProductionPlan> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
	
	@Override
	protected ProductionPlan instanciateIdentifiable() {
		ProductionPlan productionPlan = super.instanciateIdentifiable();
		productionPlan.setProductionUnit(CompanyBusinessLayer.getInstance().getProductionUnitBusiness().find(requestParameterLong(ProductionUnit.class)));
		return productionPlan;
	}
				
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ProductionPlan.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
	
	@Override
	protected void create() {
		// TODO Auto-generated method stub
		super.create();
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ProductionPlan> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ProductionUnit productionUnit;
		@Input @InputText private String code;
		@Input @InputText private String name;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Product product;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ProductionEnergy energy;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private TimeDivisionType reportIntervalTimeDivisionType;
		@Input @InputCalendar private Date nextReportDate;
		@Input @InputTextarea private String comments;
		
		@Input @InputChoice @InputManyChoice @InputManyPickList private List<Product> resources;
		@Input @InputChoice @InputManyChoice @InputManyPickList private List<InputName> metrics;
		
		@Override
		public void read() {
			super.read();
			resources = new ArrayList<>();
			for(ProductionPlanResource productionPlanResource : identifiable.getRows())
				resources.add(productionPlanResource.getProduct());
		}
		
		@Override
		public void write() {
			super.write();
			Collection<ProductionPlanResource> productionPlanResources = new ArrayList<>(identifiable.getRows());
			Collection<ProductionPlanMetric> productionPlanMetrics = new ArrayList<>(identifiable.getColumns());
			
			identifiable.getRows().clear();
			identifiable.getColumns().clear();
			
			for(Product product : resources){
				Boolean found = Boolean.FALSE;
				for(ProductionPlanResource productionPlanResource : productionPlanResources)
					if(product.equals(productionPlanResource.getProduct())){
						identifiable.getRows().add(productionPlanResource);
						found = Boolean.TRUE;
						break;
					}
				if(Boolean.FALSE.equals(found)){
					identifiable.getRows().add(new ProductionPlanResource(identifiable, product));
				}
			}
			
			for(InputName inputName : metrics){
				Boolean found = Boolean.FALSE;
				for(ProductionPlanMetric productionPlanMetric : productionPlanMetrics)
					if(product.equals(productionPlanMetric.getInputName())){
						identifiable.getColumns().add(productionPlanMetric);
						found = Boolean.TRUE;
						break;
					}
				if(Boolean.FALSE.equals(found)){
					identifiable.getColumns().add(new ProductionPlanMetric(identifiable, inputName));
				}
			}
			
		}
	}
	
}