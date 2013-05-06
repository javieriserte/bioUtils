package seqManipulation.fastamanipulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

import seqManipulation.fastamanipulator.FastaManipulatorProtocol.FastaManipulatorProtocolResponse;

public class FastaManipulatorThread extends Thread{
	private Socket socket = null;
	private int state = 0;
	private FastaManipulatorServer fastaManipulatorServer;
	private List<String> currentData = new ArrayList<String>();
	
	public FastaManipulatorThread(Socket socket, FastaManipulatorServer fastaManipulatorServer) {

		this.socket = socket;
		
		this.fastaManipulatorServer = fastaManipulatorServer;
		
	}
	
	@Override
	public void run() {
		
		try {
			
			
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(  new InputStreamReader( socket.getInputStream()));

		    String inputLine;
			FastaManipulatorProtocolResponse outputLine;
		    FastaManipulatorProtocol fnp = new FastaManipulatorProtocol();

		    while ((inputLine = in.readLine()) != null) {
		    	
			outputLine = fnp.processInput(inputLine, this.state , this);

			out.println(outputLine.Message);
			
			if (this.state != outputLine.newState) {

				this.changeToState(outputLine.newState,out);
				
			}

		    }
		    out.close();
		    in.close();
		    socket.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void changeToState(int newState, PrintWriter out) {
		
		switch(newState) {
		
			case 1:
				
				this.state = newState;
				
			break;
			
			case 2:
				
				FastaMultipleReader fmr = new FastaMultipleReader();
				
				StringBuilder fasta = new StringBuilder();
				
				String alignmentName = this.currentData.remove(0);
				
				for (String line : this.currentData) {
					
					fasta.append(line + System.getProperty("line.separator"));
					
				}
				
				this.currentData.clear();
				
				String string = fasta.toString();
				
				StringReader stringReader = new StringReader(string);
				
				BufferedReader buffer = new BufferedReader(stringReader);
				
				List<Pair<String, String>> alignment = fmr.readBuffer(buffer);
				
				this.fastaManipulatorServer.addAlignment(alignmentName, alignment);
			
				this.state = 0;
				
				break;
				
			case 3: 
				
				int align_index = -1;
				
				int seq_index = -1;
				
				align_index = Integer.valueOf(this.currentData.remove(0));
				
				if(this.currentData.size()==1) {

					seq_index = Integer.valueOf(this.currentData.remove(0));
					
					Pair<String, String> pair = this.fastaManipulatorServer.getSequence(align_index, seq_index);
					
					if (pair != null) {
					
						out.println(">" + pair.getFirst());
	
						out.println(pair.getSecond());
					
					}

				} else {
					
					List<Pair<String, String>> list = this.fastaManipulatorServer.getAlignment(align_index);
					
					if (list!= null) {
					
						for (Pair<String, String> pair : list) {
	
							out.println(">" + pair.getFirst());
	
							out.println(pair.getSecond());
							
						}
					
					}
					
				}
				
				break;
			
			case 4:
				
				this.fastaManipulatorServer.report(out);
				
				break;
				
				
				
		}
		
	}

	public void addData(String data) {
		this.currentData.add(data);
	}
	
}
