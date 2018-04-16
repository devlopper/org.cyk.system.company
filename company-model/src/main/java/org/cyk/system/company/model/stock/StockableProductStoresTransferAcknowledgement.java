package org.cyk.system.company.model.stock;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class StockableProductStoresTransferAcknowledgement extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_TRANSFER,unique=true) @NotNull private StockableProductStoresTransfer transfer;
	
	/**/
	
	public StockableProductStoresTransferAcknowledgement setTransferFromCode(String code) {
		this.transfer = getFromCode(StockableProductStoresTransfer.class, code);
		return this;
	}
	
	public static final String FIELD_TRANSFER = "transfer";
	
	public static final String COLUMN_TRANSFER = FIELD_TRANSFER;
	
	/**/
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<StockableProductStoresTransferAcknowledgement> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
}
