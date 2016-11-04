package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.web.primefaces.page.AbstractCollectionConsultPage;

@Getter @Setter
public abstract class AbstractSalableProductCollectionConsultPage<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable,ITEM_DETAILS extends AbstractOutputDetails<?>> extends AbstractCollectionConsultPage<COLLECTION,ITEM,ITEM_DETAILS> implements Serializable {
	
	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected Collection<ITEM> findByCollection(COLLECTION collection) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
