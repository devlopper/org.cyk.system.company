package org.cyk.system.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable @Accessors(chain=true) @Getter @Setter
public class Balance extends AbstractModelElement implements Serializable {
	private static final long serialVersionUID = 4330380476164276099L;

	@Column(name=COLUMN_VALUE,precision=10,scale=FLOAT_SCALE) private BigDecimal value;
	@Column(name=COLUMN_CUMUL,precision=10,scale=FLOAT_SCALE) private BigDecimal cumul;
	
	public void _set(Balance balance) {
		if(balance==null){
			/*
			this.value = null;
			this.cumul = null;
			*/
		}else{
			this.value = balance.value;
			this.cumul = balance.cumul;
		}	
	}
	
	/**/
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_CUMUL = "cumul";
	
	/* Persistence */
	public static final String COLUMN_PREFIX = "balance"+COLUMN_NAME_WORD_SEPARATOR;
	public static final String COLUMN_VALUE = COLUMN_PREFIX+FIELD_VALUE;
	public static final String COLUMN_CUMUL = COLUMN_PREFIX+FIELD_CUMUL;

}
