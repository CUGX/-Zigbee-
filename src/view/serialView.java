package view;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;

import serialException.*;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import Dao.GreenHouseStatDaoImple;
import entity.GreenHouseStat;

import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;



public class serialView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private List<String> commList = null;	//������ö˿ں�
	private SerialPort serialPort = null;	//���洮�ڶ���
	private JPanel contentPane;
	private JTextField textTem;
	private JTextField textHum;
	private JTextField txtLight;
	private JTextField textSmoke;
	
	//X...Times ����ָʾ�Ӵ��ڶ�ȡ���ݵĴ����������ظ���ȡ
	private static int temTimes = 0;
	private static int lightTimes = 0;
	private static int smokeTimes = 0;
	private static int motorTimes = 0;
	private static int smokeCount = 0;
	private static int personTimes = 0;
	//X...State����ָʾ�豸״̬
	private static boolean motorState = false;
	private static boolean lightState = false;
	private static boolean autoState = false;
	private static boolean smokeState = true;//Ĭ�� ����������
	
	//��ȡ��ʪ�ȡ�������ǿ����ֵ
	public static float tem = 0;
	public static float maxTem = 0;		//���ֵ
	public static float upperTem = 0;	//����ֵ
	public static float hum = 0;
	public static float light = 0;
	public static String smoke = null;
	public static int serialNumber = 0;
	
	//pRotation �����ת��rRotation �����ת��sRotation ���ֹͣת����
	public static byte[] pRotation = {(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] rRotation = {(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x0a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] sRotation = {(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x0b,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] rotationState = null;
	//�ƹ����allLightON  �������еƹ⡢allLightOFF �ر����еƹ�
	public static byte[] Light1ON = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light1OFF =	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light2ON =	 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light2OFF = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light3ON = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light3OFF = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light4ON = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] Light4OFF = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] allLightON = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x0c,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] allLightOFF = 	{(byte) 0xCC,(byte) 0xEE,0x01,0x09,0x0d,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightChange = null;
	
			
	private static byte[] lightSetClass1 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass2 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass3 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass4 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass5 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass6 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass7 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass8 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	private static byte[] lightSetClass9 = {(byte) 0xCC,(byte) 0xEE,0x01,0x0a,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xFF};
	public static byte[] lightRankModify = null;
	
	private  Container ct; 
    //����������塣  
	private  BackgroundPanel bgp;
	
	static serialView frame;
	private JTextField txtPerson;
	private JTextField textTemUpper;
	private JTextField textTemMax;
	private JTextField textSmokeAlarm;
	
	/**
	 * Launch the application.5
	/**/
	public static void main(String[] args) {
		
		launch();
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					serialView frame = new serialView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		//��������ͼ
//		Chart.createChart();
	}
	
	public static void launch() {
		
		try {
			serialView frame = new serialView();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					serialView frame = new serialView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		//��������ͼ
//		Chart.createChart();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				//��������ͼ
				Chart.createChart();
			}
		}).start();;
	}
	
		
	/**
	 * Create the frame.
	 */
	public serialView() {
		this.commList = SerialTool.findPort();		//ɨ����ô���
		setTitle("���ܼҾ�ϵͳ");
//		setIconImage(Toolkit.getDefaultToolkit().getImage(serialView.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ct=this.getContentPane();  
        getContentPane().setLayout(null);  
        //��������һ��ͼƬ���Խ��  
        bgp=new BackgroundPanel((new ImageIcon("pic/HointelligenceHome.jpg")).getImage());
        bgp.setBounds(0,0,1010,700);  
        ct.add(bgp);
        bgp.setLayout(null);
        
        JLabel lableTitle = new JLabel("\u667A\u80FD\u519C\u4E1A\u5927\u68DA\u76D1\u63A7\u7CFB\u7EDF");
        lableTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lableTitle.setBounds(325, 28, 360, 54);
        bgp.add(lableTitle);
        lableTitle.setFont(new Font("����", Font.BOLD, 32));
        lableTitle.setOpaque(true);
        
        JLabel lablefunc = new JLabel("\u529F\u80FD\u6A21\u5757");
        lablefunc.setHorizontalAlignment(SwingConstants.CENTER);
        lablefunc.setBounds(56, 90, 95, 30);
        bgp.add(lablefunc);
        lablefunc.setBackground(new Color(204, 255, 204));
        lablefunc.setFont(new Font("����", Font.BOLD, 18));
        lablefunc.setOpaque(true);
        
        JLabel labelCtrl = new JLabel("\u5927\u68DA\u63A7\u5236\u6A21\u5757");
        labelCtrl.setBounds(57, 330, 125, 30);
        bgp.add(labelCtrl);
        labelCtrl.setOpaque(true);
        labelCtrl.setHorizontalAlignment(SwingConstants.CENTER);
        labelCtrl.setFont(new Font("����", Font.BOLD, 18));
        labelCtrl.setBackground(new Color(204, 255, 204));
        
        JPanel funcPanel = new JPanel();
        funcPanel.setBounds(98, 120, 540, 180);
        bgp.add(funcPanel);
        funcPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(204, 255, 204), new Color(204, 255, 204), new Color(0, 204, 51), new Color(0, 204, 51)));
        funcPanel.setLayout(null);
        //���ô����Ϊ͸��
