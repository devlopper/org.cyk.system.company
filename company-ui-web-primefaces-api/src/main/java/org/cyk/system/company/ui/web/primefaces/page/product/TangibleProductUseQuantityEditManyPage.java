package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;

@Named @ViewScoped @Getter @Setter
public class TangibleProductUseQuantityEditManyPage extends AbstractTangibleProductStockManyPage<TangibleProductUseQuantityMovement> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("command.quantityinuse");
		submitCommandable.getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = -3384769965766868883L;
			@Override
			public Object fail(UICommand command, Object parameter,Throwable throwable) {
				for(TangibleProductUseQuantityMovement d : selectedList)
					d.reset();
				return super.fail(command, parameter, throwable);
			}
		});
	}
	
	@Override
	protected TangibleProductUseQuantityMovement detail(TangibleProduct tangibleProduct) {
		return new TangibleProductUseQuantityMovement(tangibleProduct);
	}

	@Override
	protected TangibleProduct tangibleProduct(TangibleProductUseQuantityMovement tangibleProductStockMovement) {
		return tangibleProductStockMovement.getTangibleProduct();
	}

	@Override
	protected void __serve__(List<TangibleProductUseQuantityMovement> details) {
		Collection<TangibleProduct> collection = new ArrayList<TangibleProduct>();
		for(TangibleProductUseQuantityMovement d : selectedList){
			collection.add(d.getTangibleProduct());
			d.apply();
		}
		tangibleProductBusiness.update(collection);
	}
	
	
	
}