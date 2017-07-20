package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.persistence.api.structure.OwnedCompanyDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class OwnedCompanyBusinessImpl extends AbstractTypedBusinessService<OwnedCompany, OwnedCompanyDao> implements OwnedCompanyBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private CompanyBusiness companyBusiness;
	
	@Inject
	public OwnedCompanyBusinessImpl(OwnedCompanyDao dao) {
		super(dao);
	}

	@Override
	public OwnedCompany update(OwnedCompany ownedCompany) {
		//System.out.println("OwnedCompanyBusinessImpl.update()");
		companyBusiness.update(ownedCompany.getCompany());
		return super.update(ownedCompany);
	}
	
	@Override
	public OwnedCompany findDefaultOwnedCompany() {
		return dao.readBySelected(Boolean.TRUE);
	}

}
