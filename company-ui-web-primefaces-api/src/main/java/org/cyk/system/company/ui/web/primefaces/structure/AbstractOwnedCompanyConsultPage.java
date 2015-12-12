package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.company.ui.web.primefaces.structure.AbstractCompanyConsultPage.DetailsAdapter;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.model.geography.ContactDetails;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.ContactDetailsAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractOwnedCompanyConsultPage extends AbstractConsultPage<OwnedCompany> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<CompanyDetails> companyDetails;
	private FormOneData<ContactDetails> contactDetails;
	
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		
		companyDetails = createDetailsForm(CompanyDetails.class, identifiable.getCompany(), new DetailsAdapter<CompanyDetails>(CompanyDetails.class){
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
		
		contactDetails = createDetailsForm(ContactDetails.class, identifiable.getCompany().getContactCollection(), new ContactDetailsAdapter.Default());		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		UICommandable contextualMenu = UIProvider.getInstance().createCommandable("button", null),commandable=null;
		contextualMenu.setLabel(contentTitle); 
		
		commandable = navigationManager.createUpdateCommandable(identifiable, "command.edit", null);
		
		if(StringUtils.isEmpty(selectedTabId))
			;
		else
			commandable.addParameter(webManager.getRequestParameterTabId(), selectedTabId);
		contextualMenu.getChildren().add(commandable);
		
		return Arrays.asList(contextualMenu);
	}
	
	
}
