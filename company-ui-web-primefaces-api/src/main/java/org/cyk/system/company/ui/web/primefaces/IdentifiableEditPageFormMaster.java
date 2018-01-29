package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.userinterface.Component;
import org.cyk.utility.common.userinterface.collection.DataTable;
import org.cyk.utility.common.userinterface.container.Form;
import org.cyk.utility.common.userinterface.event.Event;

public class IdentifiableEditPageFormMaster extends org.cyk.ui.web.primefaces.IdentifiableEditPageFormMaster implements Serializable {
	private static final long serialVersionUID = -6211058744595898478L;
	
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
			detail.setFieldsObjectFromMaster(SalableProductCollection.FIELD_COST);
			detail.addReadOnly(Cost.FIELD_VALUE);
			
			DataTable dataTable = instanciateDataTable(SalableProductCollectionItem.class,SalableProduct.class,new DataTable.Cell.Listener.Adapter.Default(){
				private static final long serialVersionUID = 1L;
				public DataTable.Cell instanciateOne(DataTable.Column column, DataTable.Row row) {
					final DataTable.Cell cell = super.instanciateOne(column, row);
					
					if(ArrayUtils.contains(new String[]{SalableProductCollectionItem.FIELD_QUANTITY},column.getPropertiesMap().getFieldName())){
						Event.instanciateOne(cell, new String[]{FieldHelper.getInstance().buildPath(SalableProductCollectionItem.FIELD_COST,Cost.FIELD_VALUE)}
						,new String[]{/*FieldHelper.getInstance().buildPath(SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE)*/});
					}
					return cell;
				}
			}
			,Boolean.TRUE,new String[]{SalableProductCollectionItem.FIELD_QUANTITY});
			dataTable.addColumnListener(new CollectionHelper.Instance.Listener.Adapter<Component>(){
				private static final long serialVersionUID = 1L;

				@Override
				public void addOne(CollectionHelper.Instance<Component> instance, Component element, Object source,Object sourceObject) {
					super.addOne(instance, element, source, sourceObject);
					if(element instanceof DataTable.Column){
						DataTable.Column column = (DataTable.Column)element;
						if(FieldHelper.getInstance().buildPath(SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE).equals(column.getPropertiesMap().getFieldName()))
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
						else if(FieldHelper.getInstance().buildPath(SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE).equals(column.getPropertiesMap().getFieldName()))
							column.setCellValueType(DataTable.Cell.ValueType.TEXT);
					}
				}
			});
			
			dataTable.getPropertiesMap().setOnPrepareAddColumnAction(Boolean.TRUE);
			//dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedFieldNames(Arrays.asList("amount"));
			//dataTable.getPropertyRowPropertiesPropertyRemoveCommandProperties().setUpdatedColumnFieldNames(Arrays.asList("amount"));
			dataTable.prepare();
			dataTable.build();
					
			//((OutputText)dataTable.getColumn("amount").getPropertiesMap().getFooter()).getPropertiesMap().setValue(((Order)detail.getMaster().getObject()).getAmount());
			//((DataTable.Columns)dataTable.getPropertiesMap().getColumns()).getPropertiesMap().setFooterRendered(Boolean.FALSE);
			if(Constant.Action.isCreateOrUpdate((Constant.Action) getPropertiesMap().getAction())){
				((CollectionHelper.Instance<Object>)dataTable.getPropertiesMap().getRowsCollectionInstance()).addListener(
					new CollectionHelper.Instance.Listener.Adapter<Object>(){
						private static final long serialVersionUID = 1L;
						
						public void addOne(CollectionHelper.Instance<Object> instance, Object element, Object source, Object sourceObject) {
							DataTable.Row row = (DataTable.Row) element;
							SalableProductCollectionItem salableProductCollectionItem = (SalableProductCollectionItem) row.getPropertiesMap().getValue();
							//orderItem.setCode(RandomHelper.getInstance().getAlphabetic(3));
							//orderItem.setName(RandomHelper.getInstance().getAlphabetic(3));
							//salableProductCollectionItem.setOrder((Order) getObject());
							salableProductCollectionItem.setCost(new Cost());
							//salableProductCollectionItem.getCost().setValueFromString("123");
						}		
						
					}
					);
			}
			
		}
		
	}
	
	
}
