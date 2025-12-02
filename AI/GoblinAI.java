package AI;

import java.util.HashMap;
import java.util.Map;

import Objects.Classic;

public class GoblinAI extends AI {

    private final Node root;

    private static class GoblinState {
        float hp = 1f;
    }

    private final Map<Objects.Character, GoblinState> states = new HashMap<>();
    private GoblinState current;

    

    //  Cerveau du Goblin 
    public GoblinAI() {

        this.root = new Selector()

            //  Séquence d’attaque à distance 
            .add(new Sequence()
                .add(new Action("Range check", (character) -> {
                    Objects.Player player = Objects.Player.getPlayer1(); 
                    if (player == null) return Node.Status.FAIL; 

                    // Vérifie la ligne de vue
                    if (!AI.rayCast(character.getPosition(), player.getPosition())) {
                        
                        return Node.Status.FAIL;
                    }

                    double dx = player.getPosition().x - character.getPosition().x;
                    double dy = player.getPosition().y - character.getPosition().y;
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    double minAttackRange = 100.0;  // trop près = il fuit
                    double maxAttackRange = 220.0;  // trop loin = il s’approche

                    if (distance >= minAttackRange && distance <= maxAttackRange) {
                        return Node.Status.SUCCESS;
                    } else {
                        return Node.Status.FAIL;
                    }
                }))
                .add(new Action("Ranged Attack", (character) -> {
                    if (character instanceof Classic c) c.attack();
                    return Node.Status.SUCCESS;
                }))
            )

            //  Fuite si le joueur est trop proche 
            .add(new Action("Keep distance", (character) -> {
                Objects.Player player = Objects.Player.getPlayer1();
                if (player == null) return Node.Status.FAIL;

                if (!AI.rayCast(character.getPosition(), player.getPosition())) {
                        
                        return Node.Status.FAIL;
                    }

                double dx = character.getPosition().x - player.getPosition().x;
                double dy = character.getPosition().y - player.getPosition().y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < 100.0) { // trop près, on fuit
                    double length = Math.max(distance, 1.0);
                    double dirX = dx / length;
                    double dirY = dy / length;
                    double speed = 3.5;

                    if(!character.getCollision().forwardBlockCheck(dirX*3.0, 0.0)) character.addX(dirX);
                    if(!character.getCollision().forwardBlockCheck(0.0, dirY*3.0)) character.addY(dirY);
                    

                    return Node.Status.RUNNING;
                }

                return Node.Status.FAIL; 
            }))

            // S’approche si le joueur est trop loin 
            .add(new Action("Approach to shoot", (character) -> {
                Objects.Player player = Objects.Player.getPlayer1();
                if (player == null) return Node.Status.FAIL;

                if (!AI.rayCast(character.getPosition(), player.getPosition())) {
                        
                        return Node.Status.FAIL;
                    }

                double dx = player.getPosition().x - character.getPosition().x;
                double dy = player.getPosition().y - character.getPosition().y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 220.0) { 
                    double length = Math.max(distance, 1.0);
                    double dirX = dx / length;
                    double dirY = dy / length;
                    double speed = 2.5;

                    if(!character.getCollision().forwardBlockCheck(dirX*3.0, 0.0)) character.addX(dirX);
                    if(!character.getCollision().forwardBlockCheck(0.0, dirY*3.0)) character.addY(dirY);
                    

                    return Node.Status.RUNNING;
                }

                return Node.Status.FAIL;
            })) 

            // Mode patrouille pour chercher le joueur
            .add(new Action("Search", (character) -> {
                AI.patrol(character);
                return Node.Status.RUNNING;
            }));
    }

    @Override
    public void tick(Objects.Character goblin) {
        root.execute(goblin);
    }
}
