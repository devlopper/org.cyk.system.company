package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionUnit extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@OneToOne @NotNull private Company company;
	
	@ManyToOne @NotNull private ProductionEnergy energy;
	
	@Temporal(TemporalType.TIMESTAMP) private Date previousReportDate;
	
	@Temporal(TemporalType.TIMESTAMP) @NotNull private Date nextReportDate;
	
	@ManyToOne @NotNull private TimeDivisionType reportIntervalTimeDivisionType;

	public ProductionUnit(Company company, ProductionEnergy energy, Date nextReportDate,
			TimeDivisionType reportIntervalTimeDivisionType) {
		super();
		this.company = company;
		this.energy = energy;
		this.nextReportDate = nextReportDate;
		this.reportIntervalTimeDivisionType = reportIntervalTimeDivisionType;
	}
	
	

}
