package org.cyk.system.company.ui.web.primefaces.payment;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class CashRegisterMovementTermCollectionListPage extends AbstractCrudManyPage<CashRegisterMovementTermCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	
}
