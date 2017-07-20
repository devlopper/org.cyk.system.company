package org.cyk.system.company.persistence.impl.accounting;

import java.io.Serializable;

import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;

public class AccountingPeriodProductDaoImpl extends AbstractAccountingPeriodResultsDaoImpl<AccountingPeriodProduct,Product> implements AccountingPeriodProductDao, Serializable {

	private static final long serialVersionUID = 7904009035909460023L;

}
