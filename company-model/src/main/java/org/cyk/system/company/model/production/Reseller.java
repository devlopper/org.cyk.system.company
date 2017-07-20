package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.party.person.AbstractActor;

@Getter @Setter @NoArgsConstructor @Entity 
public class Reseller extends AbstractActor implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private ProductionUnit productionUnit;
	
	/* Contract information */
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal salary = BigDecimal.ZERO;
	
	/* Work information */
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountGap = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal payable = BigDecimal.ZERO;
	
	/**/
	
	public static final String FIELD_PRODUCTION_UNIT = "productionUnit";
	
	/**/
	
	public static class SearchCriteria extends AbstractSearchCriteria<Reseller> {

		private static final long serialVersionUID = -7909506438091294611L;

		public SearchCriteria() {
			this(null);
		}

		public SearchCriteria(String name) {
			super(name);
		}
		
		
	}
}
