package org.cyk.system.company.model.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;

@Getter @Setter @NoArgsConstructor
public class SaleCashRegisterMovementReportFile extends AbstractReportTemplateFile<SaleCashRegisterMovementReportFile> implements Serializable {

	private static final long serialVersionUID = 7332510774063666925L;

	private SaleCashRegisterMovementReport saleCashRegisterMovement = new SaleCashRegisterMovementReport();

	public SaleCashRegisterMovementReportFile(SaleCashRegisterMovement saleCashRegisterMovement) {
		super();
		setSource(saleCashRegisterMovement);
	}
	
	@Override
	public void setSource(Object source) {
		super.setSource(source);
		SaleReport sale = new SaleReport(((SaleCashRegisterMovement)source).getSale());
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovement.setSource(source);
	}
	
	@Override
	public void generate() {
		super.generate();
		SaleReport sale = new SaleReport();
		sale.generate();
		saleCashRegisterMovement.setSale(sale);
		saleCashRegisterMovement.generate();
		
		SaleCashRegisterMovementReport previousSaleCashRegisterMovement = new SaleCashRegisterMovementReport();
		previousSaleCashRegisterMovement.setSale(saleCashRegisterMovement.getSale());
		previousSaleCashRegisterMovement.generate();
		saleCashRegisterMovement.setPrevious(previousSaleCashRegisterMovement);
		
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
