package pdb.contactmap;

import pdb.contacts.ClosestAtomPairContactCriteria;
import pdb.contacts.ContactCriteria;
import cmdGA2.returnvalues.ReturnValueParser;

public class ContactCriteriaValue extends ReturnValueParser<ContactCriteria> {
	
	////////////////////////////////////////////////////////////////////////////
	// Class Constants
	public static final String CRITERIA_CLOSEST = "closest";
	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public ContactCriteria parse(String token) {
		
		token = token.replaceAll(" ", "");
		String[] data = token.split(","); 
		switch (data[0].toLowerCase()) {
		case CRITERIA_CLOSEST:
			double distance;
			if (data.length>1) {
				distance = Double.valueOf(data[1]);	
			} else {
				distance = 6d; 
			}
			return new ClosestAtomPairContactCriteria(distance);

		default:
			break;
		}
		return null;
	}

}
