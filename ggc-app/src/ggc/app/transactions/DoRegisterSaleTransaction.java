package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.NoSuchProductException;
import ggc.app.exceptions.UnavailableProductException;
import ggc.exceptions.NotEnoughStockException;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addIntegerField("paymentDeadline", Prompt.paymentDeadline());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      String partnerKey = stringField("partnerKey");
      int paymentDeadline = integerField("paymentDeadline");
      String productKey = stringField("productKey");
      int amount = integerField("amount");
      _receiver.registerSale(partnerKey, paymentDeadline, productKey, amount);
    } catch (NoSuchPartnerException e){
      throw new UnknownPartnerKeyException(e.getKey());
    } catch (NoSuchProductException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (NotEnoughStockException e){
      throw new UnavailableProductException(e.getKey(), e.getAmount(), e.getAvailableAmount());
    }
  }
}