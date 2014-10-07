package utils.mutualinformation.misticmod.splitdata;

import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

/**
 * Split MI data in serveral set of data.
 * @author javier
 *
 */
public class MIDataSplitter {

	private List<MI_Position> firstIntraMiDataList;
	private List<MI_Position> secondIntraMiDataList;
	private List<MI_Position> interMiDataList;
	
	
	
	public MIDataSplitter() {
		super();
		this.clear();
	}

	/**
	 * Split a file of intra/inter protein MI data in three set of data. Two 
	 * for each intra MI data sets and one for inter protein MI data.
	 * This assumes that the calculation was made using an joint alignment of
	 * two proteins. The length of the first protein must be passed to properly
	 * split the data. Also, is assumed that the first index of the data is 1,
	 * not 0.
	 *
	 * 
	 * @param position
	 * @return
	 */
	public void splitAfter(int position, List<MI_Position> data) {

		
		for (MI_Position mi_Position : data) {
			
			int pos1 = mi_Position.getPos1();
			int pos2 = mi_Position.getPos2();
			
			boolean pos1_belongs_p1 = pos1<=position;
			boolean pos1_belongs_p2 = pos1>position;
			boolean pos2_belongs_p1 = pos2<=position;
			boolean pos2_belongs_p2 = pos2>position;
			
			if (pos1_belongs_p1 && pos2_belongs_p1) {
				// If both position belongs to protein 1
				// then the mi pair goes to the
				// first intra MI data set
				this.getFirstIntraMiDataList().add(mi_Position);
				
			} else 
			if (pos1_belongs_p2 && pos2_belongs_p2) {
				// If both position belongs to protein 2
				// then the mi pair goes to the
				// second intra MI data set
				this.getSecondIntraMiDataList().add(mi_Position);
			} else {
				// otherwise the mi pair goes to the 
				// inter MI data set.
				this.getInterMiDataList().add(mi_Position);
			}
			
		}
		
	}
	
	public void clear () {
		
		List<MI_Position> intra_first = new ArrayList<MI_Position>();
		List<MI_Position> intra_second = new ArrayList<MI_Position>();
		List<MI_Position> inter = new ArrayList<MI_Position>();
		this.setFirstIntraMiDataList(intra_first);
		this.setSecondIntraMiDataList(intra_second);
		this.setInterMiDataList(inter);
		
	}

	/**
	 * @return the firstIntraMiDataList
	 */
	public List<MI_Position> getFirstIntraMiDataList() {
		return firstIntraMiDataList;
	}

	/**
	 * @return the secondIntraMiDataList
	 */
	public List<MI_Position> getSecondIntraMiDataList() {
		return secondIntraMiDataList;
	}

	/**
	 * @return the interMiDataList
	 */
	public List<MI_Position> getInterMiDataList() {
		return interMiDataList;
	}

	/**
	 * @param firstIntraMiDataList the firstIntraMiDataList to set
	 */
	private void setFirstIntraMiDataList(List<MI_Position> firstIntraMiDataList) {
		this.firstIntraMiDataList = firstIntraMiDataList;
	}

	/**
	 * @param secondIntraMiDataList the secondIntraMiDataList to set
	 */
	private void setSecondIntraMiDataList(List<MI_Position> secondIntraMiDataList) {
		this.secondIntraMiDataList = secondIntraMiDataList;
	}

	/**
	 * @param interMiDataList the interMiDataList to set
	 */
	private void setInterMiDataList(List<MI_Position> interMiDataList) {
		this.interMiDataList = interMiDataList;
	}

	
}
