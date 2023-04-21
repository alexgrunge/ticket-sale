package justcloud.tickets.service.tracking;

import lombok.Getter;

public class Segment {

  @Getter private Coord startCoord;

  @Getter private Coord endCoord;

  @Getter private double size;

  @Getter private long segmentTime;

  @Getter private long restTime;

  @Getter private long timeFromStart;

  private Vector asVector;

  public Segment(Coord start, Coord end, long segmentTime, long restTime, long timeFromStart) {
    startCoord = start;
    endCoord = end;
    this.segmentTime = segmentTime;
    this.restTime = restTime;
    this.timeFromStart = timeFromStart;
    asVector = Vector.delta(startCoord, endCoord);
    size = asVector.size();
  }

  public Vector vectorToCoord(Coord c) {
    return Vector.delta(startCoord, c);
  }

  public double projectedSize(Vector v) {
    return asVector.dot(v) / size;
  }

  public double proportion(Vector v) {
    return projectedSize(v) / size;
  }

  // Distancia entre un punto y un segment, cuando el segment empieza en el origen
  public double distanceFrom(Vector v) {
    return Math.abs(v.x * asVector.y - v.y * asVector.x) / size;
  }

  public boolean atEnd(Coord c, double range) {
    return Vector.delta(endCoord, c).size() <= range;
  }

  @Override
  public String toString() {
    return String.format(
        "Start: %s, End: %s, Vector: %s, size: %f, time: %d, rest: %d, form start: %d",
        startCoord.toString(),
        endCoord.toString(),
        asVector.toString(),
        size,
        segmentTime,
        restTime,
        timeFromStart);
  }
}
