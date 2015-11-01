package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class ProductionPlanModelCrudOnePage extends AbstractCrudOnePage<ProductionPlan> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	
	
}