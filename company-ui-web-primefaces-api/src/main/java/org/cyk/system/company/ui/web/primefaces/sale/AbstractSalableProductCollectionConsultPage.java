package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.web.primefaces.page.AbstractCollectionConsultPage;

@Getter @Setter
public abstract class AbstractSalableProductCollectionConsultPage<IDENTIFIABLE extends AbstractIdentifiable> extends AbstractCollectionConsultPage<IDENTIFIABLE,SalableProductCollection,SalableProductCollectionItem,SalableProductCollectionItemDetails> implements Serializable {
	
	private static final long serialVersionUID = 3274187086682750183L;
	
}
