package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.impl.AbstractCollectionDetails;
import org.cyk.system.root.model.AbstractCollection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleCashRegisterMovementCollectionDetails extends AbstractCollectionDetails<SaleCashRegisterMovementCollection> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	private String value;
	
	public SaleCashRegisterMovementCollectionDetails(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
		super(saleCashRegisterMovementCollection);
		value = formatNumber(saleCashRegisterMovementCollection.getCashRegisterMovement().getMovement().getValue());
	}

	@Override
	public AbstractCollection<?> getCollection() {
		return master;
	}
	
	public static final String FIELD_VALUE = "value";

}