import java.util.Scanner;
import java.lang.Math;

class DataEncryptionStd
{

    public String k1,k2;
    public String input,output,lh,rh;
    final int[][] S0 = { {1,0,3,2} , {3,2,1,0} , {0,2,1,3} , {3,1,3,2} } ;
    final int[][] S1 = { {0,1,2,3},  {2,0,1,3}, {3,0,1,0}, {2,1,0,3}} ;

    public DataEncryptionStd()
    {
        k1="";
        k2="";
        lh="";
        rh="";
        input="";
        output="";
    }

    public void generateKeys(String k0)
    {
        int []p10 = {2,4,1,6,3,9,0,8,7,5};
        k1 = permute(k0, p10); 
        for(int i=0;i<5;i++)
        {
            lh = lh+k1.charAt(i);
            rh = rh+k1.charAt(i+5);
        }
        lh = Shift(lh);
        rh = Shift(rh);
        k1 = "";
        k1 = lh+rh;
        int []p8 = {5,2,6,3,7,4,9,8} ;    // 6 3 7 4 8 5 10 9
        k1 = permute(k1, p8);
        System.out.println("The Key k1 is : "+k1);


        k2 = permute(k0,p10);
        lh = Shift(lh);
        lh = Shift(lh);
        rh = Shift(rh);
        rh = Shift(rh);
        k2 = "";
        k2 = lh+rh;
        k2 = permute(k2,p8);
        System.out.println("The Key k2 is : "+k2);

    }

    public String permute(String key, int []order)
    {
        int n = order.length;
        String temp="";
        for(int i=0;i<n;i++)
        {
            temp = temp + key.charAt(order[i]);
        }
        return temp;
    }

    public String Shift(String half)
    {
        String temp="";
        for(int i=1;i<5;i++)
        {
            temp=temp+half.charAt(i);
        }
        temp = temp+half.charAt(0);
        return temp;
    }

   
    
    
    
    public String xorFunction(String k1, String expanded)
    {
        String temp = "";
        int n = k1.length();
        for(int i=0;i<n;i++)
        {
            if(k1.charAt(i) != expanded.charAt(i))
            {
                temp += "1";
            }
            else
            {
                temp += "0";
            }
        }
        return temp;
    }

    public static String decimalToBinary(int decimal)
    {
        String binary="";
        while(decimal != 0) {
            binary = (decimal % 2 == 0 ? "0" : "1") + binary; 
            decimal = decimal/2;
        }
        while(binary.length() < 2){
            binary = "0" + binary;
        }
        return binary;
    }

    public static int binaryToDecimal(String binary)
    {
        int decimal = 0;
        int counter = 0;
        int size = binary.length();
        for(int i = size-1; i >= 0; i--)
        {
            if(binary.charAt(i) == '1'){
                decimal += Math.pow(2, counter);
            }
            counter++;
        }
        return decimal;
    }


    public String sbox0(String s)
    {
        String temp = "";

        String row = "";
        row += s.charAt(0);
        row +=s.charAt(3);
        
        String col = "";
        col +=s.charAt(1);
        col +=s.charAt(2);

        int sb00 = binaryToDecimal(row);
        int sb01 = binaryToDecimal(col);

        //System.out.println("ROW : " + sb00);
        //System.out.println("COL : " + sb01);
        
        System.out.println("Sbox 0 output : "+S0[sb00][sb01]);
        temp = decimalToBinary(S0[sb00][sb01]);
        System.out.println("Sbox 0 output : "+temp);
        
    
        return temp;
    }

    public String sbox1(String s)
    {
        String temp = "";

        String row = "";
        row += s.charAt(0);
        row +=s.charAt(3);
        
        String col = "";
        col +=s.charAt(1);
        col +=s.charAt(2);

        int sb00 = binaryToDecimal(row);
        int sb01 = binaryToDecimal(col);

        //System.out.println("ROW : " + sb00);
        //System.out.println("COL : " + sb01);

        System.out.println("Sbox 1 output : "+S1[sb00][sb01]);
      
        temp = decimalToBinary(S1[sb00][sb01]);
        System.out.println("Sbox 1 output : "+temp);
        
    
        return temp;
    }

    public String encrypt(String pt)
    {
        String lip="", rip="";
        int []ip = {1,5,2,0,3,7,4,6};     //2 6 3 1 4 8 5 7
        pt = permute(pt,ip);
        System.out.println("After initial permutation text : "+pt);
    
        for(int i=0;i<4;i++)
        {
            lip = lip+pt.charAt(i);
            rip = rip+pt.charAt(i+4);
        }

        System.out.println("Right half : " + rip);
        System.out.println("Left half : " + lip);
        
        String f1_out = FunctionF(lip, rip, 1);

        lh="";
        rh = "";
        for(int i=0;i<4;i++)
        {
            lh += f1_out.charAt(i);
            rh += f1_out.charAt(i+4);
        }

        String f2_out = FunctionF(rh,lh,2);

        int InvIPArray[]={3,0,2,4,6,1,7,5};
        String invPerm = permute(f2_out,InvIPArray);
        System.out.println("After Inverse Permutation the CIPHER TEXT is : "+invPerm);
        return invPerm;
    }

