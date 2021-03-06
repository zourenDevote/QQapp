package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Login {

    private boolean Can_Drag = false;

    public static void main(String[] args) {
        Login login = new Login();
        login.UI();
    }

    public void UI() {
        JFrame frame = new JFrame();
        frame.setSize(450, 330);
        //设置不可改变大小
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        //设置在任务栏显示的图标
        frame.setIconImage(new ImageIcon("图标.jpg").getImage());
        //去边框
        frame.setUndecorated(true);
        frame.setLayout(null);

        Image image = new ImageIcon("登录背景.jpg").getImage().getScaledInstance(450, 330, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0, 0, 450, 330);
        //将背景标签放置在中间层
        frame.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE));
        JPanel panel = (JPanel) frame.getContentPane();
        //将最上层设置透明
        panel.setOpaque(false);

        // 设置窗体拖动效果
        background.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Can_Drag = false;
            }

        });

        background.addMouseMotionListener(new MouseAdapter() {
            int StartX, StartY, EndX, EndY;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!Can_Drag) {
                    StartX = e.getX();
                    StartY = e.getY();
                    Can_Drag = true;
                }
                EndX = e.getX();
                EndY = e.getY();
                frame.setLocation(frame.getX() + EndX - StartX, frame.getY() + EndY - StartY);
            }

        });

        JButton minimize = new JButton(new ImageIcon("最小化.png"));
        //将按钮设置透明
        minimize.setContentAreaFilled(false);
        //将按钮设置为无边框
        minimize.setBorderPainted(false);
        minimize.setBounds(390, 0, 30, 30);
        minimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //最小化窗口
                frame.setExtendedState(1);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //使按钮呈现灰色
                minimize.setContentAreaFilled(true);
                minimize.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimize.setContentAreaFilled(false);
            }
        });
        frame.add(minimize);

        JButton close = new JButton(new ImageIcon("关闭.png"));
        //将按钮设置透明
        close.setContentAreaFilled(false);
        //将按钮设置为无边框
        close.setBorderPainted(false);
        close.setBounds(420, 0, 30, 30);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ConnectWithServer.sendExitMsg();
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //使按钮呈现红色
                close.setContentAreaFilled(true);
                close.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setContentAreaFilled(false);
            }
        });
        frame.add(close);

        Image QQ_Head = new ImageIcon("头像.jpg").getImage().getScaledInstance(100, 100, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel head = new JLabel(new ImageIcon(QQ_Head));
        head.setBounds(20, 120, 100, 100);
        frame.add(head);

        JTextField account = new JTextField();
        account.setBounds(135, 120, 200, 30);
        frame.add(account);

        JPasswordField password = new JPasswordField();
        password.setBounds(135, 160, 200, 30);
        frame.add(password);

        JCheckBox auto_login = new JCheckBox("自动登录");
        auto_login.setBounds(135, 200, 80, 20);
        auto_login.setOpaque(false);
        frame.add(auto_login);

        JCheckBox remember = new JCheckBox("记住密码");
        remember.setBounds(240, 200, 80, 20);
        remember.setOpaque(false);
        frame.add(remember);

        Font font = new Font("宋体", Font.BOLD | Font.ITALIC, 16);

        JButton sign_up = new JButton("注册账号");
        sign_up.setBounds(330, 120, 120, 30);
        sign_up.setForeground(Color.yellow);
        sign_up.setFont(font);
        sign_up.setContentAreaFilled(false);
        sign_up.setBorderPainted(false);
        sign_up.addActionListener(e -> {
        });
        frame.add(sign_up);
        sign_up.addActionListener(e -> {
            FrameTools.registFrame();
        });

        JButton forget = new JButton("找回密码");
        forget.setBounds(330, 160, 120, 30);
        forget.setForeground(Color.yellow);
        forget.setFont(font);
        forget.setContentAreaFilled(false);
        forget.setBorderPainted(false);
        forget.addActionListener(e -> {
            FrameTools.findOutPassFrame();
        });
        frame.add(forget);

        JButton sign_in = new JButton("登        录");
        sign_in.setFont(new Font("宋体", Font.BOLD, 15));
        sign_in.setBounds(135, 250, 180, 40);
        sign_in.addActionListener(e -> {
            //判断是否合法
            String acc = account.getText();
            String pass = new String(password.getPassword());
            if (acc.length() != 5) {
                JOptionPane.showConfirmDialog(null, "您输入的账号错误，账号需为五位数字");
                account.setText("");
                password.setText("");
                return;
            }
            for (int i = 0; i < acc.length(); i++) {
                if (acc.charAt(i) - '0' < 0 || acc.charAt(i) - '0' > 9) {
                    JOptionPane.showConfirmDialog(null, "您输入的账号包含非法字符!");
                    account.setText("");
                    password.setText("");
                    return;
                }
            }
            //进行账号验证，
            final byte[] byts = {14};
            final Object obj = new Object();
            byts[0] = ConnectWithServer.login(acc, pass);
            System.out.println("接收过来的值为" + byts[0]);
            switch (byts[0]) {
                case 10: {
                    JOptionPane.showConfirmDialog(null, "账号不存在");
                    sign_in.setText("登        录");
                    return;
                }
                case 11: {
                    JOptionPane.showConfirmDialog(null, "密码错误");
                    sign_in.setText("登        录");
                    return;
                }
                case 12: {
                    JOptionPane.showConfirmDialog(null, "账号密码正确！");
                    sign_in.setText("登        录");
                    return;
                }
                case 13: {
                    JOptionPane.showConfirmDialog(null, "登陆成功！");
                    //等待接收对面的好友数据
                    String haoyoumsg = ConnectWithServer.waitForHaoYouMsg();
                    ArrayList<String> haoyou = new ArrayList<>();
                    String temps = "";
                    for (int i = 0; i < haoyoumsg.length(); i++) {
                        char temp = haoyoumsg.charAt(i);
                        if (temp == ',' || i == haoyoumsg.length() - 1) {
                            haoyou.add(temps);
                            temps = "";
                        } else {
                            temps = temps + temp;
                        }
                    }
                    System.out.println("好友信息:" + haoyou.toString());
                    //解析好友msg
                    System.out.println("好友信息：" + haoyoumsg);
                    //根据这个好友信息动态的创建一个一个动态的好友窗格
                    new Clients(haoyou).UI();
                    frame.dispose();
                    sign_in.setText("登        录");
                    return;//登陆成功!
                }
                case 14: {
                    JOptionPane.showConfirmDialog(null, "连接超时");
                    sign_in.setText("登        录");
                    return;
                }
            }

        });
        frame.add(sign_in);
        account.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_TAB){
                    System.out.println("=====");
                    password.requestFocus();
                }
            }
        });

        frame.setVisible(true);

        account.requestFocus();
    }
}
