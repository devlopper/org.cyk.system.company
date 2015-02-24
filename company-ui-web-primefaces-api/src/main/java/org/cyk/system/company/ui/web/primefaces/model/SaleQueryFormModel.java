package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;

@Getter @Setter
public class SaleQueryFormModel implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputCalendar
	private Date fromDate;
	
	@Input @InputCalendar
	private Date toDate;
	/*
	@Input @InputChoice @InputManyChoice @InputManyButton
	private List<BalanceType> balanceTypes;
	*/
}
