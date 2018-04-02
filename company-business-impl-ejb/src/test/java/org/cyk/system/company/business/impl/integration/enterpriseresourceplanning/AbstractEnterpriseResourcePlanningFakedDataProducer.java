package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;

import lombok.Getter;

@Getter @Deprecated
public abstract class AbstractEnterpriseResourcePlanningFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure(Listener listener) {
		super.structure(listener);
		createSchoolProducts();
	}

	protected void createSchoolProducts(){
		
	}
}
