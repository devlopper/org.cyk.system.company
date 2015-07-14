package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.ui.web.primefaces.model.SaleStockOutputFormModel;
import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputConsultPage.Details;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleStockOutputCrudOnePage extends AbstractCrudOnePage<SaleStockOutput> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleStockOutputBusiness saleStockOutputBusiness;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	private SaleStockInput saleStockInput;
	
	private FormOneData<Details> details;
	
	@Override
	protected void initialisation() {
		BusinessEntityInfos b = uiManager.businessEntityInfos(SaleStockInput.class);
		saleStockInput = (SaleStockInput) uiManager.getGenericBusiness().use(SaleStockInput.class).find(Long.parseLong(requestParameter(b.getIdentifier())));
		super.initialisation();
		cashRegisterController.init(identifiable.getSaleCashRegisterMovement(),Boolean.TRUE);
		
		details = (FormOneData<Details>) createFormOneData(new Details(identifiable.getSaleStockInput()), Crud.READ);
		configureDetailsForm(details);
	}
	
	@Override
	protected void create() {
		cashRegisterController.amountInChanged();
		super.create();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		return (T) saleStockOutputBusiness.newInstance((Person) userSession.getUser(),saleStockInput);
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.CREATE;
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(SaleStockOutput.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return SaleStockOutputFormModel.class;
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		//String url = null;
		//url = "http://localhost:8080/company/private/__tools__/report.jsf?clazz=Sale&identifiable=151&fileExtensionParam=pdf&ridp=pos&windowmode=windowmodedialog";
		//url = navigationManager.reportUrl(identifiable.getSale(), companyBusinessLayer.getReportPointOfSale(),uiManager.getPdfParameter(),Boolean.TRUE);
		//messageDialogOkButtonOnClick += "window.open('"+url+"', 'pointofsale"+identifiable.getSale().getIdentifier()+"', 'location=no,menubar=no,titlebar=no,toolbar=no,width=400, height=550');";
		//System.out.println(messageDialogOkButtonOnClick);
		return null;
	}
	
	/**/
	
}