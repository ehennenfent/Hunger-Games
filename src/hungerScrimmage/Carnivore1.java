package hungerScrimmage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import theHungerGames.Animal;
import theHungerGames.Carnivore;
import theHungerGames.CarnivoreEat;
import theHungerGames.Cell;
import theHungerGames.Direction;
import theHungerGames.GeneType;
import theHungerGames.Mate;
import theHungerGames.Move;
import theHungerGames.MoveToward;
import theHungerGames.Turn;

public class Carnivore1 extends Carnivore {

	static private Color color = new Color(159, 0, 0);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Kevin the Predator";
	}
	
	boolean firstSweep = false;
	Animal target = null;
	boolean targeting = false;

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			} else if (isPrey(ani) && !(ani instanceof Herbivore1)) {
				return new CarnivoreEat(ani);
			}
		}

		
		if(!firstSweep){
			return strat1();
		}
		else if(firstSweep){
			return strat2();
		}
		else{
			return new Move(Direction.randomDirection());	
		}
		
		
	}
	
	public Turn strat1(){
		List<Animal> cellAnimals = getCell().getOtherAnimals(this);
		int mates = 0;
		for(Animal ani : cellAnimals){
			if(ani instanceof Carnivore1){
				mates++;
			}
		}
		
		if(this.getCell().getX() > getArena().getXDim() - 15 && this.getCell().getY() > getArena().getYDim() - 15){
			firstSweep = true;
			return sweep(Direction.UP, Direction.RIGHT);
		}
		else if(this.getCell().getX() < getArena().getXDim() - 15 && this.getCell().getY() < getArena().getYDim() - 15){
			if(mates > 1){
				if(getRandom().nextDouble() < 0.5){
					return sweepPreference(Direction.RIGHT, Direction.UP, 0.25);
				}
				else{
					return sweepPreference(Direction.LEFT, Direction.DOWN, 0.75);
				}
			}
			else{
				return sweep(Direction.DOWN, Direction.RIGHT);
			}
		}
		else{
			List<Animal> others = getCell().getOtherAnimals(this);
			for (Animal ani : others) {
				if (checkMateability(ani)) {
					return new Mate(ani);
				}
			}
			return new Move(Direction.randomDirection());
		}
	}
	
	public Turn strat2(){
		List<Animal> allOthers = getArena().getAllAnimals(this);
		List<Animal> prey = new ArrayList<Animal>();
		for(Animal ani : allOthers){
			if (isPrey(ani) && !(ani instanceof Herbivore1)) {
				prey.add(ani);
			}
		}
		
		if(prey.size() > 0 && !targeting){
			Animal closestPrey = prey.get(0);
			target = closestPrey;
			targeting = true;
			if (isPrey(closestPrey) && !(closestPrey instanceof Herbivore1)) {
				new MoveToward(this.getCell(), closestPrey.getCell(), true);
			}
		}
		if(prey.size() > 0 && targeting){
			if (isPrey(target) && !(target instanceof Herbivore1)) {
				new MoveToward(this.getCell(), target.getCell(), true);
			}
		}
		else{
			System.out.println("No Prey!");
		}
		
		
		return new Move(Direction.randomDirection());		
	}
	
	public Turn sweep(Direction direction1, Direction direction2){
		if(getRandom().nextDouble() < 0.5){
			return new Move(direction1);
		}
		else{
			return new Move(direction2);
		}
	}
	
	public Turn sweepPreference(Direction direction1, Direction direction2, double preference){
		if(getRandom().nextDouble() < preference){
			return new Move(direction1);
		}
		else{
			return new Move(direction2);
		}
	}
	
	public double distance(Cell c1, Cell c2){
		return Math.sqrt(Math.pow((c2.getX()-c1.getX()),2)+Math.pow((c2.getY()-c1.getY()),2));
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
