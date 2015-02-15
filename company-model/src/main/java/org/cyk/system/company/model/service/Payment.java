package org.cyk.system.company.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class Payment extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private Invoice invoice;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	private BigDecimal amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	
}
