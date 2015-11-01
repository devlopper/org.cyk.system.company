package org.cyk.system.company.model.product.resell;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.model.AbstractIdentifiable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class ResellerTangibleProductConfiguration extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private Reseller reseller;
	@ManyToOne @NotNull private TangibleProduct tangibleProduct;
	
	@Embedded private ResellerTangibleProductTakingConfiguration takingConfiguration = new ResellerTangibleProductTakingConfiguration();
	@Embedded private ResellerTangibleProductSaleConfiguration saleConfiguration = new ResellerTangibleProductSaleConfiguration();
	
	@Override
	public String getUiString() {
		return toString();
	}
	
}
