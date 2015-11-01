package org.cyk.system.company.business.impl.aibsbakery;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

import lombok.Getter;

@Singleton @Getter
public class AibsBakeryFakedDataProducer extends AbstractFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;

	private CompanyBusinessLayer companyBusinessLayer = CompanyBusinessLayer.getInstance();
	
	private void structure(){
		
	}
	
	@Override
	public void produce(FakedDataProducerListener listener) {
		this.listener =listener;
		rootDataProducerHelper.setBasePackage(CompanyBusinessLayer.class.getPackage());
		
    	structure();
    	
    	
	}
		
}
