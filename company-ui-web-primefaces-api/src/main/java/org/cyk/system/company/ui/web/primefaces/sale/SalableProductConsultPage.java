package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.sale.SalableProductInstanceDetails;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SalableProductConsultPage extends AbstractConsultPage<SalableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private Table<SalableProductInstanceDetails> instanceTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		
		
		instanceTable.setShowHeader(Boolean.TRUE);
					
	}

}
