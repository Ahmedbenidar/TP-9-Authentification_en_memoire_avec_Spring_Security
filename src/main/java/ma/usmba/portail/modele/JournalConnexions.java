package ma.usmba.portail.modele;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.stereotype.Component;

import ma.usmba.portail.modele.EntreeJournal.TypeEvenement;

@Component
public class JournalConnexions {

	private static final int CAPACITE = 25;

	private final Deque<EntreeJournal> entrees = new ArrayDeque<>();

	public synchronized void enregistrer(String identifiant, TypeEvenement type) {
		entrees.addFirst(new EntreeJournal(identifiant, LocalDateTime.now(), type));
		while (entrees.size() > CAPACITE) {
			entrees.removeLast();
		}
	}

	public synchronized List<EntreeJournal> getEntrees() {
		return new ArrayList<>(entrees);
	}

	public synchronized void vider() {
		entrees.clear();
	}
}
