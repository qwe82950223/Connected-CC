
public class Property {//class for connect components
	int label;
	int numbPixels;
	int minRow;
	int minCol;
	int maxRow;
	int maxCol;
	
	Property(int number,int maxR, int maxC){//construct
		label=number;//set label
		numbPixels=0;
		minRow=maxR;
		maxRow=0;
		minCol=maxC;
		maxCol=0;
	}
	public void boundingBox(int x,int y){//caculate boundingBox and number pixel
		numbPixels++;
		if(x<minCol){
			minCol=x;
		}
		if(x>maxCol){
			maxCol=x;
		}
		if(y<minRow){
			minRow=y;
		}
		if(y>maxRow){
			maxRow=y;
		}
	}//end boundingBox
}
