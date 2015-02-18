package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Customer;
import org.cyk.ui.api.model.AbstractActorFormModel;

@Getter @Setter
public class CustomerFormModel extends AbstractActorFormModel<Customer> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;
	
}
