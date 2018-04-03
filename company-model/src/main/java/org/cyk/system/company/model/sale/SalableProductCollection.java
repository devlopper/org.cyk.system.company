package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE) @Accessors(chain=true)
public class SalableProductCollection extends AbstractCollection<SalableProductCollectionItem> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_PROPERTIES) @NotNull private SalableProductCollectionProperties properties;
	@Embedded private Cost cost;
	
	/**/
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}
	
	@Override
	public SalableProductCollection addCascadeOperationToMasterFieldNames(String... fieldNames) {
		return (SalableProductCollection) super.addCascadeOperationToMasterFieldNames(fieldNames);
	}
	
	@Override
	public SalableProductCollection setItemsSynchonizationEnabled(Boolean synchonizationEnabled) {
		return (SalableProductCollection) super.setItemsSynchonizationEnabled(synchonizationEnabled);
	}
	
	@Override
	public SalableProductCollection add(Collection<SalableProductCollectionItem> items) {
		return (SalableProductCollection) super.add(items);
	}
	
	public SalableProductCollectionProperties getProperties(Boolean instanciateIfValueIsNull){
		return readFieldValue(FIELD_PROPERTIES, instanciateIfValueIsNull);
	}
	
	public SalableProductCollection setPropertiesTypeFromCode(String code) {
		getProperties(Boolean.TRUE).setTypeFromCode(code);
		return this;
	}
	
	public static final String FIELD_PROPERTIES = "properties";
	public static final String FIELD_COST = "cost";
	
	
	public static final String COLUMN_PROPERTIES = FIELD_PROPERTIES;
}
