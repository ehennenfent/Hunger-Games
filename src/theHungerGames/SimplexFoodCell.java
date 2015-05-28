package theHungerGames;

import java.awt.Color;
import java.util.Random;

public class SimplexFoodCell extends FoodCell {
	
	private double foodAmount;
	static private final double GROWTH_PER_TURN = 1;
	static private final double GLOBAL_FOOD_MAX = 10;
	static private final double SIMPLEX_RESOLUTION = .05;
	static private final double FOOD_FUDGER = 1.65;
	private double SEASON_MULTIPLIER = 1;
	private double FOOD_MAX = 10;
	Random random;
	private double SEASON_SHIFT;
	private int day = 150;
	
	public SimplexFoodCell(Arena map, int x, int y) {
		super(map, x, y);
		random = Arena.getRandom();
		
		foodAmount = FOOD_MAX*Math.abs(SimplexNoise.noise(x*SIMPLEX_RESOLUTION, y*SIMPLEX_RESOLUTION));
		
//		foodAmount *= FOOD_FUDGER;
		if(foodAmount == 0)
			foodAmount += Math.abs(8*random.nextGaussian());
		if(foodAmount > GLOBAL_FOOD_MAX)
			foodAmount = GLOBAL_FOOD_MAX;
		
		FOOD_MAX  = foodAmount;
		SEASON_SHIFT = random.nextGaussian()/5;
	
	}
	
	public void setDay(int day){
		int tian1 = day % 365;
		int dist = Math.abs(183 - tian1);
		double summerAmount = (double)(183 - dist) / 183d + SEASON_SHIFT;
		SEASON_MULTIPLIER = summerAmount > .7d ? summerAmount : .7;
		SEASON_MULTIPLIER = SEASON_MULTIPLIER > 1d ? 1 : SEASON_MULTIPLIER;
	}

	@Override
	public double howMuchFood() {
		return foodAmount;
	}

	@Override
	public void beginningOfTurn() {
		
		setDay(day);
		day++;
		
		foodAmount += GROWTH_PER_TURN;
		if (foodAmount > FOOD_MAX) {
			foodAmount = FOOD_MAX;
		}
		
		super.beginningOfTurn();
	}

	@Override
	protected Color getColor() {
		return Color.getHSBColor((float)(25+100*(foodAmount/GLOBAL_FOOD_MAX))/360f, (float) SEASON_MULTIPLIER * 175.0f / 240,165/240f);
	}

	@Override
	public double eatFood(double amount) {
		if (amount <= foodAmount) {
			foodAmount -= amount;
			return amount * SEASON_MULTIPLIER;
		} else {
			double temp = foodAmount;
			foodAmount = 0;
			return temp * SEASON_MULTIPLIER;
		}
	}

}
