package src.test.java.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

import Objects.Actor;
import Utilitary.V2D;

public class ActorTest {

    private Actor instance;

    /** Implémentation minimale pour tester l’abstraite sans toucher au sprite. */
    private static class TestActor extends Actor {
        public TestActor(V2D size, V2D position, V2D collisionSize, String spriteName) {
            super(size, position, collisionSize, spriteName);
        }
        public TestActor(V2D size, V2D position, V2D collisionSize, String spriteName, String animName) {
            super(size, position, collisionSize, spriteName, animName);
        }
        @Override
        public void update(float dt) {
            // no-op: on évite l’appel à getSprite().updateAnim(dt)
        }
    }

    @Before
    public void setUp() {
        instance = new TestActor(
            new V2D(10.0, 10.0),  // size
            new V2D(0.0, 0.0),    // position
            new V2D(10.0, 10.0),  // collisionSize
            "dummy.png"           // spriteName
        );
    }

    @Test
    public void testGetOrientation_defaut() {
        V2D o = instance.getOrientation();
        assertNotNull(o);
        assertEquals(0.0, o.x, 1e-9);
        assertEquals(1.0, o.y, 1e-9);
    }

    @Test
    public void testSetOrientation_vecteur() {
        V2D n = new V2D(0.5, -0.25);
        instance.setOrientation(n);
        V2D o = instance.getOrientation();
        assertEquals(0.5, o.x, 1e-9);
        assertEquals(-0.25, o.y, 1e-9);
    }

    @Test
    public void testSetOrientationX() {
        instance.setOrientationX(0.3);
        assertEquals(0.3, instance.getOrientation().x, 1e-9);
        // y ne doit pas changer
        assertEquals(1.0, instance.getOrientation().y, 1e-9);
    }

    @Test
    public void testSetOrientationY() {
        instance.setOrientationY(-0.7);
        assertEquals(-0.7, instance.getOrientation().y, 1e-9);
        // x ne doit pas changer
        assertEquals(0.0, instance.getOrientation().x, 1e-9);
    }

    @Test
    public void testUpdate_ne_jetepas() {
        // Doit s’exécuter sans exception grâce au no-op override
        instance.update(0.016f);
    }
}
