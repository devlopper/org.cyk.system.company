package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class ProductCollectionItem extends AbstractIdentifiable implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @NotNull private ProductCollection collection;
	
	@ManyToOne @NotNull private Product product;

	
}
