package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class SaleProductInstance extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private SalableProductCollectionItem salableProductCollectionItem;
	@ManyToOne @NotNull private SalableProductInstance salableProductInstance;
	
	/*@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,salableProduct.getLogMessage(),quantity,commission,reduction,cost.getLogMessage(),sale.getComputedIdentifier());
	}*/
	
	/**/
	
	//private static final String LOG_FORMAT = SaleSalableProductInstance.class.getSimpleName()+"(%s Q=%s C=%s R=%s %s S=%s)";

	public static final String FIELD_SALE_PRODUCT = "saleProduct";
	public static final String FIELD_SALABLE_PRODUCT_INSTANCE = "salableProductInstance"; 
}
