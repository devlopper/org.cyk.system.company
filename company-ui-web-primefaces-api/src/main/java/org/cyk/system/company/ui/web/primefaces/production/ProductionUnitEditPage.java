package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ProductionUnitEditPage extends AbstractCrudOnePage<ProductionUnit> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
				
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ProductionUnit.class);
	}
	
	@Override
	protected ProductionUnit instanciateIdentifiable() {
		ProductionUnit productionUnit = super.instanciateIdentifiable();
		productionUnit.setCompany(new Company());
		return productionUnit;
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ProductionUnit> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputText
		private String name;
		
		@Override
		public void read() {
			super.read();
			name = identifiable.getCompany().getName();
		}
		
		@Override
		public void write() {
			super.write();
			identifiable.getCompany().setName(name);
		}
	}
	
}