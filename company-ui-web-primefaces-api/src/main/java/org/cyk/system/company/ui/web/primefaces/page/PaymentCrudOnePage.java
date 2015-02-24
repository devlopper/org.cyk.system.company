package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class PaymentCrudOnePage extends AbstractCrudOnePage<Payment> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private PaymentBusiness paymentBusiness;
	
	private PaymentController paymentController;

	@Override
	protected void initialisation() {
		super.initialisation();
		paymentController = new PaymentController(paymentBusiness,identifiable,
				StringUtils.equals(CompanyWebManager.getInstance().getRequestParameterPayback(), requestParameter(CompanyWebManager.getInstance().getRequestParameterPaymentType()))
				);
		if(identifiable.getSale()==null)
			identifiable.setSale(saleBusiness.find(requestParameterLong(uiManager.keyFromClass(Sale.class))));
	}
	
	@Override
	protected void create() {
		paymentBusiness.create(identifiable, paymentController.getPayback());
	}
	
}