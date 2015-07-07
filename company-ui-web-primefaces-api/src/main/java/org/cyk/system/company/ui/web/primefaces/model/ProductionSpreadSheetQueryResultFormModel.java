package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;

@Getter @Setter
public class ProductionSpreadSheetQueryResultFormModel extends AbstractFormModel<ProductionSpreadSheet> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;
	
	@Input @InputCalendar(format=Format.DATETIME_SHORT)
	private Date creationDate;
	
}
