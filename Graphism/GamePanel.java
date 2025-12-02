package Graphism;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Level.Level;
import Objects.Arrow;
import Objects.CObject;
import Objects.Collision;
import Objects.Effect;
import Objects.Fireball;
import Objects.LevelEnd;
import Objects.Melee;
import Objects.Projectile;
import Objects.Tile;
import Objects.ZoneDamage;
import UI.UI;
import UI.UIElement;
import UI.UIFontLoader;
import Utilitary.Gamepad;
import Utilitary.V2D;

public class GamePanel extends JPanel {

    private final boolean displayCollisions = false;

    public GamePanel() {

    }

    private int compareObjectsY(CObject o1, CObject o2)
    {
        if (o1 == null || o2 == null) return 0;
        if ((o1 instanceof Tile || o2 instanceof Tile))
        {
            if (o1.getClass() != o2.getClass()) return (o1 instanceof Tile) ? -1 : 1;
            else return Double.compare(o1.getPosition().y, o2.getPosition().y);
        }
        else if ((o1 instanceof LevelEnd || o2 instanceof LevelEnd))
        {
            if (o2 instanceof Objects.Character) return -1;
            else if (o1 instanceof Objects.Character) return 1;
            else return 0;
        }
        else if ((o1.getSprite().getAnimName().equals("torch") || o1.getSprite().getAnimName().equals("alcove")|| o1.getSprite().getAnimName().equals("candles") || o1.getSprite().getAnimName().equals("shield_wall")) && o2.getSprite().getAnimName().equals("top_wall"))
        {
            return 1;
        }
        else if ((o2.getSprite().getAnimName().equals("torch") || o2.getSprite().getAnimName().equals("alcove") || o2.getSprite().getAnimName().equals("candles") || o2.getSprite().getAnimName().equals("shield_wall")) && o1.getSprite().getAnimName().equals("top_wall"))
        {
            return -1;
        }
        else if (o1.getSprite().getAnimName().equals("alcove") && o2.getSprite().getAnimName().equals("candles")) return -1;
        else if (o1.getSprite().getAnimName().equals("candles") && o2.getSprite().getAnimName().equals("alcove")) return 1;
        Animation A1 = Animation.get(o1.getSprite().getAtlasName(), o1.getSprite().getAnimName());
        Double fs1 = A1 == null ? 0 : A1.getFrameSize().y * o1.getSize().y;
        Animation A2 = Animation.get(o2.getSprite().getAtlasName(), o2.getSprite().getAnimName());
        Double fs2 = A2 == null ? 0 : A2.getFrameSize().y * o2.getSize().y;
        return Double.compare(o1.getPosition().y + fs1, o2.getPosition().y + fs2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Fond noir
        g.setColor(Color.decode("#1d1c2a"));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (Level.getActualRoom() != null) {
            Level.getActualRoom().getObjects().sort((o1, o2) -> compareObjectsY(o1, o2));
            for (CObject obj : Level.getActualRoom().getObjects())
            {
                if (obj == null || obj instanceof Melee) continue;

                Animation A = Animation.get(obj.getSprite().getAtlasName(), obj.getSprite().getAnimName());
                if (A == null) continue;

                V2D pos = new V2D(obj.getPosition());

                //Position centrée uniquement sur les personnages, projectiles et zones de dégâts
                if (obj instanceof Objects.Character || obj instanceof Fireball || obj instanceof Arrow || obj instanceof ZoneDamage || (obj instanceof Effect e && e.getSprite().getAnimName().equals("healing")))
                {
                    pos.x -= ((A.getFrameSize().x * obj.getSize().x) / 2.f);
                    pos.y -= ((A.getFrameSize().y * obj.getSize().y) / 2.f);
                }

                //Calcul des positions écran
                int dx1 = (int) pos.x;
                int dy1 = (int) pos.y;
                int dWidth  = (int) (A.getFrameSize().x * obj.getSize().x);
                int dHeight = (int) (A.getFrameSize().y * obj.getSize().y);

                // Calcul des coordonnées source (atlas)
                int sx1, sy1, sx2, sy2;
                if (A.isbHorizontal())
                {
                    sx1 = (int) (A.getFrameStart().x + A.getFrameSize().x * obj.getSprite().getAnimIndex());
                    sy1 = (int) (A.getFrameStart().y);
                    sx2 = sx1 + (int) A.getFrameSize().x;
                    sy2 = sy1 + (int) A.getFrameSize().y;
                }
                else
                {
                    sx1 = (int) (A.getFrameStart().x);
                    sy1 = (int) (A.getFrameStart().y + A.getFrameSize().y * obj.getSprite().getAnimIndex());
                    sx2 = sx1 + (int) A.getFrameSize().x;
                    sy2 = sy1 + (int) A.getFrameSize().y;
                }

                AffineTransform oldTransform = g2d.getTransform();

                // Rotation des projectiles selon la direction
                if (obj instanceof Projectile pr)
                {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    V2D vel = pr.getVelocity();
                    double angle = Math.atan2(vel.y, vel.x);
                    if (pr.getSprite().getAnimName().equals("energy_ball_explode")) {
                        angle += Math.PI / 2.0;
                    }
                    double cx = dx1 + dWidth / 2.0;
                    double cy = dy1 + dHeight / 2.0;
                    g2d.rotate(angle, cx, cy);
                }
                else g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

                BufferedImage atlasImg = Atlas.get(obj.getSprite().getAtlasName());

                if (!(obj instanceof Objects.Character c) || c.getHit() <= 0)
                {
                    if (!(obj instanceof Effect e) || !e.isbReverse()) g2d.drawImage(atlasImg, dx1, dy1, dx1 + dWidth, dy1 + dHeight, sx1, sy1, sx2, sy2, this);
                    else g2d.drawImage(atlasImg, dx1 + dWidth, dy1, dx1, dy1 + dHeight, sx1, sy1, sx2, sy2, this);
                }
                else
                {
                    //Buffer temporaire pour colorié le sprite en rouge
                    BufferedImage temp = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gt = temp.createGraphics();
                    try
                    {
                        gt.setRenderingHint(RenderingHints.KEY_INTERPOLATION, g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION));

                        // Dessin du sprite dans le buffer temporaire
                        gt.drawImage(atlasImg, 0, 0, dWidth, dHeight, sx1, sy1, sx2, sy2, null);

                        // Teintement du sprite en rouge (l'alpha reste alpha à 100%)
                        Composite oldC = gt.getComposite();
                        gt.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
                        gt.setColor(Color.RED);
                        gt.fillRect(0, 0, dWidth, dHeight);
                        gt.setComposite(oldC);
                    }
                    finally
                    {
                        gt.dispose();
                    }

                    g2d.drawImage(temp, dx1, dy1, null);
                }

                g2d.setTransform(oldTransform);
            }
            if (this.displayCollisions) {
                Graphics2D g2 = (Graphics2D) g.create();
                try
                {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    g2.setColor(Color.RED);

                    for (Collision c : Level.getActualRoom().getCollisions())
                    {
                        if (!c.isbActive() || c.getSize().x == -1) { }
                        else if (!c.isbCircle())
                        {
                            int x = (int) c.getPosition().x;
                            int y = (int) c.getPosition().y;
                            int w = (int) c.getSize().x;
                            int h = (int) c.getSize().y;

                            g2.fillRect(x, y, w, h);
                        }
                        else
                        {
                            int cx = (int) c.getPosition().x;
                            int cy = (int) c.getPosition().y;
                            int r = (int) c.getR();

                            int x = cx - r;
                            int y = cy - r;
                            int d = r * 2;

                            g2.fillOval(x, y, d, d);
                        }
                    }
                }
                finally
                {
                    g2.dispose();
                }
            }
        }
        for (UIElement e : UI.UIs.get(UI.ActualState).getElements()) {
            if (e.isBText() == false) {

                Animation A = Animation.get(e.getSprite().getAtlasName(), e.getSprite().getAnimName());
                if (A == null) {
                    continue;
                }
                V2D pos = new V2D(e.getPosition());
                g2d.drawImage(Atlas.get(e.getSprite().getAtlasName()),
                        (int) pos.x, (int) pos.y, // position en haut à gauche
                        (int) pos.x + (int) (A.getFrameSize().x * e.getSize().x),
                        (int) pos.y + (int) (A.getFrameSize().y * e.getSize().y),
                        (int) (A.getFrameStart().x) + (int) (A.getFrameSize().x * e.getSprite().getAnimIndex()),
                        (int) (A.getFrameStart().y),
                        (int) (A.getFrameStart().x) + (int) (A.getFrameSize().x * (e.getSprite().getAnimIndex() + 1)),
                        (int) (A.getFrameStart().y) + (int) (A.getFrameSize().y),
                        this);
            } else {
                g2d.setColor(e.getTextColor());

                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                g2d.setFont(UIFontLoader.get( (float)(24.f * e.getSize().x) ));

                g2d.drawString(e.getText(),
                            (int) e.getPosition().x,
                            (int) e.getPosition().y);

            }
        }
        //Curseur gamepad
        Animation A = Animation.get(UI.gamepadCursor.getSprite().getAtlasName(), UI.gamepadCursor.getSprite().getAnimName());
        if (A != null && ((UI.ActualState != UI.EGameState.InGame && Gamepad.gamepad.isConnected()) || (!Gamepad.gamepad.isConnected())))
        {
            UIElement e = UI.gamepadCursor;
            V2D pos = new V2D(e.getPosition().x - (16.0 * e.getSize().x), e.getPosition().y - (16.0 * e.getSize().y));
            g2d.drawImage(Atlas.get(e.getSprite().getAtlasName()),
                    (int) pos.x, (int) pos.y, // position en haut à gauche
                    (int) pos.x + (int) (A.getFrameSize().x * e.getSize().x),
                    (int) pos.y + (int) (A.getFrameSize().y * e.getSize().y),
                    (int) (A.getFrameStart().x) + (int) (A.getFrameSize().x * e.getSprite().getAnimIndex()),
                    (int) (A.getFrameStart().y),
                    (int) (A.getFrameStart().x) + (int) (A.getFrameSize().x * (e.getSprite().getAnimIndex() + 1)),
                    (int) (A.getFrameStart().y) + (int) (A.getFrameSize().y),
                    this);
        }

        //Pointeur de direction
        if (UI.ActualState == UI.EGameState.InGame)
        {
            Objects.Player p = Objects.Player.getPlayer1();
            if (p != null) {
                // Orientation du joueur
                V2D dir = new V2D(p.getAim());
                double len = Math.hypot(dir.x, dir.y);
                if (len < 1e-6) {
                    // Orientation par défaut si jamais le vecteur est nul
                    dir.x = 1.0;
                    dir.y = 0.0;
                } else {
                    dir.x /= len;
                    dir.y /= len;
                }

                // Distance du pointeur par rapport au centre du joueur
                double radius = 32.0;

                // Centre du joueur (vous utilisez déjà cette pos comme centre logique)
                V2D playerPos = new V2D(p.getPosition());
                double cx = playerPos.x + dir.x * radius;
                double cy = playerPos.y + dir.y * radius;

                // Récupération de l'animation et de l'atlas de la flèche
                Animation arrowAnim = Animation.get("UI", "pointer");
                if (arrowAnim != null) {
                    BufferedImage atlasImg = Atlas.get("UI");
                    if (atlasImg != null) {
                        // Taille affichée (échelle 1:1 ici)
                        int dWidth  = (int) arrowAnim.getFrameSize().x;
                        int dHeight = (int) arrowAnim.getFrameSize().y;

                        // Rectangle source dans l'atlas
                        int sx1 = (int) arrowAnim.getFrameStart().x;
                        int sy1 = (int) arrowAnim.getFrameStart().y;
                        int sx2 = sx1 + (int) arrowAnim.getFrameSize().x;
                        int sy2 = sy1 + (int) arrowAnim.getFrameSize().y;

                        // Angle en fonction de l'orientation
                        double angle = Math.atan2(dir.y, dir.x);

                        AffineTransform old = g2d.getTransform();
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                             RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

                        // On tourne autour du centre de la flèche
                        g2d.rotate(angle, cx, cy);

                        int dx1 = (int) (cx - dWidth / 2.0);
                        int dy1 = (int) (cy - dHeight / 2.0);
                        int dx2 = dx1 + dWidth;
                        int dy2 = dy1 + dHeight;

                        g2d.drawImage(atlasImg,
                                      dx1, dy1, dx2, dy2,
                                      sx1, sy1, sx2, sy2,
                                      this);

                        g2d.setTransform(old);
                    }
                }
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
