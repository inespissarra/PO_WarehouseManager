package ggc;

import java.util.Map;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class Recipe implements Serializable {
  private Map<String, Integer> _ingredients = new LinkedHashMap<>();

  public void addIngredients(String stringRecipe){
    String[] ingredients = stringRecipe.split("#");
    String[] ingredient;
    for (String stringIngredient : ingredients) {
      ingredient = stringIngredient.split(":");
      addIngredient(ingredient[0], Integer.parseInt(ingredient[1]));
    }
  }

  public void addIngredient(String key, int amount) {
    _ingredients.put(key, amount);
  }

  public Map<String, Integer> getIngredients(){
    return _ingredients;
  }

  @Override
  public String toString() {
    String recipe = "";
    for (Map.Entry<String, Integer> ingredient : _ingredients.entrySet()) {
      if (!recipe.equals(""))
        recipe += "#";
      recipe += ingredient.getKey() + ":" + ingredient.getValue();
    }
    return recipe;
  }
}