//        funcPanel.setOpaque(false);
        
        JLabel lableTem = new JLabel("��ǰ�¶ȣ�");
        lableTem.setBackground(SystemColor.text);
        lableTem.setHorizontalAlignment(SwingConstants.CENTER);
        lableTem.setBounds(28, 25, 90, 30);
        funcPanel.add(lableTem);
        lableTem.setFont(new Font("����", Font.PLAIN, 18));
        lableTem.setOpaque(true);
        
        textTem = new JTextField();
        textTem.setBackground(SystemColor.text);
        textTem.setBounds(125, 25, 111, 30);
        funcPanel.add(textTem);
        textTem.setHorizontalAlignment(SwingConstants.CENTER);
        textTem.setFont(new Font("����", Font.PLAIN, 20));
        textTem.setText("0.0\u2103");
        textTem.setColumns(10);
        
        JLabel labelHum = new JLabel("��ǰʪ�ȣ�");
        labelHum.setBackground(SystemColor.text);
        labelHum.setHorizontalAlignment(SwingConstants.CENTER);
        labelHum.setBounds(280, 25, 90, 30);
        funcPanel.add(labelHum);
        labelHum.setFont(new Font("����", Font.PLAIN, 18));
        labelHum.setOpaque(true);
        
        textHum = new JTextField();
        textHum.setBackground(SystemColor.text);
        textHum.setBounds(380, 25, 111, 30);
        funcPanel.add(textHum);
        textHum.setHorizontalAlignment(SwingConstants.CENTER);
        textHum.setFont(new Font("����", Font.PLAIN, 20));
        textHum.setText("0.0%");
        textHum.setColumns(10);
						
        
