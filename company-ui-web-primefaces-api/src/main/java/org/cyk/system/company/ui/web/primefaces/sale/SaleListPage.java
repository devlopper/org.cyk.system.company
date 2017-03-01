package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.BalanceDetails;
import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.web.primefaces.Table.ColumnAdapter;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Named @ViewScoped
public class SaleListPage extends AbstractSalableProductCollectionListPage<Sale,SalableProductCollectionItem> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.getColumnListeners().add(columnAdapter = new ColumnAdapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean isColumn(Field field) {
				return isFieldNameIn(field, SaleDetails.FIELD_CODE,SaleDetails.FIELD_CUSTOMER,SaleDetails.FIELD_BALANCE,BalanceDetails.FIELD_VALUE
						,SaleDetails.FIELD_COST,CostDetails.FIELD_VALUE);
			}
			@Override
			public void added(Column column) {
				super.added(column);
				if(column.getField().getName().equals(CostDetails.FIELD_VALUE)){
					if(column.getField().getDeclaringClass().equals(CostDetails.class)){
						column.setTitle(text("field.cost"));
					}else if(column.getField().getDeclaringClass().equals(BalanceDetails.class)){
						column.setTitle(text("field.balance"));
					}
				}
			}
			
			@Override
			public List<String> getExpectedFieldNames() {
				return Arrays.asList(SaleDetails.FIELD_CODE,SaleDetails.FIELD_CUSTOMER,CostDetails.FIELD_VALUE,BalanceDetails.FIELD_VALUE);
			}
						
		});
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		
	}
	
}