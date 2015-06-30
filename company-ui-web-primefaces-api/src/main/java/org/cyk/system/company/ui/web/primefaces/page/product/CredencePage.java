package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.impl.product.CredenceReportTableDetails;
import org.cyk.system.company.model.product.Customer;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class CredencePage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CustomerBusiness customerBusiness;
	
	private Table<CredenceReportTableDetails> table;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("field.credence");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<CredenceReportTableDetails> details = new ArrayList<>();
		for(Customer customer : customerBusiness.findByBalanceNotEquals(BigDecimal.ZERO))
			details.add(new CredenceReportTableDetails(customer));
		table = createDetailsTable(CredenceReportTableDetails.class, details, "model.entity.customer");	
		//table.setReportIdentifier(CompanyBusinessLayer.getInstance().getReportCredence());
		table.setTitle(null);
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		table.setShowToolBar(Boolean.TRUE);
		table.setIdentifiableClass(Customer.class);
	}
	
}