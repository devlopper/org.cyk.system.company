package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.persistence.api.structure.CompanyDao;
import org.cyk.system.root.business.api.geography.ContactCollectionBusiness;
import org.cyk.system.root.business.impl.party.AbstractPartyBusinessImpl;

public class CompanyBusinessImpl extends AbstractPartyBusinessImpl<Company, CompanyDao> implements CompanyBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CompanyBusinessImpl(CompanyDao dao) {
		super(dao);
	}
	
	@Override
	public Company instanciateOne() {
		Company company = super.instanciateOne();
		company.setContactCollection(inject(ContactCollectionBusiness.class).instanciateOne());
		return company;
	}
	
	public static class BuilderOneDimensionArray extends org.cyk.system.root.business.impl.helper.InstanceHelper.BuilderOneDimensionArray<Company> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(Company.class);
			addFieldCodeName();
		}
		
	}
	
}
