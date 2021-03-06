package Source.World.GameObjects.Items;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import Source.Engine.Direction;
import Source.Engine.Handler;
import Source.Engine.ID;
import Source.World.Game;
import Source.World.GameObject;
import Source.World.GameObjects.Player;
import Source.World.GameObjects.BulletTypes.Shot;

import java.util.Scanner;

public class Chest extends GameObject {
  
  int picked = 0;
  boolean open = false;
  Direction direction;
  
  private int width = 64;
  private int height = 48;
  
  public static int[] items = new int[Game.amountOfDiffItems];
  
  public Chest(int x, int y, ID id, Handler handler, int[] items) {
    super(x, y, id, handler);
    this.items = items;
  }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x,(int)y,64,48);                                            //Methode um die Umrisse zu kriegen
  }
  
  public void tick() {
    collision();
    if(picked==1){
      Player.chestOpened[0] = true;
      if(items[0]==1) {
        handler.addObject(new HealingPotionM((int)x, (int)y-60, ID.Item, handler));
        picked = 2;
        }
    }  
    x+=velX;                                                          //Bewegungsrichtumg
    y+=velY;                                                          
    x=Game.clamp(x, 0, Game.WIDTH-16);                                              //das innerhalb des Fensters bleiben
    y=Game.clamp(y, 0, Game.HEIGHT-16);
  }
  
  public void collision() {
    for (int i = 0; i < handler.objects.size(); i++) {
      GameObject tempObject = handler.objects.get(i);
      if (tempObject.getID()==ID.Player) {
        if(getBounds().intersects(tempObject.getBounds())) {
          picked += 1;
          open = true;
        }
      }
    } 
  }
  
  public void shoot() {
    handler.addObject(new Shot((int) x,(int) y, direction, ID.Shot, handler));                                //Schuss methode(ein Schuss Object wird erstellt
  } 
  
  public void interact() { 
    x = x*(-1);
    y = y*(-1);
  }
  
  
  public void render(Graphics g) {
    
    /*Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.green);
    g2d.draw(getBounds());*/
    /*if(id == ID.Player)*/
    g.setColor(new Color(210,105,30));
    if(open){
      g.fillRect((int)x+30, (int)y, 64, 16);
      g.fillRect((int)x, (int)y+16, 64, 32); 
    }else{
      g.fillRect((int)x, (int)y, 64, 48);                                                   // Form wird ge"zeichnet"
    }
    
  }
        
  public boolean onScreen(){
    try{
      return (x - Game.player.x + width > 0 - Game.ScreenWidth / 2 && x - Game.player.x < 0 + Game.ScreenWidth / 2 && y - Game.player.y + height > 0 - Game.ScreenHeight / 2 && y - Game.player.y < 0 + Game.ScreenHeight / 2);
    }catch(Exception e){
      return (x - Game.ScreenWidth / 2 + width > 0 - Game.ScreenWidth / 2 && x - Game.ScreenWidth / 2 < 0 + Game.ScreenWidth / 2 && y - Game.ScreenHeight / 2 + height > 0 - Game.ScreenHeight / 2 && y - Game.ScreenHeight / 2 < 0 + Game.ScreenHeight / 2);
    }
  }      
}
