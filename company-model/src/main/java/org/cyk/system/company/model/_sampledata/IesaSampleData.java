package org.cyk.system.company.model._sampledata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.sale.SaleReportTemplateFile;
import org.apache.commons.io.IOUtils;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementReportTemplateFile;
import org.cyk.system.company.model.structure.EmployeeReportTemplateFile;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.cyk.utility.common.generator.RandomDataProvider.RandomFile;

public class IesaSampleData extends AbstractSampleData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Collection<EmployeeReportTemplateFile> getEmployeeReports(){
		Collection<EmployeeReportTemplateFile> collection = new IesaSampleData().__getEmployeeReports__();
		
		return collection;
	}
	
	public static Collection<SaleReportTemplateFile> getSaleReportTemplateFiles(){
		Collection<SaleReportTemplateFile> collection = new IesaSampleData().__getSaleReportTemplateFiles__();
		SaleReportTemplateFile report = collection.iterator().next();
		
		return collection;
	}
	
	protected void process(AbstractReportTemplateFile<?> report){
		RandomFile randomFile = RandomDataProvider.getInstance().getFile("/META-INF/generator/image/document/header/iesa/receipt.png");
		report.setHeaderImage(new ByteArrayInputStream(randomFile.getBytes()));
		
	}
	
	public static Collection<SaleCashRegisterMovementReportTemplateFile> getSaleCashRegisterMovementReportTemplateFiles(){
		Collection<SaleCashRegisterMovementReportTemplateFile> collection = new IesaSampleData().__getSaleCashRegisterMovementReportTemplateFiles__();
		//SaleCashRegisterMovementReportTemplateFile report = collection.iterator().next();
		return collection;
	}
	
	@Override
	protected void addLabelValues(AbstractReportTemplateFile<?> reportTemplateFile, String name, String[][] values) {
		if("Invoice".equals(name)){
			values = new String[][]{
				{"Date",""}
				,{"7/5/2016",""}
				,{"Receipt No.",""}
				,{"R0524318",""}
				,{"Cashier Name",""}
				,{"Yves Sea",""}
				,{"Parent",""}
				,{"John Max",""}
				,{"Received from",""}
				,{"Armel shon",""}
				
			};
		}
		super.addLabelValues(reportTemplateFile, name, values);
	}
	
	public static void main(String[] args) {
		System.out.println("IesaSampleData.main() : "+IesaSampleData.getSaleCashRegisterMovementReportTemplateFiles());
		SaleReportTemplateFile saleReportTemplateFile = IesaSampleData.getSaleReportTemplateFiles().iterator().next();
		System.out.println("Header : "+ ((ByteArrayInputStream)saleReportTemplateFile.getHeaderImage()).available());
		System.out.println(saleReportTemplateFile.getSale().getSalableProductCollection().getItems().iterator().next().getCurrentSalableProductCollectionItemBalance());
		RandomFile randomFile = RandomDataProvider.getInstance().getFile("/META-INF/generator/image/document/header/1.PNG");
		System.out.println(randomFile.getExtension());
	}
	
}
