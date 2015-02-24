package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(unique=true)
	private String identificationNumber;
	
	@ManyToOne
	private Customer customer;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	private BigDecimal cost = BigDecimal.ZERO;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	private BigDecimal balance;
	
	/**/
	@Transient private Collection<SaledProduct> saledProducts = new ArrayList<>();
	
	@Transient private Collection<Payment> payments = new ArrayList<>();
}
