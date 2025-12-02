package Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Objects.Door;
import Objects.Merchant;
import Objects.Obstacle;
import Objects.Tile;
import Utilitary.V2D;

/**
 * Salle spéciale de type « salle du marchand ».
 * <p>
 * Cette pièce :
 * <ul>
 *   <li>est automatiquement marquée comme déjà nettoyée (aucun combat à y mener),</li>
 *   <li>génère un sol et quelques craquelures décoratives,</li>
 *   <li>place un marchand {@link Merchant} dans la zone centrale,</li>
 *   <li>crée les portes de connexion vers les salles adjacentes en fonction de
 *       sa position dans la grille du {@link Level},</li>
 *   <li>ajoute un ensemble d’éléments décoratifs (braseros, boucliers muraux,
 *       piles d’or, tonneaux, coffres, etc.) pour renforcer l’ambiance de boutique.</li>
 * </ul>
 * Cette salle sert de zone de repos et d’achat entre deux combats.
*/
public class MerchantRoom extends Room {

    /**
     * Construit une salle de marchand aux coordonnées données dans la grille du niveau.
     * <p>
     * Le constructeur :
     * <ul>
     *   <li>marque la salle comme déjà nettoyée,</li>
     *   <li>pose le sol et des craquelures décoratives,</li>
     *   <li>instancie le marchand et le positionne,</li>
     *   <li>génère les portes et murs en fonction de la position (bords ou milieu de la carte),</li>
     *   <li>ajoute les éléments décoratifs de la boutique (or, braseros, tonneaux, coffre, etc.).</li>
     * </ul>
     *
     * @param posX indice de la salle sur l’axe X dans la grille du {@link Level}
     * @param posY indice de la salle sur l’axe Y dans la grille du {@link Level}
    */
    public MerchantRoom(int posX, int posY) {
        super();
        
        final int mapWidth = 1024;
        final int mapHeight = 744;
        final int offsetX = 415;
        final int offsetY = 108;
        this.setbCleared(false);

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

        Merchant m = new Merchant(new V2D(1.5, 1.5), new V2D(875, 475), new V2D(28, 50));

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
        // this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 350, offsetY + 32), new V2D(-1, -1), "TileSet", "torch"));
        // this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 550, offsetY + 32), new V2D(-1, -1), "TileSet", "torch"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 860, offsetY + 520), new V2D(0, 0), "TileSet", "brasero"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 60, offsetY + 520), new V2D(0, 0), "TileSet", "brasero"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 860, offsetY + 115), new V2D(0, 0), "TileSet", "brasero"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 60, offsetY + 115), new V2D(0, 0), "TileSet", "brasero"));
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 350, offsetY + 72), new V2D(0, 0), "TileSet", "shield_wall"));
        this.addObject(new Obstacle(new V2D(3, 3), new V2D(offsetX + 625, offsetY + 72), new V2D(0, 0), "TileSet", "shield_wall"));

        this.addObject(new Obstacle(new V2D(5, 5), new V2D(offsetX + 325, offsetY + 225), new V2D(0, 0), "TileSet", "gold_pile"));
        this.addObject(new Obstacle(new V2D(5, 5), new V2D(offsetX + 200, offsetY + 250), new V2D(0, 0), "TileSet", "gold_pile"));
        this.addObject(new Obstacle(new V2D(5, 5), new V2D(offsetX + 275, offsetY + 300), new V2D(0, 0), "TileSet", "gold_pile"));
        this.addObject(new Obstacle(new V2D(5, 5), new V2D(offsetX + 800, offsetY + 475), new V2D(0, 0), "TileSet", "gold_pile"));
        this.addObject(new Obstacle(new V2D(5, 5), new V2D(offsetX + 725, offsetY + 525), new V2D(0, 0), "TileSet", "gold_pile"));

        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 175, offsetY + 550), new V2D(0, 0), "TileSet", "barrel_1"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 725, offsetY + 250), new V2D(0, 0), "TileSet", "barrel_2"));
        this.addObject(new Obstacle(new V2D(3.5, 3.5), new V2D(offsetX + 460, offsetY + 350), new V2D(0, 0), "TileSet", "chest"));

        this.addObject(m);

    }
}
