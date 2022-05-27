package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownTransactionKeyException;
import ggc.exceptions.NoSuchTransactionException;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("transactionKey", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      int transactionKey = integerField("transactionKey");
      _display.popup(_receiver.showTransaction(transactionKey));
    } catch(NoSuchTransactionException e){
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }
}