package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionConsultPage extends AbstractSalableProductCollectionConsultPage<SalableProductCollection,SalableProductCollectionItem,SalableProductCollectionItemDetails> implements Serializable {
	
	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable;
	}

	@Override
	protected Collection<SalableProductCollectionItem> findByCollection(SalableProductCollection salableProductCollection) {
		return inject(SalableProductCollectionItemBusiness.class).findByCollection(salableProductCollection);
	}
	
}
