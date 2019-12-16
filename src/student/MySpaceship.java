package student;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import models.Node;
import models.NodeStatus;
import models.RescueStage;
import models.ReturnStage;
import models.Spaceship;
	
/** An instance implements the methods needed to complete the mission */
public class MySpaceship extends Spaceship {
	public HashMap<Long, Long> parent = new HashMap<Long, Long>();
	//<visited/current location id, parent location id
	
	/**
	 * Explore the galaxy, trying to find the missing spaceship that has crashed
	 * on Planet X in as little time as possible. Once you find the missing
	 * spaceship, you must return from the function in order to symbolize that
	 * you've rescued it. If you continue to move after finding the spaceship
	 * rather than returning, it will not count. If you return from this
	 * function while not on Planet X, it will count as a failure.
	 * 
	 * At every step, you only know your current planet's ID and the ID of all
	 * neighboring planets, as well as the ping from the missing spaceship.
	 * 
	 * In order to get information about the current state, use functions
	 * currentLocation(), neighbors(), and getPing() in RescueStage. You know
	 * you are standing on Planet X when foundSpaceship() is true.
	 * 
	 * Use function moveTo(long id) in RescueStage to move to a neighboring
	 * planet by its ID. Doing this will change state to reflect your new
	 * position.
	 */
	@Override
	public void rescue(RescueStage state) {
		// TODO : Find the missing spaceship
		if (state.foundSpaceship()) return;
		boolean unvisitednb = false;
		long nextid = 0;
		double maxping = 0;
		double thisping = 0;
		for (NodeStatus a: state.neighbors()){
			if (!parent.containsKey(a.getId())) {
				thisping = a.getPingToTarget();
				if(!unvisitednb || thisping > maxping){
					nextid = a.getId();
					maxping = thisping;
				}
				unvisitednb = true;
			}
		}
		if(unvisitednb){
			parent.put(nextid, state.currentLocation());
			state.moveTo(nextid);
			rescue(state);
		}else{
			state.moveTo(parent.get(state.currentLocation()));
			rescue(state);
		}
	}
	
	/**
	 * Get back to Earth, avoiding hostile troops and searching for speed
	 * upgrades on the way. Traveling through 3 or more planets that are hostile
	 * will prevent you from ever returning to Earth.
	 *
	 * You now have access to the entire underlying graph, which can be accessed
	 * through ReturnState. currentNode() and getEarth() will return Node objects
	 * of interest, and getNodes() will return a collection of all nodes in the
	 * graph.
	 *
	 * You may use state.grabSpeedUpgrade() to get a speed upgrade if there is
	 * one, and can check whether a planet is hostile using the isHostile
	 * function in the Node class.
	 *
	 * You must return from this function while on Earth. Returning from the
	 * wrong location will be considered a failed run.
	 *
	 * You will always be able to return to Earth without passing through three
	 * hostile planets. However, returning to Earth faster will result in a
	 * better score, so you should look for ways to optimize your return.
	 */
	@Override
	public void returnToEarth(ReturnStage state) {
		// TODO: Return to Earth
		int count=0;

		if (state.currentNode().equals(state.getEarth())) {return;}
		
		List<Node> path = Paths.shortestPath(state.currentNode(), state.getEarth());
		LinkedList<Node> finalpath =(LinkedList<Node>) path;

		for(int i=1;i<finalpath.size();i++){
			if(finalpath.get(i).isHostile()){count++;}
			if(count<3){
				state.moveTo(finalpath.get(i));
				if(state.currentNode().hasSpeedUpgrade()){
					state.grabSpeedUpgrade();
				}
			}
			else{
				path = Paths.shortestPathwoH(state.currentNode(), state.getEarth());
				finalpath =(LinkedList<Node>) path;
				count = 0;
				i=0;
			}

		}
	}

}