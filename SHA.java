import java.util.Scanner;

public class SHA{	
	String h0 = "01100111010001010010001100000001";
	String h1 = "11101111110011011010101110001001";
	String h2 = "10011000101110101101110011111110";
	String h3 = "00010000001100100101010001110110";
	String h4 = "11000011110100101110000111110000";
	String A = "", B = "", C = "", D = "", E = "", F = "", K = "";
	String msg = "", binaryCode = "";
	String hashedMsg = "";

	String[] wordChunks = new String[80];
	String[] chunks;	
	
	public SHA(String msg){
		this.msg = msg;
		
		for(int i = 0; i < msg.length(); i++){
			int ascii = msg.charAt(i);
			binaryCode += toBinary(ascii, 8);
			//System.out.println("ASCII: " + ascii + " binaryCode: " + binaryCode);
		}
		
		binaryCode += "1";//step 5
		//System.out.println("binaryCode: " + binaryCode);
		
		//step 6
		while(binaryCode.length() % 512 != 448){
			binaryCode += "0";
		}
		//step 6.1
		String origMsgLength = "";
		origMsgLength = Long.toBinaryString((long)msg.length()*8);
		
		for(int j = 0; j < 64 - origMsgLength.length(); j++){
			binaryCode += "0";
		}
		
		binaryCode += origMsgLength;
		//System.out.println("binaryCode: " + binaryCode);
		
		//step 7
		breakToChunks(binaryCode);
		
		//step 10
		A = h0;
		B = h1;
		C = h2;
		D = h3;
		E = h4;
		
		//step 11
		for(int i = 0; i < 80; i++){
			if(i < 20){
				F = getLogicalValue(getLogicalValue(B, C, "AND"), getLogicalValue(not(B), D, "AND"), "OR");
				K = "01011010100000100111100110011001";
			}else if(i < 40){
				F = getXORvalue(getXORvalue(B, C), D);
				K = "01101110110110011110101110100001";
			}else if(i < 60){
				F = getLogicalValue(getLogicalValue(getLogicalValue(B, C, "AND"), getLogicalValue(B, D, "AND"), "OR"), getLogicalValue(C, D, "AND"), "OR");
				K = "10001111000110111011110011011100";
			}else if(i < 80){
				F = getXORvalue(getXORvalue(B, C), D);
				K = "11001010011000101100000111010110";
			}
			
			String temp = "";
			temp = Long.toBinaryString((long)Long.parseLong(Long.toBinaryString((long)Long.parseLong(Long.toBinaryString((long)Long.parseLong(Long.toBinaryString((long)Long.parseLong(leftRotate(A, 5), 2) + Long.parseLong(F, 2)), 2) + Long.parseLong(E, 2)), 2) + Long.parseLong(K, 2)), 2) + Long.parseLong(wordChunks[i], 2));
			temp = truncate(temp);
			//System.out.println("temp: " + temp);

			E = D;
			D = C;
			C = leftRotate(B, 30);
			B = A;
			A = temp;
		}
		
		//step 12
		System.out.println();
		
		//h0 = h0 + A;
		h0 = Long.toBinaryString((long)Long.parseLong(h0, 2) + Long.parseLong(A, 2));
		h0 = truncate(h0);
		h0 = Long.toHexString((long)Long.parseLong(h0, 2));
		//System.out.println("h0: " + h0);
		
		//h1 = h1 + B;
		h1 = Long.toBinaryString((long)Long.parseLong(h1, 2) + Long.parseLong(B, 2));
		h1 = truncate(h1);
		h1 = Long.toHexString((long)Long.parseLong(h1, 2));
		//System.out.println("h1: " + h1);
		
		//h2 = h2 + C;
		h2 = Long.toBinaryString((long)Long.parseLong(h2, 2) + Long.parseLong(C, 2));
		h2 = truncate(h2);
		h2 = Long.toHexString((long)Long.parseLong(h2, 2));
		//System.out.println("h2: " + h2);
		
		//h3 = h3 + D;
		h3 = Long.toBinaryString((long)Long.parseLong(h3, 2) + Long.parseLong(D, 2));
		h3 = truncate(h3);
		h3 = Long.toHexString((long)Long.parseLong(h3, 2));
		//System.out.println("h3: " + h3);
		
		//h4 = h4 + E;
		h4 = Long.toBinaryString((long)Long.parseLong(h4, 2) + Long.parseLong(E, 2));
		h4 = truncate(h4);
		h4 = Long.toHexString((long)Long.parseLong(h4, 2));
		//System.out.println("h4: " + h4);
		
		hashedMsg = h0 + h1 + h2 + h3 + h4;
	}
	
