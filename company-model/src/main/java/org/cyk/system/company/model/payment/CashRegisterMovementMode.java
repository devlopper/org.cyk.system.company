package org.cyk.system.company.model.payment;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.ENUMERATION) @Deprecated
public class CashRegisterMovementMode extends AbstractEnumeration implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	private Boolean supportDocumentIdentifier = Boolean.FALSE;
	
	public CashRegisterMovementMode(String code, String libelle, String description) {
		super(code, libelle,null, description);
	}
	
	public static final String FIELD_SUPPORT_DOCUMENT_IDENTIFIER = "supportDocumentIdentifier";
}
