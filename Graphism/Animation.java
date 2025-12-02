package Graphism;

import java.util.HashMap;
import java.util.Map;

import Utilitary.V2D;

public class Animation {

    private final int frameNbr;
    private final V2D frameSize;
    private final V2D frameStart;
    private final boolean bInfinite;
    private boolean bHorizontal = true;
    private float animationSpeed = 1.f;
    private static final Map<String, Map<String, Animation>> animationsMap = new HashMap<>();

    public Animation(int frameNbr, V2D frameSize, V2D frameStart, boolean bInfinite) {
        this.frameNbr = frameNbr;
        this.frameSize = frameSize;
        this.frameStart = frameStart;
        this.bInfinite = bInfinite;
    }
    public Animation(int frameNbr, V2D frameSize, V2D frameStart, boolean bInfinite, float animationSpeed) {
        this.frameNbr = frameNbr;
        this.frameSize = frameSize;
        this.frameStart = frameStart;
        this.bInfinite = bInfinite;
        this.animationSpeed = animationSpeed;
    }

    public Animation(int frameNbr, V2D frameSize, V2D frameStart, boolean bInfinite, boolean bHorizontal) {
        this.frameNbr = frameNbr;
        this.frameSize = frameSize;
        this.frameStart = frameStart;
        this.bInfinite = bInfinite;
        this.bHorizontal = bHorizontal;
    }

    public Animation(int frameNbr, V2D frameSize, V2D frameStart, boolean bInfinite, boolean bHorizontal, float animationSpeed) {
        this.frameNbr = frameNbr;
        this.frameSize = frameSize;
        this.frameStart = frameStart;
        this.bInfinite = bInfinite;
        this.bHorizontal = bHorizontal;
        this.animationSpeed = animationSpeed;
    }

