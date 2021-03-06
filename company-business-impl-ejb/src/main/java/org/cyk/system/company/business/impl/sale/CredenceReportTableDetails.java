package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

@Getter @Setter @Deprecated
public class CredenceReportTableDetails implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String registrationCode;
	@Input @InputText private String names;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String balance;
	
	public CredenceReportTableDetails(Customer customer) {
		this.registrationCode = customer.getCode();
		//this.names = customer.getPerson().getNames();
		//this.balance = inject(NumberBusiness.class).format(customer.getBalance());
	}
}