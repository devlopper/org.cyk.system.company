package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Getter @Setter
public abstract class AbstractSalableProductCollectionListPage<IDENTIFIABLE extends AbstractIdentifiable> extends AbstractCrudManyPage<IDENTIFIABLE> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
}
