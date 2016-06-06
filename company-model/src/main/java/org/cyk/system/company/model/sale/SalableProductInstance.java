package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @Entity @ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.ENUMERATION)
public class SalableProductInstance extends AbstractCollectionItem<SalableProduct> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	
}
