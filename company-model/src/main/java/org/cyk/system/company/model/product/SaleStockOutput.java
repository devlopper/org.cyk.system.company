package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class SaleStockOutput extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private SaleStockInput saleStockInput; 
	
	@ManyToOne @NotNull
	private SaleCashRegisterMovement saleCashRegisterMovement;
	
	@ManyToOne @NotNull
	private TangibleProductStockMovement tangibleProductStockMovement;
	
}
