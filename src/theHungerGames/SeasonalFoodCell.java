package theHungerGames;

import java.awt.Color;
import java.util.Random;

public class SeasonalFoodCell extends FoodCell {
	
	static private Random random = new Random();
	boolean winter = false;
	static private double min = 0;
	static private double max = 0;
	float seasonHue = 75.0f;

	public SeasonalFoodCell(Arena map, int x, int y) {
		super(map, x, y);
	}
	
	public void updateSeason(){
		int days = getMap().getNDays()%360;
		if(days > 0 && days < 90){
			winter = false;
		}
		else if(days > 90 && days < 180){
			winter = true;
		}
		else if(days >180 && days < 270){
			winter = false;
		}
		else{
			winter = true;
		}
		//UPDATE
		if(winter){
			min = -0.025;
			max = 0.005;
			seasonHue = 100.0f;
		}
		else{
			min = 0.015;
			max = 0.025;
			seasonHue = 75.0f;
		}
	}

	@Override
	public void beginningOfTurn() {
		updateSeason();
		super.beginningOfTurn();
	}

	@Override
	public double getGrowthPerTurn() {
		double growth = (random.nextDouble()*(max - min)) + min;
		return growth;
	}

	@Override
	protected Color getColor() {
		double brightness = 1 - ((double)howMuchFood() / getFoodMax() * 165 / 240);
		return Color.getHSBColor(seasonHue / 240, 175.0f / 240, (float)(brightness));
	}
	
}
