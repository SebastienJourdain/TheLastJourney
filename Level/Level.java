package Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Graphism.Window;
import Objects.Player;
import UI.UI;
import UI.UIMerchant;

/**
 * Gère la génération procédurale et l’état global d’un étage du donjon.
 * <p>
 * Cette classe maintient :
 * <ul>
 *   <li>la grille de salles ({@link Room}) du niveau courant,</li>
 *   <li>la position actuelle du joueur dans cette grille,</li>
 *   <li>le type de niveau (famille de monstres rencontrés),</li>
 *   <li>le nombre de salles à explorer avant l’apparition de la salle de boss,</li>
 *   <li>ainsi que la progression en « étages » (floors).</li>
 * </ul>
 * Toutes les méthodes et champs sont statiques et représentent l’état global du niveau.
*/
public class Level {

    private static int posX = 0;
    private static int posY = 0;
    private static int roomsBeforeBoss = 1;
    private static int floor = 1;
    private static int nbrRoomCleaned = 0;
    private static final List<List<Room>> rooms = new ArrayList<>();

    private static int width;
    private static int height;

    private static final Random random = new Random();

    /**
     * Types de niveaux possibles, déterminant principalement la famille de monstres
     * rencontrés dans les salles classiques (gobelins, orcs, squelettes, etc.).
    */
    public enum ELevelType
    {
        Gobelin,
        Orc,
        Skeleton
    };

    private static ELevelType levelType = ELevelType.Skeleton;

    /**
     * Génère un nouvel étage de donjon.
     * <p>
     * Cette méthode :
     * <ul>
     *   <li>réinitialise et vide la liste des salles,</li>
     *   <li>choisit aléatoirement un type de niveau ({@link ELevelType}),</li>
     *   <li>détermine aléatoirement une taille de grille (3x4 ou 4x3),</li>
     *   <li>construit une salle classique ({@link ClassicRoom}) pour chaque case de la grille,</li>
     *   <li>choisit et enregistre la salle de départ du joueur sur un bord de la carte,</li>
     *   <li>place une salle de marchand ({@link MerchantRoom}) à une position distincte de la salle de départ.</li>
     * </ul>
    */
    public static void generateLevel() {
        rooms.clear();
        levelType = ELevelType.values()[random.nextInt(3)];
        roomsBeforeBoss = random.nextInt(6, 10);

        // --- Taille aléatoire : 4x3 ou 3x4 ---
        if (random.nextBoolean()) {
            width = 4;
            height = 3;
        } else {
            width = 3;
            height = 4;
        }

        // --- Génération initiale de salles classiques ---
        for (int y = 0; y < height; y++) {
            List<Room> line = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                line.add(new ClassicRoom(x, y, true));
            }
            rooms.add(line);
        }

        // --- Sélection de la salle de départ ---
        int startEdge = random.nextInt(2); // 0 = haut, 1 = bas, 2 = gauche, 3 = droite
        switch (startEdge) {
            case 0 -> { posX = width / 2; posY = 0; }               // haut
            case 1 -> { posX = 0; posY = height / 2; }               // gauche
        }

        //Pour tester la bossroom
        rooms.get(posY).set(posX, new ClassicRoom(posX, posY, false));
        rooms.get(posY).get(posX).setbCleared(true);

        // --- Placement du marchand ---
        int merchantX, merchantY;
        do {
            merchantX = random.nextInt(width);
            merchantY = random.nextInt(height);
        } while (merchantX == posX && merchantY == posY);

        rooms.get(merchantY).set(merchantX, new MerchantRoom(merchantX, merchantY));
    }

    public static void dischargeLevel()
    {        
        rooms.clear();
        floor = 1;
        nbrRoomCleaned = 0;

        UI.UIs.get(UI.EGameState.InGame).getElements().clear();

        Window.window.getPanel().removeKeyListener(Player.getPlayer1());
        Window.window.getPanel().removeMouseListener(Player.getPlayer1());
        Window.window.getPanel().removeMouseMotionListener(Player.getPlayer1());

        Player.setPlayer1(null);
    }

    public static List<List<Room>> getRooms() {
        return rooms;
    }

    public static int getPosX() {
        return posX;
    }

    public static int getPosY() {
        return posY;
    }

    /**
     * Décrémente le nombre de salles à visiter avant l’apparition de la salle de boss.
     * <p>
     * Lorsque le compteur atteint zéro, une salle aléatoire non encore nettoyée
     * et qui n’est pas une salle de marchand est transformée en {@link BossRoom}.
    */
    public static void decRoomsBeforeBoss() {
        roomsBeforeBoss--;
        if (roomsBeforeBoss == 0)
        {
            boolean bossRoomAppeared = false;
            while (!bossRoomAppeared)
            {
                int y = random.nextInt(height);
                int x = random.nextInt(width);
                if (!rooms.get(y).get(x).isbCleared() && !(rooms.get(y).get(x) instanceof MerchantRoom))
                {
                    rooms.get(y).set(x, new BossRoom(x, y));
                    bossRoomAppeared = true;
                }
            }
        }
    }

    /**
     * Retourne la salle actuelle dans laquelle se trouve le joueur.
     * <p>
     * La salle est déterminée par les coordonnées statiques {@code posX} et {@code posY}
     * dans la grille de salles. Si ces coordonnées ne sont pas valides, la méthode renvoie {@code null}.
     *
     * @return la salle courante ({@link Room}) ou {@code null} si aucune salle valide n’existe
    */
    public static Room getActualRoom() {
        if (posY < rooms.size() && posY >= 0) {
            List<Room> line = rooms.get(posY);
            if (posX < line.size() && posX >= 0) {
                return line.get(posX);
            }
        }
        return null;
    }

    public static void setPosX(int posX) {
        Level.posX = posX;
    }

    public static void setPosY(int posY) {
        Level.posY = posY;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getFloor() {
        return floor;
    }

    public static ELevelType getLevelType() {
        return levelType;
    }

    public static void setLevelType(ELevelType levelType) {
        Level.levelType = levelType;
    }

    /**
     * Avance au prochain étage du donjon.
     * <p>
     * Cette méthode :
     * <ul>
     *   <li>incrémente l’indice de niveau ({@code floor}),</li>
     *   <li>génère un nouveau niveau via {@link #generateLevel()},</li>
     *   <li>repositionne le joueur près du point d’apparition de la nouvelle carte,</li>
     *   <li>et ajoute le joueur à la salle actuellement active.</li>
     * </ul>
    */
    public static void nextFloor()
    {
        floor++;
        generateLevel();
        Player p = Player.getPlayer1();
        p.addX((900 - p.getPosition().x) / p.getCaracteristics().getSpeed());
        p.addY((700 - p.getPosition().y) / p.getCaracteristics().getSpeed());
        getActualRoom().addObject(p);
        ((UIMerchant)UI.UIs.get(UI.EGameState.UIMerchant)).m = null;
    }

    public static int getNbrRoomCleaned() {
        return nbrRoomCleaned;
    }

    public static void setNbrRoomCleaned(int nbrRoomCleaned) {
        Level.nbrRoomCleaned = nbrRoomCleaned;
    }
}
