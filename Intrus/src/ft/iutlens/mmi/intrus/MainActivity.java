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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try { // chargement des données
			data = new Data(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_main);

		start();
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

	private void start(){
		setScore(0);
	
		setQuestion(data.getNextQuestion());
		if (countDown != null) countDown.cancel();
		countDown = new CountDownTimer(30000, 100) {

			public void onTick(long millisUntilFinished) {
				((TextView) findViewById(R.id.countdown)).setText("Reste : " + ((int)Math.round(millisUntilFinished / 100.0)/10.0));
			}

			public void onFinish() {
				((TextView) findViewById(R.id.countdown)).setText("Terminé !");
				running = false;
			}
		}.start();
		running = true;
	}

	public void onClick(View view){
		if (!running){
			start();
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
}
