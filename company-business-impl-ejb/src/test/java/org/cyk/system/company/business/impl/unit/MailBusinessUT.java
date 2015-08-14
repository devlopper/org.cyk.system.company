package org.cyk.system.company.business.impl.unit;

import java.util.Collection;
import java.util.Date;

import org.cyk.system.root.business.api.message.MessageSendingBusiness;
import org.cyk.system.root.business.impl.message.MailBusinessImpl;
import org.cyk.system.root.model.event.Notification;
import org.cyk.utility.test.unit.AbstractUnitTest;
import org.junit.Test;
import org.mockito.InjectMocks;

public class MailBusinessUT extends AbstractUnitTest {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private MailBusinessImpl mailBusiness;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(mailBusiness);
		mailBusiness.setConnectionProperties("smtp.gmail.com", "kycdev@gmail.com", "kycdev@gmail.com", "p@ssw0rd*");
	}
	
	@Test
	public void sendToOneBlocking() {
		MessageSendingBusiness.SendOptions.BLOCKING=Boolean.TRUE;
		Notification notification = new Notification();
		notification.setDate(new Date());
		notification.setTitle("TestTitle");
		notification.setMessage("TestMessage");
		mailBusiness.send(notification, "kycdev@gmail.com");
	}

}
