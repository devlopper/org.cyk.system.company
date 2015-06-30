package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionPlanModel extends AbstractEnumeration implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	@Transient
	private Collection<ProductionPlanModelInput> inputs = new ArrayList<>();
	
	@Transient
	private Collection<ProductionPlanModelMetric> metrics = new ArrayList<>();
}
