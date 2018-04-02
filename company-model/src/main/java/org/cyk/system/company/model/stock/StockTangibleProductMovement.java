package org.cyk.system.company.model.stock;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor //@Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE) @Deprecated
public class StockTangibleProductMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private StockableProduct stockableProduct;
	@ManyToOne @NotNull private Movement movement;

	/**/
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,movement.getLogMessage(),stockableProduct.getLogMessage());
	}
	
	private static final String LOG_FORMAT = StockTangibleProductMovement.class.getSimpleName()+"(%s %s)";
	
	/**/
	
	public static final String FIELD_STOCKABLE_TANGIBLE_PRODUCT = "stockableProduct";
	public static final String FIELD_MOVEMENT = "movement";
	
}
