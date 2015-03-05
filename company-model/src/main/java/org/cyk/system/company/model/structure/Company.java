package org.cyk.system.company.model.structure;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.party.Party;

@Getter @Setter @NoArgsConstructor @Entity
public class Company extends Party implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	@ManyToOne
	private File pointOfSaleReportFile;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	@NotNull
	private BigDecimal valueAddedTaxRate = BigDecimal.ZERO;
	
}
