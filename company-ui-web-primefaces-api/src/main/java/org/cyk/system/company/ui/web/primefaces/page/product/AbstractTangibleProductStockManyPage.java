package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.IconType;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Getter @Setter
public abstract class AbstractTangibleProductStockManyPage<DETAIL> extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected TangibleProductBusiness tangibleProductBusiness;
	
	protected List<DETAIL> selectedList = new ArrayList<>();
	protected UICommandable submitCommandable;
	protected List<TangibleProduct> tangibleProducts;
	protected TangibleProduct selectedTangibleProduct;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		tangibleProducts = new ArrayList<TangibleProduct>(tangibleProductBusiness.findAll());
		submitCommandable = UIProvider.getInstance().createCommandable("command.save", IconType.ACTION_SAVE);
		submitCommandable.getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 4174035599491586029L;
			@Override
			public void serve(UICommand command, Object parameter) {
				__serve__(selectedList);
			}
			@Override
			public Object succeed(UICommand command, Object parameter) {
				navigationManager.redirectTo(CompanyWebManager.getInstance().getOutcomeStockDashBoard());
				return super.succeed(command, parameter);
			}
		});	
	}
	
	protected abstract DETAIL detail(TangibleProduct tangibleProduct);
	protected abstract TangibleProduct tangibleProduct(DETAIL detail);
	protected abstract void __serve__(List<DETAIL> details);
	
	public void add(){
		if(selectedTangibleProduct==null)
			return;
		selectedList.add(detail(selectedTangibleProduct));
		tangibleProducts.remove(selectedTangibleProduct);
		selectedTangibleProduct = null;
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