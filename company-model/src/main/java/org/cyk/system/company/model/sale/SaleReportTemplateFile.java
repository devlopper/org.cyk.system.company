package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;

@Getter @Setter @NoArgsConstructor
public class SaleReportTemplateFile extends AbstractReportTemplateFile<SaleReportTemplateFile> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport sale = new SaleReport();
	
	public SaleReportTemplateFile(Sale sale){
		setSource(sale);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		this.sale.setSource(source);
	}
	
	@Override
	public void generate() {
		super.generate();
		sale.generate();
	}
}
