package hungerScrimmage;

import java.awt.Color;
import java.util.List;

import theHungerGames.*;

public class Carnivore2 extends Carnivore {

	static private Color color = new Color(20, 43, 115);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Max Tyrone Ackerman";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			} else if (isPrey(ani)) {
				return new CarnivoreEat(ani);
			}
		}
		List<Animal> allAnimals = this.getArena().getAllAnimals();
		for (Animal a: allAnimals) {
			if (a instanceof Herbivore && getRandom().nextDouble() > .75) {
				return new MoveToward(this.getCell(),a.getCell(),true);		// move towards vinesh's herbivores
			}
		}

		return new Move(Direction.randomDirection());
	}

	@Override
	protected double getInitialGene(GeneType type) {
		double ranNum = getRandom().nextGaussian() * .1 + .5;
		
		switch(type) {
		case SIZE1:
			return 1;

		case SIZE2:
			return 1;

		case SPEED1:
			return .1;

		case SPEED2:
			return .1;

		case MARKINGS1:
			return .4;

		case MARKINGS2:
			return .4;

		case FERTILITY:
			return .8;

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
			return .3;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

}
