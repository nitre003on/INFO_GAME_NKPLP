

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Shot extends GameObject{
  
  private Direction direction;
  
  Rectangle hitBox;
  
  public Shot(int x, int y, Direction direction, ID id, Handler handler) {
    super(x, y, id, handler);
    this.direction = direction;
    velX=7;
    velY=7;
    hitBox = new Rectangle(x, y, 16, 16);                                               //Schussgeschwindigkeit
  }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x,(int)y,16,16);                              //Grenzen werden hierdurch entnommen
  }
  
  public void tick() {
    hitBox.x = (int)x; 
    hitBox.y = (int)y;
    collision();
    if (direction == Direction.Up) {
      //x+=velX;
      y-=velY;
    } else if (direction == Direction.Down) {
      //x+=velX;
      y+=velY;
    } else if (direction == Direction.Left) {
      x-=velX;                                                                                  // Sch�sse in die 4 M�glichen Richtungen
      //y+=velY;
    } else if (direction == Direction.Right) {
      x+=velX;
      //y+=velY;
    }
    handler.addObject(new BasicTrail((int)x+4, (int)y+4, ID.Trail, Color.blue, 8, 8, 0.08f, handler));
  }
  
  public void collision() {
    for (int i = 0;i < handler.objects.size();i++) {         
      GameObject tempObject = handler.objects.get(i); 
      if(handler.objects.get(i).getID() == ID.Wall){ 
        handler.removeObject(this); 
      } 
    } 
  }
  
  public void render(Graphics g) {
    g.setColor(Color.blue);
    g.fillRect((int)x, (int)y, 16, 16);                                                                   //Darstellung
  }
}
