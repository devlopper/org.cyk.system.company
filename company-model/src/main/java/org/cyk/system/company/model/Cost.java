package org.cyk.system.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.Setter;

/**
 * An amount that has to be paid or given up in order to get something.
 * @author Christian Yao Komenan
 *
 */
@Embeddable @Getter @Setter
public class Cost extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 4948598720219343584L;

	@Column(name=COLUMN_NUMBER_OF_PROCEED_ELEMENTS,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal numberOfProceedElements = BigDecimal.ZERO;
	@Column(name=COLUMN_VALUE,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal value = BigDecimal.ZERO;
	@Column(name=COLUMN_TAX,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal tax = BigDecimal.ZERO;
	@Column(name=COLUMN_TURNOVER,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal turnover = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return value.toString();
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,numberOfProceedElements,value,tax,turnover);
	}
	
	@Override
	public String toString() {
		return String.format(LOG_FORMAT,numberOfProceedElements,value,tax,turnover);
	}
	
	private static final String LOG_FORMAT = Cost.class.getSimpleName()+"(#=%s V=%s TAX=%s T=%s)";

	public static final String FIELD_NUMBER_OF_PROCEED_ELEMENTS = "numberOfProceedElements";
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_TAX = "tax";
	public static final String FIELD_TURNOVER = "turnover";
	
	/*Persistence*/
	public static final String COLUMN_PREFIX = "cost";
	public static final String COLUMN_NUMBER_OF_PROCEED_ELEMENTS = COLUMN_PREFIX+COLUMN_NAME_WORD_SEPARATOR+FIELD_NUMBER_OF_PROCEED_ELEMENTS;
	public static final String COLUMN_VALUE = COLUMN_PREFIX+COLUMN_NAME_WORD_SEPARATOR+FIELD_VALUE;
	public static final String COLUMN_TAX = COLUMN_PREFIX+COLUMN_NAME_WORD_SEPARATOR+FIELD_TAX;
	public static final String COLUMN_TURNOVER = COLUMN_PREFIX+COLUMN_NAME_WORD_SEPARATOR+FIELD_TURNOVER;
}
