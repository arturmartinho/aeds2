import java.util.Scanner;

public static class Is{
	public boolean main(String palavra){
			for(int i = 0; i < palavra.length();i++){
				char letra = palavra.charAt(i);
				if(letra.equals('a') || letra.equals('e') || letra.equals('i') || letra.equals('o') || letra.equals('u')){	
				}
				else{
					return false;
				}
				return true;
		}
	}

}
