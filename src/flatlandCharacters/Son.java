package flatlandCharacters;
import simonGraphics.PointD;


/**
 * Son of aSquare -- a pentagon
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Son extends Man {

	public Son(String name, PointD center) {
		super(name, 5, center, 50);
		
	}

	/**
	 * aSquare's son is worried about his son, the hexagon
	 */
	public void interact(Person withWhom){
		
		withWhom.sendMsg(new Msg(this, "What will I do! What will I do! \n" +
										"My son has ran away! What will I do!"));
	}

}
