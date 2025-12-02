/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package src.test.java.Objects;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import Objects.Caracteristics;

public class CaracteristicsTest {

    private Caracteristics instance;

    @Before
    public void setUp() {
        // Adaptez l’ordre et le nombre d’arguments à votre constructeur réel.
        // Exemple plausible (maxHP, actHP, damage, armor, speed, critical) :
        // instance = new Caracteristics(100, 100, 10, 5, 3.0f, 0.1f);

        // Si votre constructeur n’a que 5 paramètres (comme dans votre code),
        // utilisez des valeurs cohérentes avec votre classe.
        instance = new Caracteristics(100, 100, 10, 3.0f, 0.1f);
    }

    @Test
    public void testGetMaxHP() {
        assertEquals(100, instance.getMaxHP());
    }

    @Test
    public void testGetActHP() {
        assertEquals(100, instance.getActHP());
    }

    @Test
    public void testGetArmor() {
        assertEquals(100, instance.getActArmor());
    }

    @Test
    public void testGetDamage() {
        assertEquals(10, instance.getDamage());
    }

    @Test
    public void testGetSpeed() {
        assertEquals(3.0f, instance.getSpeed(), 1e-6f);
    }

    @Test
    public void testGetCritical() {
        assertEquals(0.1f, instance.getCritical(), 1e-6f);
    }

    @Test
    public void testSetMaxHP() {
        instance.setMaxHP(120);
        assertEquals(120, instance.getMaxHP());
        // Optionnel : vérifier l’ajustement d’actHP si votre classe le fait.
    }

    @Test
    public void testSetActHP() {
        instance.setActHP(80);
        assertEquals(80, instance.getActHP());
    }

    @Test
    public void testSetArmor() {
        instance.setActArmor(7);
        assertEquals(7, instance.getActArmor());
    }

    @Test
    public void testSetDamage() {
        instance.setDamage(15);
        assertEquals(15, instance.getDamage());
    }

    @Test
    public void testSetSpeed() {
        instance.setSpeed(4.5f);
        assertEquals(4.5f, instance.getSpeed(), 1e-6f);
    }

    @Test
    public void testSetCritical() {
        instance.setCritical(0.2f);
        assertEquals(0.2f, instance.getCritical(), 1e-6f);
    }
}
