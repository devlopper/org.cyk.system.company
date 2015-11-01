package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionReseller extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@OneToOne @NotNull private Production production;
	
	@ManyToOne @NotNull private ResellerProduction productTaking;

}
