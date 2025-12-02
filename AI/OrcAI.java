package AI;

import java.util.HashMap;
import java.util.Map;

import Objects.Character;
import Objects.Classic;
import Objects.Player;
import Utilitary.V2D;

public class OrcAI extends AI {

    private final Node root;

    
    private final boolean canFlee;

    // Analyse de la fuite de l'Orc
    private static class OrcState {
        boolean fleeing = false;
        int fleeTicks = 0;
        float hp = 1f;
    }

    private final Map<Character, OrcState> states = new HashMap<>();
    private OrcState current;

    private static final int DELAY_BEFORE_REGEN = 180; // 3 secondes à 60 FPS

    private OrcState ensureState(Character c) {
        return states.computeIfAbsent(c, k -> {
            OrcState s = new OrcState();
            s.hp = c.getPercentHP();
            return s;
        });
    }

    // Cerveau de l'Orc
    public OrcAI() {
        this.canFlee = Math.random() < 0.4;

        this.root = new Selector()

            // --- Séquence d'attaque ---
            .add(
                new Sequence()
                    .add(new Action("Range check", (character) -> {
                        Player player = Player.getPlayer1(); 
                        if (player == null) return Node.Status.FAIL; 

                        // Calcul de la distance entre le joueur et l'orc
                        double dx = player.getPosition().x - character.getPosition().x;
                        double dy = player.getPosition().y - character.getPosition().y;
                        double distance = Math.sqrt(dx * dx + dy * dy);

                        double attackRange = 40.0;

                        // Test de la distance
                        if (distance <= attackRange) {
                            
                            return Node.Status.SUCCESS;
                        } else {
                            
                            return Node.Status.FAIL;
                        }
                    }))
                    .add(new Action("Attack", (character) -> {
                        if (character instanceof Classic c) c.attack();
                        return Node.Status.SUCCESS;
                    }))
            )

            // --- Séquence de fuite ---
            .add(
                new Sequence()
                    .add(new Action("Check HP", (character) -> {

                        if (!canFlee) return Node.Status.FAIL;

                        // Synchronisation avec les vrais HP du perso
                        current.hp = character.getPercentHP();

                        float hpPercent = current.hp;

                        // Seuil : 40%
                        if (hpPercent < 0.40f || current.fleeing) {
                            
                            return Node.Status.SUCCESS;
                        } else {
                            return Node.Status.FAIL;
                        }
                    }))

                    .add(new Action("Run away", (character) -> {
                        Player player = Player.getPlayer1();
                        if (player == null) return Node.Status.FAIL;

                        // mode fuite : compte des ticks
                        current.fleeing = true;
                        current.fleeTicks++;

                        // Fuite : direction opposée au joueur
                        double dx = character.getPosition().x - player.getPosition().x;
                        double dy = character.getPosition().y - player.getPosition().y;
                        double distance = Math.sqrt(dx * dx + dy * dy);
                        double length = Math.max(distance, 1.0);
                        double dirX = dx / length;
                        double dirY = dy / length;

                        // Vitesse de fuite x3
                        double fleeSpeed = 3.0;

                        // Déplacement
                        if(!character.getCollision().forwardBlockCheck(dirX * fleeSpeed, 0.0)) 
                            character.addX(dirX * fleeSpeed);

                        if(!character.getCollision().forwardBlockCheck(0.0, dirY * fleeSpeed)) 
                            character.addY(dirY * fleeSpeed);

                        // Attente de 3 secondes avant régénération
                        if (current.fleeTicks < DELAY_BEFORE_REGEN) {
                            
                            return Node.Status.RUNNING;
                        }

                        // Régénération progressive
                        float regenRate = 0.005f;
                        if (character instanceof Classic c) c.regen(1);

                        // Quand la vie est au max → retour au combat
                        if (character.getPercentHP() >= 1.0f) {
                            current.fleeing = false;
                            current.fleeTicks = 0;
                            
                            return Node.Status.SUCCESS;
                        }

                        
                        return Node.Status.RUNNING;
                    }))
            )

            // --- Action de poursuite ---
            .add(new Action("Come closer", (character) -> {
                Player player = Player.getPlayer1();
                if (player == null) return Node.Status.FAIL;

                V2D from = character.getPosition();
                V2D to = player.getPosition();

                // Si ligne de vue directe → avance normalement
                if (AI.rayCast(from, to)) {
                    moveToward(character, to, 2.0);
                    return Node.Status.RUNNING;
                }

                // Sinon → essaie un contournement
                AI.navigateAround(character, to, 2.0);
                return Node.Status.RUNNING;
            }));
    }

    public void tick(Character orc) {
        current = ensureState(orc);
        root.execute(orc);
    }
}
