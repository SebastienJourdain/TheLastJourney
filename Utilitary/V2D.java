package Utilitary;

public class V2D
{
    public double x = 0;
    public double y = 0;

    public V2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public V2D(V2D vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void mult(double m)
    {
        this.x *= m;
        this.y *= m;
    }
    public void mult(V2D m)
    {
        this.x *= m.x;
        this.y *= m.y;
    }
    public void normalize()
    {
        double n = Math.hypot(this.x, this.y);
        if (n > 1e-6)
        {
            this.x /= n;
            this.y /= n;
        }
    }
    public double distance(V2D d) { return (Math.abs(this.x - d.x) + Math.abs(this.y - d.y)); }
}