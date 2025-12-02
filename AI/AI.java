package AI;

import Level.Level;
import Objects.Collision;
import Utilitary.V2D;




public abstract class AI {
    public void tick(Objects.Character cha) {

    }

    // Patrouille 
    private static double[] currentDirectionRef = {0}; 
    private static double leftLimit = 600;
    private static double rightLimit = 1300;
    private static double speed = 1.0;

    public static void patrol(Objects.Character character) {

    V2D pos = character.getPosition();

     

    if (currentDirectionRef[0] == 0) { // aller à droite
    boolean block = character.getCollision().forwardBlockCheck(speed*3.0, 0.0);
        if(!block)character.addX(speed);
        if (pos.x > rightLimit || block ) currentDirectionRef[0] = 1;
    } else { // aller à gauche
     boolean block = character.getCollision().forwardBlockCheck(-speed*3.0, 0.0);
        if(!block)character.addX(-speed);
        if (pos.x < leftLimit || block) currentDirectionRef[0] = 0;
    }
}

    // RayCast
    public static boolean rayCast(V2D from, V2D to) {
    double dx = to.x - from.x;
    double dy = to.y - from.y;
    double distance = Math.hypot(dx, dy);

    int steps = (int)(distance / 5.0); // test tous les 5 pixels
    double stepX = dx / steps;
    double stepY = dy / steps;

    double x = from.x;
    double y = from.y;

    for (int i = 0; i < steps; i++) {
        x += stepX;
        y += stepY;

        for (Collision c : Level.getActualRoom().getCollisions())
 {
            if (!c.isbActive()) continue;

            if (x >= c.getPosition().x - 2 && x <= c.getPosition().x + c.getSize().x + 2 &&
                y >= c.getPosition().y - 2 && y <= c.getPosition().y + c.getSize().y + 2) {

                if (c.isbBlocking()) {
                     
                    return false; // rayon bloqué
                }
            }
        }
    }
    return true; // rien n'a bloqué
}

                    // --- PATHFINDING---
    public static void moveToward(Objects.Character c, V2D target, double speed) {
    double dx = target.x - c.getPosition().x;
    double dy = target.y - c.getPosition().y;
    double distance = Math.hypot(dx, dy);
    if (distance < 1) return;

    double dirX = dx / distance;
    double dirY = dy / distance;

    if (!c.getCollision().forwardBlockCheck(dirX * 3.0, 0.0)) c.addX(dirX * speed);
    if (!c.getCollision().forwardBlockCheck(0.0, dirY * 3.0)) c.addY(dirY * speed);


    }

    // Anticipation du déplacement 
    public static void navigateAround(Objects.Character c, V2D target, double speed) {
    double bestScore = Double.MAX_VALUE;
    double bestDirX = 0;
    double bestDirY = 0;

    // On teste 16 directions
    for (int i = 0; i < 16; i++) {
        double angle = (Math.PI * 2 / 16) * i;
        double dirX = Math.cos(angle);
        double dirY = Math.sin(angle);

        // On teste plusieurs distances (anticipation)
        for (double dist = 30; dist <= 90; dist += 30) {
            double newX = c.getPosition().x + dirX * dist;
            double newY = c.getPosition().y + dirY * dist;

            // Vérifie qu'il n'y a pas de mur entre la position actuelle et ce point
            if (!AI.rayCast(c.getPosition(), new V2D(newX, newY))) continue;

            // Score = distance au joueur + pénalité selon le blocage local
            double score = Math.hypot(target.x - newX, target.y - newY);

            // Si c'est un meilleur candidat, on le garde
            if (score < bestScore) {
                bestScore = score;
                bestDirX = dirX;
                bestDirY = dirY;
            }
        }
    }

    // Si on a trouvé un point libre
    if (bestScore < Double.MAX_VALUE) {
        if (!c.getCollision().forwardBlockCheck(bestDirX * 3.0, 0.0)) c.addX(bestDirX * speed);
        if (!c.getCollision().forwardBlockCheck(0.0, bestDirY * 3.0)) c.addY(bestDirY * speed);
    }
}

    }

    






