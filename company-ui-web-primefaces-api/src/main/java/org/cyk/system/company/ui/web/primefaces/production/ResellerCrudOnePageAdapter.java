package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.api.model.party.AbstractActorEditFormModel;
import org.cyk.ui.web.primefaces.page.party.AbstractActorCrudOnePageAdapter;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

public class ResellerCrudOnePageAdapter extends AbstractActorCrudOnePageAdapter<Reseller> implements Serializable {

	private static final long serialVersionUID = 4370361826462886031L;

	public ResellerCrudOnePageAdapter() {
		super(Reseller.class);
		FormConfiguration configuration = getFormConfiguration(Crud.CREATE);
		configuration.addRequiredFieldNames(ResellerEditFormModel.FIELD_PRODUCTION_UNIT);
		configuration.addRequiredFieldNames(ResellerEditFormModel.FIELD_SALARY);
	}
	
	@Override
	public Class<?> getFormModelClass() {
		return ResellerEditFormModel.class;
	}
	/**/
	
	@Getter @Setter
	public static class ResellerEditFormModel extends AbstractActorEditFormModel<Reseller>  implements Serializable {
		private static final long serialVersionUID = -3897201743383535836L;
		@Input @InputNumber @NotNull private BigDecimal salary = BigDecimal.ZERO;
		@Input/*(label=@Text(value="field.value"))*/ @InputChoice @InputOneChoice @InputOneCombo @NotNull 
		protected ProductionUnit productionUnit;	
		
		
		public static final String FIELD_PRODUCTION_UNIT = "productionUnit";
		public static final String FIELD_SALARY = "salary";
		
		@Override
		public void write() {
			super.write();
			identifiable.setProductionUnit(productionUnit);
		}
	}

}
