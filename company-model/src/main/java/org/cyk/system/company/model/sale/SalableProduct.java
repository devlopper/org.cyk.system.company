package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.InstanceHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class SalableProduct extends AbstractCollection<SalableProductInstance> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT) @NotNull @Accessors(chain=true) private Product product;
	
	/**
	 * The unit price of the product. null means the price will be determine at runtime
	 */
	@Column(precision=10,scale=FLOAT_SCALE) @Accessors(chain=true) private BigDecimal price;
	
	@Column(precision=3,scale=FLOAT_SCALE) @Accessors(chain=true) private BigDecimal quantityMultiple;
	
	@Column(precision=PERCENT_PRECISION,scale=PERCENT_SCALE) private BigDecimal valueAddedTaxRate;
	
	@Transient @Accessors(chain=true) private Class<? extends Product> productClass;
	
	public SalableProduct(Product product, BigDecimal price) {
		super(product.getCode(),product.getName(),product.getAbbreviation(),product.getDescription());
		this.product = product;
		this.price = price;
	}
	
	@Override
	public SalableProduct setCode(String code) {
		return (SalableProduct) super.setCode(code);
	}
	
	public SalableProduct setProductFromCode(String code){
		product = InstanceHelper.getInstance().getByIdentifier(Product.class, code, ClassHelper.Listener.IdentifierType.BUSINESS);
		return this;
	}
	
	@Override
	public SalableProduct setCascadeOperationToMaster(Boolean cascadeOperationToMaster) {
		return (SalableProduct) super.setCascadeOperationToMaster(cascadeOperationToMaster);
	}
	
	@Override
	public SalableProduct setCascadeOperationToMasterFieldNames(Collection<String> cascadeOperationToMasterFieldNames) {
		return (SalableProduct) super.setCascadeOperationToMasterFieldNames(cascadeOperationToMasterFieldNames);
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_PRICE = "price";
	
	public static final String COLUMN_PRODUCT = FIELD_PRODUCT;
}
