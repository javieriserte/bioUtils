package fileformats.readers.clustal;

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

import fileformats.fastaIO.Pair;

public class ClustalReader {

	
	public List<Pair<String,String>> readClustal(BufferedReader in) {
		

		String currentline = null;
		
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		
		Map<String,StringBuilder> temp =new HashMap<String, StringBuilder>();
		
		List<String> order = new ArrayList<String>();
		
		try {
			while ( (currentline = in.readLine()) != null) {
				
				if (!currentline.trim().equals("")) {
				
					String[] elem = currentline.split(" +");
					
					if (elem.length==2) {
					
						if (temp.containsKey(elem[0])) {
							
							temp.get(elem[0]).append(elem[1]);
							
						} else {
							
							temp.put(elem[0], new StringBuilder(elem[1]));
							
							order.add(elem[0]);
							
						}
						
					} else {
						
						System.err.println("error en la linea: ");
						System.err.println(currentline);

					}
				
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String string : order) {
			
			result.add(new Pair<String,String>(string, temp.get(string).toString()) );
			
		}
		
		return result;
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Step One : Create the parser
		
		Parser parser = new Parser();
		
		// Step Two : Add Command Line Options
		
		SingleOption inputStreamOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption printStreamOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		

		// Step Three : Try to parse the command line
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.err.println("There was a error parsing the command line");
			System.exit(1);
		}
		
		
		// Program Itself
		
		if (inputStreamOpt.getValue()!= null) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)inputStreamOpt.getValue()));
		PrintStream out = (PrintStream) printStreamOpt.getValue();
		
		List<Pair<String,String>> a = (new ClustalReader()).readClustal(in);
		
		for (Pair<String, String> pair : a) {
			
			out.println(">" + pair.getFirst());
			out.println(pair.getSecond());
			
			
		}
		}

	}

}
