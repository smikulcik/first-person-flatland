package flatlandCharacters;
import java.util.Date;

/**
 * Message class to handle interaction between characters
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Msg {
	public Person from;
	public String msg;
	public Date when;
	
	public Msg(Person from, String msg) {
		this.from = from;
		this.msg = msg;
		this.when = new Date();
	}
	
	@Override
	public String toString()
	{
		return from.name + ": " + msg;
	}
}
