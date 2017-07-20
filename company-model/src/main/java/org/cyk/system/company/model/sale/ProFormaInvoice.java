package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class ProFormaInvoice extends AbstractSale implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public ProFormaInvoice() {}
	
}
