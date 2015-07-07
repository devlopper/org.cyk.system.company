package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetTemplateRow;

@Getter @Setter @NoArgsConstructor @Entity
public class ProductionSpreadSheetTemplateRow extends AbstractSpreadSheetTemplateRow<ProductionSpreadSheetTemplate> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private TangibleProduct tangibleProduct;

	public ProductionSpreadSheetTemplateRow(TangibleProduct tangibleProduct) {
		super();
		this.tangibleProduct = tangibleProduct;
	}
	
	
	
}

