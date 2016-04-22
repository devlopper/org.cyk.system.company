package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.model.geography.ContactDetails;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.ConsultPageListener;
import org.cyk.ui.web.primefaces.page.ContactDetailsAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.cdi.AbstractBean;

@Getter @Setter
public abstract class AbstractCompanyConsultPage extends AbstractConsultPage<Company> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CompanyDetails> mainDetails;
	private FormOneData<ContactDetails> contactDetails;
	
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		
		mainDetails = createDetailsForm(CompanyDetails.class, identifiable, new DetailsAdapter<CompanyDetails>(CompanyDetails.class){
			private static final long serialVersionUID = 1L;
			
			@Override
			public CompanyDetails createData(Company identifiable) {
				return new CompanyDetails(identifiable);
			}
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
			@Override
			public String getTitleId() {
				return businessEntityInfos.getUserInterface().getLabelId();
			}
			@Override
			public String getTabId() {
				return DefaultPersonEditFormModel.TAB_PERSON_ID;
			}
		});
		
		contactDetails = createDetailsForm(ContactDetails.class, identifiable.getContactCollection(), new ContactDetailsAdapter.Default());		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		UICommandable contextualMenu = instanciateCommandableBuilder().setLabel(contentTitle).create();
		contextualMenu.setLabel(contentTitle); 
		
		UICommandable commandable = Builder.createUpdate(identifiable);
		
		if(StringUtils.isEmpty(selectedTabId))
			;
		else
			commandable.addParameter(webManager.getRequestParameterTabId(), selectedTabId);
		contextualMenu.getChildren().add(commandable);
		
		return Arrays.asList(contextualMenu);
	}
					
	public static class DetailsAdapter<DETAILS extends AbstractOutputDetails<Company>> extends DetailsConfigurationListener.Form.Adapter<Company,DETAILS>{

		private static final long serialVersionUID = -9101575271431241099L;

		public DetailsAdapter(Class<DETAILS> detailsClass) {
			super(Company.class, detailsClass);
		}
		
	}
	
	public static class Adapter extends ConsultPageListener.Adapter.Default<Company>{
		private static final long serialVersionUID = -5657492205127185872L;
		
		public Adapter() {
			super(Company.class);
		}
		
		@Override
		public void initialisationEnded(AbstractBean bean) {
			super.initialisationEnded(bean);
			if(bean instanceof AbstractCompanyConsultPage){
				
				ControlSetAdapter<CompanyDetails> mainDetailsControlSetAdapter = getControlSetAdapter(CompanyDetails.class);
				if(mainDetailsControlSetAdapter!=null)
					((AbstractCompanyConsultPage)bean).getMainDetails().getControlSetListeners().add(mainDetailsControlSetAdapter);
				
				ControlSetAdapter<ContactDetails> contactDetailsControlSetAdapter = getControlSetAdapter(ContactDetails.class);
				if(contactDetailsControlSetAdapter!=null)
					((AbstractCompanyConsultPage)bean).getContactDetails().getControlSetListeners().add(contactDetailsControlSetAdapter);
				
			}
		}
	}
}
