package utils.mutualinformation.mimatrixviewer.matrixview;

public class ZoomMatrixColoringStrategyFactory {
	
	public static ZoomMatrixColoringStrategy BlackAndWhiteWithStdCutoff() {
		
		return new BlackAndWhiteZoomMatrixColoringStrategy(6.5);
		
	}
	
	public static ZoomMatrixColoringStrategy BlackAndWhiteWithCutoff10() {
		
		return new BlackAndWhiteZoomMatrixColoringStrategy(10);
		
	}


}
