package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.ui.web.primefaces.page.mathematics.AbstractMovementListPage;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementListPage extends AbstractMovementListPage<CashRegisterMovement,CashRegister> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	
}
