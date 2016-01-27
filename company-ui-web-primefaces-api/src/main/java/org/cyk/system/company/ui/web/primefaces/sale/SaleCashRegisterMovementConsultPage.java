package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementConsultPage extends AbstractConsultPage<SaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private FormOneData<SaleCashRegisterMovementDetails> details;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(SaleCashRegisterMovementDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<SaleCashRegisterMovement,SaleCashRegisterMovementDetails>(SaleCashRegisterMovement.class, SaleCashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
		
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		commandable.addChild(navigationManager.createReportCommandable(identifiable, CompanyReportRepository.getInstance().getReportPointOfSaleReceipt()
				,"command.see.receipt", null));
	}
			
}