package org.cyk.system.company.business.impl.structure;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.persistence.api.structure.DivisionDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeBusinessImpl;

public class DivisionBusinessImpl extends AbstractDataTreeBusinessImpl<Division,DivisionDao,DivisionType> implements DivisionBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public DivisionBusinessImpl(DivisionDao dao) {
        super(dao);
    }
 
}
