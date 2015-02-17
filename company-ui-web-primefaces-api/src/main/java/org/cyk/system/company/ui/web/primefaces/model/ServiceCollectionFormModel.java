package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyPickList;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Text;

@Getter @Setter
public class ServiceCollectionFormModel extends AbstractFormModel<ServiceCollection> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText @NotNull
	private String code;
	
	@Input @InputText @NotNull
	private String name;
	
	@Input(label=@Text(value="model.entity.service")) @InputChoice @InputManyChoice @InputManyPickList
	@NotNull @Size(min=2)
	private List<Service> collection;
	
	@Input @InputNumber @NotNull
	private BigDecimal price;
	
	
	
}
