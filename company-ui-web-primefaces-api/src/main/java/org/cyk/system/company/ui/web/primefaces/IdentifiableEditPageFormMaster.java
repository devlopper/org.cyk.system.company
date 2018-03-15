package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.StringHelper;
import org.cyk.utility.common.userinterface.Component;
import org.cyk.utility.common.userinterface.collection.DataTable;
import org.cyk.utility.common.userinterface.container.Form;
import org.cyk.utility.common.userinterface.event.Event;

public class IdentifiableEditPageFormMaster extends org.cyk.ui.web.primefaces.IdentifiableEditPageFormMaster implements Serializable {
	private static final long serialVersionUID = -6211058744595898478L;
	
	@Override
	protected void ____addName____() {
		if(Sale.class.equals(getPropertiesMap().getActionOnClass())){
			
		}else
			super.____addName____();
	}
	
	@Override
	protected void __prepare__() {
		super.__prepare__();
		Form.Detail detail = getDetail();
		Class<?> actionOnClass = (Class<?>) getPropertiesMap().getActionOnClass();
		detail.setFieldsObjectFromMaster();
		
		if(SalableProduct.class.equals(actionOnClass)){
			detail.add(SalableProduct.FIELD_PRODUCT).addBreak();
			detail.add(SalableProduct.FIELD_PRICE).addBreak();
		}else if(SalableProductCollection.class.equals(actionOnClass)){
						
			prepareSalableProductCollection(detail,null,Boolean.FALSE);
		}else if(Sale.class.equals(actionOnClass)){
			//((Sale)getObject()).getBalance().setValue(BigDecimal.ZERO);
			//((Sale)getObject()).getBalance().setCumul(BigDecimal.ZERO);
			prepareSalableProductCollection(detail,Sale.FIELD_SALABLE_PRODUCT_COLLECTION,Boolean.FALSE);
		}
	}
	
	public static void prepareSalableProductCollection(Form.Detail detail,final String fieldName,Boolean addExistencePeriodFromDate){
		SalableProductCollection salableProductCollection = (SalableProductCollection) (StringHelper.getInstance().isBlank(fieldName) ? detail.getMaster().getObject() 
				: FieldHelper.getInstance().read(detail.getMaster().getObject(), fieldName));
		final Boolean isCreateOrUpdate = Constant.Action.isCreateOrUpdate((Constant.Action)detail._getPropertyAction());
		salableProductCollection.getItems().setSynchonizationEnabled(isCreateOrUpdate);
		salableProductCollection.getItems().removeAll(); // will be filled up by the data table load call
		if(Boolean.TRUE.equals(addExistencePeriodFromDate))
			addExistencePeriodFromDate(detail);
		detail.setFieldsObjectFromMaster(fieldName,SalableProductCollection.FIELD_COST);
		detail.addReadOnly(Cost.FIELD_VALUE).addBreak();
		
		DataTable dataTable = detail.getMaster().instanciateDataTable(SalableProductCollectionItem.class,isCreateOrUpdate ? SalableProduct.class : null,new DataTable.Cell.Listener.Adapter.Default(),Boolean.TRUE);
		dataTable.getPropertiesMap().setChoicesIsSourceDisjoint(Boolean.FALSE);
		dataTable.getPropertiesMap().setMasterFieldName(SalableProductCollectionItem.FIELD_COLLECTION);
		dataTable.getPropertiesMap().setMaster(salableProductCollection);
		
		if(isCreateOrUpdate){
			dataTable.getPropertiesMap().setCellListener(new DataTable.Cell.Listener.Adapter.Default(){
				private static final long serialVersionUID = 1L;
				public DataTable.Cell instanciateOne(DataTable.Column column, DataTable.Row row) {
					final DataTable.Cell cell = super.instanciateOne(column, row);
					
					if(ArrayUtils.contains(new String[]{FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS)},column.getPropertiesMap().getFieldName())){
						Event.instanciateOne(cell, new String[]{FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE)}
						,new String[]{FieldHelper.getInstance().buildPath(fieldName,SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)});
					}
					return cell;
				}
			});
			
			dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedFieldNames(Arrays.asList(FieldHelper.getInstance()
					.buildPath(fieldName,SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)));
			dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedColumnFieldNames(Arrays.asList(FieldHelper.getInstance()
					.buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE)));
			
		}
		
		dataTable.addColumnListener(new CollectionHelper.Instance.Listener.Adapter<Component>(){
			private static final long serialVersionUID = 1L;

			@Override
			public void addOne(CollectionHelper.Instance<Component> instance, Component element, Object source,Object sourceObject) {
				super.addOne(instance, element, source, sourceObject);
				if(element instanceof DataTable.Column){
					DataTable.Column column = (DataTable.Column)element;
					if(FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT,SalableProduct.FIELD_PRICE).equals(column.getPropertiesMap().getFieldName())){
						if(isCreateOrUpdate)
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
					}else if(FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE).equals(column.getPropertiesMap().getFieldName())){
						if(isCreateOrUpdate)
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
						column.getPropertiesMap().setIsFooterShowable(Boolean.TRUE);
					}
				}
			}
		});
		
		dataTable.prepare();
		dataTable.build();	
				
		/*
		SalableProductCollection salableProductCollection = (SalableProductCollection) (StringHelper.getInstance().isBlank(fieldName) ? detail.getMaster().getObject() 
				: FieldHelper.getInstance().read(detail.getMaster().getObject(), fieldName));
		Boolean isCreateOrUpdate = Constant.Action.isCreateOrUpdate((Constant.Action)detail._getPropertyAction());
		salableProductCollection.getItems().setSynchonizationEnabled(isCreateOrUpdate);
		salableProductCollection.getItems().removeAll(); // will be filled up by the data table load call
		
		detail.setFieldsObjectFromMaster(fieldName,SalableProductCollection.FIELD_COST);
		detail.addReadOnly(Cost.FIELD_VALUE);
		
		DataTable dataTable = detail.getMaster().instanciateDataTable(SalableProductCollectionItem.class,isCreateOrUpdate ? SalableProduct.class : null,Boolean.TRUE);
		dataTable.getPropertiesMap().setMaster(salableProductCollection);
		
		if(isCreateOrUpdate){
			dataTable.getPropertiesMap().setCellListener(new DataTable.Cell.Listener.Adapter.Default(){
				private static final long serialVersionUID = 1L;
				public DataTable.Cell instanciateOne(DataTable.Column column, DataTable.Row row) {
					final DataTable.Cell cell = super.instanciateOne(column, row);
					
					if(ArrayUtils.contains(new String[]{SalableProductCollectionItem.FIELD_QUANTITY},column.getPropertiesMap().getFieldName())){
						Event.instanciateOne(cell, new String[]{FieldHelper.getInstance().buildPath(fieldName,SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE)}
						,new String[]{FieldHelper.getInstance().buildPath(SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)});
					}
					return cell;
				}
			});
			
			dataTable.addColumnListener(new CollectionHelper.Instance.Listener.Adapter<Component>(){
				private static final long serialVersionUID = 1L;

				@Override
				public void addOne(CollectionHelper.Instance<Component> instance, Component element, Object source,Object sourceObject) {
					super.addOne(instance, element, source, sourceObject);
					if(element instanceof DataTable.Column){
						DataTable.Column column = (DataTable.Column)element;
						if(FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT,SalableProduct.FIELD_PRICE).equals(column.getPropertiesMap().getFieldName()))
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
						else if(FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE).equals(column.getPropertiesMap().getFieldName())){
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
							column.getPropertiesMap().setIsFooterShowable(Boolean.TRUE);
						}
					}
				}
			});
			
			dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedFieldNames(Arrays.asList(FieldHelper.getInstance()
					.buildPath(fieldName,SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)));
			dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedColumnFieldNames(Arrays.asList(FieldHelper.getInstance()
					.buildPath(SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)));
			
			dataTable.getPropertiesMap().setMasterFieldName(FieldHelper.getInstance().buildPath(fieldName,SalableProductCollectionItem.FIELD_COLLECTION));
			
		}
		
		dataTable.prepare();
		dataTable.build();	
		*/
	}
	
}
