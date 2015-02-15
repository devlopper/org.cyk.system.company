package org.cyk.system.company.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyPickList;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.Text;

@Getter @Setter @NoArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ServiceCollection extends AbstractEnumeration implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	@Input @InputNumber
	private BigDecimal price;
	
	@OneToMany(fetch=FetchType.LAZY)
	@Input(label=@Text(value="model.entity.service")) @InputChoice @InputManyChoice @InputManyPickList
	@Size(min=2)
	private Collection<Service> collection = new ArrayList<Service>();
	
	
	
}