//        JLabel lableLight = new JLabel("����ǿ�ȣ�");
//        lableLight.setHorizontalAlignment(SwingConstants.CENTER);
//        lableLight.setBounds(28, 74, 90, 30);
//        funcPanel.add(lableLight);
//        lableLight.setFont(new Font("����", Font.PLAIN, 18));
//        lableLight.setOpaque(true);
        
        txtLight = new JTextField();
        txtLight.setBackground(SystemColor.text);
        txtLight.setBounds(125, 74, 111, 30);
        funcPanel.add(txtLight);
        txtLight.setText("0Lux");
        txtLight.setHorizontalAlignment(SwingConstants.CENTER);
        txtLight.setFont(new Font("����", Font.PLAIN, 20));
        txtLight.setColumns(10);
        
        JLabel labelSmoke = new JLabel("���������");
        labelSmoke.setBackground(SystemColor.text);
        labelSmoke.setHorizontalAlignment(SwingConstants.CENTER);
        labelSmoke.setBounds(280, 74, 90, 30);
        funcPanel.add(labelSmoke);
        labelSmoke.setFont(new Font("����", Font.PLAIN, 18));
        labelSmoke.setOpaque(true);
        
        textSmoke = new JTextField();
        textSmoke.setBackground(SystemColor.text);
        textSmoke.setEditable(false);
        textSmoke.setBounds(380, 74, 111, 30);
        funcPanel.add(textSmoke);
        textSmoke.setText("�������� ");
        textSmoke.setHorizontalAlignment(SwingConstants.CENTER);
        textSmoke.setFont(new Font("����", Font.PLAIN, 20));
        textSmoke.setColumns(10);
        textSmoke.setBackground(Color.GREEN);
        
        JLabel labelLight = new JLabel("����ǿ�ȣ�");
        labelLight.setBackground(SystemColor.text);
        labelLight.setOpaque(true);
        labelLight.setHorizontalAlignment(SwingConstants.CENTER);
        labelLight.setFont(new Font("����", Font.PLAIN, 18));
        labelLight.setBounds(28, 74, 90, 30);
        funcPanel.add(labelLight);
        
        JLabel labelPerson = new JLabel("������Ա");
        labelPerson.setBackground(SystemColor.text);
        labelPerson.setOpaque(true);
        labelPerson.setHorizontalAlignment(SwingConstants.CENTER);
        labelPerson.setFont(new Font("����", Font.PLAIN, 18));
        labelPerson.setBounds(28, 125, 90, 30);
        funcPanel.add(labelPerson);
        
        txtPerson = new JTextField();
        txtPerson.setText("NULL");
        txtPerson.setHorizontalAlignment(SwingConstants.CENTER);
        txtPerson.setFont(new Font("����", Font.PLAIN, 20));
        txtPerson.setEditable(false);
        txtPerson.setColumns(10);
        txtPerson.setBackground(Color.WHITE);
        txtPerson.setBounds(125, 125, 111, 30);
        funcPanel.add(txtPerson);
        
        JLabel labelSerialCon = new JLabel("\u4E32\u53E3\u63A7\u5236\u6A21\u5757");
        labelSerialCon.setHorizontalAlignment(SwingConstants.CENTER);
        labelSerialCon.setBounds(652, 90, 125, 30);
        bgp.add(labelSerialCon);
        labelSerialCon.setBackground(new Color(204, 255, 204));
        labelSerialCon.setFont(new Font("����", Font.BOLD, 18));
        labelSerialCon.setOpaque(true);
        
        JPanel serialPanel = new JPanel();
        serialPanel.setBounds(702, 120, 270, 480);
        bgp.add(serialPanel);
        serialPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(204, 255, 204), new Color(204, 255, 204), new Color(0, 204, 51), new Color(0, 204, 51)));
        serialPanel.setLayout(null);
        //���ô����Ϊ͸��
