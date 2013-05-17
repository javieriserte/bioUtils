package seqManipulation.fastamanipulator.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.StringParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class FastaManipulatorClient {
	
	public static void main(String[] args) throws IOException {
		
		Parser parser = new Parser();

		SingleOption addAlOpt = new SingleOption(parser, null , "-add", InFileParameter.getParameter());
		
		SingleOption addNameOpt= new SingleOption(parser, null, "-name", StringParameter.getParameter());
		
		SingleOption retAlOpt = new SingleOption(parser, null , "-ret", StringParameter.getParameter());
		
		SingleOption retSeqOpt = new SingleOption(parser, null , "-rseq", StringParameter.getParameter());
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e1) {
			e1.printStackTrace();
		}
		
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;

    try {
        socket = new Socket("localhost", 9999);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
    } catch (UnknownHostException e) {
        System.err.println("Don't know about host.");
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to host.");
        System.exit(1);
    }

    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    String fromServer;
    
    while ((fromServer = in.readLine()) != null) {
    	
    	if (fromServer.equals("CONNECTED")) {
    		
    		break;
    		
    	}
     }
    
    if (addAlOpt.isPresent() && addNameOpt.isPresent()) {
    	
    	File infile = (File)addAlOpt.getValue();
    	
    	FastaMultipleReader fmr = new FastaMultipleReader();
    	
    	List<Pair<String, String>> fastas = fmr.readFile(infile);

    	System.out.println("Uploading "+ fastas.size() + " sequences.");

		out.println("ADD "+ ((String)addNameOpt.getValue()));
		
		String r = in.readLine();
		
		if (!r.startsWith("OK")) {
			
			System.err.println("Protocol not recognized");

		    out.close();
		    in.close();
		    stdIn.close();
		    socket.close();
		    
		    System.exit(1);
			
		}
		
    	for (Pair<String, String> pair : fastas) {
    		
    		System.out.println("Uploaded " + pair.getFirst());
    		out.println(">"+pair.getFirst());
    		r = in.readLine();
    		
    		if (!r.startsWith("OK")) {
    			
    			System.err.println("Protocol not recognized");

    		    out.close();
    		    in.close();
    		    stdIn.close();
    		    socket.close();
    		    
    		    System.exit(1);
    			
    		}
    		
    		out.println(pair.getSecond());
    		System.out.println("Uploaded " + pair.getFirst()) ;
    		r = in.readLine();
    		
    		if (!r.startsWith("OK")) {
    			
    			System.err.println("Protocol not recognized");

    		    out.close();
    		    in.close();
    		    stdIn.close();
    		    socket.close();
    		    
    		    System.exit(1);
    			
    		}
    		
		}
    	
		out.println("DONE");
		System.out.println("Finished upload") ;
	    r = in.readLine();
		
		if (!r.startsWith("OK")) {
			
			System.err.println("Protocol not recognized");

		    out.close();
		    in.close();
		    stdIn.close();
		    socket.close();
		    
		    System.exit(1);
			
		}
    	
    } else {
    	
    }

    out.close();
    in.close();
    stdIn.close();
    socket.close();
}

}
