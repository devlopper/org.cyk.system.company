package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.model.AbstractOutputDetails;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ProductionListPage extends AbstractCrudManyPage<Production> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		/*table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		*/
		table.setShowOpenCommand(Boolean.TRUE);
		table.getOpenRowCommandable().setRendered(Boolean.TRUE);
		/*
		table.getPrintCommandable().setRendered(Boolean.FALSE);*/
		rowAdapter.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(Production.class);
	}
	
	@Override
	protected Class<?> __formModelClass__() {
		return Details.class;
	}
	
	@Getter @Setter
	public static class Details extends AbstractOutputDetails<Production> implements Serializable{
		private static final long serialVersionUID = 3230980200211455609L;
		@Input @InputText private String date;
		public Details(Production production) {
			super(production);
			date = timeBusiness.formatDate(production.getPeriod().getFromDate());
		}
		
	}
		
}