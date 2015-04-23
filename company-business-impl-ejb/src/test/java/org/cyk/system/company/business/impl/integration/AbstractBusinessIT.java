package org.cyk.system.company.business.impl.integration;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.io.IOUtils;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.validation.AbstractValidator;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.report.AbstractReport;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.utility.test.AbstractIntegrationTestJpaBased;
import org.cyk.utility.test.ArchiveBuilder;
import org.jboss.shrinkwrap.api.Archive;

public abstract class AbstractBusinessIT extends AbstractIntegrationTestJpaBased {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Inject protected ApplicationBusiness applicationBusiness;
	@Inject protected ExceptionUtils exceptionUtils;
	@Inject protected DefaultValidator defaultValidator;
	@Inject private GenericDaoImpl g;
	@Inject protected GenericBusiness genericBusiness;
	@Inject protected ProductDao productDao;

	@Inject protected ValidatorMap validatorMap;// = ValidatorMap.getInstance();
    
	//protected Installation installation;
	
	protected void fakeInstallation(){
    	applicationBusiness.install(RootBusinessLayer.fakeInstallation());
    }
	
    @Override
    public EntityManager getEntityManager() {
        return g.getEntityManager();
    }
	
    @Override
    protected void _execute_() {
        super._execute_();
        create();    
        read(); 
        update();    
        delete();    
        finds();
        businesses();
    }
    
    @Override
    protected void populate() {
    	
    }
    
	protected abstract void finds();
	
	protected abstract void businesses();
	
	/* Shortcut */
    
    protected AbstractIdentifiable create(AbstractIdentifiable object){
        return genericBusiness.create(object);
    }
    
    protected AbstractIdentifiable update(AbstractIdentifiable object){
        return genericBusiness.update(object);
    }
    
    protected void validate(Object object){
        if(object==null)
            return;
        @SuppressWarnings("unchecked")
        AbstractValidator<Object> validator = (AbstractValidator<Object>) validatorMap.validatorOf(object.getClass());
        if(validator==null){
            //log.warning("No validator has been found. The default one will be used");
            //validator = defaultValidator;
            return;
        }
        try {
            validator.validate(object);
        } catch (Exception e) {}
        
        if(!Boolean.TRUE.equals(validator.isSuccess()))
            System.out.println(validator.getMessagesAsString());
        
    }
    
    protected void jasperViewer(final byte[] bytes){
    	Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				JasperViewer jasperViewer;
				try {
					jasperViewer = new JasperViewer(new ByteArrayInputStream(bytes),Boolean.TRUE);
					
					jasperViewer.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	
    	thread.run();
    }
    
    protected void writeReport(AbstractReport<?> report){
    	try {
			IOUtils.write(report.getBytes(), new FileOutputStream( System.getProperty("user.dir")+"/target/"+report.getFileName()+"."+report.getFileExtension()));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static Archive<?> createRootDeployment() {
        return  
                new ArchiveBuilder().create().getArchive().
                    addClasses(BusinessIntegrationTestHelper.classes()).
                    addPackages(Boolean.FALSE, BusinessIntegrationTestHelper.packages()).
                    addPackages(Boolean.TRUE,"org.cyk.system.company") 
                ;
    } 
}
