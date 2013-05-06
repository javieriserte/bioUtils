package seqManipulation.fastamanipulator.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import fileformats.fastaIO.Pair;

public class FastaManipulatorServer {

	public int port;
	
    boolean listening = true;
    
    public int lastIndex=0;
    
    public List<Socket> sockets = new ArrayList<Socket>(); 
    
    private Map<Integer,FastaManipualtorAlignment> alignments = new HashMap<Integer, FastaManipualtorAlignment>();
    
    private ServerSocket serverSocket = null;
    
	public FastaManipulatorServer(int port) {
		super();
		this.port = port;
	}

	public FastaManipulatorServer() {
		super();
		
		this.port = 9999;
	}
	
    public void startServer(String[] args) throws IOException{
        
    	

        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9999.");
            System.exit(-1);
        }

        while (listening) {
        	
			Socket accept = serverSocket.accept();
			
			this.sockets.add(accept);
			
			new FastaManipulatorThread(accept, this).start();
			
		}

        serverSocket.close();
    }
    
    protected synchronized void stopServer() {
    	
        System.exit(0);
    	
    }
    
    protected synchronized Pair<String,String> getSequence (int align_index, int seq_index) {
    	
    	FastaManipualtorAlignment alignment = null;    	
    	
		if (this.alignments.containsKey(align_index)) {
    		
    		alignment = this.alignments.get(align_index);
    		
    		Pair<String, String> pair = null;
    		
    		if (seq_index<alignment.getAlignment().size() && seq_index >=0) {
    			
    			pair = alignment.getAlignment().get(seq_index);
    		
    		}
    		
    		return pair;
    		
    	}
    	
    	return null;
    }
    
    protected synchronized List<Pair<String,String>> getAlignment (int align_index) {

    	List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
    	
    	FastaManipualtorAlignment alignment = null;    	
    	
		if (this.alignments.containsKey(align_index)) {
    		
    		alignment = this.alignments.get(align_index);
    		
    		for(int seq_index=0; seq_index < alignment.getAlignment().size(); seq_index++) {
    			
    			result.add(alignment.getAlignment().get(seq_index));
    		
    		}
    		
    		return result;
    		
    	}
    	
    	return null;
    }
	
    protected synchronized void addAlignment(String alignmentName, List<Pair<String,String>> alignment) {
    	
    	int currentId = lastIndex;
    	
    	FastaManipualtorAlignment fma = new FastaManipualtorAlignment(currentId, alignmentName, alignment);
    	
    	lastIndex++;
    	
    	alignments.put(currentId, fma);
    	
    }
    
    public synchronized void report(PrintWriter out) {
    	
    	out.println("Alignment summary");
    	
		out.println("================");

		for (int index=0; index<lastIndex ; index++) {
			
			FastaManipualtorAlignment al = this.alignments.get(index);
			
			out.println(al.getId() + " | " + al.getName() + " | Contains " + al.getAlignment().size() + " Sequences");
			
		}
    	
    }
    
    public static void main(String[] args) throws IOException {
    	
    	FastaManipulatorServer fms = new FastaManipulatorServer();
    	
    	fms.startServer(null);
    	
    }
	
}
