package com.fishkey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  
 * obsluga stanu quizu
 */
class QuizState implements Parcelable {
	LinkedList<Flashcard> flashcardSet;
	LinkedList<Flashcard> correctSet;
	LinkedList<Flashcard> wrongSet;
	int countCorrect;
	int countCorrectLastRounds;
	int countWrong;
	int flashcardSetSize;
	int currentSetSize;
	private int roundNumber;
	
	/**  
	 * przygotowuje quiz do rozpoczecia
	 * 
	 * Wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz zzych odpowiedzi na 0 oraz numer rundy na 1.
	 * Posiada wlasne statystyki, ktore udostepnia
	 * 
	 * @param	 context	obiekt <code>Context</code>
	 */
	public QuizState(Context context) {
		// TODO: Wczytywanie fiszek z zewnetrznego zrodla
		flashcardSet 			= new LinkedList<Flashcard>();
		correctSet 				= new LinkedList<Flashcard>();
		wrongSet 				= new LinkedList<Flashcard>();
		countCorrect			= 0;
		countCorrectLastRounds	= 0;
		countWrong				= 0;
		roundNumber				= 1;
		
		this.importDataFromFile(context, "slowka.txt");	// TODO: zrobic petle albo mechanizm korzystajacy z tej funkcji z dynamiczna nazwa pliku albo z baza danych
		//Log.i("Opening file - spike", "Afrer Reading file"); // spike
		/*  //spike 
		flashcardSet.offer(new Flashcard("przypominac sobie","recollect"));
		flashcardSet.offer(new Flashcard("uczyc sie","learn"));
		flashcardSet.offer(new Flashcard("przywolywac wspomnienia","bring back"));
		flashcardSet.offer(new Flashcard("lubic","care for"));
		flashcardSet.offer(new Flashcard("odniesc sukces, miec powodzenie","succeed"));
		*/
		
		//Log.i("Opening file - spike", "Afrer additional flashcards"); // spike
		currentSetSize=flashcardSetSize=flashcardSet.size();
		//Log.i("Opening file - spike", "After reading size of flashcardSet"); // spike
		//ShuffleList.main(null); //spike
		new ShuffleList(flashcardSet);
	}
	
