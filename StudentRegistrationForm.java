import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.FontMetrics;

public class StudentRegistrationForm {
    public static void main(String[] args) {
        // Создаем главное окно с закругленными углами
        JFrame frame = new JFrame("Регистрация студента") {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                super.paint(g2);
                g2.dispose();
            }
        };
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 20, 20));
        frame.setLocationRelativeTo(null);

        // Основная панель с градиентом
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 150, 255), 
                              getWidth(), getHeight(), new Color(30, 80, 180));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок формы
        JLabel titleLabel = new JLabel("Регистрация студента", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Панель для полей ввода
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Стиль для меток
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = new Color(240, 240, 240);

        // Стиль для полей ввода
        JTextField[] fields = new JTextField[4]; // Исправлено: было 5, но полей 4
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            fields[i].setBackground(new Color(255, 255, 255, 200));
        }

        // Поля формы
        addFormField(formPanel, gbc, "ФИО:", fields[0], labelFont, labelColor);
        addFormField(formPanel, gbc, "Номер группы:", fields[1], labelFont, labelColor);
        addFormField(formPanel, gbc, "Email:", fields[2], labelFont, labelColor);
        addFormField(formPanel, gbc, "Телефон:", fields[3], labelFont, labelColor);
        
        // Поле для пароля
        gbc.gridy++;
        JLabel passLabel = new JLabel("Пароль:");
        passLabel.setFont(labelFont);
        passLabel.setForeground(labelColor);
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        passwordField.setBackground(new Color(255, 255, 255, 200));
        formPanel.add(passwordField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        // Стилизованная кнопка регистрации
        JButton registerButton = createStyledButton("Зарегистрироваться", new Color(70, 130, 180));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean allFieldsFilled = true;
                for (JTextField field : fields) {
                    if (field.getText().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
                
                if (allFieldsFilled && passwordField.getPassword().length > 0) {
                    JOptionPane.showMessageDialog(frame, 
                            "Регистрация прошла успешно!\n" +
                            "ФИО: " + fields[0].getText() + "\n" +
                            "Группа: " + fields[1].getText() + "\n" +
                            "Email: " + fields[2].getText(),
                            "Успешная регистрация",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    for (JTextField field : fields) {
                        field.setText("");
                    }
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, 
                            "Пожалуйста, заполните все поля!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(registerButton);
        
        // Кнопка выхода
        JButton exitButton = createStyledButton("Выход", new Color(220, 80, 60));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Добавляем возможность перемещения окна
        MouseAdapter ma = new MouseAdapter() {
            private Point initialClick;
            
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;
                
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                
                frame.setLocation(thisX + xMoved, thisY + yMoved);
            }
        };
        
        frame.addMouseListener(ma);
        frame.addMouseMotionListener(ma);
        
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
    
    private static void addFormField(JPanel panel, GridBagConstraints gbc, 
                                   String labelText, JTextField field, 
                                   Font labelFont, Color labelColor) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(labelColor);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont().deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Убираем стандартную границу
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        return button;
    }
}