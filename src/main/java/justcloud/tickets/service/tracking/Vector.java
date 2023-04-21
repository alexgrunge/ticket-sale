package justcloud.tickets.service.tracking;

class Vector {

  public double x;

  public double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static Vector delta(Vector a, Vector b) {
    return new Vector(b.x - a.x, b.y - a.y);
  }

  public static Vector delta(Coord a, Coord b) {
    return new Vector(
        b.longitude.doubleValue() - a.longitude.doubleValue(),
        b.latitude.doubleValue() - a.latitude.doubleValue());
  }

  public double sqrSize() {
    return x * x + y * y;
  }

  public double size() {
    return Math.sqrt(sqrSize());
  }

  public double dot(Vector o) {
    return x * o.x + y * o.y;
  }

  @Override
  public String toString() {
    return String.format("(%f, %f)", x, y);
  }
}
