package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadsheet;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;

@Named @ViewScoped @Getter @Setter
public class ProductionEditPage extends AbstractCrudOnePage<Production> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private ProductionSpreadsheet spreadsheet;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		spreadsheet = new ProductionSpreadsheet(identifiable);
		spreadsheet.setEditable(form.getEditable());
	}
	
	@Override
	protected Production instanciateIdentifiable() {
		return CompanyBusinessLayer.getInstance().getProductionBusiness().instanciate(
				CompanyBusinessLayer.getInstance().getProductionPlanBusiness().find(requestParameterLong(ProductionPlan.class)));
	}
				
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(Production.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<Production> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputCalendar(format=Format.DATE_LONG)
		private Date date;
		@Override
		public void write() {
			super.write();
			identifiable.getPeriod().setFromDate(date);
			identifiable.getPeriod().setToDate(identifiable.getPeriod().getFromDate());
		}
	}
	
}