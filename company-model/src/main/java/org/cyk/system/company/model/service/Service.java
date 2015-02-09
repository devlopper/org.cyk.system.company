package org.cyk.system.company.model.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class Service extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private ServiceCollection collection;
	
	@ManyToOne
	private Division division;
	
	@ManyToOne
	private ServiceName name;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	private BigDecimal price;
}
