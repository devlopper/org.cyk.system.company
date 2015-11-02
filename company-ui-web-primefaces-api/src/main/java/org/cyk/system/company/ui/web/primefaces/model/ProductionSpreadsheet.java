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
import org.cyk.ui.api.model.Spreadsheet;

@Getter @Setter @NoArgsConstructor
public class ProductionSpreadsheet extends Spreadsheet<Production, ProductionPlanResource, ProductionPlanMetric, ProductionValue> implements Serializable {

	private static final long serialVersionUID = -6574548621164268562L;

	public ProductionSpreadsheet(Production production,List<ProductionPlanResource> resources,List<ProductionPlanMetric> metrics, List<ProductionValue> values) {
		super(production,resources,metrics,values);
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
		return rows.get(row).getProduct().getUiString();
	}

}