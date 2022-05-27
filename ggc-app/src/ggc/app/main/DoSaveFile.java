package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

import java.io.IOException;
import java.io.FileNotFoundException;

import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.save();
    } catch (MissingFileAssociationException e) {
      String fileName = Form.requestString(Prompt.newSaveAs());
      try{ 
        _receiver.saveAs(fileName);
      } catch(MissingFileAssociationException e2){
        e2.printStackTrace();
      } catch (IOException e2) {
        e2.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}