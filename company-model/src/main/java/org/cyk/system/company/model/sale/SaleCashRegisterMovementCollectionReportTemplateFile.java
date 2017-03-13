package org.cyk.system.company.model.sale;

import java.io.Serializable;

import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementCollectionReportTemplateFile extends AbstractReportTemplateFile<SaleCashRegisterMovementCollectionReportTemplateFile> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleCashRegisterMovementCollectionReport saleCashRegisterMovementCollection = new SaleCashRegisterMovementCollectionReport();

	public SaleCashRegisterMovementCollectionReportTemplateFile(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
		super();
		setSource(saleCashRegisterMovementCollection);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		//saleCashRegisterMovementCollection.setAccountingPeriod(new AccountingPeriodReport(((SaleCashRegisterMovementCollection)source).getAccountingPeriod()));
		saleCashRegisterMovementCollection.setSource(source);
	}
	
	@Override
	public void generate() {
		super.generate();
		saleCashRegisterMovementCollection.generate();
		/*
		SaleReport sale = new SaleReport();
		sale.generate();
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovement.generate();
		
		SaleCashRegisterMovementReport previousSaleCashRegisterMovement = new SaleCashRegisterMovementReport();
		previousSaleCashRegisterMovement.setSale(saleCashRegisterMovement.getSale());
		previousSaleCashRegisterMovement.generate();
		saleCashRegisterMovement.setPrevious(previousSaleCashRegisterMovement);
		*/
		/*
		for(SalableProductCollectionItemReport salableProductCollectionItem :  sale.getSalableProductCollection().getItems()){
			SalableProductCollectionItemSaleCashRegisterMovementReport salableProductCollectionItemSaleCashRegisterMovement
				= new SalableProductCollectionItemSaleCashRegisterMovementReport();
			salableProductCollectionItemSaleCashRegisterMovement.setSalableProductCollectionItem(salableProductCollectionItem);
			salableProductCollectionItemSaleCashRegisterMovement.setSaleCashRegisterMovement(saleCashRegisterMovement);
			salableProductCollectionItemSaleCashRegisterMovement.generate();
			
			saleCashRegisterMovement.getSalableProductCollectionItemSaleCashRegisterMovements().add(salableProductCollectionItemSaleCashRegisterMovement);
		}*/
	}



	
	
}