    static {
        HashMap<String, Animation> tileAnims = new HashMap<>();

        HashMap<String, Animation> UIAnims = new HashMap<>();

        HashMap<String, Animation> archerAnims = new HashMap<>();
        HashMap<String, Animation> warriorAnims = new HashMap<>();
        HashMap<String, Animation> mageAnims = new HashMap<>();

        HashMap<String, Animation> gobAnims = new HashMap<>();
        HashMap<String, Animation> orcAnims = new HashMap<>();
        HashMap<String, Animation> skeletonsAnims = new HashMap<>();

        HashMap<String, Animation> minotaurusAnims = new HashMap<>();
        HashMap<String, Animation> jackAnims = new HashMap<>();
        HashMap<String, Animation> orcBossAnims = new HashMap<>();
        HashMap<String, Animation> necromancerAnims = new HashMap<>();

        HashMap<String, Animation> merchantAnims = new HashMap<>();

        HashMap<String, Animation> effectsAnims = new HashMap<>();

        // Coin supérieur gauche
        tileAnims.put("top_left_corner", new Animation(1, new V2D(32, 48), new V2D(48, 0), true));

        // Coin supérieur droit
        tileAnims.put("top_right_corner", new Animation(1, new V2D(32, 48), new V2D(64, 0), true));

        // Mur latéral gauche
        tileAnims.put("left_wall", new Animation(1, new V2D(14, 40), new V2D(192, 248), true));

        // Mur latéral droit
        tileAnims.put("right_wall", new Animation(1, new V2D(14, 40), new V2D(178, 248), true));

        // Coin inférieur gauche
        tileAnims.put("bottom_left_corner", new Animation(1, new V2D(32, 17), new V2D(48, 47), true));

        // Coin inférieur droit
        tileAnims.put("bottom_right_corner", new Animation(1, new V2D(32, 17), new V2D(64, 47), true));

        // Mur bas
        tileAnims.put("bottom_wall", new Animation(1, new V2D(16, 17), new V2D(64, 47), true));

        // Mur haut
        tileAnims.put("top_wall", new Animation(1, new V2D(16, 48), new V2D(64, 0), true));

        // Porte haute en bois
        tileAnims.put("door_closed", new Animation(1, new V2D(32, 32), new V2D(209, 96), true));
        tileAnims.put("door_opened", new Animation(1, new V2D(32, 32), new V2D(337, 96), true));
        tileAnims.put("door", new Animation(5, new V2D(32, 32), new V2D(209, 96), false));
        // Porte haute en herse
        tileAnims.put("bossdoor", new Animation(5, new V2D(32, 32), new V2D(209, 333), false));
        tileAnims.put("bossdoor_opened", new Animation(1, new V2D(32, 32), new V2D(337, 333), true));
        tileAnims.put("bossdoor_closed", new Animation(1, new V2D(32, 32), new V2D(209, 333), true));
        // Dessus de porte
        tileAnims.put("door_top", new Animation(1, new V2D(16, 16), new V2D(64, 0), true));

        //Porte gauche
        tileAnims.put("door_top_left", new Animation(1, new V2D(14, 10), new V2D(246, 218), true));
        tileAnims.put("door_bottom_left", new Animation(1, new V2D(14, 10), new V2D(246, 230), true));

        //Void porte gauche
        tileAnims.put("void_door_left", new Animation(1, new V2D(10, 17), new V2D(253, 275), true));

        //Porte droite
        tileAnims.put("door_top_right", new Animation(1, new V2D(14, 10), new V2D(232, 218), true));
        tileAnims.put("door_bottom_right", new Animation(1, new V2D(14, 10), new V2D(232, 230), true));

        //Void porte droite
        tileAnims.put("void_door_right", new Animation(1, new V2D(10, 17), new V2D(241, 275), true));

        //Porte basse
        tileAnims.put("door_bottom_l", new Animation(1, new V2D(8, 17), new V2D(198, 224), true));
        tileAnims.put("door_bottom_r", new Animation(1, new V2D(8, 17), new V2D(178, 224), true));

        //Void porte basse
        tileAnims.put("void_door_bottom", new Animation(1, new V2D(17, 10), new V2D(266, 278), true));

        //Sortie de niveau
        tileAnims.put("stair", new Animation(1, new V2D(24, 24), new V2D(4, 437), true));

        // Sol
        tileAnims.put("floor", new Animation(1, new V2D(34, 38), new V2D(7, 85), true));

        //Craquelures de sol
        tileAnims.put("crack_1", new Animation(1, new V2D(14, 13), new V2D(177, 338), true));
        tileAnims.put("crack_2", new Animation(1, new V2D(14, 13), new V2D(193, 338), true));
        tileAnims.put("crack_3", new Animation(1, new V2D(13, 12), new V2D(178, 354), true));
        tileAnims.put("crack_4", new Animation(1, new V2D(10, 8), new V2D(194, 356), true));

        //Obstacles
        tileAnims.put("barrel_1", new Animation(1, new V2D(27, 30), new V2D(292, 416), true));
        tileAnims.put("barrel_2", new Animation(1, new V2D(29, 32), new V2D(320, 400), true));
        tileAnims.put("box", new Animation(1, new V2D(25, 30), new V2D(258, 378), true));
        //Obstacles salle de boss
        tileAnims.put("skull_pile", new Animation(1, new V2D(70, 50), new V2D(199, 919), true));
        tileAnims.put("skulls", new Animation(1, new V2D(24, 21), new V2D(370, 1017), true));
        tileAnims.put("skeleton", new Animation(1, new V2D(27, 21), new V2D(338, 1013), true));
        //Salle du marchand
        tileAnims.put("chest", new Animation(1, new V2D(29, 22), new V2D(163, 404), true));
        tileAnims.put("gold_pile", new Animation(1, new V2D(21, 16), new V2D(197, 406), true));
        tileAnims.put("shield_wall", new Animation(1, new V2D(14, 14), new V2D(368, 432), true));

        //Pièges
        tileAnims.put("inactive_trap", new Animation(1, new V2D(13, 25), new V2D(422, 357), true));
        tileAnims.put("active_trap", new Animation(5, new V2D(13, 25), new V2D(422, 357), false, 0.9f));

        //Lights
        tileAnims.put("torch", new Animation(6, new V2D(40, 48), new V2D(403, 0), true, false));
        tileAnims.put("candles", new Animation(3, new V2D(17, 19), new V2D(723, 0), true, false));
        tileAnims.put("brasero", new Animation(6, new V2D(35, 48), new V2D(454, 0), true, false));

        //Alcove pour bougies
        tileAnims.put("alcove", new Animation(1, new V2D(20, 22), new V2D(149, 300), true));


//________________________________________________________________________________________________________________________
        //Anims personnages 

        archerAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        archerAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        archerAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        archerAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));

        archerAnims.put("fast_shoot_top", new Animation(5, new V2D(64, 64), new V2D(256, 1032), false));
        archerAnims.put("fast_shoot_bot", new Animation(5, new V2D(64, 64), new V2D(256, 1160), false));
        archerAnims.put("fast_shoot_left", new Animation(5, new V2D(64, 64), new V2D(256, 1096), false));
        archerAnims.put("fast_shoot_right", new Animation(5, new V2D(64, 64), new V2D(256, 1224), false));

        archerAnims.put("slow_shoot_top", new Animation(10, new V2D(64, 64), new V2D(128, 1032), false));
        archerAnims.put("slow_shoot_bot", new Animation(10, new V2D(64, 64), new V2D(128, 1160), false));
        archerAnims.put("slow_shoot_left", new Animation(10, new V2D(64, 64), new V2D(128, 1096), false));
        archerAnims.put("slow_shoot_right", new Animation(10, new V2D(64, 64), new V2D(128, 1224), false));

        warriorAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 512), true));
        warriorAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 640), true));
        warriorAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 576), true));
        warriorAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 704), true));

        warriorAnims.put("slash_top", new Animation(4, new V2D(192, 192), new V2D(384, 3458), false));
        warriorAnims.put("slash_bot", new Animation(4, new V2D(192, 192), new V2D(384, 3842), false));
        warriorAnims.put("slash_left", new Animation(4, new V2D(192, 192), new V2D(384, 3650), false));
        warriorAnims.put("slash_right", new Animation(4, new V2D(192, 192), new V2D(384, 4034), false));

        warriorAnims.put("slow_slash_top", new Animation(6, new V2D(192, 192), new V2D(0, 3458), false, 0.75f));
        warriorAnims.put("slow_slash_bot", new Animation(6, new V2D(192, 192), new V2D(0, 3842), false, 0.75f));
        warriorAnims.put("slow_slash_left", new Animation(6, new V2D(192, 192), new V2D(0, 3650), false, 0.75f));
        warriorAnims.put("slow_slash_right", new Animation(6, new V2D(192, 192), new V2D(0, 4034), false, 0.75f));

        warriorAnims.put("thrust_top", new Animation(6, new V2D(192, 192), new V2D(0, 4994), false, 1.5f));
        warriorAnims.put("thrust_bot", new Animation(6, new V2D(192, 192), new V2D(0, 5376), false, 1.5f));
        warriorAnims.put("thrust_left", new Animation(6, new V2D(192, 192), new V2D(0, 5184), false, 1.5f));
        warriorAnims.put("thrust_right", new Animation(6, new V2D(192, 192), new V2D(0, 5568), false, 1.5f));

        mageAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 512), true));
        mageAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 640), true));
        mageAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 576), true));
        mageAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 704), true));

        mageAnims.put("fast_cast_top", new Animation(7, new V2D(64, 64), new V2D(0, 0), false));
        mageAnims.put("fast_cast_bot", new Animation(7, new V2D(64, 64), new V2D(0, 128), false));
        mageAnims.put("fast_cast_left", new Animation(7, new V2D(64, 64), new V2D(0, 64), false));
        mageAnims.put("fast_cast_right", new Animation(7, new V2D(64, 64), new V2D(0, 192), false));

        mageAnims.put("slow_cast_top", new Animation(7, new V2D(64, 64), new V2D(0, 0), false, 0.5f));
        mageAnims.put("slow_cast_bot", new Animation(7, new V2D(64, 64), new V2D(0, 128), false, 0.5f));
        mageAnims.put("slow_cast_left", new Animation(7, new V2D(64, 64), new V2D(0, 64), false, 0.5f));
        mageAnims.put("slow_cast_right", new Animation(7, new V2D(64, 64), new V2D(0, 192), false, 0.5f));

        gobAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        gobAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        gobAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        gobAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));
        gobAnims.put("attack_top", new Animation(10, new V2D(64, 64), new V2D(128, 1032), false));
        gobAnims.put("attack_bot", new Animation(10, new V2D(64, 64), new V2D(128, 1160), false));
        gobAnims.put("attack_left", new Animation(10, new V2D(64, 64), new V2D(128, 1096), false));
        gobAnims.put("attack_right", new Animation(10, new V2D(64, 64), new V2D(128, 1224), false));

        orcAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        orcAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        orcAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        orcAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));
        orcAnims.put("attack_top", new Animation(6, new V2D(192, 192), new V2D(0, 3458), false, 0.75f));
        orcAnims.put("attack_bot", new Animation(6, new V2D(192, 192), new V2D(0, 3842), false, 0.75f));
        orcAnims.put("attack_left", new Animation(6, new V2D(192, 192), new V2D(0, 3650), false, 0.75f));
        orcAnims.put("attack_right", new Animation(6, new V2D(192, 192), new V2D(0, 4034), false, 0.75f));

        skeletonsAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        skeletonsAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        skeletonsAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        skeletonsAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));
        skeletonsAnims.put("attack_top", new Animation(6, new V2D(64, 64), new V2D(0, 776), false, 0.75f));
        skeletonsAnims.put("attack_bot", new Animation(6, new V2D(64, 64), new V2D(0, 904), false, 0.75f));
        skeletonsAnims.put("attack_left", new Animation(6, new V2D(64, 64), new V2D(0, 840), false, 0.75f));
        skeletonsAnims.put("attack_right", new Animation(6, new V2D(64, 64), new V2D(0, 968), false, 0.75f));

        minotaurusAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        minotaurusAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        minotaurusAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        minotaurusAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));
        minotaurusAnims.put("attack1_top", new Animation(4, new V2D(192, 192), new V2D(384, 3463), false));
        minotaurusAnims.put("attack1_bot", new Animation(4, new V2D(192, 192), new V2D(384, 3847), false));
        minotaurusAnims.put("attack1_left", new Animation(4, new V2D(192, 192), new V2D(384, 3655), false));
        minotaurusAnims.put("attack1_right", new Animation(4, new V2D(192, 192), new V2D(384, 4039), false));
        minotaurusAnims.put("attack2_top", new Animation(7, new V2D(64, 64), new V2D(0, 0), false, 0.75f));
        minotaurusAnims.put("attack2_bot", new Animation(7, new V2D(64, 64), new V2D(0, 128), false, 0.75f));
        minotaurusAnims.put("attack2_left", new Animation(7, new V2D(64, 64), new V2D(0, 64), false, 0.75f));
        minotaurusAnims.put("attack2_right", new Animation(7, new V2D(64, 64), new V2D(0, 192), false, 0.75f));

        jackAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 520), true));
        jackAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 648), true));
        jackAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 584), true));
        jackAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 712), true));

        orcBossAnims.put("walk_top", new Animation(6, new V2D(64, 64), new V2D(0, 576), true));
        orcBossAnims.put("walk_bot", new Animation(6, new V2D(64, 64), new V2D(0, 512), true));
        orcBossAnims.put("walk_left", new Animation(6, new V2D(64, 64), new V2D(0, 640), true));
        orcBossAnims.put("walk_right", new Animation(6, new V2D(64, 64), new V2D(0, 704), true));
        orcBossAnims.put("attack1_top", new Animation(6, new V2D(64, 64), new V2D(0, 320), false, 0.6f));
        orcBossAnims.put("attack1_bot", new Animation(6, new V2D(64, 64), new V2D(0, 256), false, 0.6f));
        orcBossAnims.put("attack1_left", new Animation(6, new V2D(64, 64), new V2D(0, 384), false, 0.6f));
        orcBossAnims.put("attack1_right", new Animation(6, new V2D(64, 64), new V2D(0, 448), false, 0.6f));
        orcBossAnims.put("attack2_top", new Animation(8, new V2D(64, 64), new V2D(0, 832), false, 0.7f));
        orcBossAnims.put("attack2_bot", new Animation(8, new V2D(64, 64), new V2D(0, 768), false, 0.7f));
        orcBossAnims.put("attack2_left", new Animation(8, new V2D(64, 64), new V2D(0, 896), false, 0.7f));
        orcBossAnims.put("attack2_right", new Animation(8, new V2D(64, 64), new V2D(0, 960), false, 0.7f));

        necromancerAnims.put("walk_top", new Animation(9, new V2D(64, 64), new V2D(0, 512), true));
        necromancerAnims.put("walk_bot", new Animation(9, new V2D(64, 64), new V2D(0, 640), true));
        necromancerAnims.put("walk_left", new Animation(9, new V2D(64, 64), new V2D(0, 576), true));
        necromancerAnims.put("walk_right", new Animation(9, new V2D(64, 64), new V2D(0, 704), true));
        necromancerAnims.put("attack1_top", new Animation(7, new V2D(64, 64), new V2D(0, 0), false, 0.5f));
        necromancerAnims.put("attack1_bot", new Animation(7, new V2D(64, 64), new V2D(0, 128), false, 0.5f));
        necromancerAnims.put("attack1_left", new Animation(7, new V2D(64, 64), new V2D(0, 64), false, 0.5f));
        necromancerAnims.put("attack1_right", new Animation(7, new V2D(64, 64), new V2D(0, 192), false, 0.5f));
        necromancerAnims.put("attack2_top", new Animation(7, new V2D(64, 64), new V2D(0, 0), false, 0.75f));
        necromancerAnims.put("attack2_bot", new Animation(7, new V2D(64, 64), new V2D(0, 128), false, 0.75f));
        necromancerAnims.put("attack2_left", new Animation(7, new V2D(64, 64), new V2D(0, 64), false, 0.75f));
        necromancerAnims.put("attack2_right", new Animation(7, new V2D(64, 64), new V2D(0, 192), false, 0.75f));

        effectsAnims.put("dash", new Animation(9, new V2D(80, 64), new V2D(10, 0), false));
        effectsAnims.put("energy_ball", new Animation(9, new V2D(128, 128), new V2D(0, 64), true));
        effectsAnims.put("energy_ball_explode", new Animation(7, new V2D(128, 128), new V2D(0, 192), false));
        effectsAnims.put("smoke_explosion", new Animation(11, new V2D(64, 64), new V2D(0, 310), false));
        effectsAnims.put("blue_explosion", new Animation(11, new V2D(48, 48), new V2D(736, 310), false));
        effectsAnims.put("big_explosion", new Animation(18, new V2D(48, 48), new V2D(0, 384), false));
        effectsAnims.put("thunder", new Animation(12, new V2D(64, 64), new V2D(0, 439), false));
        effectsAnims.put("arrow", new Animation(1, new V2D(70, 24), new V2D(1084, 0), true));
        effectsAnims.put("ball", new Animation(1, new V2D(12, 12), new V2D(1056, 6), true));
        effectsAnims.put("healing", new Animation(14, new V2D(128, 128), new V2D(128, 503), false));
        effectsAnims.put("summoning", new Animation(15, new V2D(48, 64), new V2D(0, 631), false));
        effectsAnims.put("blood_spell", new Animation(16, new V2D(48, 32), new V2D(48, 695), false));
        effectsAnims.put("crit", new Animation(4, new V2D(32, 32), new V2D(32, 727), false));
        effectsAnims.put("small_explosion", new Animation(14, new V2D(64, 64), new V2D(64, 791), false));

        merchantAnims.put("idle", new Animation(2, new V2D(64, 64), new V2D(0, 128), true, 0.1f));

