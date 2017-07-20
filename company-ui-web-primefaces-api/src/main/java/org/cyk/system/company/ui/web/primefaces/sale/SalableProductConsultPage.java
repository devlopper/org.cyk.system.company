package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SalableProductInstanceBusiness;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceDetails;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductConsultPage extends AbstractConsultPage<SalableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private Table<SalableProductInstanceDetails> instanceTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		instanceTable = (Table<SalableProductInstanceDetails>) createDetailsTable(SalableProductInstanceDetails.class, new DetailsConfigurationListener.Table.Adapter<SalableProductInstance,SalableProductInstanceDetails>(SalableProductInstance.class, SalableProductInstanceDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SalableProductInstance> getIdentifiables() {
				return inject(SalableProductInstanceBusiness.class).findByCollection(identifiable);
			}
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.CREATE/*,Crud.READ,Crud.UPDATE*/,Crud.DELETE};
			}
		});
		
		instanceTable.setShowHeader(Boolean.TRUE);
					
	}

}
