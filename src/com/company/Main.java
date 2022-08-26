package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20; // 蛇每一格的大小
    public static int width = 400; //視窗的寬
    public static int height = 400; //視窗的高
    public static int row = height / CELL_SIZE; // 總共有多少橫列
    public static int column = width / CELL_SIZE; // 總共有多少直列
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed = 100; //100毫秒 = 0.1秒執行一次
    private static String direction;
    private boolean allowKeyPress;  // 避免按太快，蛇還沒畫完就按下一個件造成混亂
    private int score;
    private int highest_score;
    String desktop = System.getProperty("user.home") + "/Desktop/";
    String myFile = desktop + "filename.txt";

    public Main(){
        read_highest_score();
        reset();
        addKeyListener(this);
    }
    private void setTimer(){
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                repaint();
            }               // 0的意思是不會有任何delay，timer開始跑的時候就會執行timer
        }, 0, speed); // speed 多久執行一次(單位：毫秒)
    }
    private void reset(){
        score = 0;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    @Override
    public void paintComponent(Graphics g){
        //check if the snake bites itself
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node head = snake_body.get(0);
        for (int i = 1; i < snake_body.size(); i++){
            if (snake_body.get(i).x == head.x && snake_body.get(i).y == head.y){ //代表咬到自己了
                allowKeyPress = false;
                t.cancel(); //遊戲結束了 要讓timer停止
                t.purge();
                int response = JOptionPane.showOptionDialog(this, "Game Over!! Your score is " + score + ". The highest score was "+ highest_score + ". Would you like to star over?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);
                write_a_file(score);
                switch (response){
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }

        // draw a black background
        g.fillRect(0, 0, width, height); //因為沒辦法調大小，所以不需要用getWidth
        fruit.drawFruit(g);  //fruit先寫，才不會出現在頭上面
        snake.drawSnake(g);

        //remove snake tail and put in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        if (direction.equals("Right")){
            snakeX += CELL_SIZE;
        } else if (direction.equals("Left")) {
            snakeX -= CELL_SIZE;
        } else if (direction.equals("Down")){
            snakeY += CELL_SIZE;
        } else if (direction.equals("Up")){
            snakeY -= CELL_SIZE;
        }
        Node newHead = new Node(snakeX, snakeY);

        //check if the snake eats the fruit
        if (snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()){
            fruit.setNewLocation(snake); // 1. set fruit to a new location
            fruit.drawFruit(g); // 2. drawFruit
            score++; // 3. score++
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size()-1); //去掉尾巴
        }


        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }
    public static void main(String[] args){
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);  // 使用者無法調整大小
    }



    // 因為implements KeyListener，所以一定要Override以下三項
    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress){
            if (e.getKeyCode() == 37 && !direction.equals("Right")){
                direction = "Left";
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction = "Right";
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";
            }
            allowKeyPress = false; //在下次被改成true之前，都沒辦法再keypress
        }


    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public void read_highest_score(){
        try {
            File myObj = new File(myFile);
            Scanner myReader = new Scanner(myObj);
            highest_score = myReader.nextInt();
            myReader.close();
        } catch (FileNotFoundException e){
            highest_score = 0;
            try {
                File myObj = new File(myFile);
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + 0);
            } catch (IOException err){
                System.out.println("An error occurred");
                err.printStackTrace();
            }
        }
    }

    public void write_a_file(int score) {
        try {
            FileWriter myWriter = new FileWriter(myFile);
            if (score > highest_score){
                myWriter.write("" + score);
                highest_score = score;
            } else {
                myWriter.write("" + highest_score); //如果分數是最高分，就在裡面寫最高分就好
            }
            myWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
