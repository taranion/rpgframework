/**
 * 
 */
package de.rpgframework.core;

import java.io.IOException;
import java.util.List;

/**
 * @author prelle
 *
 */
public interface PlayerService {

	public Player getMyself();

	public Player createPlayer();
	
	public List<Player> getPlayers() throws IOException;
	
	public Player getPlayer(String name) throws IOException;
	
	public void deletePlayer(Player player) throws IOException;
	
	public void savePlayer(Player player) throws IOException;
	
}
