package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class TangibleProductInventoryCrudOnePage extends AbstractCrudOnePage<TangibleProductInventory> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Inject private ProductBusiness productBusiness;
	
	private List<TangibleProductInventoryDetail> details = new ArrayList<>();

	@Override
	protected void initialisation() {
		super.initialisation();
		if(Crud.CREATE.equals(crud)){
			for(TangibleProduct tangibleProduct : productBusiness.findByClass(TangibleProduct.class))
				details.add(new TangibleProductInventoryDetail(tangibleProduct, BigDecimal.ZERO, null));
			identifiable.setDetails(details);
		}else {
			details.addAll(identifiable.getDetails());
		}
	}
	
}