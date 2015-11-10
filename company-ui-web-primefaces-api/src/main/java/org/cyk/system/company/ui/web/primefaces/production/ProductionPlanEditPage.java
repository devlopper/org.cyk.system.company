package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.production.ManufacturedProduct;
import org.cyk.system.company.model.production.ProductionEnergy;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.ResourceProduct;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.InputName;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.AbstractItemCollectionItem;
import org.cyk.ui.api.model.ItemCollectionListener.ItemCollectionAdapter;
import org.cyk.ui.web.primefaces.ItemCollection;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;

@Named @ViewScoped @Getter @Setter
public class ProductionPlanEditPage extends AbstractCrudOnePage<ProductionPlan> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private ItemCollection<ProductionPlanResourceItem,ProductionPlanResource> productionPlanResourceCollection;
	private ItemCollection<ProductionPlanMetricItem,ProductionPlanMetric> productionPlanMetricCollection;
	private List<SelectItem> resourceProductSelectItems,inputNameSelectItems;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		productionPlanResourceCollection = createItemCollection(form, "resources", ProductionPlanResourceItem.class, ProductionPlanResource.class
				, identifiable.getRows(),new ItemCollectionAdapter<ProductionPlanResourceItem,ProductionPlanResource>(){
			private static final long serialVersionUID = -2779323315299764384L;
			@Override
			public void write(ProductionPlanResourceItem item) {
				super.write(item);
				item.getIdentifiable().setResourceProduct(item.getResourceProduct());
			}
		});
		
		productionPlanMetricCollection = createItemCollection(form, "metrics", ProductionPlanMetricItem.class, ProductionPlanMetric.class
				, identifiable.getColumns(),new ItemCollectionAdapter<ProductionPlanMetricItem,ProductionPlanMetric>(){
			private static final long serialVersionUID = -2779323315299764384L;
			@Override
			public void write(ProductionPlanMetricItem item) {
				super.write(item);
				item.getIdentifiable().setInputName(item.getInputName());
			}
		});
		
		resourceProductSelectItems = webManager.getSelectItems(ResourceProduct.class);
		inputNameSelectItems = webManager.getSelectItems(InputName.class);
	}
	
	@Override
	public void transfer(UICommand arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		super.transfer(arg0, arg1);
		productionPlanResourceCollection.write();
		productionPlanMetricCollection.write();
	}
	
	@Override
	protected void create() {
		// TODO Auto-generated method stub
		//super.create();
		identifiable.setRows(productionPlanResourceCollection.getIdentifiables());
		identifiable.setColumns(productionPlanMetricCollection.getIdentifiables());
		debug(identifiable);
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
	/*
	@Override
	public void transfer(UICommand command, Object object) throws Exception {
		super.transfer(command, object);
		productionPlanResourceCollection.transfer();
		for(ProductionPlanResourceItem item : productionPlanResourceCollection.getItems())
			item.getIdentifiable().setResourceProduct(item.getResourceProduct());
	}*/
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ProductionPlan> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ProductionUnit productionUnit;
		@Input @InputText private String code;
		@Input @InputText private String name;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ManufacturedProduct manufacturedProduct;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ProductionEnergy energy;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private TimeDivisionType reportIntervalTimeDivisionType;
		@Input @InputCalendar private Date nextReportDate;
		@Input @InputTextarea private String comments;
	}
	
	@Getter @Setter
	public static class ProductionPlanResourceItem extends AbstractItemCollectionItem<ProductionPlanResource> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private ResourceProduct resourceProduct;
	}
	
	@Getter @Setter
	public static class ProductionPlanMetricItem extends AbstractItemCollectionItem<ProductionPlanMetric> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;
		private InputName inputName;
	}
	
}