    public String FunctionF(String linput, String rinput, int key)
    {

        int []exp_order={3,0,1,2,1,2,3,0};    //4 1 2 3 2 3 4 1
        int i;
        int P4[]={1,3,2,0};          //2 4 3 1
        String exp_out="";
        String xor_out = "";
        String lexor="", rexor="";
        String sbox0_out="", sbox1_out="";
        String sbox_out;
        String p4_out="";
        String fk_out="";
        String Main_out="";
        String k = "";

        if(key == 1)
        {
            k = k1;
        }
        else
        {
            k = k2;
        }


        for (i=0;i<8;i++)
        {
            exp_out +=rinput.charAt(exp_order[i]);
        }
        System.out.println("After expansion right half : " + exp_out);
        
        xor_out = xorFunction(exp_out, k);
        System.out.println("After xor : " + xor_out);

        for(i=0;i<4;i++)
        {
            lexor += xor_out.charAt(i);
            rexor += xor_out.charAt(i+4);
        }

        //sbox_out = sBoxes(xor_out);
        sbox0_out = sbox0(lexor);
        sbox1_out = sbox1(rexor);

        sbox_out = sbox0_out + sbox1_out;
        System.out.println("Output of Sbox is  :" + sbox_out);

        p4_out = permute(sbox_out,P4);
        System.out.println("P4 on Sbox Output : "+p4_out);

        fk_out = xorFunction(p4_out, linput);

        Main_out += fk_out;
        Main_out += rinput;


        System.out.println("Output of function is : "+Main_out);
        return Main_out;
    }


    public void decrypt(String pt)
    {
        String lip="", rip="";
        int []ip = {1,5,2,0,3,7,4,6};     //2 6 3 1 4 8 5 7
        pt = permute(pt,ip);
        System.out.println("After initial permutation text : "+pt);
    
        for(int i=0;i<4;i++)
        {
            lip = lip+pt.charAt(i);
            rip = rip+pt.charAt(i+4);
        }

        System.out.println("Right half : " + rip);
        System.out.println("Left half : " + lip);
        
        String f1_out = FunctionF(lip, rip, 2);

        lh="";
        rh = "";
        for(int i=0;i<4;i++)
        {
            lh += f1_out.charAt(i);
            rh += f1_out.charAt(i+4);
        }

        String f2_out = FunctionF(rh,lh,1);

        int InvIPArray[]={3,0,2,4,6,1,7,5};
        String invPerm = permute(f2_out,InvIPArray);
        System.out.println("After Inverse Permutation the PLAIN TEXT is : "+invPerm);
    }
    

    public static void main(String []args)
    {
        Scanner sc=new Scanner(System.in);
        String plainText;
        String k0;
        DataEncryptionStd d = new DataEncryptionStd() ;

        System.out.println("Enter the 10 bit key");
        k0 = sc.nextLine();
        d.generateKeys(k0);
        
        System.out.println("Enter the 8-bit Plain Text");
        plainText = sc.nextLine();

        System.out.println("Encrypting....");
        String encryptedText = d.encrypt(plainText);

        System.out.println("Decrypting....");
        d.decrypt(encryptedText);

        sc.close();

    }
}




//output

/*Enter the 10 bit key
1100011110
The Key k1 is : 11101001
The Key k2 is : 10100111
Enter the 8-bit Plain Text
00101000
Encrypting....
After initial permutation text : 00100010
Right half : 0010
Left half : 0010
After expansion right half : 00010100
After xor : 11111101
Sbox 0 output : 2
Sbox 0 output : 10
Sbox 1 output : 0
Sbox 1 output : 00
Output of Sbox is  :1000
P4 on Sbox Output : 0001
Output of function is : 00110010
After expansion right half : 10010110
After xor : 00110001
Sbox 0 output : 2
Sbox 0 output : 10
Sbox 1 output : 2
Sbox 1 output : 10
Output of Sbox is  :1010
P4 on Sbox Output : 0011
Output of function is : 00010011
After Inverse Permutation the CIPHER TEXT is : 10001010
Decrypting....
After initial permutation text : 00010011
Right half : 0011
Left half : 0001
After expansion right half : 10010110
After xor : 00110001
Sbox 0 output : 2
Sbox 0 output : 10
Sbox 1 output : 2
Sbox 1 output : 10
Output of Sbox is  :1010
P4 on Sbox Output : 0011
Output of function is : 00100011
After expansion right half : 00010100
After xor : 11111101
Sbox 0 output : 2
Sbox 0 output : 10
Sbox 1 output : 0
Sbox 1 output : 00
Output of Sbox is  :1000
P4 on Sbox Output : 0001
Output of function is : 00100010
After Inverse Permutation the PLAIN TEXT is : 00101000
*/
