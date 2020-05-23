import java.awt.Graphics;

public class exitButton extends button {

    public exitButton(int x, int y, int w, int h, ID id, Handler handler) {
      super(x, y,w,h, id, handler);
      super.txt = "exit";
    }
    
    public void tick(){
      super.tick();
    }
    
    public void event(){System.exit(1);}

    public void render(Graphics g){
      super.render(g);
    }
}