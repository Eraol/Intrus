package ft.iutlens.mmi.intrus;

public class Question {
	public final int id[]; // id des 4 images
	public final String intrus[]; // justification pour l'intrus (tags séparés par des ',')
	
		
	public Question(int[] id, String[] intrus) {
		super();
		this.id = id;
		this.intrus = intrus;
	}


	public boolean isCorrect(int answer){
		return intrus[answer] != null;
	}
	
	public String getTags(int answer){
		return intrus[answer];
	}
	
	public String explain(){
		String result ="";
		for(int i =0; i < intrus.length; ++i){
			if (intrus[i] != null) result += i+" : "+ intrus[i] +"\n";
		}
		return result;
		
	}

}
