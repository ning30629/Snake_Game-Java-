package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Snake {

    private ArrayList<Node> snakeBody; //Node是每一個身體的部分

    public Snake(){
        snakeBody = new ArrayList<>();
        snakeBody.add(new Node(80, 0)); // 第一個身體的地方
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));
    }

    public ArrayList<Node> getSnakeBody(){
        return snakeBody;
    }
    public void drawSnake(Graphics g){
        for (int i =0; i < snakeBody.size(); i++){

            if ( i == 0) {
                g.setColor(Color.GREEN); //蛇頭的顏色
            } else {
                g.setColor(Color.YELLOW); //蛇身體的顏色
            }

            Node n = snakeBody.get(i);
            //設定蛇跑超出畫面的時候要跑回來
            if (n.x >= Main.width) {
                n.x = 0; // 跑到最右邊後，從左邊回來
            }
            if (n.x < 0) {
                n.x = Main.width - Main.CELL_SIZE; // 跑到最左邊後，從最右邊減一格身體回來
            }
            if (n.y >= Main.height) {
                n.y = 0; // 跑到最下面，從最上面回來
            }
            if (n.y < 0) {
                n.y = Main.height - Main.CELL_SIZE; // 跑到最上面，從最下面減一格身體回來
            }
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
