package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.EmploymentAgreementTypeBusiness;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementTypeDao;
import org.cyk.system.root.business.impl.AbstractEnumerationBusinessImpl;

public class EmploymentAgreementTypeBusinessImpl extends AbstractEnumerationBusinessImpl<EmploymentAgreementType, EmploymentAgreementTypeDao> implements EmploymentAgreementTypeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public EmploymentAgreementTypeBusinessImpl(EmploymentAgreementTypeDao dao) {
		super(dao);
	}
	
	public static class BuilderOneDimensionArray extends AbstractEnumerationBusinessImpl.BuilderOneDimensionArray<EmploymentAgreementType> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(EmploymentAgreementType.class);
		}
		
	}	
}
