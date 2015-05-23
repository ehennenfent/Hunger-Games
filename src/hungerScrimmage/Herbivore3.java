package hungerScrimmage;

import java.awt.Color;
import java.util.List;

import theHungerGames.*;

public class Herbivore3 extends Herbivore {

	static private Color color = new Color(255, 240, 97);
	static private final double FOOD_SCALAR = 10;
	static private final double FOOD_LB_SCALAR = 10;
	static private final double HIDE_SCALAR = 10;
	static private final double TARGET_PROBABILITY = .1;
	static private final double DENSITY_DISTANCE = 6;
	static private final double MAX_DENSITY = 75;
	static private final int MONTE_CARLO_POINTS = 15;
	static private Cell targetCell;
	static private Cell initialCell;
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Michelle Hoehn";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			}
		}
			if (getCell().howMuchFood() > (FOOD_SCALAR * 0.26999308176309406) || getEnergyReserve() < (FOOD_LB_SCALAR * 0.42994026228221666)) {
				return new HerbivoreEat();
			} 
		return doMovingBehavior();
	}
	
	private Move doMovingBehavior(){
		if(getArena().getNDays() % 360 == 0){
			double min = Integer.MAX_VALUE;
			for(int i = 0; i < MONTE_CARLO_POINTS; i++){
				Cell target = new Cell(getArena(),
						Arena.getRandom().nextInt(getArena().getXDim()),
						Arena.getRandom().nextInt(getArena().getYDim()));
				if(getDensity(target) <= min){
					targetCell = target;
					initialCell = getCell();
				}
			}
		}
			for(Animal ani : getArena().getAllAnimals()){
				if(checkMateability(ani)){
					if(Arena.getRandom().nextDouble() < 0.45665207580042166){
						return new MoveToward(getCell(), ani.getCell(), true);
					}
				}
				if(ani instanceof Carnivore && distance(ani.getCell(), getCell()) < (HIDE_SCALAR * 0.4361667272802573)){
					return new MoveToward(getCell(), ani.getCell(), false);
				}
			}
//			Cell initialCell = getCell();
//			Cell targetCell = new Cell(getArena(), 15,15);
			if(Arena.getRandom().nextDouble() < TARGET_PROBABILITY && initialCell != null && targetCell != null && getArena().getNDays() > 360){
//			if(getDensity(getCell()) > MAX_DENSITY && initialCell != null && targetCell != null){
				return new MoveToward(getCell(), findDeltaCell(initialCell, targetCell, getCell()), true);
			}
		
		return new Move(Direction.randomDirection());
	}
	
	@Override
	protected double getInitialGene(GeneType type) {		
		switch(type) {
		case SIZE1:
			return 0.4610585443862264;

		case SIZE2:
			return 0.4610585443862264;

		case SPEED1:
			return 0.47581305626751774;

		case SPEED2:
			return 0.47581305626751774;

		case MARKINGS1:
			return 0.48645557972875075;

		case MARKINGS2:
			return 0.48645557972875075;

		case FERTILITY:
			return 0.471198253194545;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

	@Override
	protected double getInitialSD(GeneType type) {
		switch(type) {
		case SIZE1:
			return 0.5348987192581109;

		case SIZE2:
			return 0.5348987192581109;

		case SPEED1:
			return 0.4428054025917259;

		case SPEED2:
			return 0.4428054025917259;

		case MARKINGS1:
			return 0.3260788875192442;

		case MARKINGS2:
			return 0.3260788875192442;

		case FERTILITY:
			return 0.4936704191700037;

		default:
			throw new RuntimeException("Never reach here");

		}
	}
	
	public double distance(Cell a, Cell b){
		return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
	}
	
	//Cell where the first animal moved from, Cell where the first animal is moving towards,
	//Cell that the current animal is moving from
	public Cell findDeltaCell(Cell initial, Cell target, Cell initialOffset){
		int deltaX = initial.getX() - initialOffset.getX();
		int deltaY = initial.getY() - initialOffset.getY();
		int targetX = target.getX() + deltaX;
		int targetY = target.getY() + deltaY;
		if(targetX > getArena().getXDim())
			targetX = getArena().getXDim();
		if(targetY > getArena().getYDim())
			targetY = getArena().getYDim();
		if(targetX < 0)
			targetX = 0;
		if(targetY < 0)
			targetY = 0;
		return new Cell(getArena(), targetX, targetY);
	}
	
	public double getDensity(Cell cell){
		double density = 0;
		for(int i = 0; i < getArena().getXDim(); i++){
			for(int j = 0; j < getArena().getYDim(); j++){
				Cell c = getArena().getCell(i,j);
				if(distance(getCell(), c) < DENSITY_DISTANCE){
					density += c.getOtherAnimals(null).size();
				}
			}
		}
		return density;
	}
	
	
}