//        serialPanel.setOpaque(false);
        
        JLabel lblNewLabel = new JLabel("\u53EF\u7528\u4E32\u53E3");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(28, 32, 100, 30);
        serialPanel.add(lblNewLabel);
        lblNewLabel.setFont(new Font("����", Font.PLAIN, 18));
        lblNewLabel.setOpaque(true);
        
        //��Ӵ���ѡ��ѡ��
        JComboBox commChoice = new JComboBox();
        commChoice.setBounds(140, 32, 80, 30);
        serialPanel.add(commChoice);
        commChoice.setFont(new Font("Consolas", Font.PLAIN, 20));
        
      //����Ƿ��п��ô��ڣ��������ѡ����
  		if (commList == null || commList.size()<1) {
  				JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);
  			}
  		else {
  				for (String s : commList) {
  					commChoice.addItem(s);
  				}
  		}
        
        JComboBox bRateChoice = new JComboBox();
        bRateChoice.setMaximumRowCount(6);
        bRateChoice.setBounds(140, 90, 100, 30);
        serialPanel.add(bRateChoice);
        bRateChoice.setModel(new DefaultComboBoxModel(new String[] {"1200", "2400", "4800", "9600", "14400", "19200", "115200"}));
        bRateChoice.setFont(new Font("Consolas", Font.PLAIN, 20));
        
        JLabel lablebRate = new JLabel("\u6CE2\u7279\u7387");
        lablebRate.setHorizontalAlignment(SwingConstants.CENTER);
        lablebRate.setBounds(28, 90, 100, 30);
        serialPanel.add(lablebRate);
        lablebRate.setFont(new Font("����", Font.PLAIN, 18));
        lablebRate.setOpaque(true);
        
        
        JButton openSerialButton = new JButton("�򿪴���");
        openSerialButton.setBounds(68, 339, 120, 36);
        serialPanel.add(openSerialButton);
        openSerialButton.setFont(new Font("����", Font.PLAIN, 20));
        
        JButton closeButton = new JButton("�رմ���");
        closeButton.setBounds(68, 401, 120, 36);
        serialPanel.add(closeButton);
        closeButton.setFont(new Font("����", Font.PLAIN, 20));
        
        JLabel label = new JLabel("\u6570\u636E\u4F4D");
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("����", Font.PLAIN, 18));
        label.setBounds(28, 148, 100, 30);
        serialPanel.add(label);
        
        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"8", "7", "6", "5"}));
        comboBox.setMaximumRowCount(6);
        comboBox.setFont(new Font("Consolas", Font.PLAIN, 20));
        comboBox.setBounds(140, 148, 80, 30);
        serialPanel.add(comboBox);
        
        JLabel label_1 = new JLabel("\u505C\u6B62\u4F4D");
        label_1.setOpaque(true);
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(new Font("����", Font.PLAIN, 18));
        label_1.setBounds(28, 206, 100, 30);
        serialPanel.add(label_1);
        
        JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"1", "1.5", "2"}));
        comboBox_1.setMaximumRowCount(6);
        comboBox_1.setFont(new Font("Consolas", Font.PLAIN, 20));
        comboBox_1.setBounds(140, 206, 80, 30);
        serialPanel.add(comboBox_1);
        
        JLabel label_2 = new JLabel("\u68C0\u9A8C\u4F4D");
        label_2.setOpaque(true);
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        label_2.setFont(new Font("����", Font.PLAIN, 18));
        label_2.setBounds(28, 264, 100, 30);
        serialPanel.add(label_2);
        
        JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"None", "Odd", "Even", "Mark", "Space"}));
        comboBox_2.setMaximumRowCount(6);
        comboBox_2.setFont(new Font("Consolas", Font.PLAIN, 20));
        comboBox_2.setBounds(140, 264, 80, 30);
        serialPanel.add(comboBox_2);
        
        JPanel ctrlPanel = new JPanel();
//        ctrlPanel.setBackground(Color.WHITE);
        ctrlPanel.setBounds(98, 360, 540, 291);
        bgp.add(ctrlPanel);
        ctrlPanel.setLayout(null);
