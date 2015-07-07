package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateRow;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.ui.api.UIManager;

@Getter @Setter @NoArgsConstructor
public class ProductionSpreadSheetController implements Serializable {

	private static final long serialVersionUID = -6574548621164268562L;

	private ProductionSpreadSheet production;
	private List<ProductionSpreadSheetTemplateRow> rows;
	private List<ProductionSpreadSheetTemplateColumn> columns;
	private List<ProductionSpreadSheetCell> cells;
	private Boolean editable = Boolean.FALSE;
	private String rowHeader="HEADER",title="TITLE";

	public ProductionSpreadSheetController(ProductionSpreadSheet production,List<ProductionSpreadSheetTemplateRow> rows,List<ProductionSpreadSheetTemplateColumn> columns, List<ProductionSpreadSheetCell> cells) {
		super();
		this.production = production;
		this.rows = rows;
		this.columns = columns;
		this.cells = cells;
		this.title = UIManager.getInstance().getTimeBusiness().formatDate(production.getCreationDate());
	}
	
	public ProductionSpreadSheetCell cell(Integer row, Integer column) {
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