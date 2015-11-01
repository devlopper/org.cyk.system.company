package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.ui.api.UIManager;

@Getter @Setter @NoArgsConstructor
public class ProductionSpreadSheetController implements Serializable {

	private static final long serialVersionUID = -6574548621164268562L;

	private Production production;
	private List<ProductionPlanResource> rows;
	private List<ProductionPlanMetric> columns;
	private List<ProductionValue> cells;
	private Boolean editable = Boolean.FALSE;
	private String rowHeader="HEADER",title="TITLE";

	public ProductionSpreadSheetController(Production production,List<ProductionPlanResource> rows,List<ProductionPlanMetric> columns, List<ProductionValue> cells) {
		super();
		this.production = production;
		this.rows = rows;
		this.columns = columns;
		this.cells = cells;
		this.title = UIManager.getInstance().getTimeBusiness().formatDate(production.getCreationDate());
	}
	
	public ProductionValue cell(Integer row, Integer column) {
		return cells.get(row * columns.size() + column);
	}
	
	public String cellText(Integer row, Integer column) {
		Object value = cells.get(row * columns.size() + column).getValue();
		return value==null?"":value.toString();
	}
	
	public String rowText(Integer row) {
		return rows.get(row).getTangibleProduct().toString();
	}

}