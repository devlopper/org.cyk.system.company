package org.cyk.system.company.model.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractIdentifiableReport;

@Getter @Setter @NoArgsConstructor
public class ProductReport extends AbstractIdentifiableReport<ProductReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;
	
}
