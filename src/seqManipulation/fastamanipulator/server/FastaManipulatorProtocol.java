package seqManipulation.fastamanipulator.server;

public class FastaManipulatorProtocol {

	public static String AddAlignment = "ADD";
	public static String RetrSeq = "RETR";
	public static String Report = "REPORT";
	public static String Close = "CLOSE";
	public static String Send = "SEND";
	public static String DoneSending = "DONE";

	public static String BadRequest = "BAD";
	public static String OkRequest = "OK";
	
	
	
	
	public FastaManipulatorProtocolResponse processInput(String line, int state, FastaManipulatorThread t) {
		
		switch (state) {
		case 0:
			
			if (line.startsWith(AddAlignment)) {
				
				if (line.substring(AddAlignment.length()).trim().equals("")) {
					
					return new FastaManipulatorProtocolResponse(0, BadRequest);
					
				}
				
				t.addData(line.substring(AddAlignment.length()).trim());
				
				return new FastaManipulatorProtocolResponse(1, OkRequest);
				
			}
			
			if (line.startsWith(RetrSeq)) {
				
				String param = line.substring(RetrSeq.length()).trim();
				
				if (param.equals("")) {
					
					return new FastaManipulatorProtocolResponse(0, BadRequest+ " Parameters are necessary");
					
				} else {
					
					String[] par = param.split(" ");
					
					if (par.length == 1) {
						
						t.addData(par[0]);
						
						return new FastaManipulatorProtocolResponse(3, OkRequest+ " Retrieving a complete alignment");
						
					} else 
						
					if (par.length ==2 ) {

						t.addData(par[0]);
						
						t.addData(par[1]);
						
						return new FastaManipulatorProtocolResponse(3, OkRequest+ " Retrieving a single sequence alignment");
						
					} else {
						
						return new FastaManipulatorProtocolResponse(0, BadRequest + " Number of parameters is incorrect");
						
					}
					
				}
				
			}
			
			if (line.startsWith(Report)) {
				
				return new FastaManipulatorProtocolResponse(4, OkRequest);
				
			}
			
			if (line.startsWith(Close)) {

				return new FastaManipulatorProtocolResponse(5, OkRequest);
				
			}
			
			break;

		case 1:
			
			if (line.startsWith(DoneSending)) {
				
				return new FastaManipulatorProtocolResponse(2, OkRequest);
				
			} else {
				
				t.addData(line);
				
				return new FastaManipulatorProtocolResponse(1, OkRequest);
				
			}
			
		default:
			return new FastaManipulatorProtocolResponse(state, BadRequest);
		}
		
		return new FastaManipulatorProtocolResponse(state, BadRequest);
		
	}
	
	public class FastaManipulatorProtocolResponse {
		int newState;
		String Message;
		
		public FastaManipulatorProtocolResponse(int newState, String message) {
			super();
			this.newState = newState;
			Message = message;
		}
	}

	
}

