package org.cyk.system.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.utility.common.Constant;

import lombok.Getter;
import lombok.Setter;

@Embeddable @Getter @Setter
public class Balance extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 4330380476164276099L;

	@Column(name=COLUMN_VALUE,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal value = BigDecimal.ZERO;
	@Column(name=COLUMN_CUMUL,precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal cumul = BigDecimal.ZERO;
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	@Override
	public String toString() {
		return value+(cumul==null?Constant.EMPTY_STRING.toString():Constant.CHARACTER_COMA.toString()+cumul);
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,value,cumul);
	}
	
	private static final String LOG_FORMAT = Balance.class.getSimpleName()+"(V=%s C=%s)";
	
	/**/
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_CUMUL = "cumul";
	
	/* Persistence */
	public static final String COLUMN_PREFIX = "balance"+COLUMN_NAME_WORD_SEPARATOR;
	public static final String COLUMN_VALUE = COLUMN_PREFIX+FIELD_VALUE;
	public static final String COLUMN_CUMUL = COLUMN_PREFIX+FIELD_CUMUL;

}
