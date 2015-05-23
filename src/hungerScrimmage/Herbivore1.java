package hungerScrimmage;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import theHungerGames.Animal;
import theHungerGames.Carnivore;
import theHungerGames.Direction;
import theHungerGames.GeneType;
import theHungerGames.Herbivore;
import theHungerGames.HerbivoreEat;
import theHungerGames.Mate;
import theHungerGames.Move;
import theHungerGames.MoveToward;
import theHungerGames.Turn;

public class Herbivore1 extends Herbivore {

	static private Color color = new Color(253, 75, 75);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Guna eats Spinach";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			}
		}
		
		List<Animal> allOthers = getArena().getAllAnimals(this);
		List<Animal> predators = new ArrayList<Animal>();
		for(Animal ani : allOthers){
			if(ani instanceof Carnivore && !(ani instanceof Carnivore1)){
				predators.add(ani);
			}
		}
		if(predators.size() > 0){
			Animal closestPredator = predators.get(0);
			if(closestPredator instanceof Carnivore){
				new MoveToward(this.getCell(), closestPredator.getCell(), false);
			}
		}
		else{
			System.out.println("No Predators!");
		}
		
		if (getCell().howMuchFood() > 5) {
			return new HerbivoreEat();
		} else {
			return new Move(Direction.randomDirection());
		}
	}

	@Override
	protected double getInitialGene(GeneType type) {
		
		double ranNum = getRandom().nextGaussian() * .1 + .5;
		
		switch(type) {
		case SIZE1:
			return ranNum;

		case SIZE2:
			return ranNum;

		case SPEED1:
			return ranNum;

		case SPEED2:
			return ranNum;

		case MARKINGS1:
			return ranNum;

		case MARKINGS2:
			return ranNum;

		case FERTILITY:
			return ranNum;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

	@Override
	protected double getInitialSD(GeneType type) {
		switch(type) {
		case SIZE1:
			return .1;

		case SIZE2:
			return .1;

		case SPEED1:
			return .1;

		case SPEED2:
			return .1;

		case MARKINGS1:
			return .1;

		case MARKINGS2:
			return .1;

		case FERTILITY:
			return .1;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

}
