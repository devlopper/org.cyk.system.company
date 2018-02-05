package org.cyk.system.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.utility.common.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * An amount that has to be paid or given up in order to get something.
 * @author Christian Yao Komenan
 *
 */
@Embeddable @Accessors(chain=true) @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cost extends AbstractModelElement implements Serializable {
	private static final long serialVersionUID = 4948598720219343584L;

	@Column(name=COLUMN_VALUE,precision=10,scale=FLOAT_SCALE) private BigDecimal value;
	
	@Column(name=COLUMN_NUMBER_OF_PROCEED_ELEMENTS,precision=10,scale=FLOAT_SCALE) private BigDecimal numberOfProceedElements;
	@Column(name=COLUMN_TAX,precision=10,scale=FLOAT_SCALE) private BigDecimal tax;
	@Column(name=COLUMN_TURNOVER,precision=10,scale=FLOAT_SCALE) private BigDecimal turnover;
	
	public void _set(Cost cost) {
		if(cost==null){
			this.numberOfProceedElements = BigDecimal.ZERO;
			this.value = BigDecimal.ZERO;
			this.tax = BigDecimal.ZERO;
			this.turnover = BigDecimal.ZERO;
		}else{
			this.numberOfProceedElements = CommonUtils.getInstance().getValueIfNotNullElseDefault(cost.numberOfProceedElements,BigDecimal.ZERO);
			this.value = CommonUtils.getInstance().getValueIfNotNullElseDefault(cost.value,BigDecimal.ZERO);
			this.tax = CommonUtils.getInstance().getValueIfNotNullElseDefault(cost.tax,BigDecimal.ZERO);
			this.turnover = CommonUtils.getInstance().getValueIfNotNullElseDefault(cost.turnover,BigDecimal.ZERO);
		}	
	}
		
	public static final String FIELD_NUMBER_OF_PROCEED_ELEMENTS = "numberOfProceedElements";
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_TAX = "tax";
	public static final String FIELD_TURNOVER = "turnover";
	
	/*Persistence*/
	public static final String COLUMN_PREFIX = "cost"+COLUMN_NAME_WORD_SEPARATOR;
	public static final String COLUMN_NUMBER_OF_PROCEED_ELEMENTS = COLUMN_PREFIX+FIELD_NUMBER_OF_PROCEED_ELEMENTS;
	public static final String COLUMN_VALUE = COLUMN_PREFIX+FIELD_VALUE;
	public static final String COLUMN_TAX = COLUMN_PREFIX+FIELD_TAX;
	public static final String COLUMN_TURNOVER = COLUMN_PREFIX+FIELD_TURNOVER;

	
}
