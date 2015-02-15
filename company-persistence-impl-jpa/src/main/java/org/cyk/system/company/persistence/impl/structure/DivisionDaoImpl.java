package org.cyk.system.company.persistence.impl.structure;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.persistence.api.structure.DivisionDao;
import org.cyk.system.root.persistence.impl.pattern.tree.AbstractDataTreeDaoImpl;

public class DivisionDaoImpl extends AbstractDataTreeDaoImpl<Division,DivisionType> implements DivisionDao {

	private static final long serialVersionUID = 6920278182318788380L;

}