//        ctrlPanel.setOpaque(false);
//        com.sun.awt.AWTUtilities.setWindowOpacity(ctrlPanel, 0.3f);
        
        JButton motorButton = new JButton("�������");
        motorButton.setBounds(45, 99, 120, 30);
        ctrlPanel.add(motorButton);
        motorButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		/*
        		 * motorState == false,������ڹ���
        		 * �����������¡��������������ݣ��������
        		 * */
        		if(motorState == false) {
        			try {
    					SerialTool.sendToPort(serialPort, pRotation);
    					motorButton.setText("�رյ��");
    					motorButton.setBackground(Color.GREEN);
    					motorState = true;
    				} catch (SendDataToSerialPortFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				} catch (SerialPortOutputStreamCloseFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
        		}
        		/*
        		 * motorState == true,���������ת
        		 * �޸ģ��ٽ�motorState״̬���false
        		 * */
        		else {
        			try {
						SerialTool.sendToPort(serialPort, sRotation);
						motorButton.setText("�������");
    					motorButton.setBackground(Color.WHITE);
    					motorState = false;
					} catch (SendDataToSerialPortFailure e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SerialPortOutputStreamCloseFailure e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        			
        		}
        		
        	}
        });
        motorButton.setFont(new Font("����", Font.PLAIN, 18));
        
        JButton lightRankModifyButton = new JButton("���ڵƹ�����");
        lightRankModifyButton.setBounds(288, 160, 145, 30);
        lightRankModifyButton.setFont(new Font("����", Font.PLAIN, 18));
        ctrlPanel.add(lightRankModifyButton);
        
        
        JButton turnOnButton = new JButton("�ڰ�����");
        turnOnButton.setBounds(45, 160, 120, 30);
        ctrlPanel.add(turnOnButton);
        turnOnButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		//������ڰ��������󣬷��Ϳ�������
        		if (lightState == false ) {
        			try {
    					SerialTool.sendToPort(serialPort, allLightON);
    					turnOnButton.setBackground(Color.GREEN);
    					lightState = true;
    				} catch (SendDataToSerialPortFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				} catch (SerialPortOutputStreamCloseFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
        		}
        		else {
        			try {
    					SerialTool.sendToPort(serialPort, allLightOFF);
    					turnOnButton.setBackground(Color.WHITE);
    					lightState = false;
    				} catch (SendDataToSerialPortFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				} catch (SerialPortOutputStreamCloseFailure e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
//        			JOptionPane.showMessageDialog(null, "LED���Ѿ����ڴ�״̬��", "��ܰ��ʾ", JOptionPane.INFORMATION_MESSAGE);
        		}
        	}
        });
        turnOnButton.setFont(new Font("����", Font.PLAIN, 18));
        
        closeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		SerialTool.closePort(serialPort);
        	}
        });
        
        
        JButton autoButton = new JButton("�Զ����ģʽ");
        autoButton.setBounds(188, 224, 150, 30);
        ctrlPanel.add(autoButton);
        autoButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		/*
        		 * �������Զ����ģʽ����
        		 * �¶�>30��C�����������
        		 * ����С��20Lux�����ƹ�
        		 * */
        		if (autoState == false) {
        			autoButton.setBackground(Color.GREEN);
            		autoButton.setText("�ر��Զ����");
            		autoState = true;
            		
            		if ( tem > upperTem ) {
            			try {
    						SerialTool.sendToPort(serialPort, sRotation);
    						motorButton.setText("�������");
    						motorButton.setBackground(Color.LIGHT_GRAY);
    						motorState = false;
    					} catch (SendDataToSerialPortFailure e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					} catch (SerialPortOutputStreamCloseFailure e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
            		}
            		if ( light<0 ) {
            			try {
    						SerialTool.sendToPort(serialPort, allLightON);
    						turnOnButton.setBackground(Color.GREEN);
    						lightState = true;
    					} catch (SendDataToSerialPortFailure e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					} catch (SerialPortOutputStreamCloseFailure e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
            		}
            		else {
            			try {
        					SerialTool.sendToPort(serialPort, allLightOFF);
        					turnOnButton.setBackground(Color.LIGHT_GRAY);
        					lightState = false;
        				} catch (SendDataToSerialPortFailure e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				} catch (SerialPortOutputStreamCloseFailure e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}
//            			JOptionPane.showMessageDialog(null, "LED���Ѿ����ڹر�״̬ ��", "��ܰ��ʾ", JOptionPane.INFORMATION_MESSAGE);
            		}
            		
            		
        		}
        		else {
        			autoButton.setBackground(Color.LIGHT_GRAY);
            		autoButton.setText("�Զ����ģʽ");
            		autoState = false;
        		}
        		
        	}
        });
        autoButton.setFont(new Font("����", Font.PLAIN, 18));
        
        JLabel labelTemUpper = new JLabel("\u6E29\u5EA6\u4E0A\u9650\uFF1A");
        labelTemUpper.setOpaque(true);
        labelTemUpper.setHorizontalAlignment(SwingConstants.CENTER);
        labelTemUpper.setFont(new Font("����", Font.PLAIN, 18));
        labelTemUpper.setBackground(Color.WHITE);
        labelTemUpper.setBounds(45, 36, 90, 30);
        ctrlPanel.add(labelTemUpper);
        
        textTemUpper = new JTextField();
        textTemUpper.setText("0.0");
        textTemUpper.setHorizontalAlignment(SwingConstants.CENTER);
        textTemUpper.setFont(new Font("����", Font.PLAIN, 20));
        textTemUpper.setColumns(10);
        textTemUpper.setBackground(Color.WHITE);
        textTemUpper.setBounds(145, 36, 111, 30);
        ctrlPanel.add(textTemUpper);
        
        JLabel labelTemMax = new JLabel("\u6E29\u5EA6\u6700\u503C\uFF1A");
        labelTemMax.setOpaque(true);
        labelTemMax.setHorizontalAlignment(SwingConstants.CENTER);
        labelTemMax.setFont(new Font("����", Font.PLAIN, 18));
        labelTemMax.setBackground(Color.WHITE);
        labelTemMax.setBounds(288, 36, 90, 30);
        ctrlPanel.add(labelTemMax);
        
        textTemMax = new JTextField();
        textTemMax.setText("0.0\u2103");
        textTemMax.setHorizontalAlignment(SwingConstants.CENTER);
        textTemMax.setFont(new Font("����", Font.PLAIN, 20));
        textTemMax.setColumns(10);
        textTemMax.setBackground(Color.WHITE);
        textTemMax.setBounds(388, 36, 111, 30);
        ctrlPanel.add(textTemMax);
        
        JComboBox lightRankChoice = new JComboBox();
        lightRankChoice.setMaximumRowCount(6);
        lightRankChoice.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"}));
        /*
         * �޸�itemֵ��������Ӧ����
         * */
        lightRankChoice.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		String lightC = (String) lightRankChoice.getSelectedItem();
        		int lightCh = Integer.parseInt(lightC);
