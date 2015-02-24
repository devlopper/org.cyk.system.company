package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.cyk.ui.web.primefaces.page.tools.AbstractExportDataTablePage;

@Named @RequestScoped
public class SaleExporterPage extends AbstractExportDataTablePage implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected String fileExtension() {
		return "pdf";
	}
}
