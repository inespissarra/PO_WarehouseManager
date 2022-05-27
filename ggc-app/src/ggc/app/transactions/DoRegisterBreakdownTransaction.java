package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.NoSuchProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.app.exceptions.UnavailableProductException;
import ggc.exceptions.NotEnoughStockException;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      String partnerKey = stringField("partnerKey");
      String productKey = stringField("productKey");
      int amount = integerField("amount");
      _receiver.registerBreakdown(partnerKey, productKey, amount);
    } catch(NoSuchPartnerException e){
      throw new UnknownPartnerKeyException(e.getKey());
    } catch(NoSuchProductException e){
      throw new UnknownProductKeyException(e.getKey());
    } catch(NotEnoughStockException e){
      throw new UnavailableProductException(e.getKey(), e.getAmount(), e.getAvailableAmount());
    }
  }

}
