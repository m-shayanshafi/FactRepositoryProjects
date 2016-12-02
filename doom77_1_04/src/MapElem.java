public class MapElem {
  public Sprite sprite;
  public boolean is;

  public MapElem(String sprite, boolean is) {
    this.sprite = Gfx.sprites.get(sprite);
    this.is = is;
  }

  public boolean is() {
    return is;
  }
}