//________________________________________________________________________________________________________________________
        //Découpage UI 

        //Curseur manette
        UIAnims.put("cursor", new Animation(1, new V2D(32, 32), new V2D(1392, 16), true));
        UIAnims.put("pointer", new Animation(1, new V2D(32, 32), new V2D(976, 255), true));

        //InGame

        UIAnims.put("heart", new Animation(1, new V2D(253, 254), new V2D(85, 0), true));
        UIAnims.put("shield_heart", new Animation(1, new V2D(253, 254), new V2D(86, 261), true));
        UIAnims.put("coin", new Animation(8, new V2D(16, 16), new V2D(1392, 0), true, 0.75f));

        //GameMenu

        UIAnims.put("background", new Animation(1, new V2D(10, 10), new V2D(36, 289), true));
        UIAnims.put("menu_background", new Animation(1, new V2D(185, 197), new V2D(56, 576), true));
        UIAnims.put("close_cross", new Animation(1, new V2D(14, 11), new V2D(670, 937), true));
        UIAnims.put("click_input", new Animation(1, new V2D(76, 36), new V2D(2, 326), true));
        UIAnims.put("invisible_input", new Animation(1, new V2D(10, 10), new V2D(0, 85), true));
        //Réglage du son
        UIAnims.put("sound_set", new Animation(1, new V2D(31, 31), new V2D(860, 240), true));
        //TechMode
        UIAnims.put("tech_mode", new Animation(1, new V2D(28, 28), new V2D(597, 802), true));

        //Inventaire
        UIAnims.put("inventory", new Animation(1, new V2D(767, 633), new V2D(54, 1070), true));
        UIAnims.put("static_coin", new Animation(1, new V2D(16, 16), new V2D(1392, 0), true));
        UIAnims.put("left_arrow", new Animation(1, new V2D(27, 22), new V2D(1053, 312), true));
        UIAnims.put("right_arrow", new Animation(1, new V2D(27, 22), new V2D(1083, 312), true));

        //MainMenu & character selection
        warriorAnims.put("buste_warrior", new Animation(1, new V2D(30, 40), new V2D(17, 130), true));
        mageAnims.put("buste_mage", new Animation(1, new V2D(33, 28), new V2D(16, 141), true));
        archerAnims.put("buste_archer", new Animation(1, new V2D(25, 28), new V2D(19, 142), true));
        UIAnims.put("title_background", new Animation(1, new V2D(10, 10), new V2D(82, 590), true));
        tileAnims.put("skullking", new Animation(1, new V2D(149, 113), new V2D(441, 424), true));
        UIAnims.put("cadre", new Animation(1, new V2D(147, 153), new V2D(56, 792), true));

        //PowerUp
        UIAnims.put("damage_up", new Animation(1, new V2D(32, 31), new V2D(403, 65), true));
        UIAnims.put("speed_up", new Animation(1, new V2D(32, 31), new V2D(403, 161), true));
        UIAnims.put("critical_up", new Animation(1, new V2D(32, 31), new V2D(627, 33), true, 2.f));

        //Interface marchand
        UIAnims.put("market_interface", new Animation(1, new V2D(115, 112), new V2D(887, 0), true));
        UIAnims.put("market_items_placeholder", new Animation(1, new V2D(22, 21), new V2D(1029, 17), true));
        UIAnims.put("bracelet", new Animation(1, new V2D(32, 32), new V2D(468, 385), true));
        UIAnims.put("book", new Animation(1, new V2D(32, 32), new V2D(533, 448), true));
        UIAnims.put("ring", new Animation(1, new V2D(32, 32), new V2D(341, 289), true));

        //UI boss
        UIAnims.put("boss_health_bar_frame", new Animation(1, new V2D(60, 4), new V2D(7, 59), true));
        UIAnims.put("boss_health_bar", new Animation(1, new V2D(10, 10), new V2D(181, 100), true));

        //Mini map
        UIAnims.put("room_boss", new Animation(1, new V2D(26, 26), new V2D(882, 312), true));
        UIAnims.put("room_merchant", new Animation(1, new V2D(26, 26), new V2D(910, 312), true));
        UIAnims.put("room_unknown", new Animation(1, new V2D(26, 26), new V2D(938, 312), true));
        UIAnims.put("room_visited", new Animation(1, new V2D(26, 26), new V2D(966, 312), true));
        UIAnims.put("room_current", new Animation(1, new V2D(26, 26), new V2D(994, 312), true));

