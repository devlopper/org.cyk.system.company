package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;

@Getter @Setter
public class SaleCashRegisterMovementReport extends AbstractReportTemplateFile<SaleCashRegisterMovementReport> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleReport sale;
	private String identifier,date,amountDue,amountIn,amountToOut,amountOut,balance;

	private LabelValueCollectionReport paymentInfos = new LabelValueCollectionReport();
	
	@Override
	public void generate() {
		identifier=RandomStringUtils.randomNumeric(8);
		date=new Date().toString();
		amountDue=provider.randomInt(1, 1000000)+"";
		amountIn=provider.randomInt(1, 1000000)+"";
		amountOut=provider.randomInt(1, 1000000)+"";
		amountToOut=provider.randomInt(1, 1000000)+"";
		balance=provider.randomInt(1, 1000000)+"";
		
		sale = new SaleReport();
		sale.generate();
	}
	
}
