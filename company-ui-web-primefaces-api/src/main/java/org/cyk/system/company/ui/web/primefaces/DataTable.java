package org.cyk.system.company.ui.web.primefaces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductProperties;
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.SalableProductStoreCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.ui.web.primefaces.product.ProductIdentifiablePages;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.movement.Movement;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.Constant.Action;
import org.cyk.utility.common.helper.FieldHelper;


public class DataTable {

	public static class Listener extends org.cyk.ui.web.primefaces.DataTable.Listener {
		private static final long serialVersionUID = 1L;
		
		@Override
		public List<String> getColumnsFieldNamesOrder(org.cyk.utility.common.userinterface.collection.DataTable dataTable) {
			return super.getColumnsFieldNamesOrder(dataTable);
		}
		
		@Override
		public void processColumnsFieldNames(org.cyk.utility.common.userinterface.collection.DataTable dataTable,Collection<String> fieldNames) {
			super.processColumnsFieldNames(dataTable, fieldNames);
			Class<?> actionOnClass = (Class<?>) dataTable.getPropertiesMap().getActionOnClass();
			if(Product.class.equals(actionOnClass)){
				ProductIdentifiablePages.processProductColumnsFieldNames(dataTable, fieldNames);
			}else if(SalableProduct.class.equals(actionOnClass)){
				fieldNames.add(SalableProduct.FIELD_PRODUCT);
				fieldNames.add(SalableProduct.FIELD_PROPERTIES);
			}else if(SalableProductStoreCollection.class.equals(actionOnClass)){
				
			}else if(SalableProductStoreCollectionItem.class.equals(actionOnClass)){
				if(Constant.Action.isCreateOrUpdate((Action) dataTable.getPropertiesMap().getAction())){
					fieldNames.removeAll(Arrays.asList(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE)
							,FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME)));	
				}
				
				//fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT));
				fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_SALABLE_PRODUCT_STORE,SalableProductStore.FIELD_SALABLE_PRODUCT_PROPERTIES,SalableProductProperties.FIELD_PRICE));
				fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_COST,Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS));
				fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_COST,Cost.FIELD_REDUCTION));
				fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_COST,Cost.FIELD_VALUE));
				if(dataTable.getPropertiesMap().getMaster() == null){
					fieldNames.add(FieldHelper.getInstance().buildPath(SalableProductStoreCollectionItem.FIELD_SALABLE_PRODUCT_STORE,SalableProductStore.FIELD_PRODUCT_STORE));	
				}

				addExistencePeriodFromDate(dataTable, fieldNames);
			}else if(Sale.class.equals(actionOnClass)){
				fieldNames.remove(FieldHelper.getInstance().buildPath(Sale.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
				fieldNames.add(FieldHelper.getInstance().buildPath(Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION,SalableProductStoreCollection.FIELD_COST,Cost.FIELD_VALUE));
				//fieldNames.add(FieldHelper.getInstance().buildPath(Sale.FIELD_BALANCE_MOVEMENT_COLLECTION,MovementCollection.FIELD_VALUE));
				addExistencePeriodFromDate(dataTable, fieldNames);
			}else if(Movement.class.equals(actionOnClass)){
				if(dataTable.getPropertiesMap().getMaster() instanceof SalableProductStoreCollection || dataTable.getPropertiesMap().getMaster() instanceof Sale){
					fieldNames.remove(Movement.FIELD_COLLECTION);
				}
			}
		}
	}
	
	public static void processColumnsFieldNameSalableProductCollection(org.cyk.utility.common.userinterface.collection.DataTable dataTable,Collection<String> fieldNames){
		
	}
}
