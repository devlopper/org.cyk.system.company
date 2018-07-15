package org.cyk.system.company.ui.web.primefaces;
import java.io.Serializable;

import org.cyk.system.root.model.mathematics.movement.MovementCollectionInventory;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransfer;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransferAcknowledgement;
import org.cyk.system.root.model.mathematics.movement.MovementGroup;
import org.cyk.utility.common.userinterface.command.Menu;

public class MenuBuilder extends org.cyk.ui.web.primefaces.MenuBuilder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void addMain(Menu menu, Object principal) {
		super.addMain(menu, principal);
		menu.addNode("inventory").addNodeActionListMany(MovementCollectionInventory.class);
		menu.addNode("buying").addNodeActionListMany(MovementGroup.class);
		menu.addNode("transfert").addNodeActionListMany(MovementCollectionValuesTransfer.class
				,MovementCollectionValuesTransferAcknowledgement.class);
	}
	
}