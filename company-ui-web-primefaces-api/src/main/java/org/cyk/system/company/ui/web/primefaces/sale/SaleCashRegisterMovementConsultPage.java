package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementConsultPage extends AbstractConsultPage<SaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		/*
		details.getControlSetListeners().add(new ControlSetAdapter<SaleCashRegisterMovementDetails>(){
			@Override
			public Boolean build(Field field) {
				return !ArrayUtils.contains(SaleCashRegisterMovementDetails.getFieldsToHide(), field.getName());
			}
		});
		*/
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		commandable.addChild(Builder.createReport(identifiable, CompanyReportRepository.getInstance().getReportPointOfSaleReceipt(),"command.see.receipt", null));
	}
			
}