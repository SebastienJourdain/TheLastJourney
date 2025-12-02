package AI ;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    // L'état renvoyé par chaque nœud après exécution
    public enum Status {
        FAIL,
        SUCCESS,
        RUNNING
    }

    // Liste d'enfants (utile pour Selector et Sequence)
    protected List<Node> children = new ArrayList<>();

    // Pour ajouter un enfant et permettre le chaînage (.add(...))
    public Node add(Node child) {
        children.add(child);
        return this;
    }

    // Méthode abstraite que chaque nœud doit implémenter
    public abstract Status execute(Objects.Character character);
}
