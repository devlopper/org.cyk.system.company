package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.FileExtensionGroup;
import org.cyk.utility.common.annotation.user.interfaces.FileExtensions;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputFile;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractCompanyDetails<IDENTIFIABLE extends AbstractIdentifiable> extends AbstractOutputDetails<IDENTIFIABLE> implements Serializable {
	
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputFile (extensions=@FileExtensions(groups=FileExtensionGroup.IMAGE)) protected File logo;
	@Input @InputText protected String name/*,manager*/,signer;
	
	public AbstractCompanyDetails(IDENTIFIABLE identifiable) {
		super(identifiable);
		Company company = getCompany();
		logo = company.getImage();
		name = company.getName();
		
		//if(company.getManager()!=null)
		//	manager = company.getManager().getNames();
		
		//if(company.getSigner()!=null)
		//	signer = company.getSigner().getNames();
	}
	
	protected abstract Company getCompany();
	
	public static final String FIELD_LOGO = "logo";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_MANAGER = "manager";
	public static final String FIELD_SIGNER = "signer";
	
}