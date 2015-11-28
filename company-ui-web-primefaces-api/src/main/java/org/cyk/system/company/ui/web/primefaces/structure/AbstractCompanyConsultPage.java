package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.model.file.File;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.model.geography.ContactDetails;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.ConsultPageListener;
import org.cyk.ui.web.primefaces.page.ContactDetailsAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.FileExtensionGroup;
import org.cyk.utility.common.annotation.user.interfaces.FileExtensions;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputFile;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.cdi.AbstractBean;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractCompanyConsultPage extends AbstractConsultPage<Company> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private FormOneData<MainDetails> mainDetails;
	private FormOneData<ContactDetails> contactDetails;
	
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		
		mainDetails = createDetailsForm(MainDetails.class, identifiable, new CompanyDetailsFormOneDataConfigurationAdapter<MainDetails>(MainDetails.class){
			private static final long serialVersionUID = 1L;
			
			@Override
			public MainDetails createData(Company identifiable) {
				return new MainDetails(identifiable);
			}
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
			@Override
			public String getTitleId() {
				return businessEntityInfos.getUiLabelId();
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
		
	@Getter @Setter
	public static class MainDetails extends AbstractOutputDetails<Company> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		@Input @InputFile (extensions=@FileExtensions(groups=FileExtensionGroup.IMAGE)) private File logo;
		@Input @InputText private String name,manager,signer;
		public MainDetails(Company company) {
			super(company);
			logo = company.getImage();
			name = company.getName();
			if(company.getManager()!=null)
				manager = company.getManager().getNames();
			if(company.getSigner()!=null)
				signer = company.getSigner().getNames();
		}
		
		public static final String FIELD_LOGO = "logo";
		public static final String FIELD_NAME = "name";
		public static final String FIELD_MANAGER = "manager";
		public static final String FIELD_SIGNER = "signer";
		
	}
				
	public static class CompanyDetailsFormOneDataConfigurationAdapter<DETAILS extends AbstractOutputDetails<Company>> extends DetailsConfigurationListener.Form.Adapter<Company,DETAILS>{

		private static final long serialVersionUID = -9101575271431241099L;

		public CompanyDetailsFormOneDataConfigurationAdapter(Class<DETAILS> detailsClass) {
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
				
				ControlSetAdapter<MainDetails> mainDetailsControlSetAdapter = getControlSetAdapter(MainDetails.class);
				if(mainDetailsControlSetAdapter!=null)
					((AbstractCompanyConsultPage)bean).getMainDetails().getControlSetListeners().add(mainDetailsControlSetAdapter);
				
				ControlSetAdapter<ContactDetails> contactDetailsControlSetAdapter = getControlSetAdapter(ContactDetails.class);
				if(contactDetailsControlSetAdapter!=null)
					((AbstractCompanyConsultPage)bean).getContactDetails().getControlSetListeners().add(contactDetailsControlSetAdapter);
				
			}
		}
		
		public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass){
			return null;
		}
	}
}
