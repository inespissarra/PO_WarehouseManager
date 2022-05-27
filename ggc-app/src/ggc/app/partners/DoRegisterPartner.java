package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.DuplicatePartnerKeyException;
import ggc.exceptions.PartnerAlreadyExistsException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);

    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("partnerName", Prompt.partnerName());
    addStringField("partnerAddress", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    try {
      String partnerKey = stringField("partnerKey");
      String partnerName = stringField("partnerName");
      String address = stringField("partnerAddress");
      _receiver.registerPartner(partnerKey, partnerName, address);
    } catch (PartnerAlreadyExistsException e) {
      throw new DuplicatePartnerKeyException(e.getKey());
    }
  }

}
