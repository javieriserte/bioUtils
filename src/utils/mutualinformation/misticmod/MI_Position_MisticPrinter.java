package utils.mutualinformation.misticmod;

/**
 * Export MI data into Mistic format.<br>
 * Example: '6	-	1035	L	-2.108127'
 * 
 * @author javier iserte
 *
 */
public class MI_Position_MisticPrinter implements MI_Position_Printer {

	@Override
	public String print(MI_Position position) {
		return position.getPos1() + "\t" + position.getAa1() + "\t" + position.getPos2() + "\t" + position.getAa2()+ "\t" + position.getMi();
	}

}
