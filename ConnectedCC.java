import java.io.PrintWriter;
import java.util.Scanner;

public class ConnectedCC{
	
	int numCC;
	int newLabel;// number of components
	int[][] zeroFramedAry;// 2d array to store the pixel
	int[] neighborAry;
	int[] EQAry;
	int numRows;
	int numCols;
	int minVal;
	int maxVal;
	int newMin;
	int newMax;
	
	public ConnectedCC(Scanner scan,PrintWriter printer,PrintWriter printer3){
		if(scan.hasNext()){//get the first line 
			if(scan.hasNextInt()){//read row, col, min, max number from first line
				numRows=scan.nextInt();
			}
			if(scan.hasNextInt()){
				numCols=scan.nextInt();
			}
			if(scan.hasNextInt()){
				minVal=scan.nextInt();
			}
			if(scan.hasNextInt()){
				maxVal=scan.nextInt();
			}
		}
		
		newMin = maxVal;
		newMax = minVal;
		
		newLabel=0;
		numCC=0;
		
		int EQsize=(numRows*numCols)/4;//initallize EQAry
		EQAry = new int[EQsize];
		for(int x=0;x<EQsize;x++){
			EQAry[x]=x;
		}
		
		zeroFramedAry = new int[numCols+2][numRows+2];//initallize zeroFramedAry
		neighborAry = new int[3];//initallize neighborAry
		
		loadImage(scan);
		zeroFramed();
		
		ConnectCC_Pass1();	
		prettyPrint(printer);
		printEQAry(printer);
		
		ConnectCC_Pass2();
		prettyPrint(printer);
		printEQAry(printer);
		
		manageEQAry();
		printer.println("after manage EQAry");
		printEQAry(printer);
		
		ConnectCC_Pass3(printer3);
		prettyPrint(printer);
	}


	private void loadImage(Scanner scan) {
		int i=1;
		int j=1;
		while(j<numCols+1){
			if(scan.hasNext()){//get every line 
				while(i<numRows+1){
					if(scan.hasNextInt()){//get every number from each line
						zeroFramedAry[j][i]=scan.nextInt();
					}
					i++;
				}
				i=1;
			}
			j++;
		}
	}
	
	private void zeroFramed() {
		for(int k=0;k<numCols+1;k++){
			zeroFramedAry[k][0]=0;
			zeroFramedAry[k][numCols+1]=0;
		}
		for(int l=0;l<numRows+1;l++){
			zeroFramedAry[0][l]=0;
			zeroFramedAry[numRows+1][l]=0;
		}
		
	}
	
	private void loadNeighbors(int x, int y, boolean passOne){
		neighborAry[2]=zeroFramedAry[x][y];//itself
		if(passOne){//for pass 1
			neighborAry[0]=zeroFramedAry[x-1][y];//up neighbor
			neighborAry[1]=zeroFramedAry[x][y-1];//left neighbor
		}
		else{//for pass 2
			neighborAry[0]=zeroFramedAry[x+1][y];//bottom neighbor
			neighborAry[1]=zeroFramedAry[x][y+1];//right neighbor
		}
	}
	
	private void manageEQAry(){
		for(int i=1;i<newLabel+1;i++){
			if(EQAry[i]==i){
				numCC++;
				EQAry[i]=numCC;
			}
			else{
				EQAry[i]=EQAry[EQAry[i]];
			}
		}
	}
	
	private void ConnectCC_Pass1(){
		for(int i=1;i<numCols+1;i++){
			for(int j=1;j<numRows+1;j++){
				if(zeroFramedAry[i][j]>0){
					loadNeighbors(i,j,true);
					
					if(neighborAry[0]==0||neighborAry[1]==0){//when they have at least one zero
						if(neighborAry[0]==0&&neighborAry[1]==0){//when they both zero
							newLabel++;
							zeroFramedAry[i][j]=newLabel;
						}
						
						else{//when they have one zero
							if(neighborAry[0]==0){
									zeroFramedAry[i][j]=neighborAry[1];
							}
							else{
								zeroFramedAry[i][j]=neighborAry[0];
							}
						}
					}
					
					else{// when they are not zero
						if(neighborAry[0]!=neighborAry[1]){//if they are different
							int min=neighborAry[0];
							if(min>neighborAry[1]){
								min=neighborAry[1];
							}
							zeroFramedAry[i][j]=min;
						}
						else{//if they are same
							zeroFramedAry[i][j]=neighborAry[0];
						}
					}
					
				}
			}
		}
		
	}
	
