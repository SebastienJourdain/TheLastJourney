package Level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Objects.Door;
import Objects.Gobelin;
import Objects.NonPlayerCharacter;
import Objects.Obstacle;
import Objects.Orc;
import Objects.Skeleton;
import Objects.Tile;
import Objects.Trap;
import Utilitary.V2D;

/**
 * Salle « classique » d’un niveau.
 * <p>
 * Cette pièce :
 * <ul>
 *   <li>génère un sol et un décor standards,</li>
 *   <li>place aléatoirement des craquelures et des obstacles sans chevauchement,</li>
 *   <li>instancie un certain nombre de monstres en fonction du type de niveau
 *       (gobelins, orcs, squelettes, etc.),</li>
 *   <li>crée les portes de connexion vers les salles adjacentes suivant la position
 *       de la pièce dans la grille du {@link Level},</li>
 *   <li>ajoute murs, coins et éléments décoratifs selon la position (bords du niveau, etc.).</li>
 * </ul>
 * Elle représente une salle de combat « standard » par opposition aux salles spéciales
 * comme la salle de boss.
*/
public class ClassicRoom extends Room {

    /**
     * Construit une salle classique aux coordonnées données dans la grille du niveau.
     * <p>
     * Le constructeur :
     * <ul>
     *   <li>place le sol et le fond de la salle,</li>
     *   <li>génère aléatoirement craquelures, obstacles et pièges sans chevauchement,</li>
     *   <li>fait apparaître un nombre aléatoire de monstres correspondant au type
     *       de niveau courant,</li>
     *   <li>crée les portes et murs en fonction de la position (bords ou milieu de la carte),</li>
     *   <li>ajoute certaines décorations (torches, etc.) lorsque cela est pertinent.</li>
     * </ul>
     *
     * @param posX indice de la salle sur l’axe X dans la grille du {@link Level}
     * @param posY indice de la salle sur l’axe Y dans la grille du {@link Level}
    */
    public ClassicRoom(int posX, int posY, boolean bEnnemies) {
        super();
        
        final int mapWidth = 1024;
        final int mapHeight = 744;
        final int offsetX = 415;
        final int offsetY = 108;

        // Portes
        if (posY > 0)
        {
            //Porte haute
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 448, offsetY + 0), new V2D(0, 0), "TileSet", "door_top"));
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 512, offsetY + 0), new V2D(0, 0), "TileSet", "door_top"));
            this.addObject(new Door(new V2D(4, 4), new V2D(offsetX + 448, offsetY + 64), new V2D(32, 24), "TileSet", "door_closed", posX, posY - 1));
        }
        if (posX > 0)
        {
            //Porte gauche
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 352), new V2D(0, 0), "TileSet", "door_top_left"));
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 472), new V2D(0, 0), "TileSet", "door_bottom_left"));
            //Void gauche
            this.addObject(new Door(new V2D(5, 5), new V2D(offsetX - 5, offsetY + 392), new V2D(10.0, 17.0), "TileSet", "void_door_left", posX -1, posY));
        }
        if (posX < Level.getWidth() - 1)
        {
            //Porte droite
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 968, offsetY + 352), new V2D(0, 0), "TileSet", "door_top_right"));
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 968, offsetY + 472), new V2D(0, 0), "TileSet", "door_bottom_right"));
            //Void droite
            this.addObject(new Door(new V2D(5, 5), new V2D(offsetX + 977, offsetY + 392), new V2D(10.0, 17.0), "TileSet", "void_door_right", posX + 1, posY));
        }
        if (posY < Level.getHeight() - 1)
        {
            //Porte basse
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 448, offsetY + 676), new V2D(0, 0), "TileSet", "door_bottom_l"));
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 544, offsetY + 676), new V2D(0, 0), "TileSet", "door_bottom_r"));
            //Void bas
            this.addObject(new Door(new V2D(4, 4), new V2D(offsetX + 480, offsetY + 700), new V2D(17.0, 10.0), "TileSet", "void_door_bottom", posX, posY + 1));
        }

        // Coins et murs gauches
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 0), new V2D(32, 48), "TileSet", "top_left_corner"));
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 192), new V2D(14, 40), "TileSet", "left_wall"));
        if (posX == 0) this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 352), new V2D(14, 40), "TileSet", "left_wall"));
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 512), new V2D(14, 40), "TileSet", "left_wall"));
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 0, offsetY + 672), new V2D(32, 17), "TileSet", "bottom_left_corner"));

        // Murs du bas
        List<Integer> bottomXs = new ArrayList<>(List.of(128, 192, 256, 320, 384, 576, 640, 704, 768, 832));
        if (posY == Level.getHeight() - 1)
        {
            bottomXs = new ArrayList<>(List.of(128, 192, 256, 320, 384, 448, 512, 576, 640, 704, 768, 832));
        }
        for (int x : bottomXs) {
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + x, offsetY + 672), new V2D(16, 17), "TileSet", "bottom_wall"));
        }

        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 896, offsetY + 672), new V2D(0, 0), "TileSet", "bottom_right_corner"));

        // Murs droits
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 968, offsetY + 192), new V2D(0, 0), "TileSet", "right_wall"));
        if (posX == Level.getWidth() - 1) this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 968, offsetY + 352), new V2D(0, 0), "TileSet", "right_wall"));
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 968, offsetY + 512), new V2D(0, 0), "TileSet", "right_wall"));

        // Coin supérieur droit
        this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 896, offsetY + 0), new V2D(32, 48), "TileSet", "top_right_corner"));

        // Murs supérieurs
        List<Integer> topXs = new ArrayList<>(List.of(128, 192, 256, 320, 384, 576, 640, 704, 768, 832));
        if (posY == 0)
        {
            topXs = new ArrayList<>(List.of(128, 192, 256, 320, 384, 448, 512, 576, 640, 704, 768, 832));
        }
        for (int x : topXs) {
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + x, offsetY + 0), new V2D(16, 44), "TileSet", "top_wall"));
        }

        // Décorations
        if (posY > 0)
        {
            this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 350, offsetY + 32), new V2D(-1, -1), "TileSet", "torch"));
            this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 550, offsetY + 32), new V2D(-1, -1), "TileSet", "torch"));
        }

        // Sol
        this.addObject(new Tile(new V2D(30.1, 19.4), new V2D(offsetX, offsetY), "TileSet", "floor"));

        Random rand = new Random();
        int nbCracks = 20;

        int minX = offsetX + 56;
        int maxX = offsetX + mapWidth - 56;
        int minY = offsetY + 192;
        int maxY = offsetY + mapHeight - 68;

        // Craquelures
        for (int i = 0; i < nbCracks; i++) {
            double x = rand.nextInt(maxX - minX + 1) + minX;
            double y = rand.nextInt(maxY - minY + 1) + minY;
            String crackName = "crack_" + (1 + rand.nextInt(4));
            this.addObject(new Tile(new V2D(4, 4), new V2D(x, y), "TileSet", crackName));
        }

        // Obstacles et pièges
        int nbObstacles = rand.nextInt(5, 10);
        String[] types = {"barrel_1", "barrel_2", "box", "inactive_trap"};
        ArrayList<Rectangle> placedObstacles = new ArrayList<>();

        for (int j = 0; j < nbObstacles; j++) {
            String obstacleType = types[rand.nextInt(types.length)];
            V2D size = Graphism.Animation.get("TileSet", obstacleType).getFrameSize();

            int scaledWidth = (int) (size.x * 3);
            int scaledHeight = (int) (size.y * 3);

            Rectangle newRect;
            boolean overlap;
            int attempts = 0;
            double ox = 0, oy = 0;

            do {
                overlap = false;
                ox = rand.nextInt(maxX - minX - scaledWidth) + minX;
                oy = rand.nextInt(maxY - minY - scaledHeight) + minY;
                newRect = new Rectangle((int) ox, (int) oy, scaledWidth, scaledHeight);

                // Vérifie le chevauchement
                for (Rectangle rPlaced : placedObstacles) {
                    if (newRect.intersects(rPlaced)) {
                        overlap = true;
                        break;
                    }
                }
                attempts++;
            } while (overlap && attempts < 100);

            if (overlap) continue;
            
            placedObstacles.add(newRect);

            if (obstacleType.equals("inactive_trap")) {
                this.addObject(new Trap(new V2D(3, 3), new V2D(ox, oy)));
            } else {
                this.addObject(new Obstacle(new V2D(3, 3), new V2D(ox, oy), new V2D(0, 0), "TileSet", obstacleType));
            }
        }

        //Monstres
        if (bEnnemies)
        {
            String monsterName = Level.getLevelType().toString();
            int nbMonsters = switch ((monsterName == null ? "" : monsterName))
            {
                case "Gobelin" -> rand.nextInt(4, 6);
                case "Orc" -> rand.nextInt(2, 6);
                case "Skeleton" -> rand.nextInt(5, 10);
                default -> 5;
            };
            minX += 50;
            
            for (int i = 0; i < nbMonsters; i++)
            {
                NonPlayerCharacter npc;
                V2D size = Graphism.Animation.get(monsterName, "walk_bot").getFrameSize();

                int scaledWidth = (int) (size.x * 2);
                int scaledHeight = (int) (size.y * 2);

                Rectangle newRect;
                boolean overlap;
                int attempts = 0;
                double ox = 0, oy = 0;

                do {
                    overlap = false;
                    ox = rand.nextInt(maxX - minX - scaledWidth) + minX;
                    oy = rand.nextInt(maxY - minY - scaledHeight) + minY;
                    newRect = new Rectangle((int) ox - 20, (int) oy - 20, scaledWidth + 20, scaledHeight + 20);

                    // Vérifie le chevauchement
                    for (Rectangle rPlaced : placedObstacles) {
                        if (newRect.intersects(rPlaced)) {
                            overlap = true;
                            break;
                        }
                    }
                    attempts++;
                } while (overlap && attempts < 100);

                if (overlap) continue;
                
                placedObstacles.add(newRect);

                npc = switch ((monsterName == null ? "" : monsterName))
                {
                    case "Gobelin" -> new Gobelin(new V2D(1.0, 1.0), new V2D(ox, oy), new V2D(28.0, 26.0));
                    case "Orc" -> new Orc(new V2D(1.5, 1.5), new V2D(ox, oy), new V2D(28.0, 26.0));
                    case "Skeleton" -> new Skeleton(new V2D(1.25, 1.25), new V2D(ox, oy), new V2D(28.0, 26.0));
                    default -> null;
                };
                if (npc != null) this.addObject(npc);
            }
        }
    }
}