	public static void menu(){
		char choice = '\0';
		int num = 0;
		String msg = "", no = "";
		
		Scanner input = new Scanner(System.in);
		System.out.println();
		System.out.println();
		System.out.println("	SHA-1 \n1. Enter a string\n2. Enter a set of strings\n3. Exit");
		System.out.print("User choice: ");
		choice = input.next().charAt(0);
		
		if(choice == '1'){
			input = new Scanner(System.in);
			System.out.print("Enter string: ");
			msg = input.nextLine();

			SHA sha = new SHA(msg);
			System.out.println();
			System.out.println("STRING: " + msg);
			System.out.println("DIGEST: " + sha.getDigest());
			System.out.println();
			System.out.println("----------Encryption Successfully Ended----------");
			menu();
		}else if(choice == '2'){
			String[] string;
			String[] hashed;
			
			boolean correctInput = false;
			while(!correctInput){
				input = new Scanner(System.in);
				System.out.print("Enter number of strings to be digested: ");
				no = input.nextLine();
				
				try{
					Integer.parseInt(no);
					correctInput = true;
				}catch(Exception e){
					System.out.println();
					System.out.println("INVALID INPUT");
					
				}
			}
			
			num = Integer.parseInt(no);
			
			string = new String[num];
			hashed = new String[num];
			
			input = new Scanner(System.in);
			for(int i = 0; i < num; i++){
				System.out.print("Enter string " + (i+1) + ": ");
				msg = input.nextLine();
				string[i] = msg;
				SHA sha = new SHA(msg);
				hashed[i] = sha.getDigest();
			}
			
			for(int j = 0; j < num; j++){
				System.out.println("STRING: " + string[j]);
				System.out.println("DIGEST: " + hashed[j]);
				System.out.println();
			}

			System.out.println("----------Encryption Successfully Ended----------");
			menu();
		}else if(choice == '3'){
			System.exit(0);
		}else{
			System.out.println("INVALID CHOICE!");
			menu();
		}
	}
	
	public String toBinary(int num, int bitLength){
		String binary = "", temp = "";
		
		for(int i = 0; i < bitLength; i++){
			if(num%2 == 0){
				temp += "0";
			}else{
				temp += "1";
			}
			num = num/2;
		}
		
		for(int j = bitLength-1; j >= 0; j--){
			binary += temp.charAt(j);
		}
		return binary;
	}	
	
	//step 7
	public void breakToChunks(String chunkMsg){
		chunks = new String[binaryCode.length()/512];
		int ctr = 0, ctr2 = 512;
		
		for(int i = 0; i < binaryCode.length()/512; i++){
			chunks[i] = "";
			while(ctr < ctr2){
				chunks[i] += binaryCode.charAt(ctr);
				ctr++;
			}
			ctr = ctr2;
			ctr2 += 512;
			breakChunkToWords(chunks[i]);
		}
	}
	//step 8
	public void breakChunkToWords(String chunk){
		int ctr = 0, ctr2 = 32;
		
		for(int j = 0; j < 16; j++){
			wordChunks[j] = "";
			while(ctr < ctr2){
				wordChunks[j] += chunk.charAt(ctr);
				ctr++;
			}
			ctr = ctr2;
			ctr2 += 32;
		}
		//step 9: extend to 80 words
		for(int i = 16; i < 80; i++){
			wordChunks[i] = leftRotate(getXORvalue( getXORvalue( getXORvalue(wordChunks[i-3], wordChunks[i-8]), wordChunks[i-14]),  wordChunks[i-16]), 1);
		}
	}
	//for step 11
	public String getLogicalValue(String str1, String str2, String op){
		String result = "";
		for(int i = 0; i < 32; i++){
			if(op.equals("AND")){
				if((str1.charAt(i) == '1') && (str2.charAt(i) == '1')){
					result += "1";
				}else{
					result += "0";
				}
			}else if(op.equals("OR")){
				if(str1.charAt(i) == '1' || str2.charAt(i) == '1'){
					result += "1";
				}else{
					result += "0";
				}
			}
		}
		return result;
	}

	public String not(String str){
		String notStr = "";
		for(int i = 0; i < 32; i++){
			if(str.charAt(i) == '0'){
				notStr += "1";
			}else{
				notStr += "0";
			}
		}
		return notStr;
	}
	
	public String getXORvalue(String str1, String str2){
		String xorValue = "";
		for(int i = 0; i < 32; i++){
			if(str1.charAt(i) != str2.charAt(i)){
				xorValue += "1";
			}else{
				xorValue += "0";
			}
		}
		return xorValue;
	}
	
	public String leftRotate(String str, int bits){
		return str.substring(bits, str.length()) + str.substring(0, bits);
	}
	
	public String truncate(String str){
		if(str.length() > 32){
			return str.substring(str.length()-32, str.length());
		}else{
			return str;
		}
	}
	
	public String getDigest(){
		return hashedMsg;
	}
	
	public static void main(String[] args){
		menu();
	}
}