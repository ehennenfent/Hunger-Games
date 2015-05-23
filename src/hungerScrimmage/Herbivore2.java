package hungerScrimmage;


import java.awt.Color;
import java.util.List;

import theHungerGames.*;

public class Herbivore2 extends Herbivore {

	static private Color color = new Color(91, 129, 253);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Vinesh Girish Kannan";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		
		// mate if possible
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			}
		}
		
		if (getCell().howMuchFood() > 5) {
			return new HerbivoreEat();
		} else {
			List<Animal> allAnimals = this.getArena().getAllAnimals();
			for (Animal a: allAnimals) {
				if (a.getName().equals("Max Tyrone Ackerman")) {
					return new MoveToward(this.getCell(),a.getCell(),true);		// move towards our carnivores
				}
			}
			return new Move(Direction.randomDirection()); 
		}
		
		
		
	}
	
	@Override
	protected double getInitialGene(GeneType type) {
		
		double ranNum = getRandom().nextGaussian() * .1 + .5;
		
		switch(type) {
		case SIZE1:
			return .1;

		case SIZE2:
			return .1;

		case SPEED1:
			return 1.1;

		case SPEED2:
			return 1.1;

		case MARKINGS1:
			return .4;

		case MARKINGS2:
			return .4;

		case FERTILITY:
			return 1;
					
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
			return .3;

		case MARKINGS2:
			return .3;

		case FERTILITY:
			return 1;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

}
