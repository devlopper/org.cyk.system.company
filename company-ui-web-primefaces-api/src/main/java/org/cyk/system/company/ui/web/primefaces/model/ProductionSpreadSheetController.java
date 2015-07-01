package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;

@Getter @Setter @NoArgsConstructor
public class ProductionSpreadSheetController implements Serializable {

	private static final long serialVersionUID = -6574548621164268562L;

	private Production production;
	private List<ProductionPlanModelInput> rows;
	private List<ProductionPlanModelMetric> columns;
	private List<ProductionInput> cells;
	private Boolean editable = Boolean.FALSE;
	private String rowHeader="HEADER";

	public ProductionSpreadSheetController(Production production,
			List<ProductionPlanModelInput> rows,
			List<ProductionPlanModelMetric> columns, List<ProductionInput> cells) {
		super();
		this.production = production;
		this.rows = rows;
		this.columns = columns;
		this.cells = cells;
	}
	
	public ProductionInput cell(Integer row, Integer column) {
		return cells.get(row * columns.size() + column);
	}
	
	public String cellText(Integer row, Integer column) {
		return cells.get(row * columns.size() + column)+"";
	}
	
	public String rowText(Integer row) {
		return rows.get(row).getTangibleProduct().toString();
	}

}