	/** 
	 * dodaje podana fiszke do listy dobrze odgadnietych fiszek 
	 * TODO: Robiæ to w tle - watku/usludze
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @deprecated
	 */
	public void importDataFromFile(Context context, String fileName) {
		InternalStorage intStor = new InternalStorage();
		String slowka = intStor.read(fileName);
		
		
		/*// Poprzednia wersja z assets
		try{
			//Log.i("Opening file - spike", "Before open"); // spike
			InputStream in = context.getAssets().open(fileName);
			//Log.i("Opening file - spike", "After open"); // spike
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));		// TODO: Kontrolowac kodowanie pliku, ewentualnie zamieniac je wczesniej na UTF-8
			//Log.i("Opening file - spike", "new Buffer"); // spike
			String line;												// Tu beda wczytywane kolejne linie
			String[] text;												// Tablica bedzie przechowywac dwa stringi: odpowiedz oraz pytanie fiszki (w podanej kolejnosci)
			do {
				line = reader.readLine();								// Wczytanie nastepnej linii
				if(line==null)											// Trzeba bylo tak "nieelegancko" zrobic, zeby mozna bylo uzyc potem funkcji split na nie-null'owym stringu
					break;
				text = line.split(" - ");								// Dwie spacje obok umozliwiaja podanie slowka zawierajacego "gêst¹ spacjê", np. semi-detached								// Rozdzielenie pytania od odpowiedzi przez separator (myslnik)
				//Log.i("Opening file - spike", "read Line: "+line); // spike
				flashcardSet.offer(new Flashcard(text[1].trim(),text[0].trim()));		// TODO: od kolejnosci zalezy, co jest pytaniem a co odpowiedzia. powinno sie to sprawdzac jakos wczesniej! Zadbac o usuniecie brzegowych spacji
				//Log.i("Opening file - spike", "Adding to LinkedList"); // spike
			} while(true);												// "Nieeleganncko" - ale wewn¹trz pêtli jest warunek z instrukcj¹ break;
			//Log.i("Opening file - spike", "out of while"); // spike
			in.close();
			//Log.i("Opening file - spike", "after closing"); // spike
		} catch (IOException e) {
			Log.i("Quiz", "QuizState - cannot open a file"); // spike
		}
		*/
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka dobrze odgadnieta
	 */
	public void answerCorrect(Flashcard f) {
		flashcardSet.remove();				// Usuniecie ostatnio pobranej fiszki z listy
		correctSet.offer(f);				// Dodanie podanej fiszki do listy dobrze odgadnietych
		++countCorrect;						// Zwiekszenie licznika dobrze odgadnietych fiszek
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka zle odgadnieta
	 */
	public void answerWrong(Flashcard f) {
		flashcardSet.remove();				// Usuniecie ostatnio pobranej fiszki z listy
		wrongSet.offer(f);					// Dodanie podanej fiszki do listy zle odgadnietych
		++countWrong;						// Zwiekszenie licznika zle odgadnietych fiszek
	}
	
	/** 
	 * pobiera ale nie usuwa(!) fiszke z listy i zwraca ja w return 
	 * 
	 * @return	f	fiszka pobierana z kolejki
	 */
	public Flashcard getFlashcard(){
		return flashcardSet.peek();
	}
	
	/** 
	 * zwraca liczbe udzielonych dobrych odpowiedzi 
	 * 
	 * @return	int	liczba dobrych odpowiedzi
	 * */
	int getStateCorrect() {
		return countCorrect;
	}
	
	/** 
	 * zwraca liczbe poprawionych odpowiedzi udzielonych w poprzednich rundach
	 * 
	 * @return	int	liczba odpowiedzi udzielonych w poprzednich rundach 
	 */
	int getStateCorrectLastRounds() {
		return countCorrectLastRounds;
	}
	
	/** 
	 * zwraca liczbe udzielonych zlych odpowiedzi
	 * 
	 * @return	int	liczba zlych odpowiedzi w aktualnej rundzie
	 */
	int getStateWrong() {
		return countWrong;
	}
	
	/**
	 * zwraca liczbe wszystkich fiszek w zestawie
	 * 
	 * @return 	int	ilosc wszystkich odpowiedzi w aktualnej rundzie
	 */
	int getStateAllCurrentSet() {
		return currentSetSize;
	}
	
	int getStateAll() {
		return flashcardSetSize;
	}
	
	/** 
	 * zwraca numer rundy
	 * 
	 * @return	int	numer rundy
	 */
	int getRoundNumber() {
		return roundNumber;
	}
	
	/** 
	 * rozpoczyna nowa runde i podmienia zestaw slowek quizu z tymi zle odgadnietymi w poprzedniej rundzie
	 */
	void startNextRound() {
		++roundNumber;								// Zwieksz numer rundy
		countCorrectLastRounds += countCorrect;		// Dodaj do licznika poprawnych odpowiedzi fiszki poprawione w ostatniej rundzie
		currentSetSize			= countWrong;		// Ustaw jako liczbe fiszek w nastepnej rundzie - liczbe zle odgadnietych fiszek
		countCorrect	  		= 0;				// Wyzeruj licznik poprawianych odpowiedzi
		countWrong 				= 0;				// Wyzeruj licznik zlych odpowiedzi
		flashcardSet.clear();						// Wyrzuc wszystkie fiszki z zestawu
		for(Flashcard wrongFlashcard: wrongSet){	// Przerzuc zle odgadniete fiszki do zestawu odgadywanych w nastepnej rundzie
			flashcardSet.offer(wrongFlashcard);
		}
		wrongSet.clear();							// Wyrzuc wszystkie fiszki zle odgadniete w poprzedniej rundzie
	}
	
	/* Okreslanie potrzebnych funkcji do serializacji danych: */
	
	/**
	 * Pole sluzace do serializacji
	 */
	//spike
	public static final Parcelable.Creator<QuizState> CREATOR = new Parcelable.Creator<QuizState>() {
        public QuizState createFromParcel(Parcel in) { return new QuizState(in); }
        public QuizState[] newArray(int size) { return new QuizState[size]; }
    };
    
	
	/**
	 * Metoda do serializacji, ktorej po prostu wymaga framework.
	 */
    @Override
	public int describeContents() {
		return 0;									// Framework tak chce :)
	}
	
    /**
	 * Metoda sluzaca do serializacji
	 */
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		Log.i("Bundle", "writeToParcel(Parcel parcel, int flags) in"); // spike
		/* Listy bedace polami klasy */
		/* Serializowanie informacji o rozmiarach list */
		int remainingSetSize 	= flashcardSet.size();
		int correctSetSize 		= correctSet.size();
		int wrongSetSize 		= wrongSet.size();
		parcel.writeInt(remainingSetSize);
		parcel.writeInt(correctSetSize);
		parcel.writeInt(wrongSetSize);
		
		for(int i=0; i<remainingSetSize; ++i){
			Flashcard f = flashcardSet.poll();
			parcel.writeString(f.getQuestion());
			parcel.writeString(f.getAnswer());
		}
		for(int i=0; i<correctSetSize; ++i){
			Flashcard f = correctSet.poll();
			parcel.writeString(f.getQuestion());
			parcel.writeString(f.getAnswer());
		}
		for(int i=0; i<wrongSetSize; ++i){
			Flashcard f = wrongSet.poll();
			parcel.writeString(f.getQuestion());
			parcel.writeString(f.getAnswer());
		}

		/* Pola prymitywne klasy */
		parcel.writeInt(countCorrect);
		parcel.writeInt(countCorrectLastRounds);
		parcel.writeInt(countWrong);
		parcel.writeInt(flashcardSetSize);
		parcel.writeInt(currentSetSize);
		parcel.writeInt(roundNumber);
		Log.i("Bundle", "writeToParcel(Parcel parcel, int flags) out"); // spike
	}
	
	public void readFromParcel(Parcel parcel) {
		Log.i("Bundle", "readFromParcel(Parcel parcel) in"); // spike
		/* Listy bedace polami klasy */
		int remainingSetSize 	= parcel.readInt();
		int correctSetSize 		= parcel.readInt();
		int wrongSetSize 		= parcel.readInt();
		for(int i=0; i<remainingSetSize; ++i){
			flashcardSet.offer(new Flashcard(parcel.readString(),parcel.readString()));
		}
		for(int i=0; i<correctSetSize; ++i){
			correctSet.offer(new Flashcard(parcel.readString(),parcel.readString()));
		}
		for(int i=0; i<wrongSetSize; ++i){
			wrongSet.offer(new Flashcard(parcel.readString(),parcel.readString()));
		}

		/* Pola prymitywne klasy */
		parcel.writeInt(countCorrect);
		parcel.writeInt(countCorrectLastRounds);
		parcel.writeInt(countWrong);
		parcel.writeInt(flashcardSetSize);
		parcel.writeInt(currentSetSize);
		parcel.writeInt(roundNumber);
		Log.i("Bundle", "readFromParcel(Parcel parcel) out"); // spike
	}
	
	/**
	 * Metoda sluzaca do odczytywania serializacji.
	 */
	//spike
	private QuizState(Parcel parcel) {
		Log.i("Bundle", "QuizState(Parcel parcel) in"); // spike
		this.readFromParcel(parcel);
		Log.i("Bundle", "QuizState(Parcel parcel) out"); // spike
	}
	
	
	
	
}