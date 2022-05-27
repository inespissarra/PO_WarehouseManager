package ggc.app.transactions;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.exceptions.NoSuchProductException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    String productKey = stringField("productKey");
    double price = realField("price");
    int amount = integerField("amount");
    try{
      _receiver.registerAcquisition(partnerKey, productKey, price, amount);
    } catch (NoSuchPartnerException e){
      throw new UnknownPartnerKeyException(e.getKey());
    } catch (NoSuchProductException e){
      Form request = new Form();
      if(request.confirm(Prompt.addRecipe())){
        int nComponents = request.requestInteger(Prompt.numberOfComponents());
        double aggravation = request.requestReal(Prompt.alpha());
        String recipe = "";
        for (int i = 0; i < nComponents; i++) {
          if(i!=0)
            recipe += "#";
          recipe += request.requestString(Prompt.productKey()) + ":" + 
            request.requestInteger(Prompt.amount());
        }
        _receiver.registerDerivedProduct(productKey, aggravation , recipe);
      }
      else{
        _receiver.registerSimpleProduct(productKey);
      }
      try{
        _receiver.registerAcquisition(partnerKey, productKey, price, amount);
      } catch (NoSuchPartnerException e2){
        throw new UnknownPartnerKeyException(e2.getKey());
      } catch (NoSuchProductException e2){
        throw new UnknownProductKeyException(e2.getKey());
      }
    }
  }

}
