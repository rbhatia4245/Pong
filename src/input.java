import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.Color;
public class input{
    public static class in extends JPanel implements Runnable,KeyListener
    {


        Thread t;
        ArrayList<Integer> keysDown;
        Game game;
        boolean w,s,u,d;
        public in(Game g)
        {
            game=g;
            keysDown= new ArrayList<Integer>();
            w=false;
            s=false;
            u=false;
            d=false;
        }

        public void run()
        {
            moveRect();
        }


        public void keyPressed(KeyEvent e) {

            if(!keysDown.contains(e.getKeyCode()))
                keysDown.add(new Integer(e.getKeyCode()));

            if(keysDown.contains(new Integer(KeyEvent.VK_DOWN)))
                d=true;
            if(keysDown.contains(new Integer(KeyEvent.VK_UP)))
                u=true;
            if(keysDown.contains(new Integer(KeyEvent.VK_S)))
                s=true;
            if(keysDown.contains(new Integer(KeyEvent.VK_W)))
                w=true;


            moveRect();
        }
        public void moveRect()
        {

            int y1=0,y2=0;
            if(d)
                y2+=2;
            else if(u)
                y2=-2;
            if(s)
                y1+=2;
            else if(w)
                y1=-2;

            game.set(y1,y2);

        }

        public void keyReleased(KeyEvent e) {


            if(keysDown.contains(new Integer(KeyEvent.VK_DOWN)))
                d=false;
            if(keysDown.contains(new Integer(KeyEvent.VK_UP)))
                u=false;
            if(keysDown.contains(new Integer(KeyEvent.VK_S)))
                s=false;
            if(keysDown.contains(new Integer(KeyEvent.VK_W)))
                w=false;
            keysDown.remove(new Integer(e.getKeyCode()));

        }
        public void keyTyped(KeyEvent e) {

        }
    }


    @SuppressWarnings("serial")
    public static class Game extends JPanel implements Runnable,ActionListener{
        Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle racket_1,racket_2;
        Ellipse2D.Double ball;
        int s[];
        Thread t;
        int x,y,xflag,yflag,r[];
        int top=50,bottom=dim.height/3*2,left=50,right=dim.width/3*2;
        public void initialise(int i)
        {
            try{
                int temp[]={bottom/2-100,bottom/2-100};
                r=temp;
                x=right/2;y=bottom/2;
                xflag=1;yflag=0;
                if(i==1)
                    xflag=1;
                else if(i==2)
                    xflag=0;
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                repaint();
            }
        }

        public void run()
        {
            addKeyListener(new in(this));
            set_ball();
            repaint();
        }
        public void calculateScore()
        {
            if(x>=right-20)
            {
                s[0]++;
            }
            else {
                s[1]++;
            }

        }
        public int getScore()
        {
            try{
                if(s[0]==5)
                {
                    repaint();
                    return 1;
                }
                else if(s[1]==5)
                {
                    repaint();
                    return 2;
                }}
            catch(Exception e)
            {}
            return 0;
        }
        public void set_ball()
        {
            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();

            if(x>=right-20||x<=left)
            {
                calculateScore();
                if(x>=right-20)
                    initialise(1);
                else
                    initialise(2);
            }
            else if(ball.intersects(racket_2))
                xflag=0;
            else if(ball.intersects(racket_1))
                xflag=1;
            if(y>=bottom-20)
                yflag=0;
            else if(y<=top)
                yflag=1;


            if(xflag==1)
                x+=4;
            else
                x-=4;
            if(yflag==1)
                y+=4;
            else
                y-=4;

            ball.setFrame(x,y,20,20);
            repaint();
        }

        public void set(int y1,int y2)
        {
            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
            if(r[0]+y1>top&&r[0]+y1<bottom-100)
                r[0]+=y1;
            if(r[1]+y2>top&&r[1]+y2<bottom-100)
                r[1]+=y2;
            racket_1.setLocation(left,r[0]);
            racket_2.setLocation(right-25,r[1]);
            repaint();
        }



        public Game()
        {
            initialise(3);
            int temp[]={0,0};
            s=temp;
            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
            racket_1=new Rectangle(left,bottom/2-100,25,100);
            racket_2=new Rectangle(right-25,bottom/2-100,25,100);
            ball=new Ellipse2D.Double(x,y,20,20);
            addKeyListener(new in(this));
        }

        public void actionPerformed(ActionEvent e){repaint();}

