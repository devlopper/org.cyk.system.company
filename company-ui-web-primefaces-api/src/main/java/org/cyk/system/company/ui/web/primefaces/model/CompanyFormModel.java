package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Company;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.FileExtensionGroup;
import org.cyk.utility.common.annotation.user.interfaces.FileExtensions;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputFile;

@Getter @Setter
public class CompanyFormModel extends org.cyk.ui.api.model.AbstractPartyFormModel<Company> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputFile(extensions=@FileExtensions(groups=FileExtensionGroup.IMAGE))
	private File image;
	
}
