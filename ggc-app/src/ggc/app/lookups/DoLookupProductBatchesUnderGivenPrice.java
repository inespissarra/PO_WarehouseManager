package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addRealField("priceLimit", Prompt.priceLimit());
  }

  @Override
  public void execute() throws CommandException {
    double priceLimit = realField("priceLimit");
    _display.popup(_receiver.lookupProductBatchesUnderGivenPrice(priceLimit));
  }

}
