package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.structure.OwnedCompanyDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.utility.common.helper.FieldHelper;

public class OwnedCompanyBusinessImpl extends AbstractTypedBusinessService<OwnedCompany, OwnedCompanyDao> implements OwnedCompanyBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public OwnedCompanyBusinessImpl(OwnedCompanyDao dao) {
		super(dao);
	}
	
	public static class BuilderOneDimensionArray extends org.cyk.system.root.business.impl.helper.InstanceHelper.BuilderOneDimensionArray<OwnedCompany> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(OwnedCompany.class);
			addFieldCodeName().addParameterArrayElementString(OwnedCompany.FIELD_COMPANY
					,FieldHelper.getInstance().buildPath(OwnedCompany.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_DEFAULTED));
		}
		
	}
	
}
