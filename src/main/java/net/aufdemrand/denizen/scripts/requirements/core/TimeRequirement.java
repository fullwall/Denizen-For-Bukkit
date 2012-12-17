package net.aufdemrand.denizen.scripts.requirements.core;

import java.util.List;

import org.bukkit.entity.Player;

import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;



public class TimeRequirement extends AbstractRequirement{
	
	private enum TIME {DAWN, DAY, DUSK, NIGHT }
	
	@Override
	public void onEnable() {
		//nothing to do here
	}

	TIME time;
	
	@Override
	public boolean check(Player player, DenizenNPC npc, String scriptName,
			List<String> args) throws RequirementCheckException {
		
		boolean outcome = false;
		
		for (String thisArg : args){
			
			if (aH.matchesArg("DAWN", thisArg)) time = time.DAWN;
			
			else if (aH.matchesArg("DAY", thisArg)) time = time.DAY;
			
			else if (aH.matchesArg("DUSK", thisArg)) time = time.DUSK;
			
			else if (aH.matchesArg("NIGHT", thisArg)) time = time.NIGHT;
			
		}
		/* IM LOST
			if (!Character.isDigit(theTime.charAt(0))) {
				if (theTime.equalsIgnoreCase("DAWN")
						&& theWorld.getTime() > 23000) outcome = true;

				else if (theTime.equalsIgnoreCase("DAY")
						&& theWorld.getTime() > 0
						&& theWorld.getTime() < 13500) outcome = true;

				else if (theTime.equalsIgnoreCase("DUSK")
						&& theWorld.getTime() > 12500
						&& theWorld.getTime() < 13500) outcome = true;

				else if (theTime.equalsIgnoreCase("NIGHT")
						&& theWorld.getTime() > 13500) outcome = true;
			}

			else if (Character.isDigit(theTime.charAt(0))) 
				if (theWorld.getTime() > Long.valueOf(theTime)
						&& theWorld.getTime() < Long.valueOf(highTime)) outcome = true;
		*/
		return outcome;
	}

}