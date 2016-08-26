package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.EmploymentAgreementBusiness;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class EmploymentAgreementBusinessImpl extends AbstractTypedBusinessService<EmploymentAgreement, EmploymentAgreementDao> implements EmploymentAgreementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public EmploymentAgreementBusinessImpl(EmploymentAgreementDao dao) {
		super(dao);
	}

}
