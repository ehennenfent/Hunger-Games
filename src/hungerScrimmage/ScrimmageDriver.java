package hungerScrimmage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import theHungerGames.Animal;
import theHungerGames.Arena;
import theHungerGames.FoodCell;
import theHungerGames.SeasonalFoodCell;
import theHungerGames.SimplexFoodCell;
import theHungerGames.Viewer;
import theHungerGames.WallCell;

public class ScrimmageDriver {

	/**
	 * Driver class for ecology simulation
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	
	private static boolean RANDOMIZE_STARTING_POSITIONS = true;
	private static boolean SAVE_SCREENSHOTS = true;
	private static Class<? extends FoodCell> CELL_CLASS = SeasonalFoodCell.class;
	private static Constructor<?> constructor = CELL_CLASS.getDeclaredConstructors()[0];
	
	public static Type[] herbivore = {null, Herbivore1.class, Herbivore2.class, Herbivore3.class, Herbivore4.class};
	public static Type[] carnivore = {null, Carnivore1.class, Carnivore2.class, Carnivore3.class, Carnivore4.class,};
	public static List<Integer> teams = Arrays.asList(1,2,3,4);
	
	private static void addAnimal(Arena arena, int number, Class<? extends Animal> type) throws InstantiationException, IllegalAccessException {
		int team = 0;
		for (int i = 0; i < number; ++i) {
			Animal newAnimal = Animal.makeRandomAnimal(type);
			if(type == herbivore[1] || type == carnivore[1]){
				team = teams.get(0);	
			}
			else if(type == herbivore[2] || type == carnivore[2]){
				team = teams.get(1);	
			}
			else if(type == herbivore[3] || type == carnivore[3]){
				team = teams.get(2);	
			}
			else if(type == herbivore[4] || type == carnivore[4]){
				team = teams.get(3);	
			}
			arena.addRandomTeamAnimal(newAnimal, team);
		}
	}
	
	private static final double TIME_INCREMENT = 1;
	private static final int TIMER_SPEED = 50;
	
	public static void main(String args[]) throws InstantiationException, IllegalAccessException {
		if(RANDOMIZE_STARTING_POSITIONS)
		Collections.shuffle(teams);
		// These control the size of the grid
		final int xSize = 61;
		final int ySize = 61;
		final int cellSize = 10;

		final int nHerbivore = 200;
		final int nCarnivore = 100;
		
		Arena mymap = new Arena(xSize, ySize, cellSize);

		for (int ix = 0; ix < mymap.getXDim(); ++ix) {
			for (int iy = 0; iy < mymap.getYDim(); ++iy) {
					try {
						mymap.changeCell(ix, iy, (FoodCell) constructor.newInstance(mymap, ix, iy));
					} catch (IllegalArgumentException
							| InvocationTargetException e) {
						System.out.println("It might be obvious that I don't understand Java's Type system");
						e.printStackTrace();
					}
			}
		}
		
		for (int iy = 0; iy < mymap.getYDim(); ++iy) {
			mymap.changeCell(31, iy, new WallCell(mymap, 31, iy));
		}
		
		for (int ix = 0; ix < mymap.getXDim(); ++ix) {
			mymap.changeCell(ix, 31, new WallCell(mymap, ix, 31));
		}
		
		addAnimal(mymap, nHerbivore, Herbivore1.class);
		addAnimal(mymap, nCarnivore, Carnivore1.class);
		addAnimal(mymap, nHerbivore, Herbivore2.class);
		addAnimal(mymap, nCarnivore, Carnivore2.class);
		addAnimal(mymap, nHerbivore, Herbivore3.class);
		addAnimal(mymap, nCarnivore, Carnivore3.class);
		addAnimal(mymap, nHerbivore, Herbivore4.class);
		addAnimal(mymap, nCarnivore, Carnivore4.class);

		Viewer.runViewer(mymap, TIME_INCREMENT, TIMER_SPEED);
	}

	public static Constructor getCellConstructor() {
		return constructor;
	}
	public static boolean getImageState(){
		return SAVE_SCREENSHOTS;
	}

}
