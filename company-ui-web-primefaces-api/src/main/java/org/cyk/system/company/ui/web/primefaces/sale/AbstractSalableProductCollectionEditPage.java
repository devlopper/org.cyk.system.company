package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.web.primefaces.page.AbstractCollectionEditPage;

@Getter @Setter
public abstract class AbstractSalableProductCollectionEditPage<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable> extends AbstractCollectionEditPage<COLLECTION,ITEM> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	protected abstract SalableProductCollection getSalableProductCollection();
	
	@Override
	protected SalableProductCollection getCollection() {
		return getSalableProductCollection();
	}
	
	@Getter @Setter
	public static abstract class AbstractgetSalableProductCollectionForm<COLLECTION extends AbstractIdentifiable,ITEM extends AbstractIdentifiable> extends AbstractCollectionEditPage.AbstractForm<COLLECTION,ITEM> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		protected abstract SalableProductCollection getSalableProductCollection();
		
		@Override
		protected AbstractCollection<?> getCollection() {
			return getSalableProductCollection();
		}
		
		/**/
		
		@Getter @Setter
		public static abstract class Default<COLLECTION extends AbstractCollection<ITEM>,ITEM extends AbstractCollectionItem<COLLECTION>> extends AbstractgetSalableProductCollectionForm<COLLECTION,ITEM> implements Serializable{
			private static final long serialVersionUID = -4741435164709063863L;
			
		}
		
	}
	
	@Getter @Setter
	public static abstract class AbstractDefaultForm<COLLECTION extends AbstractCollection<ITEM>,ITEM extends AbstractCollectionItem<COLLECTION>> extends AbstractForm.Extends<COLLECTION,ITEM> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;

		
	}

}
