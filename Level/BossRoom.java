package Level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Objects.CObject;
import Objects.Door;
import Objects.LevelEnd;
import Objects.Minotaurus;
import Objects.Necromancer;
import Objects.Obstacle;
import Objects.OrcBoss;
import Objects.Tile;
import Objects.Trap;
import Utilitary.V2D;

/**
 * Salle spéciale de type « boss room ».
 * <p>
 * Cette pièce :
 * <ul>
 *   <li>génère un sol et un décor fixes adaptés au combat de boss,</li>
 *   <li>place aléatoirement des craquelures et obstacles sans chevauchement,</li>
 *   <li>instancie le boss (Minotaurus) au centre de la zone de combat,</li>
 *   <li>crée les portes de connexion vers les salles adjacentes en fonction de la position
 *       de la pièce dans la grille du niveau,</li>
 *   <li>place la sortie de fin de niveau qui pourra être activée une fois le boss vaincu.</li>
 * </ul>
 * Elle hérite du comportement générique d’une {@link Room}.
*/
public class BossRoom extends Room {

    /**
     * Construit une salle de boss aux coordonnées données dans la grille de niveau.
     *
     * @param posX indice de la salle sur l’axe X dans la grille du {@link Level}
     * @param posY indice de la salle sur l’axe Y dans la grille du {@link Level}
     *             (sert notamment à décider où placer les portes et les murs pleins).
    */
    public BossRoom(int posX, int posY) {
        super();
        
        final int mapWidth = 1024;
        final int mapHeight = 744;
        final int offsetX = 415;
        final int offsetY = 108;

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

        // Escalier de fin de niveau
        LevelEnd le = new LevelEnd(new V2D(4, 4), new V2D(880, 488), new V2D(-1, -1), "TileSet", "stair");
        this.addObject(le);

        // Obstacles et pièges
        int nbObstacles = rand.nextInt(5, 8);
        String[] types = {"skull_pile", "skulls", "skeleton"};
        boolean skullPile = false;
        ArrayList<Rectangle> placedObstacles = new ArrayList<>();
        //BOSS
        V2D bossSize = Graphism.Animation.get("Necromancer", "walk_bot").getFrameSize();
        placedObstacles.add(new Rectangle(offsetX + 400, offsetY + 300, ((int)bossSize.x * 2) + 100, ((int)bossSize.y * 2) + 100));
        switch (rand.nextInt(3))
        {
            case 0 -> this.addObject(new Necromancer(new V2D(450 + offsetX, 350 + offsetY)));
            case 1 -> this.addObject(new OrcBoss(new V2D(450 + offsetX, 350 + offsetY)));
            case 2 -> this.addObject(new Minotaurus(new V2D(450 + offsetX, 350 + offsetY)));
            default -> { }
        }

        for (int j = 0; j < nbObstacles; j++) {
            String obstacleType = types[rand.nextInt(types.length)];
            if (skullPile) while (obstacleType.equals("skull_pile")) obstacleType = types[rand.nextInt(types.length)];
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
            } while (overlap && attempts < 200);

            if (overlap) continue;

            placedObstacles.add(newRect);

            if (obstacleType.equals("inactive_trap")) {
                this.addObject(new Trap(new V2D(3, 3), new V2D(ox, oy)));
            } else {
                this.addObject(new Obstacle(new V2D(3, 3), new V2D(ox, oy), new V2D(0, 0), "TileSet", obstacleType));
                if (obstacleType.equals("skull_pile")) skullPile = true;
            }
        }

        // Portes
        if (posY > 0)
        {
            //Porte haute
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 448, offsetY + 0), new V2D(0, 0), "TileSet", "door_top"));
            this.addObject(new Obstacle(new V2D(4, 4), new V2D(offsetX + 512, offsetY + 0), new V2D(0, 0), "TileSet", "door_top"));
            this.addObject(new Door(new V2D(4, 4), new V2D(offsetX + 448, offsetY + 64), new V2D(32, 24), "TileSet", "bossdoor_closed", posX, posY - 1));
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
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 300, offsetY + 72), new V2D(-1, -1), "TileSet", "alcove"));
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 305, offsetY + 72), new V2D(-1, -1), "TileSet", "candles"));
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 664, offsetY + 72), new V2D(-1, -1), "TileSet", "alcove"));
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 669, offsetY + 72), new V2D(-1, -1), "TileSet", "candles"));
    }

    /**
     * Active la sortie de fin de niveau en agrandissant sa zone de collision.
     * <p>
     * Parcourt tous les objets de la salle, trouve l’instance de {@link LevelEnd}
     * et augmente la taille de sa collision afin que le joueur puisse interagir
     * plus facilement avec l’escalier de sortie une fois le boss vaincu.
    */
    public void openLevelEnd()
    {
        for (CObject o : this.getObjects())
        {
            if (o instanceof LevelEnd)
            {
                o.getCollision().setSize(new V2D(24.0 * 4, 24.0 * 4));
                o.getCollision().setbActive(true);
                return;
            }
        }
    }
}
