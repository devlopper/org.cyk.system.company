package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class Employee extends AbstractActor implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne private Division division;

	@Transient private EmploymentAgreement employmentAgreement;
	
	/**/
	
	public static class SearchCriteria extends AbstractSearchCriteria<Employee> {

		private static final long serialVersionUID = -7909506438091294611L;

		public SearchCriteria() {
			this(null);
		}

		public SearchCriteria(String name) {
			super(name);
		}
		
		
	}
}
