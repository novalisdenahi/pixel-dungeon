package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.utils.Highlighter;

public class HighlightedText extends Component {

  protected BitmapTextMultiline normal;
  protected BitmapTextMultiline highlighted;

  protected int nColor = 0xFFFFFF;
  protected int hColor = 0xFFFF44;

  public HighlightedText(final float size) {
    normal = PixelScene.createMultiline(size);
    add(normal);

    highlighted = PixelScene.createMultiline(size);
    add(highlighted);

    setColor(0xFFFFFF, 0xFFFF44);
  }

  @Override
  protected void layout() {
    normal.x = highlighted.x = x;
    normal.y = highlighted.y = y;
  }

  public void setColor(final int n, final int h) {
    normal.hardlight(n);
    highlighted.hardlight(h);
  }

  public void text(final String value, final int maxWidth) {
    Highlighter hl = new Highlighter(value);

    normal.text(hl.text);
    normal.maxWidth = maxWidth;
    normal.measure();

    if (hl.isHighlighted()) {
      normal.mask = hl.inverted();

      highlighted.text(hl.text);
      highlighted.maxWidth = maxWidth;
      highlighted.measure();

      highlighted.mask = hl.mask;
      highlighted.visible = true;
    } else {
      highlighted.visible = false;
    }

    width = normal.width();
    height = normal.height();
  }
}