	private void ConnectCC_Pass2(){
		for(int i=numCols;i>0;i--){
			for(int j=numRows;j>0;j--){
				if(zeroFramedAry[i][j]>0){
					loadNeighbors(i,j,false);
					
					if(neighborAry[0]==0||neighborAry[1]==0){//when they have at least one zero
						if(neighborAry[0]!=0){
							if(neighborAry[0]<zeroFramedAry[i][j]){
								EQAry[zeroFramedAry[i][j]]=neighborAry[0];
								zeroFramedAry[i][j]=neighborAry[0];
							}
						}
						
						if(neighborAry[1]!=0){
							if(neighborAry[1]<zeroFramedAry[i][j]){
								EQAry[zeroFramedAry[i][j]]=neighborAry[1];
								zeroFramedAry[i][j]=neighborAry[1];
							}
						}
					}
					
					else{// when they are not zero
						int min=neighborAry[2];
						for(int z=0;z<2;z++){//find min label
							if(min>neighborAry[z]){
								min=neighborAry[z];
							}
						}
						EQAry[zeroFramedAry[i][j]]=min;
						zeroFramedAry[i][j]=min;
					}
					
				}
			}
		}
	}
	
	private void ConnectCC_Pass3(PrintWriter printer){
		Property[] p = new Property[numCC+1];
		for(int x=1;x<numCC+1;x++){
			p[x]= new Property(x,numRows,numCols);
		}
		
		
		for(int i=1;i<numCols+1;i++){//update every pixel to their final label and set property for they
			for(int j=1;j<numRows+1;j++){
				if(zeroFramedAry[i][j]>0){
					zeroFramedAry[i][j]=EQAry[zeroFramedAry[i][j]];
					p[zeroFramedAry[i][j]].boundingBox(i,j);
				}
				if(zeroFramedAry[i][j]<newMin){//get newMin and newMax
						newMin=zeroFramedAry[i][j];
				}
				if(zeroFramedAry[i][j]>newMax){
						newMax=zeroFramedAry[i][j];
				}
			}
		}
		
		for(int z=1;z<numCC+1;z++){
		printer.println("label:"+p[z].label+" numbPixels:"+p[z].numbPixels+" min Row:"+p[z].minRow+" min Col:"+p[z].minCol+" max row:"+p[z].maxRow+" max col:"+p[z].maxCol);
		}
	}
	
	private void prettyPrint(PrintWriter printer){//pretty print the image
		for(int i=1;i<numCols+1;i++){
			for(int j=1;j<numRows+1;j++){
				if(zeroFramedAry[i][j]==0){
					printer.print("  ");
				}
				else{
					printer.print(zeroFramedAry[i][j]+" ");
				}
			}
			printer.println();
		}
	}
	
	
	private void printEQAry(PrintWriter printer){//print EQAry
		printer.print("current EQAry is: ");
		for(int z=1;z<newLabel+1;z++){
			printer.print(EQAry[z]+" ");
		}
		printer.println();
	}
	
	public void printBinaryImage(PrintWriter printer){//print Binary Image
		printer.println(numCols+" "+numRows+" "+newMin+" "+newMax);
		for(int i=1;i<numCols+1;i++){
			for(int j=1;j<numRows+1;j++){
				if(zeroFramedAry[i][j]==0){
					printer.print("0 ");
				}
				else{
					printer.print(zeroFramedAry[i][j]+" ");
				}
			}
			printer.println();
		}
	}
}