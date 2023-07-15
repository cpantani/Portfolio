import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;



public class GamePanel  extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    static final int w = 500;
    static final int h = 500;
    static final int u = 20;
    static final int u_num = (w * h) / (u * u);

    final int x[] = new int[u_num];
    final int y[] = new int[u_num];

    int length = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play(){
        addFood();
        running = true;

        timer = new Timer(80, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move(){
        for (int i = length; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(direction == 'L'){
            x[0] = x[0] - u;
        } else if (direction == 'R'){
            x[0] = x[0] + u;
        }else if (direction == 'U'){
            y[0] = y[0] - u;
        } else {
            y[0] = y[0] + u;
        }
    }

    public void checkFood(){
        if(x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }


    public void draw(Graphics graphics){
        if(running) {
            graphics.setColor(new Color(210, 115, 90));
            graphics.fillOval(foodX, foodY, u, u);

            graphics.setColor(Color.white);
            graphics.fillRect(x[0], y[0], u, u);

            for(int i = 1; i < length; i++){
                graphics.setColor(new Color(40, 200, 150));
                graphics.fillRect(x[i], y[i], u, u);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (w - metrics.stringWidth("Score: "+ foodEaten))/ 2, graphics.getFont().getSize());
        
        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int)(w / u)) * u;
        foodY = random.nextInt((int)(w / u)) * u;
    }

    public void checkHit() {
        for(int i = length; i > 0; i--){
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if(x[0] < 0 || x[0] > w || y[0] < 0 || y[0] > h){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics){
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));    
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (w - metrics.stringWidth("Game Over")) / 2, h /2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (w - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());

        //aded testing for restart
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 20));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Press 'R' to play again", (w - metrics.stringWidth("Press 'R' to play again"))/2, h/2 + 70);
    }

    //added
    public void restartGame() {
        setVisible(false);
        new GameFrame();
    }

    public void dispose() {
        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();
    }
    //to here for restart

    @Override
    public void actionPerformed(ActionEvent arg0){
        if(running){
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;    
                
                case KeyEvent.VK_R:
                    restartGame();
                    dispose();
                    System.out.println("restart");
                    break;  
            }
        }
    }



}
