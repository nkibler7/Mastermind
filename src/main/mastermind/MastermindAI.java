package main.mastermind;
import java.awt.Color;


public class MastermindAI {
	int[] colorOrder;
	int[] correctColors;
	int[] code;
	int turn =0;
	int correctFound =0;
	int missingColor = 0;
	int colorMarker =0;
	int spotMarker = 0;
	boolean openSpot=false;
	//1=green 2=blue 3=yellow 4=orange 5=red
	public MastermindAI(){
		correctColors = new int[4];
		colorOrder = new int[]{1,2,3,4,5};
		code = new int[4];

		changeOrder();
		changeOrder();
		changeOrder();
		changeOrder();
		changeOrder();
		changeOrder();

	}

	public Color[] guess(int black, int white){
		int[] guess = new int[4];

		if(correctFound<4 &&turn!=0){
			if(black ==0){
				missingColor = colorOrder[turn-1];
			}else{
				for(int i = correctFound;i<correctFound+black;i++){

					correctColors[i]=colorOrder[turn-1];

				}
				correctFound+=black;
				if(correctFound == 4 && missingColor==0){
					missingColor=colorOrder[turn];
				}
			}
		}
		if(correctFound<4){
			if(turn<4){
				for (int i = 0; i < guess.length; i++) {
					guess[i]=colorOrder[turn];
				}
			}else{
				guess=nextSpot();
			}
		}else{
			if(black == 1 && !(spotMarker==0&&colorMarker==0)){
				code[spotMarker -1]= correctColors[colorMarker];
				colorMarker++;
				if(colorMarker<3 && correctColors[colorMarker]==correctColors[colorMarker-1]){
					spotMarker++;
				}else{
					spotMarker=0;
				}





			}


			if(colorMarker==3){
				for (int i = 0; i < code.length;i++) {
					if(code[i]==0){
						code[i]=correctColors[3];
					}


				}
				return convertGuess(code);
			}

			guess = nextSpot();

		}

		turn++;
		//guess = convertGuess(guess);
		return convertGuess(guess);
	}
	public void changeOrder(){
		int index1 = (int)(Math.random() *5);
		int index2 = (int)(Math.random() *5);

		int temp = colorOrder[index1];
		colorOrder[index1]=colorOrder[index2];
		colorOrder[index2]=temp;
	}
	private Color[] convertGuess(int[] theGuess){
		Color[] output = new Color[4];
		for (int i = 0; i < theGuess.length; i++) {
			if(theGuess[i] == 1){
				output[i]=Color.GREEN;
			}else if(theGuess[i] == 2){
				output[i]=Color.BLUE;
			}else if(theGuess[i] == 3){
				output[i]=Color.YELLOW;
			}else if(theGuess[i] == 4){
				output[i]= new Color(255,165,0);
			}else{
				output[i]=Color.RED;
			}

		}
		return output;
	}
	private int[] nextSpot(){
		int[]nextGuess = new int[]{missingColor,missingColor,missingColor,missingColor};
		if(!openSpot&&spotMarker==0){
			if(code[spotMarker]==0){
				nextGuess[spotMarker]=correctColors[colorMarker];
				openSpot = true;
			}
			spotMarker++;
		}
		if(!openSpot&&spotMarker==1){

			if(colorMarker>=1&&code[3]!=0&&code[2]!=0){
				code[1]=correctColors[colorMarker];
				colorMarker++;
				spotMarker=0;
				openSpot=true;

				if(colorMarker==3){
					for (int i = 0; i < code.length; i++) {
						if(code[i]==0){
							code[i]=correctColors[3];
						}
					}
					nextGuess=code;
				}else{
					return nextSpot();
				}

			}else{
				if(code[spotMarker]==0){
					nextGuess[spotMarker]=correctColors[colorMarker];
					openSpot = true;
				}
				spotMarker++;
			}
		}
		if(!openSpot&&spotMarker==2){
			if(colorMarker>=1&&code[3]!=0){
				code[2]=correctColors[colorMarker];
				colorMarker++;
				spotMarker=0;
				openSpot=true;

				if(colorMarker==3){
					for (int i = 0; i < code.length; i++) {
						if(code[i]==0){
							code[i]=correctColors[3];
						}
					}
					nextGuess=code;
				}else{
					return nextSpot();
				}

			}else{
				if(code[spotMarker]==0){
					nextGuess[spotMarker]=correctColors[colorMarker];
					openSpot = true;
				}
				spotMarker++;
			}
		}
		if(!openSpot&&spotMarker==3){
			code[3]=correctColors[colorMarker];
			colorMarker++;
			spotMarker=0;

			if(colorMarker==3){
				for (int i = 0; i < code.length; i++) {
					if(code[i]==0){
						code[i]=correctColors[3];
					}
				}
				nextGuess=code;
			}else{
				return nextSpot();
			}

		}
		openSpot=false;
		return nextGuess;
	}
	public Color[] newCode(){
		int[] outputCode = new int[4];
		for (int i = 0; i < outputCode.length; i++) {
			int color = (int)(Math.random() *4)+1;
			outputCode[i] = color;
		}
		return convertGuess(outputCode);
	}
}
