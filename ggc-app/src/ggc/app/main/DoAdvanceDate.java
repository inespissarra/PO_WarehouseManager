package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.app.exceptions.InvalidDateException;
import ggc.exceptions.DateOutOfBoundsException;

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("days", Prompt.daysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      int days = integerField("days");
      _receiver.advanceDate(days);
    } catch (DateOutOfBoundsException e) {
      throw new InvalidDateException(e.getDays());
    }
  }
}