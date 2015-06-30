package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.event.AbstractIdentifiablePeriod;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Production extends AbstractIdentifiablePeriod implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	@ManyToOne
	private ProductionPlanModel productionPlanModel;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull 
	private Date creationDate;		
	
	@Transient
	private Collection<ProductionInput> inputs = new ArrayList<>();		
	
}
