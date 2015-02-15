package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.ui.web.primefaces.page.crud.CrudManyPage;

@Named @ViewScoped @Getter @Setter
public class ServicePackageManyPage extends CrudManyPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

}