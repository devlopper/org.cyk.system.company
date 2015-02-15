package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.ui.web.primefaces.page.crud.CrudOnePage;

@Named @ViewScoped @Getter @Setter
public class PaymentCrudOnePage extends CrudOnePage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	/*
	@Override
	protected void initialisation() {
		super.initialisation();
		title = "One File Support";
	}*/
	
	/*
	@Override
	protected Object identifiableFormData(Class<?> dataClass) throws InstantiationException, IllegalAccessException {
		return AbstractFormModel.instance(PersonFormModel.class,identifiable);
	}*/

}