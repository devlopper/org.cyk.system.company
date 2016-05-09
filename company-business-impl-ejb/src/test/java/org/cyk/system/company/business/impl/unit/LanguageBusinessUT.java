package org.cyk.system.company.business.impl.unit;

import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.utility.test.unit.AbstractUnitTest;
import org.junit.Test;
import org.mockito.InjectMocks;

public class LanguageBusinessUT extends AbstractUnitTest {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private LanguageBusinessImpl languageBusiness;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(languageBusiness);
		languageBusiness.registerResourceBundle("org.cyk.system.company.business.impl.resources.message", CompanyBusinessLayer.class.getClassLoader());
	}

	@Test
    public void customFieldLabelText() {
		
		//assertEquals("Numero March.",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(SaleStockReportTableRow.class, "saleStockInputExternalIdentifier", Boolean.TRUE)));
	    /*assertEquals("Utilisateur",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(MyClass.class, "user", Boolean.TRUE))); 
	    assertEquals("Quantite de Utilisateur",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(MyClass.class, "userQuantity", Boolean.TRUE))); 
	    assertEquals("Prix de Utilisateur",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(MyClass.class, "userPrice", Boolean.TRUE))); 
	    assertEquals("Prix unitaire de Utilisateur",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(MyClass.class, "userUnitPrice", Boolean.TRUE))); 
	    assertEquals("Couleur",languageBusiness.findFieldLabelText(FieldUtils.getDeclaredField(MyClass.class, "color", Boolean.TRUE)));*/
	    
    }
	
}
