package org.cyk.system.company.business.impl.iesa;

import java.io.Serializable;
import java.util.Locale;

import javax.inject.Singleton;

import lombok.Getter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningFakedDataProducer;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.utility.common.generator.AbstractGeneratable;

@Singleton @Getter
public class IesaFakedDataProducer extends AbstractEnterpriseResourcePlanningFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure(Listener listener) {
		AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
    	PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/salecashregistermovementlogo.png";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
		super.structure(listener);
		
	
	}
	
}
