package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementCrudOnePage extends AbstractCrudOnePage<SaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private CashierBusiness cashierBusiness;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		if(identifiable.getSale()==null){
			identifiable.setSale(saleBusiness.find(requestParameterLong(uiManager.keyFromClass(Sale.class))));
			identifiable.setCashRegisterMovement(new CashRegisterMovement(cashierBusiness.findByPerson((Person) getUserSession().getUser()).getCashRegister()));
			/*if(identifiable.getCashier()==null){
				renderViewErrorMessage("View Init Error!!!", "View Init Error Details!!!");
				return;
			}*/
		}
		cashRegisterController.init(identifiable,!StringUtils.equals(CompanyWebManager.getInstance().getRequestParameterPayback(), 
				requestParameter(CompanyWebManager.getInstance().getRequestParameterPaymentType())));
	}
	

}