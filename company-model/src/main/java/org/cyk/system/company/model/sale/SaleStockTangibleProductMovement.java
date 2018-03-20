package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS) @Deprecated
public class SaleStockTangibleProductMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@OneToOne @NotNull private Sale sale;
	
	@Transient private StockTangibleProductMovement stockTangibleProductMovement;

	/**/
	
	public static final String FIELD_SALE = "sale";
	public static final String FIELD_STOCK_TANGIBLE_PRODUCT_STOCK_MOVEMENT = "stockTangibleProductStockMovement";
}
