package AI;

import java.util.HashMap;
import java.util.Map;

import Objects.Classic;

public class SkeletonAI extends AI {

    private final Node root;

    private static class SkeletonState {
        float hp = 1f;
    }

    private final Map<Objects.Character, SkeletonState> states = new HashMap<>();
    private SkeletonState current;

    // Cerveau du Skeleton
    public SkeletonAI() {
        
        this.root = new Selector()

        // Séquence d'attaque
        .add(
            new Sequence()
                .add(new Action("Range check", (character) -> {
                    Objects.Player player = Objects.Player.getPlayer1(); 
                    if (player == null) return Node.Status.FAIL;

                    // Calcul de la distance entre le joueur et le skeleton
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

        //  Action de poursuite 
        .add(new Action("Come closer", (character) -> {
            Objects.Player player = Objects.Player.getPlayer1();
            if (player == null) return Node.Status.FAIL;

            double dx = player.getPosition().x - character.getPosition().x;
            double dy = player.getPosition().y - character.getPosition().y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // On arrête de bouger si le skeleton est assez proche
            if (distance < 40) {
                return Node.Status.SUCCESS;
            }

            // Déplacement vers le joueur
            double length = Math.max(distance, 1.0); 
            double dirX = dx / length;
            double dirY = dy / length;
            double speed = 3.5;

            if(!character.getCollision().forwardBlockCheck(dirX*3.0, 0.0)) character.addX(dirX);
            if(!character.getCollision().forwardBlockCheck(0.0, dirY*3.0)) character.addY(dirY);

           
            return Node.Status.RUNNING;
        }));
    } 

    public void tick(Objects.Character skeleton) {
        root.execute(skeleton);
    }
} 
