package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceEditPage extends AbstractCrudOnePage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
	}
	
	@Override
	protected SalableProductInstance instanciateIdentifiable() {
		SalableProductInstance salableProductInstance = super.instanciateIdentifiable();
		salableProductInstance.setCollection(identifiableFromRequestParameter(SalableProduct.class,uiManager.businessEntityInfos(SalableProduct.class).getIdentifier()));
		return salableProductInstance;
	}
		
	public static class Form extends AbstractFormModel<SalableProductInstance> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private SalableProduct salableProduct;
		@Input @InputText @NotNull private String code;
		
		@Override
		public void read() {
			super.read();
			salableProduct = identifiable.getCollection();
		}
		
	}

}
