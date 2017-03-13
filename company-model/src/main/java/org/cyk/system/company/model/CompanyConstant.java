package org.cyk.system.company.model;

import java.io.Serializable;

public interface CompanyConstant {

	public static class Code implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static class File implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String COMPANYLOGO = "COMPANYLOGO";
			public static String DOCUMENT_HEADER = "DOCUMENTHEADER";
			public static String DOCUMENT_FOOTER = "DOCUMENTFOOTER";
			public static String DOCUMENT_BACKGROUND = "DOCUMENTBACKGROUND";
			public static String DOCUMENT_BACKGROUND_DRAFT = "DOCUMENTBACKGROUNDDRAFT";
		}
		
		public static class ReportTemplate implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String PRO_FORMA_INVOICE = "PROFORMAINVOICE";
			public static String INVOICE = "INVOICE";
			public static String INVOICE_AND_PAYMENT_RECEIPT = "INVOICEANDPAYMENTRECEIPT";
			public static String PAYMENT_RECEIPT = "PAYMENTRECEIPT";
			public static String SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4 = "SALECASHREGISTERMOVEMENTCOLLECTION_A4";
			
			/*
			public static String TEMPLATE_SALE_INVOICE_A4 = "SALE_INVOICE_A4";
			public static String TEMPLATE_SALE_CASH_REGISTER_MOVEMENT_PAYMENT_RECEIPT_A4 = "SALE_CASH_REGISTER_MOVEMENT_PAYMENT_RECEIPT_A4";
			public static String TEMPLATE_SALE_INVOICE_AND_SALE_CASH_REGISTER_MOVEMENT_PAYMENT_RECEIPT_A4 = "SALE_INVOICE_AND_SALE_CASH_REGISTER_MOVEMENT_PAYMENT_RECEIPT_A4";
			*/
			public static String EMPLOYEE_EMPLOYMENT_CONTRACT = "EMPLOYEEEMPLOYMENTCONTRACT";
			public static String EMPLOYEE_WORK_CERTIFICATE = "EMPLOYEEWORKCERTIFICATE";
			public static String EMPLOYEE_EMPLOYMENT_CERTIFICATE = "EMPLOYEEEMPLOYMENTCERTIFICATE";
		}
	
		public static class BusinessServiceCollection implements Serializable {
			private static final long serialVersionUID = 1L;
		
			public static String PRODUCT = "PRODUCT";
			public static String PAYMENT = "PAYMENT";
			public static String COMPANY = "COMPANY";
			public static String SALE = "SALE";
			public static String STOCK = "STOCK";
			public static String PRODUCTION = "PRODUCTION";
			public static String ACCOUNTING = "ACCOUNTING";
			
		}
		
		public static class Product implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String STOCKING = "STOCKING";
		}
		
		public static class IntangibleProduct implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String STOCKING = "STOCKING";
		}
		
		public static class FiniteStateMachineAlphabet implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String SEND = "SEND";
			public static String RECEIVE = "RECEIVE";
			public static String SELL = "SELL";
			public static String USE = "USE";
		}
		
		public static class FiniteStateMachineState implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String ASSIGNED = "ASSIGNED";
			public static String SENT = "SENT";
			public static String RECEIVED = "RECEIVED";
			public static String SOLD = "SOLD";
			public static String USED = "USED";
		}
		
		public static class CashRegisterMovementMode implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static String CASH = "CASH";
			public static String CHEQUE = "CHEQUE";
			public static String GIFT_CARD = "GIFTCARD";
		}
	}
		
	public static class Configuration {
		
		public static class Sale implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static Boolean AUTOMATICALLY_GENERATE_REPORT_FILE = Boolean.FALSE;
			
		}
		
		public static class SaleCashRegisterMovementCollection implements Serializable {
			private static final long serialVersionUID = 1L;
			
			public static Boolean AUTOMATICALLY_GENERATE_REPORT_FILE = Boolean.FALSE;
			
		}
		
		
		
	}
	
	
	/* Gift card management */
	
	String GIFT_CARD_WORKFLOW = "GIFT_CARD_WORKFLOW";
	
	String GIFT_CARD_WORKFLOW_STATE_ASSIGNED = "GIFT_CARD_WORKFLOW_STATE_ASSIGNED";
	
	String GIFT_CARD_WORKFLOW_ALPHABET_SEND = "GIFT_CARD_WORKFLOW_ALPHABET_SEND";
	String GIFT_CARD_WORKFLOW_STATE_SENT = "GIFT_CARD_WORKFLOW_STATE_SENT";
	
	String GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE = "GIFT_CARD_WORKFLOW_ALPHABET_RECEIVE";
	String GIFT_CARD_WORKFLOW_STATE_RECEIVED = "GIFT_CARD_WORKFLOW_STATE_RECEIVED";
	
	String GIFT_CARD_WORKFLOW_ALPHABET_SELL = "GIFT_CARD_WORKFLOW_ALPHABET_SELL";
	String GIFT_CARD_WORKFLOW_STATE_SOLD = "GIFT_CARD_WORKFLOW_STATE_SOLD";
	
	String GIFT_CARD_WORKFLOW_ALPHABET_USE = "GIFT_CARD_WORKFLOW_ALPHABET_USE";
	String GIFT_CARD_WORKFLOW_STATE_USED = "GIFT_CARD_WORKFLOW_STATE_USED";
	
}
