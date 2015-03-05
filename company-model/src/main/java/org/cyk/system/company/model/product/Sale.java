package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(unique=true)
	private String identificationNumber;
	
	@ManyToOne @NotNull
	private Cashier cashier;
	
	@ManyToOne
	private Customer customer;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal cost = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal valueAddedTax = BigDecimal.ZERO;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull
	private Date date;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	private BigDecimal balance;
	
	@OneToOne
	private File report;
	
	/**/
	
	@Transient private Collection<SaledProduct> saledProducts = new ArrayList<>();
	
	@Transient private Collection<SaleCashRegisterMovement> saleCashRegisterMovements = new ArrayList<>();
}
