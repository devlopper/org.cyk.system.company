package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.ui.web.primefaces.page.AbstractCollectionItemListPage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Named @ViewScoped
public class SaleCashRegisterMovementListPage extends AbstractCollectionItemListPage<SaleCashRegisterMovement,SaleCashRegisterMovementCollection> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
				
}