//________________________________________________________________________________________________________________________
        // Enregistrement dans la map globale
        animationsMap.put("TileSet", tileAnims);

        animationsMap.put("UI", UIAnims);

        animationsMap.put("Archer", archerAnims);
        animationsMap.put("Warrior", warriorAnims);
        animationsMap.put("Mage", mageAnims);

        animationsMap.put("Gobelin", gobAnims);
        animationsMap.put("Orc", orcAnims);
        animationsMap.put("Skeleton", skeletonsAnims);

        animationsMap.put("Minotaurus", minotaurusAnims);
        animationsMap.put("Jack", jackAnims);
        animationsMap.put("OrcBoss", orcBossAnims);
        animationsMap.put("Necromancer", necromancerAnims);

        animationsMap.put("Merchant", merchantAnims);

        animationsMap.put("Effects", effectsAnims);
    }

    public boolean isbHorizontal() {
        return bHorizontal;
    }

    public static Animation get(String SpriteName, String name) {
        Map<String, Animation> atlas = animationsMap.get(SpriteName);
        if (atlas == null) return null; 
        else return animationsMap.get(SpriteName).get(name);
    }

    public int getFrameNbr() {
        return this.frameNbr;
    }

    public V2D getFrameSize() {
        return this.frameSize;
    }

    public V2D getFrameStart() {
        return this.frameStart;
    }

    public boolean isbInfinite() {
        return this.bInfinite;
    }

    public float getAnimationSpeed() {
        return this.animationSpeed;
    }

}
