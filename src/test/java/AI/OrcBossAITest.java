package src.test.java.AI;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import AI.OrcBossAI;
import Objects.Character;

/**
 * Tests unitaires de base pour {@link OrcBossAI}
 *
 * On vérifie ici la logique interne de gestion d'état :
 *  - création de l'état BossState,
 *  - incrément des cooldowns à chaque tick,
 *  - robustesse de tick(null) (aucune exception).
 *
 * Les branches d'attaque, de régénération et de poursuite dépendent fortement
 * de Player, Level, OrcBoss, etc. Sans framework de mock, on se concentre donc
 * sur la mécanique interne accessible.
 */
public class OrcBossAITest {

    private OrcBossAI ai;

    @Before
    public void setUp() {
        ai = new OrcBossAI();
    }

    @SuppressWarnings("unchecked")
    private Map<Character, ?> getStatesMap() throws Exception {
        Field f = OrcBossAI.class.getDeclaredField("states");
        f.setAccessible(true);
        return (Map<Character, ?>) f.get(ai);
    }

    private Object getBossState(Character key) throws Exception {
        Map<Character, ?> map = getStatesMap();
        return map.get(key);
    }

    private int getIntField(Object bossState, String fieldName) throws Exception {
        Field f = bossState.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return (int) f.get(bossState);
    }

    @Test
    public void testTickAvecNullNeJettePas() {
        ai.tick(null);
        ai.tick(null);
    }

    @Test
    public void testTickCreeUnEtatPourLeBoss() throws Exception {
        Map<Character, ?> before = getStatesMap();
        assertTrue("La map d'états doit être vide au départ", before.isEmpty());

        ai.tick(null);

        Map<Character, ?> after = getStatesMap();
        assertFalse("La map d'états ne doit plus être vide après tick(null)", after.isEmpty());

        Object state = getBossState(null);
        assertNotNull("Un BossState doit être créé pour la clé null", state);
    }

    @Test
    public void testCooldownsIncremententAChaqueTick() throws Exception {
        ai.tick(null);
        Object state1 = getBossState(null);
        assertNotNull("BossState devrait être créé après le premier tick", state1);

        int a1_1 = getIntField(state1, "attack1Ticks");
        int a2_1 = getIntField(state1, "attack2Ticks");

        ai.tick(null);
        Object state2 = getBossState(null);
        assertSame(
            "Le même BossState doit être réutilisé pour la même clé (ici null)",
            state1,
            state2
        );

        int a1_2 = getIntField(state2, "attack1Ticks");
        int a2_2 = getIntField(state2, "attack2Ticks");

        assertEquals("attack1Ticks doit s'incrémenter à chaque tick", a1_1 + 1, a1_2);
        assertEquals("attack2Ticks doit s'incrémenter à chaque tick", a2_1 + 1, a2_2);
    }

    @Test
    public void testPlusieursTicksAugmententLineairementLesCooldowns() throws Exception {
        final int nTicks = 10;

        for (int i = 0; i < nTicks; i++) {
            ai.tick(null);
        }

        Object state = getBossState(null);
        assertNotNull("BossState doit exister après plusieurs ticks", state);

        int a1 = getIntField(state, "attack1Ticks");
        int a2 = getIntField(state, "attack2Ticks");

        assertEquals("attack1Ticks doit être égal au nombre de ticks", nTicks, a1);
        assertEquals("attack2Ticks doit être égal au nombre de ticks", nTicks, a2);
    }
}
