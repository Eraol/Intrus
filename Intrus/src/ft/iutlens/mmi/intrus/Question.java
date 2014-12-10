package ft.iutlens.mmi.intrus;

import android.os.Bundle;

public class Question {
	public final int id[]; // id des 4 images
	public final String intrus[]; // justification pour l'intrus (tags séparés par des ',')
	
		
	public Question(int[] id, String[] intrus) {
		super();
		this.id = id;
		this.intrus = intrus;
	}
	
	public Question(Bundle bundle){ // re-création à partir d'un bundle
		super();
		id = bundle.getIntArray("id"); // on récupère chaque champ avec la clef correspondante,
		intrus = bundle.getStringArray("intrus"); // en faisant attention aux types.
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
	
	public Bundle toBundle(){ // sauvegarde des champs dans un bundle.
		Bundle bundle = new Bundle();
		bundle.putIntArray("id", id); // le nom du champ est utilisé comme clef
		bundle.putStringArray("intrus", intrus); // on fait attention au type de données
		return bundle;
	}

}
