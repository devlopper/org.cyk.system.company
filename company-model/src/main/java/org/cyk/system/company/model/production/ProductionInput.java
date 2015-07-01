package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetCell;

@Getter @Setter @Entity
public class ProductionInput extends AbstractSpreadSheetCell<Production, ProductionPlanModelInput, ProductionPlanModelMetric, BigDecimal> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public ProductionInput() {}

	public ProductionInput(Production spreadSheet) {
		super(spreadSheet);
	}

	public ProductionInput(ProductionPlanModelInput row, ProductionPlanModelMetric column) {
		super(row, column);
	}
	
	@Override @Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	public BigDecimal getValue() {
		return super.getValue();
	}
}

