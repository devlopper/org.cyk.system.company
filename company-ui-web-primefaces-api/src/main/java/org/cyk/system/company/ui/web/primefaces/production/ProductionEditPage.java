package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadsheet;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

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
		return inject(ProductionBusiness.class).instanciateOne(
				inject(ProductionPlanBusiness.class).find(requestParameterLong(ProductionPlan.class)));
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
		@Input @InputCalendar private Date date;
		@Input @InputNumber private BigDecimal manufacturedQuantity;
		
		@Override
		public void read() {
			super.read();
			date = identifiable.getExistencePeriod().getFromDate();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.getExistencePeriod().setFromDate(date);
			identifiable.getExistencePeriod().setToDate(identifiable.getExistencePeriod().getFromDate());
		}
	}
	
}