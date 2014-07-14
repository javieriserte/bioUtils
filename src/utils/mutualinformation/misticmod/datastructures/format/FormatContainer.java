package utils.mutualinformation.misticmod.datastructures.format;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FormatContainer {
	
	private static Set<Class<? extends FormatMi>> registeredFormats = getSetOfRegisteredFormats(); 
	
	public FormatMi format;
	
	public FormatContainer() {
		super();
		this.format = null;
	}

	public boolean isNullFormat() {
		
		return this.format==null;
		
	}
	
	public void setCorrespondingFormat(String positionLine) {
		
		for (Class<? extends FormatMi> formatClass : registeredFormats) {
			
			try {
				
				FormatMi formatInstance = formatClass.newInstance();
				
				String formatLine = formatInstance.getFormatLine();
				
				if (Pattern.matches(formatLine, positionLine)) {

					this.format = formatInstance;
				
				} 
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} 
		

		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Class Private Methods
	private static Set<Class<? extends FormatMi>> getSetOfRegisteredFormats() {
		Set<Class<? extends FormatMi>> registered = new HashSet<>();
		registered.add(MisticFormatMi.class);
		registered.add(MortemFormatMi.class);
		return registered;
	}
	////////////////////////////////////////////////////////////////////////////
}