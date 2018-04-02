package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.time.Period;
import org.cyk.system.root.persistence.impl.globalidentification.GlobalIdentifierPersistenceMappingConfiguration;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.cdi.AbstractBean;
import org.cyk.utility.common.helper.FieldHelper;

public class GlobalIdentifierPersistenceMappingConfigurations extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -4261213077562876945L;

	public void configure(){
		FieldHelper.Field.get(IntangibleProduct.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE
				)).getConstraints().setIsNullable(Boolean.FALSE).setIsUnique(Boolean.TRUE);
		
		FieldHelper.Field.get(TangibleProduct.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE
				)).getConstraints().setIsNullable(Boolean.FALSE).setIsUnique(Boolean.TRUE);
		
		FieldHelper.Field.get(ProductStore.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE
				)).getConstraints().setIsNullable(Boolean.FALSE).setIsUnique(Boolean.TRUE);
		
		FieldHelper.Field.get(SalableProduct.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE
				)).getConstraints().setIsNullable(Boolean.FALSE).setIsUnique(Boolean.TRUE);
		
		FieldHelper.Field.get(SalableProductCollectionItem.class, SalableProductCollectionItem.FIELD_COLLECTION).getConstraints().setIsNullable(Boolean.FALSE);
		
		FieldHelper.Field.get(StockableProductStore.class, FieldHelper.getInstance().buildPath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE
				)).getConstraints().setIsNullable(Boolean.FALSE).setIsUnique(Boolean.TRUE);
		
		FieldHelper.Field.get(Sale.class, FieldHelper.getInstance().buildPath(Sale.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_EXISTENCE_PERIOD
				,Period.FIELD_FROM_DATE)).getConstraints().setDatePart(Constant.Date.Part.DATE_AND_TIME_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
		
		GlobalIdentifierPersistenceMappingConfiguration configuration = new GlobalIdentifierPersistenceMappingConfiguration();
		GlobalIdentifierPersistenceMappingConfiguration.Property property = new GlobalIdentifierPersistenceMappingConfiguration.Property(
				commonUtils.attributePath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER, GlobalIdentifier.FIELD_CODE),new javax.persistence.Column() {
			@Override public Class<? extends Annotation> annotationType() {return null;}
			@Override public boolean updatable() {return false;}	
			@Override public boolean unique() {return Boolean.TRUE;}
			@Override public String table() {return null;}
			@Override public int scale() {return 0;}
			@Override public int precision() {return 0;}
			@Override public boolean nullable() {return false;}
			@Override public String name() {return null;}
			@Override public int length() {return 0;}
			@Override public boolean insertable() {return false;}
			@Override public String columnDefinition() {return null;}
		});
        configuration.addProperties(property);
        GlobalIdentifierPersistenceMappingConfiguration.register(Sale.class, configuration);
        
        property = new GlobalIdentifierPersistenceMappingConfiguration.Property(
				commonUtils.attributePath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER, GlobalIdentifier.FIELD_CODE),new javax.persistence.Column() {
			@Override public Class<? extends Annotation> annotationType() {return null;}
			@Override public boolean updatable() {return false;}	
			@Override public boolean unique() {return Boolean.TRUE;}
			@Override public String table() {return null;}
			@Override public int scale() {return 0;}
			@Override public int precision() {return 0;}
			@Override public boolean nullable() {return false;}
			@Override public String name() {return null;}
			@Override public int length() {return 0;}
			@Override public boolean insertable() {return false;}
			@Override public String columnDefinition() {return null;}
		});
        configuration.addProperties(property);
        GlobalIdentifierPersistenceMappingConfiguration.register(SalableProduct.class, configuration);
        
        property = new GlobalIdentifierPersistenceMappingConfiguration.Property(
				commonUtils.attributePath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER, GlobalIdentifier.FIELD_CODE),new javax.persistence.Column() {
			@Override public Class<? extends Annotation> annotationType() {return null;}
			@Override public boolean updatable() {return false;}	
			@Override public boolean unique() {return Boolean.TRUE;}
			@Override public String table() {return null;}
			@Override public int scale() {return 0;}
			@Override public int precision() {return 0;}
			@Override public boolean nullable() {return false;}
			@Override public String name() {return null;}
			@Override public int length() {return 0;}
			@Override public boolean insertable() {return false;}
			@Override public String columnDefinition() {return null;}
		});
        configuration.addProperties(property);
        GlobalIdentifierPersistenceMappingConfiguration.register(SaleCashRegisterMovementCollection.class, configuration);
        
        property = new GlobalIdentifierPersistenceMappingConfiguration.Property(
				commonUtils.attributePath(AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER, GlobalIdentifier.FIELD_CODE),new javax.persistence.Column() {
			@Override public Class<? extends Annotation> annotationType() {return null;}
			@Override public boolean updatable() {return false;}	
			@Override public boolean unique() {return Boolean.TRUE;}
			@Override public String table() {return null;}
			@Override public int scale() {return 0;}
			@Override public int precision() {return 0;}
			@Override public boolean nullable() {return false;}
			@Override public String name() {return null;}
			@Override public int length() {return 0;}
			@Override public boolean insertable() {return false;}
			@Override public String columnDefinition() {return null;}
		});
        configuration.addProperties(property);
        GlobalIdentifierPersistenceMappingConfiguration.register(SaleCashRegisterMovement.class, configuration);
	}
	
}
