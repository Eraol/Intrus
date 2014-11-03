package ft.iutlens.mmi.intrus;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class Data {

	Vector<Integer> ids; // liste des ids de toutes les images
	Vector<String> tags; // liste des tags possibles
	Map<String, Set<Integer> > map; // tags et images associées à chacun


	public Data(Context context) throws XmlPullParserException, IOException{

		//instanciations
		ids = new Vector<Integer>();
		tags = new Vector<String>();
		map = new HashMap< String, Set<Integer> >();

		//initialisations diverses
		String text = null;
		int currentId=-1;
		Set<String> allTags = new HashSet(); // liste des tags (sans doublon)

		// R�cup�rer le fichier xml
		XmlResourceParser xpp = context.getResources().getXml(R.xml.data);

		//d�but de l'analyse du xml
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){// tant que pas fini...
			if(eventType == XmlPullParser.START_TAG){
				if ("image".equals(xpp.getName())){// début d'une nouvelle image
					currentId = context.getResources().getIdentifier( // on récupère l'id
							xpp.getAttributeValue(null,"id"), "drawable", context.getPackageName());
					ids.add(currentId);
				}
			} else if(eventType == XmlPullParser.END_TAG) { // fin d'une image
				if ("image".equals(xpp.getName())){ 
					String[] content = text.split(","); // on découpe les tags
					for(String s : content){
						setTag(currentId,s.trim()); // et on les ajoutes aux donnnées
						allTags.add(s); // ainsi qu'à la liste des tags
					}
				}
			} else if(eventType == XmlPullParser.TEXT) {
				text = xpp.getText(); // On met le text de coté pour la fin de la balise.
			}
			eventType = xpp.next(); // au suivant !
		}

		tags.addAll(allTags); // On remplit la liste des tags. Les doublons ont été évités.
	}

	public void setTag(int id, String tag){
		Set<Integer> set = map.get(tag); // set : images avec le tag
		if (set == null){ // première image du tag
			set = new HashSet<Integer>(); // créer l'ensemble
			map.put(tag, set); // et l'associer au tag
		}
		set.add(id); // ajout de l'image à la liste pour ce tag.	
	}

	public Object getRandom(Vector v){
		return v.elementAt((int) (v.size()*Math.random())); 
	}

	public Object removeRandom(Vector v){
		return v.remove((int) (v.size()*Math.random())); 
	}

	public Question getNextQuestion(){

		int[] image = new int[4]; // ids choisis (instanciation)
		boolean[] intrus = new boolean[4]; 

		int count = 0;
		int essai = 3;

		while (count != 1 && essai>0){
			--essai;

			String tag = (String) getRandom(tags); // tirage d'un tag;
			int pos = (int) (Math.random()*4); //tirage de la position

			Vector<Integer> with = new Vector<Integer>(map.get(tag)); //images avec le tag

			Vector<Integer> without = new Vector<Integer>(ids); // images sans = toutes
			without.removeAll(map.get(tag));                    // sauf celles avec

			for(int i =0; i <4;++i){
				if (i != pos) image[i] =  (Integer) removeRandom(with); //tirage d'une image avec le tag
				else image[i] = (Integer) getRandom(without) ; //tirage d'une image sans
			}


			for(Set<Integer> set : map.values()) { //recherche pour chaque tag
				count = 0;
				int last = -1; // indice de l'intrus
				for(int i =0; i<4;++i){ // compte le nombre d'apparition du tag sur les 4 images
					if (set.contains(image[i])) ++count; else last = i;
				}
				if (count == 3) intrus[last] = true; //ajout de l'intrus s'il existe (3 tags sur 4 images)
			}

			count = 0;
			for(int i =0; i<4;++i){
				if (intrus[i]) ++count;
			}

		}

		return new Question(image,intrus);
	}
}
