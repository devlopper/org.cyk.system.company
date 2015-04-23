package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.AbstractDashboardPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class StockDashBoardPage extends AbstractDashboardPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private ProductBusiness productBusiness;
	
	private Table<TangibleProductDetails> tangibleProductTable;
	/* 
	private List<TangibleProduct> tangibleProducts;
	private List<SalableTangibleProduct> salableTangibleProducts;
	private List<TangibleProductStockMovement> tangibleProductStockMovements;
	*/
	@SuppressWarnings("unchecked")
	@Override
	protected void initialisation() {
		super.initialisation();
		/*tangibleProducts = new ArrayList<>(productBusiness.findAll(TangibleProduct.class));
		salableTangibleProducts = new ArrayList<>();
		tangibleProductStockMovements = new ArrayList<>();*/
		
		tangibleProductTable = (Table<TangibleProductDetails>) createTable(TangibleProductDetails.class, null, null);
		configureDetailsTable(tangibleProductTable, "model.entity.product");
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<TangibleProduct> tangibleProducts = productBusiness.findAll(TangibleProduct.class);
		for(TangibleProduct tangibleProduct : tangibleProducts)
			tangibleProductTable.addRow(new TangibleProductDetails(tangibleProduct));
		tangibleProductTable.setShowFooter(Boolean.FALSE);
	}
	
	/**/
	
	@Getter @Setter
	private class TangibleProductDetails implements Serializable {
		private static final long serialVersionUID = -6341285110719947720L;
		
		@Input @InputText
		private String code,name,stockQuantity,useQuantity,usedQuantity;
		
		public TangibleProductDetails(TangibleProduct tangibleProduct) {
			this.code = tangibleProduct.getCode();
			this.name = tangibleProduct.getName();
			this.stockQuantity = tangibleProduct.getStockQuantity().toString();
			this.useQuantity = tangibleProduct.getUseQuantity().toString();
			this.usedQuantity = tangibleProduct.getUsedQuantity().toString();
		}
	}
	
	
}