package utils.mutualinformation.misticmod.tocytoscape.labelers;

import io.onelinelister.OneLineListReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA2.returnvalues.ReturnValueParser;

public class NodeLabelerValue extends ReturnValueParser<NodeLabeler> {

	@Override
	public NodeLabeler parse(String token) {
		
		if (token.trim().isEmpty()) {
			
			return new NumericNodeLabeler();
			
		} else {
			
			File file = new File(token.trim());
			
			if (file.exists()) {
				
				Map<Integer,String> labelsMap = new HashMap<Integer, String>();
				
				List<String> lines = OneLineListReader.createOneLineListReaderForString().read(file);
				
				for (String line : lines) {
					
					String[] values = line.split("\\s+");
					
					if (values.length == 2 ) {
						
						int key = Integer.valueOf(values[0]);
						
						String value = values[1];
						
						labelsMap.put(key, value);						
					}
					
				}
				
				return new CustomNodeLabeler(labelsMap);
				
			}
			
		}
		
		return new NumericNodeLabeler();
		
	}

}
