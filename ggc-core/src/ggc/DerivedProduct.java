package ggc;

import java.io.Serializable;

public class DerivedProduct extends Product {
  private double _aggravation;
  private Recipe _recipe;

  public DerivedProduct(String id, double aggravation, Recipe recipe) {
    super(id, 3);
    _aggravation = aggravation;
    _recipe = recipe;
  }

  // getters and setters (aggravation)
  public void setAggravation(double aggravation) {
    _aggravation = aggravation;
  }

  public double getAggravation() {
    return _aggravation;
  }

  // getters and setters (recipe)
  public void setRecipe(Recipe recipe) {
    _recipe = recipe;
  }

  public Recipe getRecipe() {
    return _recipe;
  }

  @Override
  public String toString() {
    return super.toString() + "|" + getAggravation()
        + "|" + getRecipe().toString();
  }
}
