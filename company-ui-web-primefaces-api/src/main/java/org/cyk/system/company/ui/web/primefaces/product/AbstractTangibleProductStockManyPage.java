package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.cyk.system.company.model.product.Product;
import org.cyk.ui.api.Icon;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractTangibleProductStockManyPage<DETAIL> extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	
	protected List<DETAIL> selectedList = new ArrayList<>();
	protected UICommandable submitCommandable;
	protected List<Product> tangibleProducts;
	protected Product selectedProduct;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		//tangibleProducts = new ArrayList<Product>(tangibleProductBusiness.findAll());
		submitCommandable = Builder.create("command.save", Icon.ACTION_SAVE);
		submitCommandable.getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 4174035599491586029L;
			@Override
			public void serve(UICommand command, Object parameter) {
				__serve__(selectedList);
			}
			@Override
			public Object succeed(UICommand command, Object parameter) {
				navigationManager.redirectTo(__succeedOutcome__());
				return super.succeed(command, parameter);
			}
		});	
	}
	
	protected abstract DETAIL detail(Product tangibleProduct);
	protected abstract Product tangibleProduct(DETAIL detail);
	protected abstract void __serve__(List<DETAIL> details);
	public abstract BigDecimal maxValue(DETAIL detail);
	public abstract BigDecimal minValue(DETAIL detail);
	protected abstract String __succeedOutcome__();
	
	public void add(){
		if(selectedProduct==null)
			return;
		selectedList.add(detail(selectedProduct));
		tangibleProducts.remove(selectedProduct);
		selectedProduct = null;
	}
	
	public void remove(DETAIL selected){
		selectedList.remove(selected);
		tangibleProducts.add(tangibleProduct(selected));
	}
	
	@Override
	public Boolean getShowContentMenu() {
		return Boolean.TRUE;
	}
}