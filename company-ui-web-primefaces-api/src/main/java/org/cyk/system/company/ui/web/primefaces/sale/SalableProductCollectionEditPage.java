package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionEditPage extends AbstractSalableProductCollectionEditPage<SalableProductCollection,SalableProductCollectionItem,SalableProductCollectionEditPage.Item> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
		
	@Override
	protected void initialisation() {
		super.initialisation();
		itemCollection = createItemCollection(Item.class, SalableProductCollectionItem.class,identifiable ,new AbstractSalableProductCollectionEditPage
				.ItemCollectionAdapter.SalableProductCollectionItemAdapter<Item>(identifiable,crud));
	}
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable;
	}
	
	@Getter @Setter
	public static class Form extends AbstractDefaultForm<SalableProductCollection,SalableProductCollectionItem> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Override
		protected SalableProductCollection getSalableProductCollection() {
			return identifiable;
		}
		
	}
	
	/**/
	
	@Getter @Setter
	public static class Item extends AbstractSalableProductCollectionEditPage.AbstractItem<SalableProductCollectionItem> implements Serializable {
		private static final long serialVersionUID = 3828481396841243726L;

		@Override
		protected SalableProductCollectionItem getSalableProductCollectionItem() {
			return identifiable;
		}
		
	}

}
