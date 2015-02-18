package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private Customer customer;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	private BigDecimal cost;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private Boolean soldOut;
	
	/**/
	@Transient
	private List<SaledProduct> saledProducts = new ArrayList<>();
	
}
