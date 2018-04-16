package org.cyk.system.company.model.stock;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransfer;
import org.cyk.system.root.model.store.Store;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class StockableProductStoresTransfer extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_MOVEMENT_COLLECTION_VALUES_TRANSFER,unique=true) @NotNull private MovementCollectionValuesTransfer movementCollectionValuesTransfer;
	
	@Transient private Store sender;
	@Transient private Store receiver;
	
	public StockableProductStoresTransfer setSenderFromCode(String code) {
		this.sender = getFromCode(Store.class, code);
		return this;
	}
	
	public StockableProductStoresTransfer setReceiverFromCode(String code) {
		this.receiver = getFromCode(Store.class, code);
		return this;
	}
	
	public MovementCollectionValuesTransfer getMovementCollectionValuesTransfer(Boolean createIfNull) {
		if(this.movementCollectionValuesTransfer == null && Boolean.TRUE.equals(createIfNull))
			this.movementCollectionValuesTransfer = instanciateOne(MovementCollectionValuesTransfer.class);
		return this.movementCollectionValuesTransfer;
	}
	
	/**/
	
	public static final String FIELD_MOVEMENT_COLLECTION_VALUES_TRANSFER = "movementCollectionValuesTransfer";
	public static final String FIELD_SENDER = "sender";
	public static final String FIELD_RECEIVER = "receiver";
	
	public static final String COLUMN_MOVEMENT_COLLECTION_VALUES_TRANSFER = FIELD_MOVEMENT_COLLECTION_VALUES_TRANSFER;
	
	/**/
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<StockableProductStoresTransfer> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
}
