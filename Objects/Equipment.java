package Objects;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un équipement que le joueur peut obtenir.  
 *  
 * Un équipement peut :
 * <ul>
 *   <li>fournir des caractéristiques bonus ({@link Caracteristics}) ;</li>
 *   <li>posséder un taux de drop ;</li>
 *   <li>avoir un prix (pour les marchands) ;</li>
 *   <li>déclencher des effets contextuels via des callbacks</li>
 * </ul>
 *
 * Un ensemble d’équipements prédéfinis est accessible via
 * {@link Equipment#getEquipmentsList()}.
 */
public class Equipment implements EquipmentEffect
{
    public enum EEquipmentType
    {
        Ring,
        Bracelet,
        Book
    }

    private static List<String> equipmentNames = List.of(
        "Anneau du Paon",
        "Anneau de vitalité",
        "Livre du voyageur",
        "Anneau d'armure magique",
        "Bracelet d'acier runique",
        "Livre du souffle astral",
        "Anneau sanglant",
        "Bracelet de chance",
        "Livre de la tempête",

        "Bague ébréchée",
        "Bracelet de vitesse",

        "Anneau du pugiliste",
        "Livre d'épines",

        "Anneau explosif",
        "Bracelet de l'invincible"
    );

    private static Map<String, Equipment> equipmentsList = Map.ofEntries(

        Map.entry("Anneau de vitalité", new Equipment(
            new Caracteristics(2, 0, 0, 0.f, 0.f),
            0.2f,
            40,
            "Force vitale accrue.",
            EEquipmentType.Ring,
            (Equipment e) -> { Player.getPlayer1().getCaracteristics().setActHP(Player.getPlayer1().getCaracteristics().getActHP() + 1); },
            null, null, null, null
        )),

        Map.entry("Livre du voyageur", new Equipment(
            new Caracteristics(0, 0, 0, 0.3f, 0.f),
            0.25f,
            20,
            "Guide des chemins.",
            EEquipmentType.Book,
            null, null, null, null, null
        )),

        Map.entry("Anneau d'armure magique", new Equipment(
            new Caracteristics(0, 2, 0, 0.f, 0.f),
            0.18f,
            30,
            "Protection mystique.",
            EEquipmentType.Ring,
            null, null, null, null, null
        )),

        Map.entry("Bracelet d'acier runique", new Equipment(
            new Caracteristics(0, 0, 1, 0.f, 0.1f),
            0.15f,
            35,
            "Runes d’acier actif.",
            EEquipmentType.Bracelet,
            null, null, null, null, null
        )),

        Map.entry("Livre du souffle astral", new Equipment(
            new Caracteristics(0, 0, 0, 0.3f, 0.1f),
            0.12f,
            45,
            "Souffle d’ailleurs.",
            EEquipmentType.Book,
            null,
            (Equipment e) -> { teleportPlayer(e); },
            null,
            null,
            null
        )),

        Map.entry("Anneau sanglant", new Equipment(
            new Caracteristics(0, 0, 2, 0.f, 0.0f),
            0.08f,
            50,
            "Puissance du sang.",
            EEquipmentType.Ring,
            null,
            null,
            (Equipment e) -> { lifeSteal(e); },
            null,
            null
        )),

        Map.entry("Bracelet de chance", new Equipment(
            new Caracteristics(0, 0, 0, 0.f, 0.15f),
            0.07f,
            45,
            "Favorise le destin.",
            EEquipmentType.Bracelet,
            null,
            null,
            null,
            (Equipment e) -> { autoCrit(e); },
            null
        )),

        Map.entry("Livre de la tempête", new Equipment(
            new Caracteristics(0, 0, 0, 0.2f, 0.1f),
            0.05f,
            45,
            "Appel du tonnerre.",
            EEquipmentType.Book,
            null,
            null,
            null,
            null,
            (Equipment e) -> { thunderDash(e); }
        )),

        // --- Peu puissants et peu chers ---

        Map.entry("Bague ébréchée", new Equipment(
            new Caracteristics(1, 0, 0, 0.f, 0.f),
            0.35f,
            15,
            "Vie fragile, tenace.",
            EEquipmentType.Ring,
            null, null, null, null, null
        )),

        Map.entry("Bracelet de vitesse", new Equipment(
            new Caracteristics(0, 0, 0, 0.15f, 0.0f),
            0.30f,
            15,
            "Pas plus rapides.",
            EEquipmentType.Bracelet,
            null, null, null, null, null
        )),

        Map.entry("Anneau du Paon", new Equipment(
            new Caracteristics(0, 0, 0, 0.15f, 0.0f),
            0.30f,
            15,
            "Crac crac boum boum.",
            EEquipmentType.Ring,
            null, null, null, null, null
        )),

        // --- Standards ---

        Map.entry("Anneau du pugiliste", new Equipment(
            new Caracteristics(0, 0, 1, 0.f, 0.05f),
            0.16f,
            30,
            "Frappe affûtée.",
            EEquipmentType.Ring,
            null, null, null, null, null
        )),

        Map.entry("Livre d'épines", new Equipment(
            new Caracteristics(0, 0, 0, 0.f, 0.f),
            0.14f,
            45,
            "Épine en retour.",
            EEquipmentType.Book,
            null,
            (Equipment e) -> { damagePayBack(e); },
            null,
            null,
            null
        )),

        // --- Chers et puissants ---

        Map.entry("Anneau explosif", new Equipment(
            new Caracteristics(0, 0, 2, 0.f, 0.f),
            0.06f,
            60,
            "Colère instable.",
            EEquipmentType.Ring,
            null,
            null,
            (Equipment e) -> { ennemyExplosion(e); },
            null,
            null
        )),

        Map.entry("Bracelet de l'invincible", new Equipment(
            new Caracteristics(3, 2, 0, 0.0f, 0.0f),
            0.04f,
            60,
            "Endurance sacrée.",
            EEquipmentType.Bracelet,
            (Equipment e) -> { Player.getPlayer1().getCaracteristics().setActHP(Player.getPlayer1().getCaracteristics().getActHP() + 2); },
            null, null, null, null
        ))

    );

    /** Bonus accordés par l'équipement. */
    private Caracteristics bonus = null;

    /** Probabilité de drop de l’objet. */
    private float dropRate = 0.1f;

    /** Prix d'achat chez le marchand. */
    private int price = 0;

    private String name = "";

    private String description = "";

    private EEquipmentType type = EEquipmentType.Ring;

    private int counter = 0;

    /** Effet déclenché à la fin d'une salle. */
    private Consumer<Equipment> onRoomClearedRunnable = null;

    /** Effet déclenché lorsque le joueur subit un coup. */
    private Consumer<Equipment> onHitRunnable = null;

    /** Effet déclenché lorsqu'un ennemi est tué. */
    private Consumer<Equipment> onEnemyKilledRunnable = null;

    /** Effet déclenché lorsque le joueur touche un ennemi. */
    private Consumer<Equipment> onEnemyHitRunnable = null;

    /** Effet déclenché lors d’un dash. */
    private Consumer<Equipment> onDashRunnable = null;

    /**
     * Crée un équipement simple avec bonus, taux de drop et prix.
     *
     * @param bonus     bonus de caractéristiques octroyés
     * @param dropRate  probabilité de drop
     * @param price     coût d’achat
     */
    public Equipment(Caracteristics bonus, float dropRate, int price)
    {
        this.bonus = bonus;
        this.dropRate = dropRate;
        this.price = price;
    }

    /**
     * Crée un équipement avec bonus et callbacks personnalisés.
     *
     * @param bonus    bonus de caractéristiques
     * @param dropRate taux de drop
     * @param price    prix de l’objet
     * @param orc      callback exécuté lors du nettoyage d’une salle
     * @param oh       callback lors d’un coup reçu
     * @param oek      callback lors d’un ennemi tué
     * @param oeh      callback lors d’un coup porté
     * @param od       callback lors d’un dash
     */
    public Equipment(Caracteristics bonus, float dropRate, int price, String description, EEquipmentType type, Consumer<Equipment> orc, Consumer<Equipment> oh, Consumer<Equipment> oek, Consumer<Equipment> oeh, Consumer<Equipment> od)
    {
        this.bonus = bonus;
        this.dropRate = dropRate;
        this.price = price;
        this.description = description;
        this.type = type;
        this.onRoomClearedRunnable = orc;
        this.onHitRunnable = oh;
        this.onEnemyKilledRunnable = oek;
        this.onEnemyHitRunnable = oeh;
        this.onDashRunnable = od;
    }

    /**
     * Constructeur de copie profonde.
     *
     * @param e équipement à copier
     */
    public Equipment(Equipment e)
    {
        this.bonus = new Caracteristics(e.bonus);
        this.dropRate = e.dropRate;
        this.price = e.price;
        this.description = e.description;
        this.name = e.name;
        this.type = e.type;

        this.onRoomClearedRunnable = e.onRoomClearedRunnable;
        this.onHitRunnable = e.onHitRunnable;
        this.onEnemyKilledRunnable = e.onEnemyKilledRunnable;
        this.onEnemyHitRunnable = e.onEnemyHitRunnable;
        this.onDashRunnable = e.onDashRunnable;
    }

    /**
     * Crée un équipement à partir de son nom dans la liste statique.
     *
     * @param name nom de l’équipement
     * @throws IllegalArgumentException si l’équipement n’existe pas
     */
    public Equipment(String name)
    {
        Equipment e = equipmentsList.get(name);
        if (e == null) throw new IllegalArgumentException("Unknown equipment: " + name);

        this.bonus = new Caracteristics(e.bonus);
        this.dropRate = e.dropRate;
        this.price = e.price;
        this.description = e.description;
        this.name = name;
        this.type = e.type;

        this.onRoomClearedRunnable = e.onRoomClearedRunnable;
        this.onHitRunnable = e.onHitRunnable;
        this.onEnemyKilledRunnable = e.onEnemyKilledRunnable;
        this.onEnemyHitRunnable = e.onEnemyHitRunnable;
        this.onDashRunnable = e.onDashRunnable;
    }

    /** {@inheritDoc} */
    @Override
    public void onRoomCleared()
    {
        if (this.onRoomClearedRunnable != null) this.onRoomClearedRunnable.accept(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onHit()
    {
        if (this.onHitRunnable != null) this.onHitRunnable.accept(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onEnemyKilled()
    {
        if (this.onEnemyKilledRunnable != null) this.onEnemyKilledRunnable.accept(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onEnemyHit()
    {
        if (this.onEnemyHitRunnable != null) this.onEnemyHitRunnable.accept(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onDash()
    {
        if (this.onDashRunnable != null) this.onDashRunnable.accept(this);
    }

    public static void teleportPlayer(Equipment e)
    {
        Player p = Player.getPlayer1();
        V2D nmiCtr = new V2D(0,0);
        int nbrNmi = 0;
        for (CObject o : Level.getActualRoom().getObjects())
        {
            if (o instanceof NonPlayerCharacter npc)
            {
                nmiCtr.x += npc.getPosition().x;
                nmiCtr.y += npc.getPosition().y;
                nbrNmi++;
            }
        }

        if (nbrNmi == 0) return;

        nmiCtr.x /= nbrNmi;
        nmiCtr.y /= nbrNmi;

        double maxDist = 300.0;
        int angleStep  = 10;

        V2D bestPos = new V2D(p.getPosition().x, p.getPosition().y);
        double bestDist2 = 0.0;

        for (int angleDeg = 0; angleDeg < 360; angleDeg += angleStep)
        {
            double angleRad = Math.toRadians(angleDeg);
            double dirX = Math.cos(angleRad);
            double dirY = Math.sin(angleRad);

            V2D candidate = p.getCollision().getLongestDash(new V2D(dirX, dirY), maxDist);
            

            if (candidate != null && candidate.x != 0 && candidate.y != 0)
            {
                double dx = (p.getPosition().x + candidate.x) - nmiCtr.x;
                double dy = (p.getPosition().y + candidate.y) - nmiCtr.y;
                double dist2 = dx * dx + dy * dy;

                if (dist2 > bestDist2)
                {
                    bestDist2 = dist2;
                    bestPos.x = candidate.x;
                    bestPos.y = candidate.y;
                }
            }
        }

        p.addX(bestPos.x / p.getCaracteristics().getSpeed());
        p.addY(bestPos.y / p.getCaracteristics().getSpeed());
    }

    public static void lifeSteal(Equipment e)
    {
        e.counter++;
        if (e.counter == 5)
        {
            e.counter = 0;
            Player p = Player.getPlayer1();
            p.getCaracteristics().setActHP(p.getCaracteristics().getActHP() + 1);
        }
    }

    public static void thunderDash(Equipment e)
    {
        Player p = Player.getPlayer1();
        ZoneDamage damage = new ZoneDamage(new V2D(3.0, 3.0), new V2D(p.getPosition().x, p.getPosition().y), new V2D(32, 32), p.getCaracteristics().getDamage(), 12, 8, p, "thunder", 60.f, true);
        Level.getActualRoom().addObject(damage);
    }

    public static void damagePayBack(Equipment e)
    {
        Player p = Player.getPlayer1();
        if (p == null) return;
        for (Collision c : p.getCollision().getColliding())
        {
            if (c.getObject() instanceof Damage d && d.getOwner() instanceof NonPlayerCharacter npc)
            {
                int damage = d.getDamage() / 2;
                damage = damage == 0 ? 1 : damage;
                npc.takeDamage(damage);
            }
        }
    }

    public static void ennemyExplosion(Equipment e)
    {
        Player p = Player.getPlayer1();
        if (p == null) return;
        for (CObject o : Level.getActualRoom().getObjects())
        {
            if (o instanceof Damage d && d.getOwner() == p)
            {
                for (CObject o2 : d.getAlreadyHitted())
                {
                    if (o2 instanceof NonPlayerCharacter npc && npc.getCaracteristics().getActHP() <= 0)
                    {
                        ZoneDamage damage = new ZoneDamage(new V2D(2.0, 2.0), new V2D(npc.getPosition()), new V2D(32, 32), p.getCaracteristics().getDamage(), 14, 10, p, "small_explosion", 60.f, true);
                        Level.getActualRoom().addObject(damage);
                    }
                }
            }
        }
    }

    public static void autoCrit(Equipment e)
    {
        Player p = Player.getPlayer1();
        if (p == null) return;
        if (e.counter == 10)
        {
            p.getCaracteristics().setCritical(p.getCaracteristics().getCritical() + 100.f);
            e.counter++;
        }
        else if (e.counter > 10)
        {
            p.getCaracteristics().setCritical(p.getCaracteristics().getCritical() - 100.f);
            e.counter = 0;
        }
        else e.counter++;
    }
    /**
     * @return la liste statique des équipements prédéfinis
     */
    public static Map<String, Equipment> getEquipmentsList() { return equipmentsList; }
    public static List<String> getEquipmentsNames() { return equipmentNames; }

    public int getPrice() { return this.price; }

    public Caracteristics getBonus() {
        return this.bonus;
    }

    public float getDropRate() {
        return this.dropRate;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public EEquipmentType getType() {
        return this.type;
    }
}