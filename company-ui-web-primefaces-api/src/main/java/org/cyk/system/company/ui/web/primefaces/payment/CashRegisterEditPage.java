package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementCollectionEditPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterEditPage extends AbstractMovementCollectionEditPage<CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected MovementCollection getMovementCollection() {
		return identifiable.getMovementCollection();
	}
	
	@Getter @Setter
	public static class Form extends AbstractMovementCollectionForm<CashRegister> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Override
		protected MovementCollection getMovementCollection() {
			return identifiable.getMovementCollection();
		}
		
	}
	
	public static class Adapter extends AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.Adapter.Default<CashRegister> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public Adapter() {
			super(CashRegister.class);
			FormConfiguration configuration = createFormConfiguration(Crud.CREATE, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			configuration.addRequiredFieldNames(Form.FIELD_CODE,Form.FIELD_NAME);
			
			configuration = createFormConfiguration(Crud.UPDATE, UIManager.getInstance().businessEntityInfos(entityTypeClass).getUserInterface().getLabelId());
			configuration.addRequiredFieldNames(Form.FIELD_CODE,Form.FIELD_NAME);
			
			configuration = createFormConfiguration(Crud.DELETE, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
			configuration.addFieldNames(Form.FIELD_CODE,Form.FIELD_NAME);
		}
		
	}

}
