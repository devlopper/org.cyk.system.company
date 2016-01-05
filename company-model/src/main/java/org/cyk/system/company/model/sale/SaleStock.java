package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
@Inheritance(strategy=InheritanceType.JOINED)
public class SaleStock extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@OneToOne @NotNull
	protected TangibleProductStockMovement tangibleProductStockMovement;

	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT_STOCK_MOVEMENT = "tangibleProductStockMovement";
}
