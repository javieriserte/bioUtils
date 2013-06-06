package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class NetPicornaResultParser {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IncorrectParameterTypeException 
	 */
	public static void main(String[] args) throws IOException, IncorrectParameterTypeException {
		

		Parser parser = new Parser();
		
		SingleOption infileOpt = new SingleOption(parser, System.in, "-in", InputStreamParameter.getParameter());
		
		SingleOption outfileOpt = new SingleOption(parser, System.out, "-in", PrintStreamParameter.getParameter());
		
		parser.parseEx(args);

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) infileOpt.getValue()));

		PrintStream out = (PrintStream) outfileOpt.getValue();
		
		String currentline = null;
		
		String lastsGi = null;
		
		Map<String,List<NetPicornaResult>> results = new HashMap<String, List<NetPicornaResult>>();
		
		NetPicornaResultParser nprParser = new NetPicornaResultParser();
		
		while ((currentline = in.readLine())!= null) {
			
			String gi = nprParser.tryToGetGIFrom(currentline);
			
			if (!gi.equals("")) {
				
				lastsGi=gi;
				
				results.put(lastsGi, new ArrayList<NetPicornaResult>());
				
			} else {
				
				NetPicornaResult result = nprParser.getResult(currentline);
				
				List<NetPicornaResult> currentlist = results.get(lastsGi);
				
				if(!currentlist.contains(result)) {
					
					if (result.score>= 0.75)
					
					currentlist.add(result);
					
				}
				
			}
			
		}
		
		for (String gi : results.keySet()) {
			
			out.println(gi + " | " + results.get(gi).size()); 
			
		} 
		
	}

	private NetPicornaResult getResult(String currentline) {

		String[] data = currentline.split("\t");
		
		NetPicornaResult result = new NetPicornaResult();
		
		result.aminoacid = data[1].trim().charAt(0);
		
		result.position = Integer.valueOf(data[2].trim());
		
		result.score = Double.valueOf(data[3].trim());
		
		result.surface = Double.valueOf(data[4].trim());
		
		result.Sequence  = data[5].trim();

		return result;
	}

	private String tryToGetGIFrom(String currentline) {
		
		if (currentline.startsWith(">")) {
			
			String[] data = currentline.split("_");
			
			return data[1];
		}
		
		
		return "";
		
		
	}

}

class NetPicornaResult {

	char aminoacid;
	int position;
	double score;
	double surface;
	String Sequence;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetPicornaResult other = (NetPicornaResult) obj;
		if (position != other.position)
			return false;
		return true;
	}
	
	
	
}
