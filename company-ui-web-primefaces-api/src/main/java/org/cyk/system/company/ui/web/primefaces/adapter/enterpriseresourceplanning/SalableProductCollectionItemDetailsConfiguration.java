package org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductCollectionItemEditPage;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.party.person.AbstractPersonDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.Table.ColumnAdapter;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.ui.web.primefaces.page.DetailsConfiguration;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class SalableProductCollectionItemDetailsConfiguration extends DetailsConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("rawtypes")
	@Override
	public ControlSetAdapter.Details getFormControlSetAdapter(Class clazz) {
		return new DetailsControlSetAdapter(clazz);
	}
	
	@Override
	public ColumnAdapter getTableColumnAdapter(@SuppressWarnings("rawtypes") Class clazz,AbstractPrimefacesPage page) {
		return new Table.ColumnAdapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean isColumn(Field field) {
				return isFieldNameIn(field,SalableProductCollectionItemDetails.FIELD_SALABLE_PRODUCT
						,SalableProductCollectionItemDetails.FIELD_CODE,SalableProductCollectionItemDetails.FIELD_QUANTITY
						,SalableProductCollectionItemDetails.FIELD_REDUCTION/*,SalableProductCollectionItemDetails.FIELD_COMMISSION*/
						,SalableProductCollectionItemDetails.FIELD_COST,CostDetails.FIELD_VALUE/*,CostDetails.FIELD_TAX,CostDetails.FIELD_TURNOVER*/);
			}
			
			@Override
			public List<String> getExpectedFieldNames() {
				return Arrays.asList(SalableProductCollectionItemDetails.FIELD_CODE,SalableProductCollectionItemDetails.FIELD_SALABLE_PRODUCT
						,SalableProductCollectionItemDetails.FIELD_QUANTITY,SalableProductCollectionItemDetails.FIELD_REDUCTION
						,SalableProductCollectionItemDetails.FIELD_COST,CostDetails.FIELD_VALUE);
			}
			
			@Override
			public void added(Column column) {
				super.added(column);
				if(column.getField().getDeclaringClass().equals(CostDetails.class))
					column.setTitle(inject(LanguageBusiness.class).findText("field.cost"));
			}
			
		};			
	}
	
	/**/
	
	@Getter @Setter
	public static class FormControlSetAdapter extends ControlSetAdapter.Form<Object> implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public FormControlSetAdapter(Class<?> identifiableClass){
			super(identifiableClass,Crud.CREATE);
		}
		
		@Override
		public List<String> getExpectedFieldNames() {
			return Arrays.asList(SalableProductCollectionItemEditPage.Form.FIELD_CODE,SalableProductCollectionItemEditPage.Form.FIELD_SALABLE_PRODUCT
					,SalableProductCollectionItemEditPage.Form.FIELD_COLLECTION,SalableProductCollectionItemEditPage.Form.FIELD_QUANTITY
					,SalableProductCollectionItemEditPage.Form.FIELD_REDUCTION//,SalableProductCollectionItemEditPage.Form.FIELD_COMMISSION
					,SalableProductCollectionItemEditPage.Form.FIELD_COST,CostFormModel.FIELD_VALUE/*,CostFormModel.FIELD_TURNOVER,CostFormModel.FIELD_TAX*/);
		}
		
	}
	
	@Getter @Setter @NoArgsConstructor
	public static class DetailsControlSetAdapter extends DetailsConfiguration.DefaultControlSetAdapter implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public DetailsControlSetAdapter(Class<?> identifiableClass) {
			super(identifiableClass);
			addFieldNamePairOrder(AbstractPersonDetails.FIELD_NAME, AbstractPersonDetails.FIELD_LASTNAMES);
		}
			
		@Override
		public List<String> getExpectedFieldNames() {
			return Arrays.asList(SalableProductCollectionItemDetails.FIELD_COLLECTION,SalableProductCollectionItemDetails.FIELD_SALABLE_PRODUCT
					,SalableProductCollectionItemDetails.FIELD_CODE,SalableProductCollectionItemDetails.FIELD_QUANTITY
					,SalableProductCollectionItemDetails.FIELD_REDUCTION//,SalableProductCollectionItemDetails.FIELD_COMMISSION
					,SalableProductCollectionItemDetails.FIELD_COST,CostDetails.FIELD_VALUE/*,CostDetails.FIELD_TAX,CostDetails.FIELD_TURNOVER*/);
		}
		
		@Override
		public Boolean build(Object data,Field field) {
			if(data instanceof SalableProductCollectionItemDetails)
				return isFieldNameIn(field,SalableProductCollectionItemDetails.FIELD_COLLECTION,SalableProductCollectionItemDetails.FIELD_SALABLE_PRODUCT
						,SalableProductCollectionItemDetails.FIELD_CODE,SalableProductCollectionItemDetails.FIELD_QUANTITY
						,SalableProductCollectionItemDetails.FIELD_REDUCTION//,SalableProductCollectionItemDetails.FIELD_COMMISSION
						,SalableProductCollectionItemDetails.FIELD_COST);
			if(data instanceof CostDetails)
				return isFieldNameIn(field,CostDetails.FIELD_VALUE,CostDetails.FIELD_TAX,CostDetails.FIELD_TURNOVER);
			return Boolean.FALSE;
		}
		
		@Override
		public String fiedLabel(
				ControlSet<AbstractOutputDetails<AbstractIdentifiable>, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
				Object data, Field field) {
			if(data instanceof CostDetails)
				return inject(LanguageBusiness.class).findText("field.cost");
			return super.fiedLabel(controlSet, data, field);
		}
		
	}
}
