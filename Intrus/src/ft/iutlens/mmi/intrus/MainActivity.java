package ft.iutlens.mmi.intrus;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Data data;
	private Question question;

	private boolean running;

	private int[] imageId = {
			R.id.imageButton1,
			R.id.imageButton2,
			R.id.imageButton3,
			R.id.imageButton4};
	private int score;
	private CountDownTimer countDown;
	protected long timeLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try { // chargement des données
			data = new Data(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null){ // pas de sauvegarde à restaurer, on démarre une partie
			start(0,
				  data.getNextQuestion(),
				  30000);
		} else { // sauvegarde présente, on reprend là où on en était
			start(0, //score -- à modifier pour le restaurer
				  new Question(savedInstanceState.getBundle("question")),//question
				  30000); //durée en ms 
		}
		
	}

	private void setQuestion(Question question) {
		this.question = question;

		for(int i=0; i < 4; ++i){ //affiche les images
			((ImageButton) findViewById(imageId[i])).setImageResource(question.id[i]);
		}
		// Log.d("Explain",question.explain());

	}

	
	void setScore(int score){
		this.score = score;
		((TextView) findViewById(R.id.score)).setText("Score : "+score);
	}

	private void start(int score, Question question, int duration){
		setScore(score);
		setQuestion(question);
		startCountDown(duration);
	}

	public void startCountDown(long duration) {
		if (countDown != null) countDown.cancel();
		countDown = new CountDownTimer(duration, 100) {

			public void onTick(long millisUntilFinished) {
				timeLeft = millisUntilFinished; //sauvegarde le temps restant dans timeLeft;
				((TextView) findViewById(R.id.countdown)).setText("Reste : " + ((int)Math.round(millisUntilFinished / 100.0)/10.0));
			}

			public void onFinish() {
				((TextView) findViewById(R.id.countdown)).setText("Terminé !");
				running = false;
				countDown = null;
			}
		}.start();
		running = true;
	}

	public void onClick(View view){
		if (!running){
			start(0, data.getNextQuestion(), 30000);
		} else {
			int answer=0;
			while (answer<4 && view.getId() != imageId[answer]) ++answer; //cherche l'indice du bouton

			if (question.isCorrect(answer)){ //bonne réponse
				setScore(score+1);
				setQuestion(data.getNextQuestion()); //question suivante			
			} else { //mauvaise réponse
				setScore(score-1);
				Log.d("Explain",question.explain());
			}			
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) { //sauvegarde de l'état de l'activité
		super.onSaveInstanceState(outState);
		
		outState.putBundle("question", question.toBundle()); 
		     //question est un objet, on lui demande donc de se placer lui-même dans un bundle
		     //que l'on inclus dans la sauvegarde
		
		//ajoutez vos propre sauvegardes
	}

	@Override
	protected void onStop() { //si l'activité passe en pause ...
		super.onPause();
		
		if (countDown != null) countDown.cancel(); // arrêt du timer
		countDown = null;
	}

	@Override
	protected void onRestart() { //si l'activité redevient active ...
		super.onResume();
		
		if (running && countDown == null){
			startCountDown(timeLeft); //redémarrage du timer si nécessaire
		}	
	}
	
	
}
