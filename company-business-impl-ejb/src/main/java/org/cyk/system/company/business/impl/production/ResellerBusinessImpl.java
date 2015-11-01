package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ResellerBusiness;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.persistence.api.production.ResellerDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

@Stateless
public class ResellerBusinessImpl extends AbstractActorBusinessImpl<Reseller, ResellerDao> implements ResellerBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ResellerBusinessImpl(ResellerDao dao) {
		super(dao);
	}

}
