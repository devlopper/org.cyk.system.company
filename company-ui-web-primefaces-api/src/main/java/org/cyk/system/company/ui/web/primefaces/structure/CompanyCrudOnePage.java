package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.party.AbstractPartyFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class CompanyCrudOnePage extends AbstractCrudOnePage<Company> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
		
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.UPDATE;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		return (T) ownedCompanyBusiness.findDefaultOwnedCompany().getCompany();
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(Company.class);
	}
	
	/**/
	
	@Getter @Setter
	public static class Form extends AbstractPartyFormModel<Company> implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;

		
	}
			
}