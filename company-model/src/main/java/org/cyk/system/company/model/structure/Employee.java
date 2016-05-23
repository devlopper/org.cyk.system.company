package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.cyk.system.root.model.party.person.AbstractActor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class Employee extends AbstractActor implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne private Division division;

	@OneToOne private EmploymentAgreement employmentAgreement;
	
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