        @Override
        public void paint(Graphics g) {
            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.fill(racket_1);
            g2d.fill(racket_2);
            g2d.fill(ball);
            g2d.drawLine((right-left)/2+50,top,(right-left)/2+50,bottom);
            g2d.drawLine(left,top,left,bottom);
            g2d.drawLine(right,top,right,bottom);
            g2d.drawLine(left,bottom,right,bottom);
            g2d.drawLine(left,top,right,top);
            g.setFont(new Font("ArielBlack",Font.PLAIN,55));
            g.drawString(String.valueOf(s[0]),(right-left)/2-50,top+50);
            g.drawString(String.valueOf(s[1]),(right-left)/2+100,top+50);

        }


    }

    public static void main(String[] args) throws InterruptedException {
        Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("PONG");
        JFrame frame1=new JFrame("PONG");
        JFrame framewin=new JFrame("");
        frame1.setLayout(null);
        JButton jb1=new JButton("PLAY");
        JButton jb2=new JButton("Exit");
        jb1.setBackground(new java.awt.Color(153, 153, 255));
        jb1.setFont(new java.awt.Font("Lucida Sans", 1, 30));
        jb2.setBackground(new java.awt.Color(153, 153, 255));
        jb2.setFont(new java.awt.Font("Lucida Sans", 1, 30));
        frame1.add(jb1);
        frame1.add(jb2);
        JLabel label=new JLabel("PONG");
        label.setFont(new java.awt.Font("Tahoma",1,36));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setBounds(dim.width/3-50,0,200,80);
        frame1.add(label);
        frame1.setSize(dim.width/3*2+100,dim.height/3*2+100);

        JLabel  pLabel1= new JLabel("Player 1: ");
        JLabel  pLabel2 = new JLabel("Player 2: ");
        final JTextField player1 = new JTextField(6);
        final JTextField player2 = new JTextField(6);
        pLabel1.setFont(new java.awt.Font("Tahoma",1,20));
        pLabel2.setFont(new java.awt.Font("Tahoma",1,20));
        pLabel1.setBounds(dim.width/3-100,150,200,80);
        pLabel2.setBounds(dim.width/3-100,200,200,80);
        player1.setBounds(dim.width/3,182,150,23);
        player2.setBounds(dim.width/3,232,150,23);
        frame1.add(pLabel1);
        frame1.add(player1);
        frame1.add(pLabel2);
        frame1.add(player2);



        jb1.setBounds(dim.width/3-25,350,166,52);
        jb1.setVisible(true);
        jb2.setBounds(dim.width/3-25,420,166,52);
        jb2.setVisible(true);
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framewin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jb2.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        jb1.addActionListener((ActionEvent e) -> {
            frame1.setVisible(false);
            Game game = new Game();
            in In=new in(game);
            JFrame dialog=new JFrame(" ");
            dialog.setLayout(null);
            dialog.setSize(dim.width/3*2,dim.height/3*2);
            JLabel wins=new JLabel();

            wins.setFont(new java.awt.Font("Tahoma",1,34));
            wins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            wins.setBounds(dim.width/3-300,0,600,150);

            dialog.add(wins);
            JButton stop=new JButton("OK");
            stop.setFont(new java.awt.Font("Lucida Sans", 1, 20));
            stop.setBounds(dim.width/3-80,250,166,52);
            dialog.add(stop);
            frame.add(game);
            frame.setSize(dim.width/3*2+100,dim.height/3*2+100);
            frame.setVisible(true);
            frame.addKeyListener(In);
            frame.setFocusable(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.initialise(3);
            Timer timer=new Timer(4,new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(game.getScore()==1||game.getScore()==2)
                    {
                        frame1.dispose();
                        frame.dispose();
                        String no=Integer.toString(game.getScore());
                        String x="";
                        if(Integer.parseInt(no)==1)
                            x=player1.getText();
                        else if(Integer.parseInt(no)==2)
                            x=player2.getText();
                        x+=" WINS";
                        wins.setText("<html>&nbsp GAME OVER <br><br>"+x+"<html>");
                        dialog.setVisible(true);
                    }
                    else
                    {
                        game.run();
                        In.run();
                    }
                }
            });
            timer.start();
            stop.addActionListener((ActionEvent f) ->{
                timer.stop();
                dialog.dispose();
                frame1.setVisible(true);

            });
        });

    }
}