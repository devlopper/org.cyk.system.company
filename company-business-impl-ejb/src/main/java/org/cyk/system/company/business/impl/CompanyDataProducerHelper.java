package org.cyk.system.company.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.persistence.api.file.FileDao;
import org.cyk.utility.common.cdi.AbstractBean;

@Singleton @Deprecated
public class CompanyDataProducerHelper extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 8420754068573843133L;

	protected CompanyBusinessLayer getCompanyBusinessLayer(){
		return CompanyBusinessLayer.getInstance();
	}
	
	@Deprecated
	public ReportTemplate createReportTemplate(String code,String name,Boolean male,String templateRelativeFileName){
		return inject(RootDataProducerHelper.class).createReportTemplate(code, name, male, templateRelativeFileName, inject(FileDao.class).read(CompanyConstant.Code.File.DOCUMENT_HEADER)
				, inject(FileDao.class).read(CompanyConstant.Code.File.DOCUMENT_BACKGROUND), inject(FileDao.class).read(CompanyConstant.Code.File.DOCUMENT_BACKGROUND_DRAFT));
	}
	
}
