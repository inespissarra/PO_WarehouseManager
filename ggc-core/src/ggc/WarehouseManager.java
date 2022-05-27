package ggc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ggc.exceptions.MissingFileAssociationException;
import ggc.exceptions.UnavailableFileException;
import ggc.exceptions.ImportFileException;
import ggc.exceptions.BadEntryException;
import ggc.exceptions.NoSuchProductException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.exceptions.PartnerAlreadyExistsException;
import ggc.exceptions.DateOutOfBoundsException;
import ggc.exceptions.NotEnoughStockException;
import ggc.exceptions.NoSuchTransactionException;

/** Façade for access. */
public class WarehouseManager {
 
  /* * Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  private boolean _save = false;

  // getters and setters (filename)
  public void setFilename(String filename) {
    _filename = filename;
  }

  public String getFilename() {
    return _filename;
  }

  // getters and setters (warehouse)
  public void setWarehouse(Warehouse warehouse) {
    _warehouse = warehouse;
  }

  public Warehouse getWarehouse() {
    return _warehouse;
  }

  // getters and setters (save)
  public void setSave(boolean b) {
    _save = b;
  }

  public boolean getSave() {
    return _save;
  }

  /* -------------------------- Partner -------------------------- */

  public void registerPartner(String partnerKey, String name, String address) 
      throws PartnerAlreadyExistsException {
    getWarehouse().registerPartner(partnerKey, name, address);
    _save = true;
  }

  public void removePartner(String partnerKey) throws NoSuchPartnerException{
    getWarehouse().removePartner(partnerKey);
    _save = true;
  }

  public String displayAllPartners() {
    return getWarehouse().displayAllPartners();
  }

  public String displayPartner(String partnerKey) 
      throws NoSuchPartnerException {
    return getWarehouse().displayPartner(partnerKey);
  }

  public void toggleProductNotifications(String partnerKey, String productKey)
      throws NoSuchPartnerException, NoSuchProductException {
    getWarehouse().toggleProductNotifications(partnerKey, productKey);
  }

  public String showPartnerAcquisitions(String partnerKey)
      throws NoSuchPartnerException {
    return getWarehouse().showPartnerAcquisitions(partnerKey);
  }

  public String showPartnerSales(String partnerKey)
      throws NoSuchPartnerException {
    return getWarehouse().showPartnerSales(partnerKey);
  }

  public String lookupPaymentsByPartner(String partnerKey)
      throws NoSuchPartnerException {
    return getWarehouse().lookupPaymentsByPartner(partnerKey);
  }

  /* -------------------------- Products ------------------------- */

  public void registerSimpleProduct(String productKey){
    getWarehouse().registerSimpleProduct(productKey);
  }

  public void registerDerivedProduct(String productKey, double aggravation, String recipe){
    getWarehouse().registerDerivedProduct(productKey, aggravation, recipe);
  }

  public String displayAllProducts() {
    return getWarehouse().displayAllProducts();
  }

  /* -------------------------- Batches -------------------------- */

  public void registerBatch(String productKey, 
      String partnerKey, double price, int amount)
      throws NoSuchProductException, NoSuchPartnerException{
    getWarehouse().registerBatch(productKey, partnerKey, 
      price, amount);
  }

  public String displayAvailableBatches() {
    return getWarehouse().displayAvailableBatches();
  }

  public String displayBatchesByProduct(String productKey) 
      throws NoSuchProductException {
    return getWarehouse().displayBatchesByProduct(productKey);
  }

  public String displayBatchesByPartner(String partnerKey) 
      throws NoSuchPartnerException {
    return getWarehouse().displayBatchesByPartner(partnerKey);
  }

  public String lookupProductBatchesUnderGivenPrice(double price){
    return getWarehouse().lookupProductBatchesUnderGivenPrice(price);
  }

  /* ----------------------- Transactions ------------------------ */

  public void receivePayment(int transactionKey) throws NoSuchTransactionException{
    getWarehouse().receivePayment(transactionKey);
  }

  public String showTransaction(int transactionKey)
      throws NoSuchTransactionException {
    return getWarehouse().showTransaction(transactionKey);
  }

  public void registerAcquisition(String partnerKey, String productKey, 
      double price, int amount) throws NoSuchPartnerException, NoSuchProductException{
    getWarehouse().registerAcquisition(partnerKey, productKey, price, amount);
  }

  public void registerSale(String partnerKey, int paymentDeadline,
      String productKey, int amount) throws NoSuchPartnerException, 
      NoSuchProductException, NotEnoughStockException {
    getWarehouse().registerSale(partnerKey, paymentDeadline, productKey, amount);
  }

  public void registerBreakdown(String partnerKey, String productKey, int amount)
      throws NoSuchPartnerException, NoSuchProductException, NotEnoughStockException{
    getWarehouse().registerBreakdown(partnerKey, productKey, amount);
  }

  /* -------------------------- Balance -------------------------- */
  public int showAccountingBalance() {
    return getWarehouse().showAccountingBalance();
  }

  public int showAvailableBalance() {
    return getWarehouse().showAvailableBalance();
  }

  /* ---------------------------- Date --------------------------- */
  
  public void advanceDate(int days) throws DateOutOfBoundsException {
    getWarehouse().advanceDate(days);
    _save = true;
  }

  public int getDate() {
    return getWarehouse().getDate();
  }

  /* ----------------------- Save and Load ----------------------- */

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, 
      MissingFileAssociationException {
    if(getSave()){
      if(!getFilename().equals("")){
        ObjectOutputStream oos = new ObjectOutputStream(
          new BufferedOutputStream(new FileOutputStream(getFilename())));
        oos.writeObject(getWarehouse());
        oos.close();
        _save = false;
      } else{
        throw new MissingFileAssociationException();
      }
    }
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) 
      throws MissingFileAssociationException, FileNotFoundException, IOException {
    setFilename(filename);
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    try {
      ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(filename)));
      setWarehouse((Warehouse) ois.readObject());
      ois.close();
      setFilename(filename);
    } catch (IOException | ClassNotFoundException e) {
      throw new UnavailableFileException(filename);
    }
  }

  /* ------------------------ Import File ------------------------ */

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      getWarehouse().importFile(textfile);
      _save = true;
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(textfile);
    }
  }

}