package seqManipulation;

public class Complementary {

	public static String complementary(String sequence) {
		
		StringBuilder result = new StringBuilder(sequence.length());
		for (int i = 0; i< sequence.length(); i++) {
			result.append(complementaryBase(Character.toUpperCase(sequence.charAt(i))));
		}
		
		return result.toString();
		
	}

	public static String reverse(String sequence) {
		
		StringBuilder result = new StringBuilder(sequence.length());
		for (int i = sequence.length()-1; i>=0; i--) {
			result.append(Character.toUpperCase(sequence.charAt(i)));
		}
		
		return result.toString();
		
	}

	public static String reverseComplementary(String sequence) {
		
		StringBuilder result = new StringBuilder(sequence.length());
		for (int i = sequence.length()-1; i>=0; i--) {
			result.append(complementaryBase(Character.toUpperCase(sequence.charAt(i))));
		}
		
		return result.toString();
		
	}

	public static char complementaryBase(char base) {
		char result;
		base = Character.toUpperCase(base);
		switch (base) {
			case 'A': result ='T' ; break;
			case 'C': result ='G' ; break;
			case 'T': result ='A' ; break;
			case 'G': result ='C' ; break;
			case 'W': result ='W' ; break;
			case 'S': result ='S' ; break;
			case 'R': result ='Y' ; break;
			case 'Y': result ='R' ; break;
			case 'K': result ='M' ; break;
			case 'M': result ='K' ; break;
			case 'B': result ='V' ; break;
			case 'D': result ='H' ; break;
			case 'H': result ='D' ; break;
			case 'V': result ='B' ; break;
			case 'N': result ='N' ; break;
			case '-': result ='-' ; break;
			default:  result ='N' ; break;
		};
		return result;
	}
	
}
