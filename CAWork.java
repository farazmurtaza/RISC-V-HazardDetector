package cawork;

import java.util.Scanner;

public class CAWork {


    public static void main(String[] args) {
        System.out.println("ENTER NUMBER OF INSTRUCTION");
        Scanner s=new Scanner(System.in);
        Scanner m=new Scanner(System.in);
        //input number of instructions
        int n=s.nextInt();
        String []instruction=new String[n];//array of instructions
        String []function=new String[n];//array of extracted functions from instructions
        String []destination=new String[n];//array of extracted destinations from instructions
        String []source1=new String[n];//array of extracted sources1 from instructions
        String []source2=new String[n];//array of extracted sources2 from instructions
        boolean [] datacount=new boolean[n];//array to determine whether datahazard on any instruction has already occured or not?
        int []registers=new int[32];//array to keep track of register values after each instruction
        for(int i=0; i<32;i++){
            registers[i]=0;//assuming each register to be equal to 0 initially
        }
        for (int i = 0; i < n; i++) {
            
            datacount[i]=false;

        }
        for (int i = 0; i < n; i++) {
            
            instruction[i]=m.nextLine();//instruction input

        }
        //for loop to separate functions, destinations, source1, source2
        for(int i=0;i<n;i++)
        {
            function[i] = "";
            destination[i]="";
            source1[i]="";
            source2[i]="";
            int j=0;
            while(instruction[i].charAt(j)!=' ')
            {
                function[i]=function[i]+instruction[i].charAt(j);
                j++;
            }
            j++;
            while(instruction[i].charAt(j)!=',')
            {
                if(instruction[i].charAt(j)!='('&&instruction[i].charAt(j)!=')'){
                    destination[i]=destination[i]+instruction[i].charAt(j);
                }
                j++;
            }
            j++;
             while(instruction[i].charAt(j)!=',')
            {
                if(instruction[i].charAt(j)!='('&&instruction[i].charAt(j)!=')'){
                    source1[i]=source1[i]+instruction[i].charAt(j);
                }
                j++;
                if(j>=instruction[i].length()){
                    break;
                }
            }
            j++;
             while(j<instruction[i].length())
            {
                if(instruction[i].charAt(j)!='('&&instruction[i].charAt(j)!=')'){
                source2[i]=source2[i]+instruction[i].charAt(j);
                }
                j++;
            }

        }


        for(int i=0;i<n;i++)
        {  
            int n1=0;
            int n2=0;
            int n3=0;
            int n4=0;
            String str="";
            String str1="";
            String str2="";
            String str3="";
            String str4="";
            //conditions and operations to keep track of values of each register after the execution of each instruction
            if(function[i].equals("lw")||function[i].equals("sw")||function[i].equals("add")||function[i].equals("sub")||function[i].equals("and")||function[i].equals("or")){
            for(int x=1; x<destination[i].length();x++)
            {
                str=str+destination[i].charAt(x);
            }
            int regno=Integer.parseInt(str);
            regno--;
            
            if(source1[i].charAt(0)=='r'){
                for(int q=1;q<source1[i].length();q++){
                    str1=str1+source1[i].charAt(q);
                }
                n1=registers[Integer.parseInt(str1)-1];
            }
            if(source2[i].isEmpty()){}else{
            if(source2[i].charAt(0)=='r'){
                for(int q=1;q<source2[i].length();q++){
                    str2=str2+source2[i].charAt(q);
                }
                n2=registers[Integer.parseInt(str2)-1];
            }
            }
                if(source1[i].charAt(0)=='#'){
                for(int q=1;q<source1[i].length();q++){
                    str3=str3+source1[i].charAt(q);
                }
                n3=Integer.parseInt(str3);
            }
                if(source2[i].isEmpty()){}else{
                if(source2[i].charAt(0)=='#'){
                for(int q=1;q<source2[i].length();q++){
                    str4=str4+source2[i].charAt(q);
                }
                n4=Integer.parseInt(str4);
            }
            }
            if(function[i].equals("add")||function[i].equals("lw")){
            registers[regno]=n1+n2+n3+n4;
            }else{
                if(function[i].equals("sub")){
                    registers[regno]=(n1+n3)-(n2+n4);
                }else{
                    if(function[i].equals("and")){
                        registers[regno]=(n1+n3) & (n2+n4);
                    }else{
                        if(function[i].equals("or")){
                            registers[regno]=(n1+n3) | (n2+n4);
                        }
                    }
                }
            }
            
            }

            
            
            
            
            
            
            int regno1=0;
            if(function[i].equals("lw"))
                {
                    //condition to check structural hazard if alu instructions occur after load instruction
                    if(i+1<n){
                        if(function[i+1].equals("add")||function[i+1].equals("sub")||function[i+1].equals("and")||function[i+1].equals("or")){
                            System.out.println("Structural Hazard at line = "+(i+2)+" due to overlapping of WB stages with instruction line = "+(i+1));
                        }
                    }
                    //condition to check structural hazard if 3rd next instruction after load is alu instruction
                    if(i+3<n){
                        if(function[i+3].equals("lw")||function[i+3].equals("sw")||function[i+3].equals("add")||function[i+3].equals("sub")||function[i+3].equals("and")||function[i+3].equals("or")){
                            System.out.println("Structural Hazard at line = "+(i+4)+" due to overlapping of Memory Access stage with Instruction fetch stage of instruction line = "+(i+1));
                        }
                    }
                    /*condition to check structural hazard if 3rd next instruction after load is alu and that alu instruction 
                    uses a source register same as destination register of load instruction*/
                    if(i+3<n){
                        if(function[i+3].equals("lw")||function[i+3].equals("add")||function[i+3].equals("sub")||function[i+3].equals("and")||function[i+3].equals("or")){
                            if(destination[i].equals(source1[i+3])||destination[i].equals(source2[i+3])){
                                System.out.println("Structural Hazard at line = "+(i+4)+" due to overlapping of Write Back stage with Instruction Decode stage of instruction line = "+(i+1));
                            }
                        }
                    }
                    //conditions to check if data hazard occurs due to use of same register in source in next 3 instruction
                    for(int k=i+1;k<i+4&&k<n;k++){
                        if(function[k].equals("lw")||function[k].equals("sw")||function[k].equals("add")||function[k].equals("sub")||function[k].equals("and")||function[k].equals("or")){
                            if(destination[i].equals(source1[k])){
                                if(datacount[k]==false){
                                    System.out.println("Load Delay at line = "+(k+1)+" due to register "+source1[k]);
                                    datacount[k]=true;
                                }
                            }
                            if(destination[i].equals(source2[k])){
                                if(datacount[k]==false){
                                    System.out.println("load delay at line = "+(k+1)+" due to register "+source2[k]);
                                    datacount[k]=true;
                                }
                            }
                        }
                    }
                }
            
            if(function[i].equals("add")||function[i].equals("sub")||function[i].equals("and")||function[i].equals("or")){
                if(i+2<n){
                    if(function[i+2].equals("lw")||function[i+2].equals("sw")||function[i+2].equals("add")||function[i+2].equals("sub")||function[i+2].equals("and")||function[i+2].equals("or")){
                        if(destination[i].equals(source1[i+2])||destination[i].equals(source2[i+2])){
                            System.out.println("Structural Hazard at line = "+(i+3)+" due to overlapping of Write Back stage with Instruction Decode stage of instruction line = "+(i+1));
                        }
                    }
                }
                //conditions to check if data hazard occurs due to use of same register in source in next 2 instruction
                    for(int k=i+1;k<i+3&&k<n;k++){
                        {
                        if(function[k].equals("lw")||function[k].equals("sw")||function[k].equals("add")||function[k].equals("sub")||function[k].equals("and")||function[k].equals("or")){
                            if(destination[i].equals(source1[k])){
                                if(datacount[k]==false){
                                System.out.println("Raw Hazard at line = "+(k+1)+" due to register "+source1[k]);
                                datacount[k]=true;
                            }
                        }
                            if(destination[i].equals(source2[k])){
                                if(datacount[k]==false){
                                System.out.println("Raw Hazard at line = "+(k+1)+" due to register "+source2[k]);
                                datacount[k]=true;
                            }
                        }
                       }
                    }
                    }
                }
            if(function[i].equals("beq"))
            {
            //conditions to fetch values of used operands in branch condition to check whether control hazard occurs or not
            int n7=0;
            int n8=0;
            String str6="";
            String str7="";
            String str8="";
            for(int x=1; x<destination[i].length();x++)
            {
                str6=str6+destination[i].charAt(x);
            }
            regno1=registers[Integer.parseInt(str6)-1];
            if(source1[i].charAt(0)=='r'){
                for(int q=1;q<source1[i].length();q++){
                    str7=str7+source1[i].charAt(q);
                }
                n7=registers[Integer.parseInt(str7)-1];
            }
            if(source1[i].charAt(0)=='#'){
                for(int q=1;q<source1[i].length();q++){
                    str8=str8+source1[i].charAt(q);
                }
                n8=Integer.parseInt(str8);
            }
            if(regno1==(n7+n8)){
                System.out.println("Control Hazard at line = "+(i+1)+" because "+destination[i]+" and "+source1[i]+" are equal");
            }
            }
            if(function[i].equals("bneq"))
            {
            int n7=0;
            int n8=0;
            String str6="";
            String str7="";
            String str8="";
            for(int x=1; x<destination[i].length();x++)
            {
                str6=str6+destination[i].charAt(x);
            }
            regno1=registers[Integer.parseInt(str6)-1];
            if(source1[i].charAt(0)=='r'){
                for(int q=1;q<source1[i].length();q++){
                    str7=str7+source1[i].charAt(q);
                }
                n7=registers[Integer.parseInt(str7)-1];
            }
            if(source1[i].charAt(0)=='#'){
                for(int q=1;q<source1[i].length();q++){
                    str8=str8+source1[i].charAt(q);
                }
                n8=Integer.parseInt(str8);
            }
            if(regno1!=(n7+n8)){
                System.out.println("Control Hazard at line = "+(i+1));
            }
            }
        }
        }
    
        
    }

    
    

