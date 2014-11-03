package ft.iutlens.mmi.intrus;

public class Question {
	public final int id[]; // id des 4 images
	public final boolean intrus[]; // vrai == intrus
	
		
	public Question(int[] id, boolean[] intrus) {
		super();
		this.id = id;
		this.intrus = intrus;
	}


	public boolean isCorrect(int answer){
		return intrus[answer];
	}

}
