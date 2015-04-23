package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputManyPickList;

@Getter @Setter @NoArgsConstructor @Entity 
public class IntangibleProductDetails extends AbstractIdentifiable implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;

	private IntangibleProduct intangibleProduct;
	
	@OneToMany 
	@Input @InputChoice @InputManyChoice @InputManyPickList
	private Collection<TangibleProduct> tangibleProductsUsed = new ArrayList<>();
	
	public IntangibleProductDetails(IntangibleProduct intangibleProduct,TangibleProduct...tangibleProductsUsed) {
		this.intangibleProduct = intangibleProduct;
		if(tangibleProductsUsed!=null)
			this.tangibleProductsUsed.addAll(Arrays.asList(tangibleProductsUsed));
	}
	
}
