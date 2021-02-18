package flatlandCharacters;

import flatlandGame.GameVars;
import simonGraphics.PointD;

/**
 * The grandson who ran away that you need to find.
 * 
 * @author Simon Mikulcik
 * date: 4/30/2014
 *
 */
public class RunawayGrandson extends Grandson {

	public RunawayGrandson(String name, PointD center) {
		super(name, center);
	}

	/**
	 * Runaway Grandson's messages to aSquare for various situations
	 */
	public void interact(Person withWhom){
		withWhom.sendMsg(new Msg(this, "Hey grandad, I was having fun running around!"));
		withWhom.sendMsg(new Msg(GameVars.aSquare, "You Rascall!! Come on home."));
		GameVars.hasFoundSonYet = true;
	}

}
