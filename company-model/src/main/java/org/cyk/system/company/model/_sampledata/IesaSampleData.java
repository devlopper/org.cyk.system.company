package org.cyk.system.company.model._sampledata;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;

import org.cyk.system.company.model.sale.SaleCashRegisterMovementReportFile;
import org.cyk.system.company.model.sale.SaleReportTemplateFile;
import org.cyk.system.company.model.structure.EmployeeReportTemplateFile;
import org.cyk.system.root.model.file.report.AbstractReportTemplateFile;
import org.cyk.utility.common.generator.AbstractGeneratable;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.cyk.utility.common.generator.RandomDataProvider.RandomFile;

public class IesaSampleData extends AbstractSampleData implements Serializable {

	private static final long serialVersionUID = 1L;

	static {
		AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
	}
	
	public static Collection<EmployeeReportTemplateFile> getEmployeeReports(){
		Collection<EmployeeReportTemplateFile> collection = new IesaSampleData().__getEmployeeReports__();
		
		return collection;
	}
	
	public static Collection<SaleReportTemplateFile> getSaleReportTemplateFiles(){
		Collection<SaleReportTemplateFile> collection = new IesaSampleData().__getSaleReportTemplateFiles__();
		//SaleReportTemplateFile report = collection.iterator().next();
		
		return collection;
	}
	
	protected void process(AbstractReportTemplateFile<?> report){
		RandomFile randomFile = RandomDataProvider.getInstance().getFile("/META-INF/generator/image/document/header/iesa/receipt.png");
		report.setHeaderImage(new ByteArrayInputStream(randomFile.getBytes()));
		
	}
	
	public static Collection<SaleCashRegisterMovementReportFile> getSaleCashRegisterMovementReportTemplateFiles(){
		Collection<SaleCashRegisterMovementReportFile> collection = new IesaSampleData().__getSaleCashRegisterMovementReportTemplateFiles__();
		//SaleCashRegisterMovementReportTemplateFile report = collection.iterator().next();
		return collection;
	}
	
	@Override
	protected void addLabelValues(AbstractReportTemplateFile<?> reportTemplateFile, String name, String[][] values) {
		if("Invoice".equals(name)){
			values = new String[][]{
				{"Date",""}
				,{"7/5/2016",""}
				,{"Invoice No.",""}
				,{"F0524318",""}
				,{(reportTemplateFile instanceof SaleReportTemplateFile ? "Commercial":"Cashier")+" Name",""}
				,{"Yves Sea",""}
				,{"Parent",""}
				,{"John Max",""}
				,{reportTemplateFile instanceof SaleReportTemplateFile ? null:"Received From",""}
				,{"Robert Varga",""}
			};
		}
		super.addLabelValues(reportTemplateFile, name, values);
	}
	
	public static void main(String[] args) {
		System.out.println("IesaSampleData.main() 1 : "+IesaSampleData.getSaleReportTemplateFiles());
		System.out.println("IesaSampleData.main() 2 : "+IesaSampleData.getSaleCashRegisterMovementReportTemplateFiles().iterator().next()
				.getCreationDate());
		SaleReportTemplateFile saleReportTemplateFile = IesaSampleData.getSaleReportTemplateFiles().iterator().next();
		System.out.println("Header : "+ ((ByteArrayInputStream)saleReportTemplateFile.getHeaderImage()).available());
		RandomFile randomFile = RandomDataProvider.getInstance().getFile("/META-INF/generator/image/document/header/1.PNG");
		System.out.println(randomFile.getExtension());
	}
	
}
