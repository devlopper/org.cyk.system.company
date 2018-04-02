package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.StockDashBoardReportTableDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractDashboardPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class StockDashBoardPage extends AbstractDashboardPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	//@Inject private TangibleProductBusiness tangibleProductBusiness;
	
	private Table<StockDashBoardReportTableDetails> tangibleProductTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("company.report.dashboard.stock.title");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<StockDashBoardReportTableDetails> details = new ArrayList<>();
		//for(TangibleProduct tangibleProduct : tangibleProductBusiness.findAll())
		//	details.add(new StockDashBoardReportTableDetails(tangibleProduct));
		//tangibleProductTable = createDetailsTable(StockDashBoardReportTableDetails.class, details, "model.entity.tangibleProduct");	
		tangibleProductTable.setTitle(null);
		tangibleProductTable.setShowHeader(Boolean.FALSE);
		tangibleProductTable.setShowFooter(Boolean.FALSE);
		tangibleProductTable.setShowToolBar(Boolean.TRUE);
		tangibleProductTable.setReportIdentifier(CompanyReportRepository.getInstance().getReportStockDashboard());
		tangibleProductTable.setIdentifiableClass(StockTangibleProductMovement.class);
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().stockContextCommandables(getUserSession());
	}
	
}