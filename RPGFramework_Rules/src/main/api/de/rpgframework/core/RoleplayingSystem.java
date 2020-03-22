/**
 *
 */
package de.rpgframework.core;

import java.util.MissingResourceException;

import de.rpgframework.core.impl.RPGFrameworkConstants;

/**
 * @author prelle
 *
 */
public enum RoleplayingSystem {

	ALL(Genre.values()),
	CORIOLIS(Genre.SCIFI),
	CTHULHU(Genre.VINTAGE_1920, Genre.HORROR),
	DEADLANDS(Genre.WESTERN),
	DRESDEN_FILES(Genre.TODAY),
	DSA(Genre.FANTASY),
	DSA5(Genre.FANTASY),
	HEX(Genre.VINTAGE_1930),
	MURPG(Genre.TODAY, Genre.SUPERHEROES),
	MUTANT_YEAR_ZERO(Genre.APOCALYPTIC, Genre.TODAY),
	SPLITTERMOND(Genre.FANTASY),
	SHADOWRUN(Genre.CYBERPUNK),
	SHADOWRUN6(Genre.CYBERPUNK),
	SPACE1889(Genre.STEAMPUNK),
	STARWARS(Genre.SCIFI),
	TALES_FROM_THE_LOOP(Genre.TODAY, Genre.SCIFI, Genre.VINTAGE_1980),
	TORG_ETERNITY(Genre.TODAY, Genre.FANTASY, Genre.SCIFI, Genre.CYBERPUNK),
	;

	private Genre[] genres;

	//--------------------------------------------------------------------
	private RoleplayingSystem(Genre... genre) {
		this.genres = genre;
	}

	//--------------------------------------------------------------------
	public Genre[] getGenre() {
		return genres;
	}

	//--------------------------------------------------------------------
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("roleplayingsystem."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "roleplayingsystem."+this.name().toLowerCase();
	}

	//--------------------------------------------------------------------
	public String toString() {
		return name();
	}

}
