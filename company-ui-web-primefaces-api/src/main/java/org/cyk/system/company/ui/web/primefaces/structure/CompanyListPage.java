package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.structure.Company;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CompanyListPage extends AbstractCrudManyPage<Company> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		rowAdapter.setOpenable(Boolean.TRUE);
		rowAdapter.setUpdatable(Boolean.TRUE);
		//table.getPrintCommandable().setRendered(Boolean.FALSE);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		table.setShowOpenCommand(Boolean.TRUE);
		table.setShowToolBar(Boolean.FALSE);
	}
	
}
