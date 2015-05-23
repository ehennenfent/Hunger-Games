package hungerScrimmage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import theHungerGames.Animal;
import theHungerGames.Arena;
import theHungerGames.Carnivore;
import theHungerGames.Cell;
import theHungerGames.Direction;
import theHungerGames.GeneType;
import theHungerGames.Mate;
import theHungerGames.Move;
import theHungerGames.Turn;

public class Carnivore3 extends Carnivore {

	static private Color color = new Color(226, 204, 4);
	static private ArrayList<Integer> messageStatuses = new ArrayList<>();
	static private ArrayList<Integer> dayCounted = new ArrayList<>();
	static private ArrayList<String> messagesSaid = new ArrayList<>();
	static private HashMap<Class<? extends Animal>, Integer> timesToLive= new HashMap<Class<? extends Animal>, Integer>();

	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Carnivore 3";
	}

	private void say(String message) {
		if (!messagesSaid.contains(message)) {
			javax.swing.JOptionPane.showMessageDialog(null, message);
			messagesSaid.add(message);
		}
	}

	/**
	 *  Kill every species except this one.
	 */
	private void hellfire() {
		for (int x = 0; x < getArena().getXDim(); x++) {
			for (int y = 0; y < getArena().getYDim(); y++) {
				Cell cell = getArena().getCell(x, y);
				for (Animal an : cell.getOtherAnimals(this)) {
					cell.removeAnimal(an);
				}
			}
		}
	}

	/**
	 * Kill off a specific species.
	 * @param speciesToKill The species to kill off.
	 */
	private void handOfGod(Class<? extends Animal> speciesToKill) {
		for (int x = 0; x < getArena().getXDim(); x++) {
			for (int y = 0; y < getArena().getYDim(); y++) {
				Cell cell = getArena().getCell(x, y);
				for (Animal an : cell.getOtherAnimals(this)) {
					if (an.getClass().equals(speciesToKill)) {
						cell.removeAnimal(an);
					}
				}
			}
		}
	}

	@Override
	protected Turn userDefinedChooseMove() {

		int days = getArena().getNDays();
		
		// Always make sure day counter has enough indices available
		while (dayCounted.size() < days + 1) {
			dayCounted.add(0);
		}

		// Always make sure our status array has enough indices available
		while (messageStatuses.size() < days + 1) {
			messageStatuses.add(0);
		}

		// Custom messages for certain days (only said once)
		if (days == 1 && messageStatuses.get(days) != 1) {
			say("Oh, you fucked up now.");
			messageStatuses.set(days, 1);
		}
		else if (days == 10 && messageStatuses.get(days) != 1) {
			say("10 days in. How are your prepations going?");
			messageStatuses.set(days, 1);
		}
		else if (days == 25 && messageStatuses.get(days) != 1) {
			say("25 down. 75 to go. Feeling nervous?");
			messageStatuses.set(days, 1);
		}
		else if (days == 50 && messageStatuses.get(days) != 1) {
			say("Woo! Halfway there. Should I give you a hint?");
			messageStatuses.set(days, 1);
		}
		else if (days == 75 && messageStatuses.get(days) != 1) {
			say("Hint: Don't touch me. I'm toxic af.");
			messageStatuses.set(days, 1);
		}
		else if (days == 99 && messageStatuses.get(days) != 1) {
			say("One more day. They call me Hand of God.");
			messageStatuses.set(days, 1);
		}
		else if (days == 100 && messageStatuses.get(days) != 1) {
			say("Operation Hellfire you are go for launch.");
			messageStatuses.set(days, 1);
		}

		// There are still walls. Congregate in the upper right and mate.
		if (days < 100) {
			// Prioritize moving over mating for the first 25 turns
			if (days < 25) {
				return Arena.getRandom().nextDouble() < 0.5 ? 
						new Move(Direction.UP) : new Move(Direction.RIGHT);
			}
			// Otherwise look for mates. If none are found, continue moving up and to the right.
			List<Animal> others = getCell().getOtherAnimals(this);
			for (Animal ani : others) {
				if (checkMateability(ani)) {
					return new Mate(ani);
				}
			}
			return Arena.getRandom().nextDouble() < 0.5 ? 
					new Move(Direction.UP) : new Move(Direction.RIGHT);
		}
		// There are no walls. Commence Operation zHellfire.
		else {
			// Check if other animals are ready to die. If so, kill them.
			for (Entry<Class<? extends Animal>, Integer> ent : timesToLive.entrySet()) {
				if (ent.getValue() == 0) {
					say(ent.getKey().toString() + " has been marked for extinction. Goodbye.");
					handOfGod(ent.getKey());
					// don't care about removing the entries because fuck it
				}
				else {
					ent.setValue(ent.getValue() - 1);
					// this will fucking run for each animal on here which means species will
					// get deleted instantly. fuck. this.
				}
			}
			// Search the other animals on this cell.
			List<Animal> others = getCell().getOtherAnimals(this);
			for (Animal an : others) {
				// If another species is occupying this cell, mark it for extinction.
				if (!an.getClass().equals(Herbivore3.class) && !an.getClass().equals(this.getClass())) {
					timesToLive.put(an.getClass(), 10);
				}
			}
			// Pick a direction to move in the range [-90, 180] degrees.
			int dirAngle = (int) (270*Arena.getRandom().nextDouble() - 90);
			if (dirAngle > -90 && dirAngle < 0) { // 4th quadrant
				return Arena.getRandom().nextDouble() < 0.5 ? 
						new Move(Direction.DOWN) : new Move(Direction.RIGHT);
			}
			else if (dirAngle > 0 && dirAngle < 90) { // 1st quadrant
				return Arena.getRandom().nextDouble() < 0.5 ? 
						new Move(Direction.UP) : new Move(Direction.RIGHT);
			}
			else if (dirAngle > 90 && dirAngle < 180) { // 2nd quadrant
				return Arena.getRandom().nextDouble() < 0.5 ? 
						new Move(Direction.LEFT) : new Move(Direction.UP);
			}
			else {
				return null;
			}
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
