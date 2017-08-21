package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.persistence.api.structure.DivisionTypeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class DivisionTypeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<DivisionType,DivisionTypeDao> implements DivisionTypeBusiness {

	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public DivisionTypeBusinessImpl(DivisionTypeDao dao) {
        super(dao);
    } 
	
	public static class BuilderOneDimensionArray extends AbstractDataTreeTypeBusinessImpl.BuilderOneDimensionArray<DivisionType> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(DivisionType.class);
		}
		
	}

}
