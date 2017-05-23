package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.file.FileRepresentationTypeBusiness;
import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.cyk.system.root.model.file.File;
import org.cyk.ui.api.IdentifierProvider;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractCollectionConsultPage;
import org.cyk.utility.common.builder.javascript.OpenWindowStringBuilder;

@Named @ViewScoped @Getter @Setter
public class SaleCashRegisterMovementCollectionConsultPage extends AbstractCollectionConsultPage.Extends<SaleCashRegisterMovementCollection,SaleCashRegisterMovement,SaleCashRegisterMovementDetails> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		itemTable.setTitle(inject(LanguageBusiness.class).findClassLabelText(SaleCashRegisterMovement.class));
		itemTable.setShowToolBar(Boolean.FALSE);
		itemTable.setShowActionsColumn(Boolean.FALSE);
	}
	
	@Override
	protected Boolean getEnableItemTableInDefaultTab() {
		return Boolean.TRUE;
	}
	
	@Override
	protected String getItemTableTabId() {
		return IdentifierProvider.Adapter.getTabOf(SaleCashRegisterMovementCollection.class);
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		
		Commandable seeCommandable = (Commandable) Builder.create("command.see.receipt", null,"");
		
		Collection<File> files = inject(FileBusiness.class).findByRepresentationTypesByIdentifiables(Arrays.asList(inject(FileRepresentationTypeBusiness.class)
				.find(CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4)),Arrays.asList(identifiable));
		
		OpenWindowStringBuilder openWindowStringBuilder = inject(OpenWindowStringBuilder.class).setName("receipt"+identifiable.getIdentifier());
		openWindowStringBuilder.getUrlStringBuilder().addFiles(files);
		seeCommandable.setOnClick(openWindowStringBuilder.build());
		
		commandable.addChild(seeCommandable);
	}

			
}