package org.cyk.system.company.business.impl.iesa;

import java.io.Serializable;
import java.util.Locale;

import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningFakedDataProducer;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.utility.common.generator.AbstractGeneratable;

import lombok.Getter;

@Singleton @Getter
public class IesaFakedDataProducer extends AbstractEnterpriseResourcePlanningFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure() {
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
		super.structure();
		create(inject(TangibleProductBusiness.class).instanciateMany(new String[][]{{"TP01","Books Package Primary"},{"TP02", "Polo shirt Primary"}
		,{"TP03", "Sportswear Primary"},{"TP04","ID Card"},{"TP05","School Uniform (Up and Down) Primary"},{"TP06","Culottes Primary"}}));
		create(inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{{"IP01","Re-registration"},{"IP02", "Tuition fees"},{"IP03", "Exam (STA)"}
			,{"IP04","UCMAS Program"},{"IP05","Swimming (First, Second & Third Terms)"},{"IP06","Art and Craft (First, Second & Third Terms)"}
			,{"IP07","Transportation (till June 2017)"}}));
		create(inject(SalableProductBusiness.class).instanciateMany(new String[][]{{"","","","","","","","","","","TP01","60000"}
			,{"","","","","","","","","","","TP02", "7000"},{"","","","","","","","","","","TP03", "7000"},{"","","","","","","","","","","TP04", "4000"}
			,{"","","","","","","","","","","TP05", "14000"},{"","","","","","","","","","","TP06", "7000"},{"","","","","","","","","","","IP01", "65000"}
			,{"","","","","","","","","","","IP02", "1450000"},{"","","","","","","","","","","IP03", "45000"},{"","","","","","","","","","","IP04", "40000"}
			,{"","","","","","","","","","","IP05", "30000"},{"","","","","","","","","","","IP06", "30000"},{"","","","","","","","","","","IP07", "30000"}}));
		
		
	}

	@Override
	protected void doBusiness(Listener listener) {
		
	}

	
}
