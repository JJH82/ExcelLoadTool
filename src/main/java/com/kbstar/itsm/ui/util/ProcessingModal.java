package com.kbstar.itsm.ui.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;


public enum ProcessingModal  {
	
	
	getInstance;
	
	JWindow back;
	JWindow content;
	ModalListener listener = new ModalListener();
	Optional<Component> parent = Optional.empty();
	String msg = "처리중..";
	
	private ProcessingModal()  {

		back = new JWindow();
		content = new JWindow();
		
		// 윈도우중 최상단예표시

//		content.setAlwaysOnTop(true);
//		back.setAlwaysOnTop(true);
		
		// 입력 금지만드는 부분 투명하게
		back.setOpacity(0.1f);
		
		// 처리메시지
		new Table
		.Builder(content)
		.tr()
		.td(new TD.Builder(new JLabel(new ImageIcon(this.getClass().getResource("loader.gif")))).build())
		.tr()
		.td( new TD.Builder(new JLabel(msg)).build() )
		.build();
		
		// 내용크기에 맞게 설정
		content.pack();
		
	}
	
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void open(Component parent) {
		this.parent = Optional.of(parent);
		
		back.setSize(parent.getSize());
		back.setLocationRelativeTo(parent);
		back.setLocation(back.getLocation().x, back.getLocation().y+27);
		content.setLocationRelativeTo(parent);		

		back.setVisible(true);
		content.setVisible(true);
		
		// 모달창이 대상이 크기변경이나 이동시 같이 이동할수 있도록 이벤트추가
		parent.addComponentListener(listener);
		
		back.toFront();
		content.toFront();
		
	}
	
	
	public void close() {

		this.parent.ifPresent( (o) -> o.removeComponentListener(listener));
		
		content.setVisible(false);
		back.setVisible(false);
	}
	
	
	private class ModalListener extends ComponentAdapter{

		public void componentMoved(ComponentEvent evt) {
			
			content.setLocationRelativeTo(evt.getComponent());
			
			back.setLocationRelativeTo(evt.getComponent());				
			back.setLocation(back.getLocation().x, back.getLocation().y+27); // 타이클바 크기만큼 줄임
		}
		
		public void componentResized(ComponentEvent evt) {
			
			back.setSize(evt.getComponent().getSize());
			back.setLocationRelativeTo(evt.getComponent());
			back.setLocation(back.getLocation().x, back.getLocation().y+27);
			
			back.toFront();
			content.toFront();
			
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		ProcessingModal m = ProcessingModal.getInstance;
		
		JFrame j1 = new JFrame();
		j1.setSize(300,300);
		j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j1.add(new JButton("버튼"));
		
		j1.setVisible(true);
		
		m.open(j1);
		
		
		
		Thread.sleep(20000);
		System.out.println("실행");
		m.close();
		
		System.out.println(new File("src\\main\\java\\com\\kbstar\\itsm\\ui\\util\\loader.gif").getAbsolutePath());
	}
	


	


	


	
	
}
