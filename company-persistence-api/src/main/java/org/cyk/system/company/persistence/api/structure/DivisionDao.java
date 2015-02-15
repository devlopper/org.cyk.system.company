package org.cyk.system.company.persistence.api.structure;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.root.persistence.api.pattern.tree.AbstractDataTreeDao;

public interface DivisionDao extends AbstractDataTreeDao<Division,DivisionType> {

}
