package ggc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import ggc.exceptions.BadEntryException;
import ggc.exceptions.NoSuchProductException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.exceptions.PartnerAlreadyExistsException;
import ggc.exceptions.DateOutOfBoundsException;
import ggc.exceptions.NotEnoughStockException;
import ggc.exceptions.NoSuchTransactionException;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Warehouse accounting balance */
  private double _accountingBalance = 0.0;

  /** Warehouse available balance */
  private double _availableBalance = 0.0;

  /** Transactions counter */
  private int _transactionsNumber = 0;

  /** Warehouse date */
  private int _date = 0;

  /** Warehouse partners */
  private Map<String, Partner> _partners = 
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  /** Warehouse products */
  private Map<String, Product> _products = 
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  /** Warehouse batches */
  private Set<Batch> _batches = new TreeSet<Batch>();

  private Map<Integer, Transaction> _transactions = new TreeMap<>();

  /**
   * @return accounting Balance of the Warehouse
   */
  public double getAccountingBalance() {
    return _accountingBalance;
  }

  public void setAccountingBalance(double value){
    _accountingBalance = value;
  }

  public int showAccountingBalance(){
    return (int) Math.round(getAccountingBalance());
  }

  /**
   * @return available balance of the Warehouse
   */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  public void setAvailableBalance(double value){
    _availableBalance = value;
  }

  public int showAvailableBalance(){
    return (int) Math.round(getAvailableBalance());
  }

  /**
   * @return current number of transactions
   */
  public int getTransactionsNumber(){
    return _transactionsNumber;
  }

  /**
   * @return current date
   */
  public int getDate() {
    return _date;
  }

  /**
   * Advance date counter
   * 
   * @param days number of days to advance
   * @throws DateOutOfBoundsException
   */
  public void advanceDate(int days) throws DateOutOfBoundsException {
    if (days > 0){
      _date += days;
      update();
    }
    else
      throw new DateOutOfBoundsException(days);
  }


  public void update(){
    for(Map.Entry<String, Partner> partner : _partners.entrySet()){
      Map<Integer, Sale> partnerSales  = partner.getValue().getSales();
      for(Map.Entry<Integer, Sale> sale : partnerSales.entrySet()){
        if(sale.getValue().getPaymentDay()==-1){
          double oldPrice = sale.getValue().getPaymentValue();
          double price = calculatePrice(partner.getValue(), sale.getValue().getBaseValue(), 
            sale.getValue().getPaymentDeadline(), 
            _products.get(sale.getValue().getProductKey()).getPeriodDays());
          sale.getValue().setPaymentValue(price);
          setAccountingBalance(getAccountingBalance() + price - oldPrice);
        }
      }
    }
  }

  /* -------------------------- Partner -------------------------- */
  /**
   * @param partnerKey partner ID
   * @return true if partner ID exists, false if it does not exist
   */
  public boolean containsPartnerKey(String partnerKey){
    return _partners.containsKey(partnerKey);
  }

  /**
   * @param partnerKey partner ID
   * @return the partner
   * @throws NoSuchPartnerException
   */
  public Partner getPartner(String partnerKey) 
      throws NoSuchPartnerException {
    if (!containsPartnerKey(partnerKey))
      throw new NoSuchPartnerException(partnerKey);
    return _partners.get(partnerKey);
  }

  /**
   * Create and register partner. 
   * If partner ID already exists, an exception is thrown.
   * 
   * @param partnerKey partner ID
   * @param name       name of the partner
   * @param address    address of th partner
   * @throws PartnerAlreadyExistsException
   */
  public void registerPartner(String partnerKey, String name, String address)
      throws PartnerAlreadyExistsException {
    if (containsPartnerKey(partnerKey)) {
      throw new PartnerAlreadyExistsException(partnerKey);
    }

    Partner partner = new Partner(partnerKey, name, address);

    _partners.put(partner.getKey(), partner);
    
    turnOnPartnerNotificationsForAllProducts(partner);
  }

  /**
   * Remove a partner. 
   * If partner ID does not exists, an exception is thrown.
   * 
   * @param partnerKey ID of the partner to be removed
   * @throws NoSuchPartnerException
   */
  public void removePartner(String partnerKey) 
      throws NoSuchPartnerException {
    getPartner(partnerKey);

    _partners.remove(partnerKey);
  }

  /**
   * Get partner informations given ID. 
   * If partner ID does not exist, an exception is thrown.
   * 
   * @param partnerKey partner ID
   * @return partner's toString
   * @throws NoSuchPartnerException
   */
  public String displayPartner(String partnerKey) 
      throws NoSuchPartnerException {
    Partner partner = getPartner(partnerKey);

    String res = ""+partner;      
  
    List<Notification> notifications = partner.getNotifications();
    for(Notification notification : notifications) {
      res += "\n" + notification;
    }

    partner.wipeNotifications();

    return res;
  }

  /**
   * Display all partners.
   * 
   * @return string containing all partners.
   */
  public String displayAllPartners() {
    String allPartners = "";

    for (Map.Entry<String, Partner> partner : _partners.entrySet()) {
      allPartners += partner.getValue() + "\n";
    }

    return allPartners.strip();
  }

  public void toggleProductNotifications(String partnerKey, String productKey)
      throws NoSuchPartnerException, NoSuchProductException {
    Partner partner = getPartner(partnerKey);
    Product product = getProduct(productKey);
    product.toggleNotifications(partner);
  }

  public void turnOnNotificationsForAllPartners(Product product){
    for(Map.Entry<String, Partner> partner : _partners.entrySet()){
      product.registerObserver(partner.getValue());
    }
  }

  public void turnOnPartnerNotificationsForAllProducts(Partner partner){
    for(Map.Entry<String, Product> product : _products.entrySet()){
      product.getValue().registerObserver(partner);
    }
  }

  public String showPartnerAcquisitions(String partnerKey)
      throws NoSuchPartnerException {
    String acquisitions = "";
    Map<Integer, Transaction> partnerAcquisitions = getPartner(partnerKey).getAcquisitions();

    for (Map.Entry<Integer, Transaction> acquisition : partnerAcquisitions.entrySet()) {
      acquisitions += acquisition.getValue() + "\n";
    }
    return acquisitions.strip();
  }
 

  public String showPartnerSales(String partnerKey)
      throws NoSuchPartnerException {
    String sales = "";
    
    Map<Integer, Transaction> salesAndBreakdowns = getPartner(partnerKey).getSalesAndBreakdowns();
    
    for (Map.Entry<Integer, Transaction> res : salesAndBreakdowns.entrySet()) {
      sales += res.getValue() + "\n";

    }

    return sales.strip();
  }

  public String lookupPaymentsByPartner(String partnerKey)
      throws NoSuchPartnerException {
    Partner partner = getPartner(partnerKey);
    Map<Integer, Transaction> partnerSales = partner.getSalesAndBreakdowns();

    String payments = "";
      for(Map.Entry<Integer, Transaction> sale : partnerSales.entrySet()){
        if(sale.getValue().getPaymentDay()!=-1)
          payments += sale.getValue() + "\n";
      }
    return payments.strip();
  }

  /* -------------------------- Products ------------------------- */
  /**
   * @param productKey product ID
   * @return true if product ID exists, false if it does not exist
   */
  public boolean containsProductKey(String productKey){
    return _products.containsKey(productKey);
  }

  /**
   * @param productKey product ID
   * @return the product
   * @throws NoSuchProductException
   */
  public Product getProduct(String productKey) 
      throws NoSuchProductException {
    if (!containsProductKey(productKey))
      throw new NoSuchProductException(productKey);
    return _products.get(productKey);
  }

  public void registerSimpleProduct(String productKey){
    Product product = new Product(productKey);
    addProduct(product);
    turnOnNotificationsForAllPartners(product);
  }

  public void registerDerivedProduct(String productKey, double aggravation, String stringRecipe){
    Recipe recipe = new Recipe();
    recipe.addIngredients(stringRecipe);
    Product product = new DerivedProduct(productKey, aggravation, recipe);
    addProduct(product);
    turnOnNotificationsForAllPartners(product);
  }

  /**
   * Add a given product to products.
   * 
   * @param product product to add
   */
  public void addProduct(Product product) {
    _products.put(product.getKey(), product);
  }

  /**
   * Remove a product.
   * If product ID does not exist, an exception is thrown.
   * 
   * @param productKey ID of the product to be removed
   * @throws NoSuchProductException
   */
  public void removeProduct(String productKey) 
      throws NoSuchProductException {
    getProduct(productKey);

    _products.remove(productKey);
  }

  /**
   * Display all products.
   * 
   * @return string containing all products.
   */
  public String displayAllProducts() {
    String allProducts = "";

    for (Map.Entry<String, Product> product : _products.entrySet()) {
      allProducts += product.getValue() + "\n";
    }
    return allProducts.strip();
  }

  /* -------------------------- Batches -------------------------- */
  /**
   * Add a given batch to batches.
   * 
   * @param batch batch to add
   */
  public void addBatch(Batch batch) {
    _batches.add(batch);
  }

  /**
   * Remove a batch.
   * 
   * @param batch batch to be removed
   */
  public void removeBatch(Batch batch) {
    removePartnerBatch(batch);
    removeProductBatch(batch);
    _batches.remove(batch);
  }

  /**
   * Add batch to partner batches.
   * If partner ID does not exist, an exception is thrown.
   * 
   * @param partnerKey partner ID
   * @param batch      batch to add
   * @throws NoSuchPartnerException
   */
  public void addBatchToPartner(String partnerKey, Batch batch) {
    _partners.get(partnerKey).addBatch(batch);
  }

  /**
   * Remove batch from partner batches.
   * 
   * @param batch batch to be removed
   */
  public void removePartnerBatch(Batch batch) {
    Partner partner = _partners.get(batch.getPartnerKey());
    if (partner != null)
      partner.removeBatch(batch);
  }

  /**
   * Add batch to product batches.
   * If product ID does not exist, an exception is thrown.
   * 
   * @param productKey product ID
   * @param batch      batch to add
   * @throws NoSuchProductException
   */
  public void addBatchToProduct(String productKey, Batch batch) {
    _products.get(productKey).addBatch(batch);
  }

  /**
   * Remove batch from product batches.
   * 
   * @param batch batch to be removed
   */
  public void removeProductBatch(Batch batch) {
    Product product = _products.get(batch.getProductKey());
    product.removeBatch(batch);
  }

  public void registerBatch(String productKey, 
  String partnerKey, double price, int actualStock)
      throws NoSuchProductException, NoSuchPartnerException{
    Partner partner = getPartner(partnerKey);
    Product product = getProduct(productKey);
    Batch batch = new Batch(product.getKey(), partner.getKey(), price, actualStock);

    updateNotifications(product, price);

    product.setTotalStock(product.getTotalStock() + actualStock);

    if (product.getMaxPrice() < price) {
      product.setMaxPrice(price);
    }

    addBatch(batch);
    addBatchToPartner(partnerKey, batch);
    addBatchToProduct(productKey, batch);
  }

  public void updateNotifications(Product product, double price){
    if(product.getMaxPrice()!=0 && product.getTotalStock()==0){
      Notification notification = new NewNotification(product.getKey(), price);
      product.notification(notification);
    }
    Batch cheaperBatch = product.getCheaperBatch();
    if(product.getMaxPrice()!=0 && cheaperBatch!=null && price < cheaperBatch.getPrice()){
      Notification notification = new BargainNotification(product.getKey(), price);
      product.notification(notification);
    }
  }

  /**
   * Creat and register a simple batch.
   * 
   * @param productKey  product ID
   * @param partnerKey  partner ID
   * @param price       batch price
   * @param actualStock batch stock
   */
  public void importSimpleBatch(String productKey, 
      String partnerKey, double price, int actualStock) {

    if (!containsProductKey(productKey))
      registerSimpleProduct(productKey);
    importBatch(productKey, partnerKey, price, actualStock);
  }

  /**
   * Creat and register a multiple batch.
   * 
   * @param productKey  product ID
   * @param partnerKey  partner ID
   * @param price       batch price
   * @param actualStock batch stock
   * @param aggravation product aggravation
   * @param recipe      product recipe
   */
  public void importMultipleBatch(String productKey, 
      String partnerKey, double price, int actualStock,
      double aggravation, String recipe) {

    if (!containsProductKey(productKey)) 
      registerDerivedProduct(productKey, aggravation, recipe);
    importBatch(productKey, partnerKey, price, actualStock);
  }

  public void importBatch(String productKey, String partnerKey, 
      double price, int actualStock){
    Batch batch = new Batch(productKey, partnerKey, price, actualStock);
    Product product = _products.get(productKey);
    if (product.getMaxPrice() < price) {
      product.setMaxPrice(price);
    }
    product.setTotalStock(product.getTotalStock() + actualStock);
    addBatch(batch);
    addBatchToPartner(partnerKey, batch);
    addBatchToProduct(productKey, batch);
  }

  /**
   * Display available batches
   * 
   * @return string containing available batches
   */
  public String displayAvailableBatches() {
    String availableBatches = "";

    for (Batch batch : _batches) {
      availableBatches += batch + "\n";
    }

    return availableBatches.strip();
  }

  /**
   * Display batches of a given product. If product ID does not exist, an
   * exception is thrown.
   * 
   * @param productKey product ID
   * @return string containing all batches of the product.
   * @throws NoSuchProductException
   */
  public String displayBatchesByProduct(String productKey) 
      throws NoSuchProductException {
    String batches = "";
    Set<Batch> productBatches = getProduct(productKey).getBatches();

    for (Batch batch : productBatches) {
      batches += batch + "\n";
    }
    return batches.strip();
  }

  /**
   * Display batches of a given partner.
   * If partner ID does not exist, an exception is thrown.
   * 
   * @param partnerKey partner ID
   * @return string containing all batches of the partner.
   * @throws NoSuchPartnerException
   */
  public String displayBatchesByPartner(String partnerKey) 
      throws NoSuchPartnerException {
    String batches = "";
    Set<Batch> partnerBatches = getPartner(partnerKey).getBatches();

    for (Batch batch : partnerBatches) {
      batches += batch + "\n";
    }

    return batches.strip();
  }

  public String lookupProductBatchesUnderGivenPrice(double price){
    String batches = "";

    for(Batch batch : _batches)
      if(batch.getPrice() < price)
        batches += batch + "\n";

    return batches.strip();
  }

    /* ----------------------- Transactions ------------------------ */

  public boolean containsTransactionKey(int transactionKey){
    return transactionKey >= 0 && _transactionsNumber > transactionKey;
  }
  
  public Transaction getTransaction(int transactionKey)
      throws NoSuchTransactionException {
    if(containsTransactionKey(transactionKey))
      return _transactions.get(transactionKey);
    else
      throw new NoSuchTransactionException(transactionKey);
  }
  
  public void addTransaction(Transaction transaction){
    _transactions.put(transaction.getKey(), transaction);
  }
  
  public String showTransaction(int transactionKey) 
      throws NoSuchTransactionException{
    return getTransaction(transactionKey).toString(); 
  }

  public void receivePayment(int transactionKey) throws NoSuchTransactionException{
    if(containsTransactionKey(transactionKey)){
      Partner partner = _partners.get(getTransaction(transactionKey).getPartnerKey());
      if(partner!=null){
        double paymentValue = partner.paySale(transactionKey, getDate());
        setAvailableBalance(getAvailableBalance() + paymentValue);
      }
    } else
      throw new NoSuchTransactionException(transactionKey);
  }
  
  public void registerAcquisition(String partnerKey, String productKey, 
      double price, int amount) throws NoSuchPartnerException, 
      NoSuchProductException{

    Partner partner = getPartner(partnerKey);
    Product product = getProduct(productKey);
    double totalPrice = amount * price;
    registerBatch(productKey, partnerKey, price, amount);

    Acquisition acquisition = new Acquisition(_transactionsNumber, 
      partner.getKey(), product.getKey(), amount, totalPrice, _date);
    _transactionsNumber++;
  
    addTransaction(acquisition);
    partner.addAcquisition(acquisition);

    setAccountingBalance(getAccountingBalance() - totalPrice);
    setAvailableBalance(getAvailableBalance() - totalPrice);
  }

  public void registerSale(String partnerKey, int paymentDeadline,
      String productKey, int totalAmount) throws NoSuchPartnerException, 
      NoSuchProductException, NotEnoughStockException {

    Partner partner = getPartner(partnerKey);
    Product product = getProduct(productKey);
    int stock = product.getTotalStock();
    int amount = totalAmount;
    double price = 0;

    if (amount <= stock) {
      price = sellProduct(product, amount);
    }
    else {
      if (product.getRecipe() != null) {
        price = sellProduct(product, stock);
        amount -= stock;
        price += aggregate(product, amount);
      }
      else {
        throw new NotEnoughStockException(product.getKey(), totalAmount, stock);
      }
    }

    double baseValue = price;
    double paymentValue = calculatePrice(partner, baseValue, paymentDeadline, product.getPeriodDays());

    Sale sale = new Sale(_transactionsNumber, partner.getKey(), product.getKey(), totalAmount, baseValue, paymentValue, paymentDeadline);

    _transactionsNumber++;

    addTransaction(sale);
    partner.addSale(sale);

    _accountingBalance += paymentValue;


  }

  public void registerBreakdown(String partnerKey, String productKey, int amount)
      throws NoSuchPartnerException, NoSuchProductException, NotEnoughStockException{
    Partner partner = getPartner(partnerKey);
    Product product = getProduct(productKey);
    if(product.getRecipe()!=null){
      if(amount<= product.getTotalStock()){
        Breakdown breakdown = breakdown(product, partner, amount);
        addTransaction(breakdown);
        partner.addBreakdown(breakdown);
        setAccountingBalance(getAccountingBalance() + breakdown.getPaymentValue());
        setAvailableBalance(getAvailableBalance() + breakdown.getPaymentValue());
      } else{
        throw new NotEnoughStockException(product.getKey(), amount, product.getTotalStock());
      }
    }
  }

  public Breakdown breakdown(Product product, Partner partner, int amount)
      throws NoSuchProductException, NoSuchPartnerException {
    Map<String, Integer> ingredients = product.getRecipe().getIngredients();
    double sellPrice = sellProduct(product, amount);
    double totalPurchasePrice = 0;
    String products = "";
    for(Map.Entry<String, Integer> ingredient: ingredients.entrySet()){
      Product recipeProduct = getProduct(ingredient.getKey());
      int amountRecipeProduct = ingredient.getValue() * amount;
      double purchasePrice;
      Batch cheaperBatch =recipeProduct.getCheaperBatch();
      if(cheaperBatch!=null)
        purchasePrice = cheaperBatch.getPrice();
      else
        purchasePrice = recipeProduct.getMaxPrice();
      totalPurchasePrice += purchasePrice * amountRecipeProduct;
      registerBatch(recipeProduct.getKey(), partner.getKey(), purchasePrice, amountRecipeProduct);
      if(!products.equals(""))
        products += "#";
      products += ingredient.getKey() + ":" + amountRecipeProduct + ":"
        + Math.round(purchasePrice) * amountRecipeProduct;
    }
    double baseValue = sellPrice - totalPurchasePrice;
    double paymentValue = 0;
    if(baseValue > 0){
      paymentValue = baseValue;
      partner.addPoints(paymentValue * 10);
    }
    Breakdown breakdown = new Breakdown(_transactionsNumber, 
      partner.getKey(), product.getKey(), amount, baseValue, paymentValue, getDate(), products);
    _transactionsNumber++;
    return breakdown;
  }

  public double sellProduct(Product product, int amount){
    int totalStock = product.getTotalStock();
    double price = 0;

    product.setTotalStock(product.getTotalStock() - amount);

    while (amount > 0) {
      Batch batch = product.getCheaperBatch();
      int batchStock = batch.getActualStock();
      if(batchStock > amount){
        batch.setActualStock(batchStock - amount);
        price += batch.getPrice() * amount;
        amount = 0;
      } 
      else {
        price += batch.getPrice() * batchStock;
        removeBatch(batch);
        amount -= batchStock;
      }
    }
    return price;
  }

  public double aggregate(Product product, int amount) throws NotEnoughStockException{
    Map<String, Integer> ingredients = product.getRecipe().getIngredients();
    for(Map.Entry<String, Integer> ingredient: ingredients.entrySet()){
      int neededAmount = ingredient.getValue() * amount;
      Product neededProduct = _products.get(ingredient.getKey());
      if(neededAmount > neededProduct.getTotalStock()){
        throw new NotEnoughStockException(neededProduct.getKey(), neededAmount, neededProduct.getTotalStock());
      }
    }
    double totalPrice = 0;
    for(int i = 0; i<amount; i++){
      double price = 0;
      for(Map.Entry<String, Integer> ingredient: ingredients.entrySet()){
        int neededAmount = ingredient.getValue();
        Product neededProduct = _products.get(ingredient.getKey());
        price += sellProduct(neededProduct, neededAmount) * (1 + product.getAggravation());
      }
      if(product.getMaxPrice() < price)
        product.setMaxPrice(price);
      totalPrice += price;
    }
    return totalPrice;
  
  }

  public double calculatePrice(Partner partner, double baseValue, int paymentDeadline, int n){
    int daysForDeadline = paymentDeadline - _date;
    if(daysForDeadline >= n){
      return partner.calculatePrice(baseValue, daysForDeadline,"P1");
    }
    else if(daysForDeadline >= 0){
      return partner.calculatePrice(baseValue, daysForDeadline, "P2");
    }
    else if(daysForDeadline >= -n){
      return partner.calculatePrice(baseValue, -daysForDeadline, "P3");
    }
    else{
      return partner.calculatePrice(baseValue, -daysForDeadline, "P4");
    }
  }

  /*---------------------------------------------------------------*/

  /**
   * Function to parse information about partners and batches.
   * PARTNER|id|nome|endereço BATCH_S|idProduto|idParceiro|preço|
   *  stock-actual
   * BATCH_M|idProduto|idParceiro|preço|stock-actual|agravamento|
   *  componente-1:quantidade-1#...#componente-n:quantidade-n
   * 
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException {
    try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
      String s;
      while ((s = in.readLine()) != null) {
        String line = new String(s.getBytes(), "UTF-8");
        String[] fields = line.split("\\|");
        switch (fields[0]) {
        case "PARTNER" -> registerPartner(fields[1], fields[2], fields[3]);
        case "BATCH_S" -> importSimpleBatch(fields[1], fields[2], 
            Double.parseDouble(fields[3]), Integer.parseInt(fields[4]));
        case "BATCH_M" -> importMultipleBatch(fields[1], fields[2], 
            Double.parseDouble(fields[3]), Integer.parseInt(fields[4]), 
            Double.parseDouble(fields[5]), fields[6]);
        default -> throw new BadEntryException(fields[0]);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    } catch (PartnerAlreadyExistsException e) {
      e.printStackTrace();
    }
  }

}
