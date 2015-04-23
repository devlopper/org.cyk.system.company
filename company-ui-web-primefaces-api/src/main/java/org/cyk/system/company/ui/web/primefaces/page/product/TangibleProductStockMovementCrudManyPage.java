package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;

@Named @ViewScoped @Getter @Setter
public class TangibleProductStockMovementCrudManyPage extends AbstractTangibleProductStockManyPage<TangibleProductStockMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("model.entity.tangibleProductStockMovement");
	}
	
	@Override
	protected TangibleProductStockMovement detail(TangibleProduct tangibleProduct) {
		return new TangibleProductStockMovement(tangibleProduct, null, BigDecimal.ZERO, null);
	}

	@Override
	protected TangibleProduct tangibleProduct(TangibleProductStockMovement detail) {
		return detail.getTangibleProduct();
	}

	@Override
	protected void __serve__(List<TangibleProductStockMovement> details) {
		tangibleProductStockMovementBusiness.create(details);
	}
}