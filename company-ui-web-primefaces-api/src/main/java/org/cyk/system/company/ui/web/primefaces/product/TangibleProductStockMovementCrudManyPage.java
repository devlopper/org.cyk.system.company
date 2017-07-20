package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;

@Named @ViewScoped @Getter @Setter
public class TangibleProductStockMovementCrudManyPage extends AbstractTangibleProductStockManyPage<StockTangibleProductMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private StockTangibleProductMovementBusiness tangibleProductStockMovementBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("model.entity.tangibleProductStockMovement");
	}
	
	@Override
	protected StockTangibleProductMovement detail(TangibleProduct tangibleProduct) {
		return null;//new TangibleProductStockMovement(tangibleProduct, null, BigDecimal.ZERO, null);
	}

	@Override
	protected TangibleProduct tangibleProduct(StockTangibleProductMovement detail) {
		return null;//detail.getTangibleProduct();
	}

	@Override
	protected void __serve__(List<StockTangibleProductMovement> details) {
		tangibleProductStockMovementBusiness.create(details);
	}
	
	
	
	public BigDecimal maxValue(StockTangibleProductMovement tangibleProductStockMovement){
		return null;
	}
	
	public BigDecimal minValue(StockTangibleProductMovement tangibleProductStockMovement){
		return null;//tangibleProductStockMovement.getTangibleProduct().getStockQuantity().negate();
	}

	@Override
	protected String __succeedOutcome__() {
		return CompanyWebManager.getInstance().getOutcomeTangibleProductStockMovementList();
	}
}