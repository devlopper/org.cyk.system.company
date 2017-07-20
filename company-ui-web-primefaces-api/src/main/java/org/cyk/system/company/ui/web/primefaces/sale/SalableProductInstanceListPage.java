package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductInstanceListPage extends AbstractCrudManyPage<SalableProductInstance> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		rowAdapter.setOpenable(Boolean.TRUE);
		rowAdapter.setUpdatable(Boolean.TRUE);
		rowAdapter.setDeletable(Boolean.TRUE);
		/*table.setShowHeader(Boolean.FALSE);
		table.setShowToolBar(Boolean.FALSE);
		*/
		table.setShowOpenCommand(Boolean.TRUE);
	}
	
}
