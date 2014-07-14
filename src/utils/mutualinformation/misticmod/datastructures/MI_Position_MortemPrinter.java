package utils.mutualinformation.misticmod.datastructures;

/**
 * Exports MI data in Mortem's format.<br>
 * Example: 'MI[ 6 - ][ 1035 L ] = -0.040435 -0.001988 0.018238 -2.108127'
 */
public class MI_Position_MortemPrinter implements MI_Position_Printer {
	@Override
	public String print(MI_Position position) {
		return "MI[ "+position.getPos1()+" "+position.getAa1()+ " ][ " +
				      position.getPos2()+" "+position.getAa2()+ " ] = " +
	                  position.getRaw_mi() + " " + 
				      position.getMean_mi() + " "+
	                  position.getSd_mi() + " " + 
				      position.getMi();
	}

}
