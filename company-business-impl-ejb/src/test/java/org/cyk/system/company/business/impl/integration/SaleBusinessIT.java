package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.FileRepresentationType;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
   
    @Override
    protected void businesses() {
    	SaleBusiness saleBusiness = inject(SaleBusiness.class);
    	Sale sale = saleBusiness.instanciateOne();
    	create(sale); 
    	File file = saleBusiness.createFile(sale, FileRepresentationType.POINT_OF_SALE);
    	inject(FileBusiness.class).writeTo(file, new java.io.File(System.getProperty("user.dir")+"/target"), "mypointofsale");
    }
    
    /* Exceptions */
    

}
