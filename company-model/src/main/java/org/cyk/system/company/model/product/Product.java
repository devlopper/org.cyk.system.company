package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.model.information.Tangibility;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.Store;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.helper.ArrayHelper;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.InstanceHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity 
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE) @Accessors(chain=true)
public class Product extends AbstractEnumeration implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @JoinColumn(name=COLUMN_TANGIBILITY) private Tangibility tangibility;
	@ManyToOne @JoinColumn(name=COLUMN_GROUP) private ProductGroup group;
	@ManyToOne @JoinColumn(name=COLUMN_CATEGORY) private ProductCategory category;
	@ManyToOne @JoinColumn(name=COLUMN_TYPE) private ProductType type;
	@Column(precision=10,scale=FLOAT_SCALE) @Text(value="buying.price") private BigDecimal price;
	
	/* Derived */
	private Boolean salable;
	private Boolean stockable;
	private Boolean storable;
	private Boolean providerable;
	
	@Transient private List<Store> stores;
	@Transient private Party providerParty;
	@Transient private MovementCollection stockQuantityMovementCollection;
	
	@Transient @Text(value="selling.price") private BigDecimal salableProductPropertiesPrice;
	@Transient @Text(value="stock") private BigDecimal stockQuantityMovementCollectionInitialValue;
	
	public Product addStoreFromCode(String code){
		addStores(getFromCode(Store.class, code));
		return this;
	}
	
	public Product setProviderPartyFromCode(String code){
		providerParty = getFromCode(Party.class, code);
		return this;
	}
	
	public Product setStockQuantityMovementCollectionInitialValueFromObject(Object value){
		stockQuantityMovementCollectionInitialValue = getNumberFromObject(BigDecimal.class, value);
		return this;
	}
	
	public Product setSalableProductPropertiesPriceFromObject(Object value){
		salableProductPropertiesPrice = getNumberFromObject(BigDecimal.class, value);
		return this;
	}
	
	public Product addStores(Collection<Store> stores){
		this.stores = (List<Store>) CollectionHelper.getInstance().add(this.stores, Boolean.TRUE, stores);
		return this;
	}
	
	public Product addStores(Store...stores){
		this.stores = (List<Store>) CollectionHelper.getInstance().add(this.stores, Boolean.TRUE, stores);
		return this;
	}
	
	public Product addStoresAll(){
		addStores(InstanceHelper.getInstance().get(Store.class));
		return this;
	}
	
	public Product addStoresFromCode(Collection<String> codes){
		if(CollectionHelper.getInstance().isNotEmpty(codes))
			for(String index : codes)
				addStores(getFromCode(Store.class, index));
		return this;
	}
	
	public Product addStoresFromCode(String...codes){
		if(ArrayHelper.getInstance().isNotEmpty(codes))
			return addStoresFromCode(Arrays.asList(codes));
		return this;
	}
	
	public static final String FIELD_GROUP = "group";
	public static final String FIELD_TANGIBILITY = "tangibility";
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_PROVIDER_PARTY = "providerParty";
	public static final String FIELD_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "stockQuantityMovementCollectionInitialValue";
	public static final String FIELD_STORE = "store";
	public static final String FIELD_OWNED_COMPANY = "ownedCompany";
	public static final String FIELD_SALABLE = "salable";
	public static final String FIELD_STOCKABLE = "stockable";
	public static final String FIELD_STORABLE = "storable";
	public static final String FIELD_PROVIDERABLE = "providerable";
	public static final String FIELD_SALABLE_PRODUCT_PROPERTIES_PRICE = "salableProductPropertiesPrice";
	public static final String FIELD_STORES = "stores";
	
	
	public static final String COLUMN_GROUP = COLUMN_NAME_UNKEYWORD+FIELD_GROUP;
	public static final String COLUMN_TANGIBILITY = FIELD_TANGIBILITY;
	public static final String COLUMN_CATEGORY = FIELD_CATEGORY;
	public static final String COLUMN_TYPE = FIELD_TYPE;
	
}
