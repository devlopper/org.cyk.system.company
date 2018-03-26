package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.store.Store;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudInheritanceStrategy;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity 
@Inheritance(strategy=InheritanceType.JOINED)
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,crudInheritanceStrategy=CrudInheritanceStrategy.CHILDREN_ONLY) @Accessors(chain=true)
public class Product extends AbstractEnumeration implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @JoinColumn(name=COLUMN_CATEGORY) protected ProductCategory category;
	@ManyToOne @JoinColumn(name=COLUMN_TYPE) protected ProductType type;
	@Column(precision=10,scale=FLOAT_SCALE) private BigDecimal price;
	
	@Transient private Store store;
	@Transient private Party providerParty;
	@Transient private BigDecimal stockQuantityMovementCollectionInitialValue;
	
	@Transient protected OwnedCompany ownedCompany;
	
	public Product setStoreFromCode(String code){
		store = getFromCode(Store.class, code);
		return this;
	}
	
	public Product setProviderPartyFromCode(String code){
		providerParty = getFromCode(Party.class, code);
		return this;
	}
	
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_PROVIDER_PARTY = "providerParty";
	public static final String FIELD_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "stockQuantityMovementCollectionInitialValue";
	public static final String FIELD_STORE = "store";
	public static final String FIELD_OWNED_COMPANY = "ownedCompany";
	
	public static final String COLUMN_CATEGORY = FIELD_CATEGORY;
	public static final String COLUMN_TYPE = FIELD_TYPE;
	
}
