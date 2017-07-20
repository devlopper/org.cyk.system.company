package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemSaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemSaleCashRegisterMovementDetails;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.ui.api.IdentifierProvider;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.FileExtension;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementConsultPage extends AbstractConsultPage<SaleCashRegisterMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private Table<SalableProductCollectionItemSaleCashRegisterMovementDetails> salableProductCollectionItemSaleCashRegisterMovementTable;	
	
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		salableProductCollectionItemSaleCashRegisterMovementTable = createDetailsTable(SalableProductCollectionItemSaleCashRegisterMovementDetails.class,new DetailsConfigurationListener.Table.Adapter<SalableProductCollectionItemSaleCashRegisterMovement, SalableProductCollectionItemSaleCashRegisterMovementDetails>(SalableProductCollectionItemSaleCashRegisterMovement.class, SalableProductCollectionItemSaleCashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SalableProductCollectionItemSaleCashRegisterMovement> getIdentifiables() {
				return inject(SalableProductCollectionItemSaleCashRegisterMovementBusiness.class).findBySaleCashRegisterMovement(identifiable);
			}
			
			@Override
			public String getTabId() {
				return IdentifierProvider.Adapter.getTabOf(SaleCashRegisterMovement.class);
			}
			
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		commandable.addChild(Builder.create("command.see.receipt", null,WebNavigationManager.getInstance()
				.getUrlToFileConsultManyPage(CompanyConstant.Code.ReportTemplate.PAYMENT_RECEIPT,identifiable, FileExtension.PDF)));
	}
			
}