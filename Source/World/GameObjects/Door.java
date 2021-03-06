package Source.World.GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Source.World.Game;
import Source.Engine.Handler;
import Source.Engine.ID;
import Source.Engine.Graphics.animationHandler;
import Source.World.GameObject;


public class Door extends GameObject{
  
  int width, height;
  public float TPLocX, TPLocY;  //Position an die der Spieler fuer die andere Tuer teleportiert werden soll
  public int teleportID;        //ID um zwei Tueren zu verbinden
  private int doorFacing;       //0 = norden, 1 = osten, 2 = sueden, 3 = westen.
  public int[] roomBounds;      //Um die Tuer zu oder auf zu machen
  public boolean isOpen;
  private animationHandler ah;
  
  //Konstruktor
  public Door(float x, float y, ID id, Handler handler, int width, int height, float teleportPosX, float teleportPosY, int teleportID, int doorFacing, int[] roomBounds, boolean isOpen) {
    super(x, y,width,height, id, handler);
    this.width = width;
    this.height = height;
    this.TPLocX = teleportPosX;
    this.TPLocY = teleportPosY;
    this.teleportID = teleportID;
    this.doorFacing = doorFacing;
    this.roomBounds = roomBounds;
    this.isOpen = isOpen;

    ah = new animationHandler("Content/Environment/door.png", 47);
    ah.createAnimation("Vopen", 1, 10);
    ah.createAnimation("Hopen", 11, 20);
    ah.createAnimation("Vclosed", 21,21);
    ah.createAnimation("Hclosed", 22, 22);
  }
  
  public int getTeleportID(){
    return teleportID;
  }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x,(int)y,width,height);                         //Grenzen werden HierarchyBoundsAdapter entnommen
  }
  
  public void tick() {
    ah.tick();
    if(isOpen && width > height){
      if(ah.curPlaying != "Vopen")
        ah.playAnimation("Vopen", 0.05f, true, true);
    }else if (isOpen){
      if(ah.curPlaying != "Hopen")
        ah.playAnimation("Hopen", 0.05f, true, true);
    }else if(width > height){
      ah.playAnimation("Vclosed", 0.05f, true, true);
    }else{
      ah.playAnimation("Hclosed", 0.05f, true, true);
    }

    if(width > height){ //falls noerdlich / suedliche tuer
      if(Game.player.y < y){
        ah.faceUp();
      }else{
        ah.faceDown();
      }
    }else{
      if(Game.player.x < x){
        ah.faceRight();
      }else{
        ah.faceLeft();
      }
    }
  }
  
  public void teleport(Player player, int door) {
    for (int i = 0; i < handler.objects.size(); i++) {         
      if(handler.objects.get(i) instanceof Door){                        //Liste aller Objekte wird nach Tueren durchsucht
        Door tempDoor = (Door)handler.objects.get(i);  
        if (i != door && tempDoor.teleportID == this.teleportID) {       //Die korrespondierende Tuer wird entnommen
          float tempX = tempDoor.TPLocX;
          float tempY = tempDoor.TPLocY;
          //Anpassen der teleporter Werte
          if (this.doorFacing == 2) {
            tempX -= player.playerLength/2;
          }
          else if (this.doorFacing == 3) {
            tempX -= player.playerLength;
            tempY -= player.playerHeight/2;                                  //Die teleport Methode passt die Position an, sodass der Spieler nicht in zwei Tueren fest sitzt.
          }                                                                  //Dann wird der Spieler an die Position der jeweils anderen Tuer teleportiert
          else if (this.doorFacing == 0){
            tempX -= player.playerLength/2;
            tempY -= player.playerHeight;
          }
          else if (this.doorFacing == 1) {
            tempY -= player.playerHeight/2;
          }
          
          //Die Koordinaten des Spielers werden ver�ndert
          player.x = tempX;                                          
          player.y = tempY;
          player.roomBounds = tempDoor.roomBounds;
          if (Game.debug) {
            System.out.println("tempX: " + tempX);
            System.out.println("tempY: " + tempY);
          }  
        }
      } 
    }
  }
  
  public boolean isUnlocked(){
    boolean isUnlocked = true;
    //Hier wird �berpr�ft ob sich noch Gegner in dem Raum befinden an dem sich diese Tuer befindet
    for (int i = 0; i < Game.handler.enemies.size(); i++) {
      GameObject tempObject = Game.handler.enemies.get(i);
      if (inRange((int)tempObject.x, this.roomBounds[0], this.roomBounds[0] + this.roomBounds[2]) && inRange((int)tempObject.y, this.roomBounds[1], this.roomBounds[1] + this.roomBounds[3])) {
        isUnlocked = false;
      }
    }
    
    return isUnlocked;
  }
  
  public void checkIfOpen() {
    if (isUnlocked()) {
      isOpen = true;
    }
  }
  
  public boolean inRange(int toCheck, int start, int end){ return start <= toCheck && toCheck <= end; }
  
  public void render(Graphics g) {
    g.setColor(Color.darkGray);
    g.fillRect((int)x, (int)y, width, height);                                    //Graphische Darstellung
    if (Game.debug) {
      g.fillRect((int)TPLocX, (int)TPLocY, 3, 3);
      g.setColor(Color.WHITE);
      g.drawString(Integer.toString(teleportID), (int)x, (int)y);
    }
    if(width > height)  //falls noerdlich / suedliche tuer
      ah.draw(g, (int)x, (int)y,width / 2 - 60, -58 / 2- height, 120);
    else
      ah.draw(g, (int)x, (int)y,(int)(width / 2 - 60 ), 14, 120);
  }
  
  public boolean onScreen(){
    try{
      return (x - Game.player.x + width > 0 - Game.ScreenWidth / 2 && x - Game.player.x < 0 + Game.ScreenWidth / 2 && y - Game.player.y + height > 0 - Game.ScreenHeight / 2 && y - Game.player.y < 0 + Game.ScreenHeight / 2);
    }catch(Exception e){
      return (x - Game.ScreenWidth / 2 + width > 0 - Game.ScreenWidth / 2 && x - Game.ScreenWidth / 2 < 0 + Game.ScreenWidth / 2 && y - Game.ScreenHeight / 2 + height > 0 - Game.ScreenHeight / 2 && y - Game.ScreenHeight / 2 < 0 + Game.ScreenHeight / 2);
    }
  }
}
