package hw4;
import java.io.IOException;

public class HW4Test 
{
	public static void main(String[] args) throws IOException
	{
		RobotPath rPath = new RobotPath();
		// must read from args
		//rPath.readInput(args[0]);
		rPath.readInput("testfile.txt");
		
		System.out.println("\n planShortest:\n");
		rPath.planShortest();
		rPath.output();
		
		System.out.println("\n quickPlan:\n");
		rPath.quickPlan();
		rPath.output();
	}
}
