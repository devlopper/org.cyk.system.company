package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.web.primefaces.page.AbstractCollectionListPage;

@Getter @Setter
public abstract class AbstractSalableProductCollectionListPage<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable> extends AbstractCollectionListPage<COLLECTION,ITEM> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
}