//        		System.out.println("lightCh"+lightCh);
        		
        		switch(lightCh) {
        		case 1: lightRankModify = lightSetClass1;
        		case 2: lightRankModify = lightSetClass2;
        		case 3: lightRankModify = lightSetClass3;
        		case 4: lightRankModify = lightSetClass4;
        		case 5: lightRankModify = lightSetClass5;
        		case 6: lightRankModify = lightSetClass6;
        		case 7: lightRankModify = lightSetClass7;
        		case 8: lightRankModify = lightSetClass8;
        		case 9: lightRankModify = lightSetClass9;
        		}
        		
        		try {
					SerialTool.sendToPort(serialPort, lightRankModify);
					System.out.println("����ȼ�"+lightCh);
				} catch (SendDataToSerialPortFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        lightRankChoice.setFont(new Font("Consolas", Font.PLAIN, 20));
        lightRankChoice.setBounds(442, 160, 50, 30);
        ctrlPanel.add(lightRankChoice);
        
        JComboBox rotChoice = new JComboBox();
        rotChoice.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		String rotC = (String) rotChoice.getSelectedItem();
        		if(rotC.equals("��ת")) {
        			rotationState = sRotation;
        			motorButton.setBackground(Color.GREEN);
        		}
        		else if (rotC.equals("��ת")) {
        			rotationState = pRotation;
        			motorButton.setBackground(Color.GREEN);
        		}
        		else if (rotC.equals("ֹͣ")) {
        			rotationState = sRotation;
        			motorButton.setBackground(Color.WHITE);
        		}
        		
        		try {
					SerialTool.sendToPort(serialPort, rotationState);
				} catch (SendDataToSerialPortFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        rotChoice.setModel(new DefaultComboBoxModel(new String[] {"��ת", "��ת", "ֹͣ"}));
        rotChoice.setMaximumRowCount(6);
        rotChoice.setFont(new Font("����", Font.PLAIN, 18));
        rotChoice.setBounds(175, 101, 74, 30);
        ctrlPanel.add(rotChoice);
        
        textSmokeAlarm = new JTextField();
        textSmokeAlarm.setText("��������");
        textSmokeAlarm.setHorizontalAlignment(SwingConstants.CENTER);
        textSmokeAlarm.setFont(new Font("����", Font.PLAIN, 20));
        textSmokeAlarm.setEditable(false);
        textSmokeAlarm.setColumns(10);
        textSmokeAlarm.setBackground(Color.WHITE);
        textSmokeAlarm.setBounds(331, 104, 111, 30);
        ctrlPanel.add(textSmokeAlarm);
        
        JComboBox lightChoice = new JComboBox();
        lightChoice.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		String lightCC = (String) lightChoice.getSelectedItem();
        		switch(lightCC) {
        		case "��LED1": lightChange = Light1ON;
        		case "��LED1": lightChange = Light1OFF;
        		case "��LED2": lightChange = Light2ON;
        		case "��LED2": lightChange = Light2OFF;
        		case "��LED3": lightChange = Light3ON;
        		case "��LED3": lightChange = Light3OFF;
        		case "��LED4": lightChange = Light4ON;
        		case "��LED4": lightChange = Light4OFF;
        		case "LEDȫ��": lightChange = allLightON;
        		case "LEDȫ��": lightChange = allLightOFF;
        		}
        		
        		try {
					SerialTool.sendToPort(serialPort, lightChange);
					System.out.println(lightCC);
				} catch (SendDataToSerialPortFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        lightChoice.setModel(new DefaultComboBoxModel(new String[] {"\u5F00LED1", "\u5173LED1", "\u5F00LED2", "\u5173LED2", "\u5F00LED3", "\u5173LED3", "\u5F00LED4", "\u5173LED4", "LED\u5168\u5F00", "LED\u5168\u5173"}));
        lightChoice.setMaximumRowCount(6);
        lightChoice.setFont(new Font("����", Font.PLAIN, 18));
        lightChoice.setBounds(175, 160, 90, 30);
        ctrlPanel.add(lightChoice);
        
        
        //��Ӵ򿪴��ڰ�ť���¼�����
        openSerialButton.addActionListener(new ActionListener() {

        	public void actionPerformed(ActionEvent e) {
        					
        		//��ȡ��������
        		String commName = (String) commChoice.getSelectedItem();			
        		//��ȡ������
        		String bpsStr = (String) bRateChoice.getSelectedItem();
        		
        		//��鴮�������Ƿ��ȡ��ȷ
        		if (commName == null || commName.equals("")) {
        			JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);			
        		}
        		else {
        			//��鲨�����Ƿ��ȡ��ȷ
        			if (bpsStr == null || bpsStr.equals("")) {
        				JOptionPane.showMessageDialog(null, "�����ʻ�ȡ����", "����", JOptionPane.INFORMATION_MESSAGE);
        			}
        			else {
        				//�������������ʾ���ȡ��ȷʱ
        				int bps = Integer.parseInt(bpsStr);
        				try {
        					
        					//��ȡָ���˿����������ʵĴ��ڶ���
        					serialPort = SerialTool.openPort(commName, bps);
        					//�ڸô��ڶ�������Ӽ�����
        					SerialTool.addListener(serialPort, new SerialListener());
        					//�����ɹ�������ʾ
        					JOptionPane.showMessageDialog(null, "�����ɹ����Ժ���ʾ������ݣ�", "��ܰ��ʾ", JOptionPane.INFORMATION_MESSAGE);
        					
        				} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
        					//��������ʱʹ��һ��Dialog��ʾ����Ĵ�����Ϣ
        					JOptionPane.showMessageDialog(null, e1, "����", JOptionPane.INFORMATION_MESSAGE);
        				}
        			}
        		}
        		
        	}
        });
        this.setSize(1027,744);
        this.setVisible(true);
				
	}
	
	/**
	 * ���ڲ�����ʽ����һ�����ڼ�����
	 *
	 */
	private class SerialListener implements SerialPortEventListener {
		
		/**
		 * �����ص��Ĵ����¼�
		 */
	    public void serialEvent(SerialPortEvent serialPortEvent) {
	    	
	    	
	        switch (serialPortEvent.getEventType()) {

	            case SerialPortEvent.BI: // 10 ͨѶ�ж�
	            	JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
	            	break;

	            case SerialPortEvent.OE: // 7 ��λ�����������

	            case SerialPortEvent.FE: // 9 ֡����

	            case SerialPortEvent.PE: // 8 ��żУ�����

	            case SerialPortEvent.CD: // 6 �ز����

	            case SerialPortEvent.CTS: // 3 �������������

	            case SerialPortEvent.DSR: // 4 ����������׼������

	            case SerialPortEvent.RI: // 5 ����ָʾ

	            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
	            	break;
	            
	            case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
	           
	            	//System.out.println("found data");
					String readbuff = null;
					temTimes = 0;
					lightTimes = 0;
					smokeTimes = 0;
					motorTimes = 0;
					personTimes = 0;

					try {
						if (serialPort == null) {
							JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
						}
						else {						
							readbuff = SerialTool.readFromPort(serialPort);	//��ȡ���ݣ������ַ���
//							System.out.println(readbuff);//System.out.println();
							
							//�Զ����������
							if( readbuff == null || readbuff.length()<1 ) {
								//JOptionPane.showMessageDialog(null, "��ȡ���ݹ�����δ��ȡ����Ч���ݣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
								//System.exit(0);
							}
							else {
								
								String[] elements0x = null;	//�������水�ո���ԭʼ�ַ�����õ����ַ�������
								int[] elementsD = null;
								int[] temEles = new int[16];	//��ȡ��ʪ�ȴ���������ֵ
								int[] lightEles = new int[16];	//��ȡ��������������ֵ
								int[] smokeEles = new int[16];	//��ȡ��������������ֵ	
								int[][] motorEles = new int[13][16];
								int[] personEles = new int[16];
								
								
								elements0x = readbuff.split(" ");
								System.out.println("elements0x "+Arrays.toString(elements0x));
								//��16�����ַ�����ת����10��������
								elementsD = new int[elements0x.length];
								for (int i = 0; i < elements0x.length; i++) {
									elementsD[i] = Integer.valueOf(elements0x[i], 16);
						        }
								System.out.println("elementsD "+Arrays.toString(elementsD));
								
								for(int i=0; i<elementsD.length; i++) {
									if (elementsD[i]==84) {
										/**
										 * 
										 */
										int t1 = elementsD[i+1] - 48;
										int t2 = elementsD[i+2]  - 48;
										int h1 = elementsD[i+5] - 48;
										int h2 = elementsD[i+6] - 48;
											
										tem = t1*10+t2;
										hum = h1*10+h2;
										
										textTem.setText(String.valueOf(tem)+"��");
										textHum.setText(String.valueOf(hum)+"%");
										
									}
									
									
								}	//��elementsD��ȡ��Ч���ݽ���
								
								String upperTem0 = null;
								upperTem0 = textTemUpper.getText();
								upperTem = Float.parseFloat(upperTem0);
								System.out.println("upperTem:"+upperTem);
								
								/*
								 * �Զ�����
								 * */
								
								//����ȡ������Ч���ݱ��浽���ݿ���
								serialNumber++;
								System.out.println(serialNumber+" "+tem+" "+hum);
								
//								saveToSQL.toSQL();
								GreenHouseStat ghs = new GreenHouseStat();
								ghs.setGno(serialNumber);
								ghs.setTemper(tem);
								ghs.setHum(hum);
								ghs.setSmoke(smoke);
								ghs.setDate(new Date());
								
								GreenHouseStatDaoImple ghsdi = new GreenHouseStatDaoImple();
								ghsdi.GreenHouseStat_Add(ghs);
								
								//��ʱ��ÿ1s��ȡһ��
						    	/**/
						    	try   
						    	{   
						    		Thread.currentThread();
									Thread.sleep(2000);//����   
						    	}
						    	catch(Exception e){}
								
							}	//401��else����
						}		//392��else����		
						
					} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
						JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);	//������ȡ����ʱ��ʾ������Ϣ���˳�ϵͳ
					}
		            
					break;
	        }	//switch����
	    }	//�����ص��Ĵ����¼�����

	}//���ڼ����ڲ������
}	//�������


	class BackgroundPanel extends JPanel  
	{  
	    /**
		 * serialVersionUID������Java�����л����ơ�����˵��Java�����л�������ͨ���ж����serialVersionUID����֤�汾һ���Եġ�
		 */
		private static final long serialVersionUID = 1L;
		
		Image im;  
	    public BackgroundPanel(Image im)  
	    {  
	        this.im=im;  
	        this.setOpaque(true);  
	    }  
	    //Draw the back ground.  
	    public void paintComponent(Graphics g)  
	    {  
	        super.paintComponents(g);  
	        g.drawImage(im,0,0,this.getWidth(),this.getHeight(),this);  
	          
	    }  
	} 

