
import java.util.*;
import java.util.Date.*;
import javax.swing.SwingUtilities;

import org.json.simple.parser.ParseException;



import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import java.text.*;

import java.awt.*;
import java.awt.event.*;

enum ButtonType {INFO, LOAD, SAVE, ADD, DEL, ASSIGN, LEAVE};

public class LeaveAppSys extends JFrame implements ActionListener{
    public static final long serialVersionUID=138938126;
    private int x, y, info_w, info_h, gen_w, gen_h;
    private Company company;
    private JFrame assignFrame = null;
	private JTextField assignText;

    private JFrame addFrame;
	private JComboBox<String> addComboBox;
	private JTextField addText_name;
	private JTextField addText_id;
	private JTextField addText_rank;
	private JTextField addText_sid;
	
    private JFrame leaveFrame;
	private JTextField leaveText_fr_dd;
	private JTextField leaveText_fr_mm;
	private JTextField leaveText_fr_yyyy;
	private JTextField leaveText_to_dd;
	private JTextField leaveText_to_mm;
	private JTextField leaveText_to_yyyy;
	
	public Boolean approveState = false;
	public Boolean waitingForApproval = false;
	
	
    private class SButton extends JButton{
    	public ButtonType type;
    	public Staff staff;
    	public SButton(ButtonType type, Staff staff){
    		super();
    		if(type==ButtonType.INFO) this.setText(staff.GetSimpleStringInfo());
    		else if(type==ButtonType.DEL) this.setText("Delete");
    		else if(type==ButtonType.ASSIGN) this.setText("Assign");
    		else if(type==ButtonType.LEAVE) this.setText("Leave");
    		else if(type==ButtonType.LOAD) this.setText("Load");
    		else if(type==ButtonType.SAVE) this.setText("Save");
    		else if(type==ButtonType.ADD) this.setText("Add");
    		this.type = type;
    		this.staff = staff;
    	}
    }
    private JLabel CreateGradeLabel(String grade){
        JLabel label = new JLabel(grade, JLabel.LEFT);
        label.setSize(this.info_w, this.info_h);
        label.setLocation(this.x, this.y);
        this.y += 10; 
        return label;
    }

    private SButton CreateSButton(ButtonType type, Staff staff) throws ParseException, Exception, Throwable{
    	SButton button = new SButton(type, staff);
    	button.setSize(this.gen_w, this.gen_h);
    	if(type==ButtonType.INFO){
        	button.setSize(this.info_w, this.info_h);
    		button.setLocation(this.x, this.y);
    	}
    	else if(type==ButtonType.DEL) button.setLocation(this.x+this.info_w+10, this.y);
    	else if(type==ButtonType.ASSIGN) button.setLocation(this.x+this.info_w+this.gen_w+20, this.y);
    	else if(type==ButtonType.LEAVE){
    		button.setLocation(this.x+this.info_w+this.gen_w*2+30, this.y);
    		if(staff.getClass().getName().equals("Director")) button.setLocation(this.x+this.info_w+10, this.y);
    	}
    	else if(type==ButtonType.LOAD) button.setLocation(this.x, this.y);
    	else if(type==ButtonType.SAVE) button.setLocation(this.x+this.gen_w+10, this.y);
    	else if(type==ButtonType.ADD) button.setLocation(this.x+this.gen_w*2+20, this.y);
    	button.addActionListener(this);
    	return button;
    }

	private void AddSButtonList(JPanel panel, ArrayList<Staff> staffList) throws ParseException, Exception, Throwable{
		for(int i = 0; i < staffList.size(); i++){
			SButton button = this.CreateSButton(ButtonType.INFO, staffList.get(i));
//			button.setSize(this.w,this.h);
//			button.setLocation(this.x,this.y);
			panel.add(button);
			button = this.CreateSButton(ButtonType.DEL, staffList.get(i));
			panel.add(button);
			button = this.CreateSButton(ButtonType.ASSIGN, staffList.get(i));
			panel.add(button);
			button = this.CreateSButton(ButtonType.LEAVE, staffList.get(i));
			panel.add(button);
			this.y += 20;
        	this.y += 20;
		}
	}

	private void Render() throws ParseException, Exception, Throwable{

        JPanel panel = new JPanel();
//        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        
        this.x = 5;
        this.y = 5;
        this.info_w = 150;
        this.info_h = 30;
        this.gen_w = 100;
        this.gen_h = 30;

        JButton button;
    	button = this.CreateSButton(ButtonType.LOAD, null);
    	panel.add(button);
    	button = this.CreateSButton(ButtonType.SAVE, null);
    	panel.add(button);
    	button = this.CreateSButton(ButtonType.ADD, null);
    	panel.add(button);
    	y += 20;
    	y += 20;

        
        
        JLabel label = this.CreateGradeLabel("Director");
        panel.add(label);
        this.y += 20;
        Staff director = company.GetDirector();
        if(director != null){
        	button = this.CreateSButton(ButtonType.INFO, director);
        	panel.add(button);
        	button = this.CreateSButton(ButtonType.LEAVE, director);
        	panel.add(button);
        	y += 20;
        }
    	y += 20;

        label = this.CreateGradeLabel("Supervisors");
        panel.add(label);
    	y += 20;
        ArrayList<Staff> supervisors = company.GetSupervisors();
        this.AddSButtonList(panel, supervisors);

        label = this.CreateGradeLabel("Employees");
        panel.add(label);
    	y += 20;
        ArrayList<Staff> employees = company.GetEmployees();
        this.AddSButtonList(panel, employees);

        
        this.setContentPane(panel);
        int width = 10 + this.info_w + 10 + this.gen_w*3 + 10*3 + 10;
        int height = 10 + this.gen_h*4 +10*3 + 10; // initial buttons and labels
        height += this.company.GetNumStaff()*(this.gen_h + 10);
        
        this.setSize(width, height);
        this.setLocationByPlatform(true);
        this.setVisible(true);
		
	}
	
	public LeaveAppSys() throws ParseException, Exception, Throwable{
		super("Leave Application System");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.company = new Company();
		this.Render();

	}
	
	
	public static void main(String[] args) throws ParseException, Exception, Throwable {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	try {
						new LeaveAppSys();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
		 });
	}

	private void InfoButton(SButton b){
		
		if(this.assignFrame != null){
			if(this.assignFrame.isVisible()){
				System.out.println(assignText);
				assignText.setText(b.staff.getID());
				return;
			}
		}
		
		if(this.addFrame != null){
			if(this.addFrame.isVisible()){
				System.out.println(this.addText_sid);
				this.addText_sid.setText(b.staff.getID());
				return;
			}
		}

		JTextPane textPane = new JTextPane();
//		textPane.setSize(500, 100);
		textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
		Document doc = textPane.getDocument();
		try {
			String message = b.staff.GetStringInfo();
			int additionalHeight = 0;
			String leaveStr = b.staff.GetLeaveString();
			if(!leaveStr.equals("")){
				additionalHeight = 50*(b.staff.GetNumLeave()+1);
				message += "\nApproved Leave(s):" + leaveStr;
			}
//			doc.insertString(doc.getLength(), b.staff.GetStringInfo(), new SimpleAttributeSet());
			doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
			textPane.setSize(500,100+50*additionalHeight);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JScrollPane scrollPane = new JScrollPane(textPane);
//		JOptionPane.showMessageDialog(null, textPane, b.staff.GetSimpleStringInfo() + "'s info", JOptionPane.INFORMATION_MESSAGE);		
		JOptionPane.showMessageDialog(null, scrollPane, b.staff.GetSimpleStringInfo() + "'s info", JOptionPane.INFORMATION_MESSAGE);		
	}

	private void DelButton(SButton b) throws ParseException, Exception, Throwable{
		String name = b.staff.getName();
		String id = b.staff.getID();
		Boolean result = this.company.DelStaff(b.staff.getClass(), id);
		
		JTextPane textPane = new JTextPane();
		textPane.setSize(500, 100);
		textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
		Document doc = textPane.getDocument();
		try {
			String message = "Staff " + name + ", " + id + " deleted successfully";
			if(!result) message = "Staff " + name + ", " + id + " delete unsuccessful";
			doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, textPane, b.staff.GetSimpleStringInfo() + "'s info", JOptionPane.INFORMATION_MESSAGE);
		this.setVisible(false);;
		this.Render();
	}

	private void AssignButton(SButton b){
		this.assignFrame = new JFrame();
		this.assignFrame.setLayout(new BorderLayout());
		JLabel label = new JLabel("Enter the supervisor ID");
		this.assignText = new JTextField("");
		this.assignFrame.add(label, BorderLayout.NORTH);
		this.assignFrame.add(this.assignText, BorderLayout.CENTER);
		JButton btnGetText = new JButton("Assign");	
		Company company = this.company;
		JTextField text = this.assignText;
		JFrame frame = this.assignFrame;
		btnGetText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = String.format("The new supervisor is '%s'", text.getText());
				if(!company.AssignSupervisor(b.staff.getID(), text.getText()))
					message = String.format("Cannot assign '%s' as '%s''s supervisor", text.getText(),b.staff.getID());
				JOptionPane.showMessageDialog(frame, message);
			}
		});
		assignFrame.add(btnGetText, BorderLayout.SOUTH);
        assignFrame.setSize(200, 250);
		assignFrame.setVisible(true);
		assignText.setText("");
		assignFrame.pack();
	}
	
	public class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws java.text.ParseException{
	        return dateFormatter.parseObject(text);
	    }

	    public String valueToString(Object value) throws java.text.ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}


	public void ApprovalDialog(Staff applicant, Leave leave, Boolean approved){
		JTextPane textPane = new JTextPane();
		textPane.setSize(500, 100);
		textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
		Document doc = textPane.getDocument();
		try {
			String fr = leave.fr.get(Calendar.YEAR) + "-" + leave.fr.get(Calendar.MONTH) + "-" + leave.fr.get(Calendar.DATE); 
			String to = leave.to.get(Calendar.YEAR) + "-" + leave.to.get(Calendar.MONTH) + "-" + leave.to.get(Calendar.DATE); 
			String message = applicant.getName() + ", " + applicant.getID()
			+ ", your leave application from " + fr + " to " + to;
			if(approved) message += " is approved!";
			else message += " is rejected!";
			doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, textPane, applicant.GetSimpleStringInfo() + "'s info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void ApproveStep(Staff applicant, Leave leave, Staff current){
		
		if(current == null){

			applicant.AddLeave(leave);
			this.ApprovalDialog(applicant, leave, true);
			this.waitingForApproval = false;
			this.approveState = true;
			this.setEnabled(true);
			return;
		}
		
		LeaveAppSys parentFrame = this;
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setOpaque(true);
	    panel.setBackground(Color.WHITE);
	    panel.setLayout(null);

	    int ddw = this.gen_w/3;
	    int mmw = ddw;
	    int yyyyw = 2* mmw;

	    int y_lvl = 10;
		JLabel label = new JLabel(current.getName() + ", " + current.getID());
    	label.setSize(this.gen_w*2,this.gen_h);
    	label.setLocation(10, y_lvl);
		panel.add(label);
	    y_lvl += this.gen_h + 10;
		label = new JLabel("Do you want to approve staff " + applicant.getName() + ", " + applicant.getID() + "'s leave application, ");
    	label.setSize(this.gen_w*4,this.gen_h);
    	label.setLocation(10, y_lvl);
		panel.add(label);
	    y_lvl += this.gen_h + 10;
	    Calendar fr = leave.fr;
	    Calendar to = leave.to;
	    String datefr = String.format("%s-%s-%s", fr.get(Calendar.YEAR),fr.get(Calendar.MONTH),fr.get(Calendar.DAY_OF_MONTH));
	    String dateto = String.format("%s-%s-%s", to.get(Calendar.YEAR),to.get(Calendar.MONTH),to.get(Calendar.DAY_OF_MONTH));
		label = new JLabel("from " + datefr + " to " + dateto);
    	label.setSize(this.gen_w*2,this.gen_h);
    	label.setLocation(10, y_lvl);
		panel.add(label);
	    y_lvl += this.gen_h + 10;
		
		JButton appBtn = new JButton("Approve");
		JButton rejBtn = new JButton("Reject");
		appBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parentFrame.approveState = true;
				parentFrame.waitingForApproval = true;
				parentFrame.ApproveStep(applicant, leave, current.GetSupervisor());
				frame.dispose();
			}
		});
		rejBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parentFrame.ApprovalDialog(applicant, leave, false);
				parentFrame.approveState = false;
				parentFrame.waitingForApproval = false;
				parentFrame.setEnabled(true);
				frame.dispose();
			}
		});
		
    	appBtn.setSize(this.gen_w,this.gen_h);
    	rejBtn.setSize(this.gen_w,this.gen_h);
    	appBtn.setLocation(10, y_lvl);
    	rejBtn.setLocation(10 + this.gen_w + 10, y_lvl);
		panel.add(label);
	    y_lvl += this.gen_h + 10;
		
		panel.add(appBtn);
		panel.add(rejBtn);
        frame.setContentPane(panel);
        frame.setSize(10 + this.gen_w*4 + 10*3 + 10, 10 + this.gen_h*5 + 10*4 + 10);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
		
	}
		

	
	
	private void LeaveButton(SButton b){

		this.approveState = false;
		this.waitingForApproval = true;
		this.setEnabled(false);

		LeaveAppSys parentFrame = this;
		this.leaveFrame = new JFrame();
		this.leaveFrame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setOpaque(true);
	    panel.setBackground(Color.WHITE);
	    panel.setLayout(null);

	    int ddw = this.gen_w/3;
	    int mmw = ddw;
	    int yyyyw = 2* mmw;

	    int y_lvl = 10;
		JLabel label = new JLabel("From");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10+this.gen_w+10, y_lvl);
		panel.add(label);
		label = new JLabel("To");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10+this.gen_w+10+ddw+mmw+yyyyw+10, y_lvl);
		panel.add(label);
	    y_lvl += this.gen_h+10;
		
		label = new JLabel("DD/MM/YYYY");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	panel.add(label);

    	this.leaveText_fr_dd = new JTextField("");
    	this.leaveText_fr_dd.setSize(ddw,this.gen_h);
    	this.leaveText_fr_dd.setLocation(10 + this.gen_w+10, y_lvl);
    	panel.add(this.leaveText_fr_dd);
    	this.leaveText_fr_mm = new JTextField("");
    	this.leaveText_fr_mm.setSize(mmw,this.gen_h);
    	this.leaveText_fr_mm.setLocation(10 + this.gen_w + 10 + ddw, y_lvl);
    	panel.add(this.leaveText_fr_mm);
    	this.leaveText_fr_yyyy = new JTextField("");
    	this.leaveText_fr_yyyy.setSize(yyyyw,this.gen_h);
    	this.leaveText_fr_yyyy.setLocation(10 + this.gen_w + 10 + ddw + mmw, y_lvl);
    	panel.add(this.leaveText_fr_yyyy);

    	this.leaveText_to_dd = new JTextField("");
    	this.leaveText_to_dd.setSize(ddw,this.gen_h);
    	this.leaveText_to_dd.setLocation(10 + this.gen_w + 10 + ddw+mmw+yyyyw +10, y_lvl);
    	panel.add(this.leaveText_to_dd);
    	this.leaveText_to_mm = new JTextField("");
    	this.leaveText_to_mm.setSize(mmw,this.gen_h);
    	this.leaveText_to_mm.setLocation(10 + this.gen_w + 10 + ddw+mmw+yyyyw +10+ddw, y_lvl);
    	panel.add(this.leaveText_to_mm);
    	this.leaveText_to_yyyy = new JTextField("");
    	this.leaveText_to_yyyy.setSize(yyyyw,this.gen_h);
    	this.leaveText_to_yyyy.setLocation(10 + this.gen_w + 10 + ddw+mmw+yyyyw +10 + ddw+mmw, y_lvl);
    	panel.add(this.leaveText_to_yyyy);
	    y_lvl += this.gen_h+10;

	    JFrame frame = this.leaveFrame;
    	JTextField fr_dd_ = this.leaveText_fr_dd;
    	JTextField to_dd_ = this.leaveText_to_dd;
    	JTextField fr_mm_ = this.leaveText_fr_mm;
    	JTextField to_mm_ = this.leaveText_to_mm;
    	JTextField fr_yyyy_ = this.leaveText_fr_yyyy;
    	JTextField to_yyyy_ = this.leaveText_to_yyyy;


		JButton applyBtn = new JButton("Apply");
		Company company = this.company;
		applyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int fr_dd, to_dd, fr_mm, to_mm, fr_yyyy, to_yyyy;
				try{
					fr_dd = Integer.parseInt(fr_dd_.getText());
					to_dd = Integer.parseInt(to_dd_.getText());
					fr_mm = Integer.parseInt(fr_mm_.getText());
					to_mm = Integer.parseInt(to_mm_.getText());
					fr_yyyy = Integer.parseInt(fr_yyyy_.getText());
					to_yyyy = Integer.parseInt(to_yyyy_.getText());
//					System.out.println(fr_dd);
//					System.out.println(fr_mm);
//					System.out.println(fr_yyyy);
//					System.out.println(to_dd);
//					System.out.println(to_mm);
//					System.out.println(to_yyyy);
					
					Leave leave = new Leave();
//					leave.SetRange(fr_yyyy, fr_mm, fr_dd, to_yyyy, to_mm, to_dd);
					if(leave.SetRange(fr_yyyy, fr_mm, fr_dd, to_yyyy, to_mm, to_dd)
							&& !b.staff.IsOverlap(leave)){
						System.out.println("Not overlapping");
						Staff current = b.staff;
						if(current.GetSupervisor() != null){
							System.out.println("Ask supervisor");
							current = current.GetSupervisor();
							parentFrame.waitingForApproval = true;
							parentFrame.ApproveStep(b.staff,leave,current);
							frame.dispose();
						}
						else{
							parentFrame.ApprovalDialog(b.staff, leave, true);
							parentFrame.approveState = true;
							parentFrame.waitingForApproval = false;
							parentFrame.setEnabled(true);
							b.staff.AddLeave(leave);
							frame.dispose();
						}
					}
					else{
						System.out.println("Incorrect date range or overlapping");
						JTextPane textPane = new JTextPane();
						textPane.setSize(500, 100);
						textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
						Document doc = textPane.getDocument();
						String message = "Either incorrect date range or leave dates overlapped!";
						try {
							doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
						} catch (BadLocationException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, textPane, "Leave Application", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}catch(Exception e1){
					JTextPane textPane = new JTextPane();
					textPane.setSize(500, 100);
					textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
					Document doc = textPane.getDocument();
					String message = "Date format is wrong!";
					try {
						doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, textPane, "Leave Application", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		});
		applyBtn.setSize(this.gen_w + 10 + ddw+mmw+yyyyw +10 + ddw+mmw+yyyyw, this.gen_h);
		applyBtn.setLocation(10, y_lvl);
		y_lvl += this.gen_h + 10;
		panel.add(applyBtn);
        frame.setContentPane(panel);
        frame.setSize(10 + this.gen_w + 10 + ddw+mmw+yyyyw +10 + ddw+mmw+yyyyw +20, 10 + this.gen_h*4 + 10*3 + 10);
        frame.setLocationByPlatform(true);
        
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
        		parentFrame.approveState = false;
        		parentFrame.waitingForApproval = false;
        		parentFrame.setEnabled(true);
            }
        } );
        
        
        frame.setVisible(true);

	}
	
	private void LoadButton(SButton b) throws ParseException, Exception, Throwable{
		this.setVisible(false);;
		this.Render();
	}
	
	private void SaveButton(SButton b){
		JTextPane textPane = new JTextPane();
		textPane.setSize(500, 100);
		textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
		Document doc = textPane.getDocument();
		try {
			String message = "Current configuration saved successfully!";
			if(!this.company.WriteJSON()) message = "Cannot save the current configuration!";
			doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, textPane, "Save", JOptionPane.INFORMATION_MESSAGE);
	}

	private void AddButton(SButton b) throws ParseException, Exception, Throwable{

		LeaveAppSys parentFrame = this;
		this.addFrame = new JFrame();
		this.addFrame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setOpaque(true);
	    panel.setBackground(Color.WHITE);
	    panel.setLayout(null);
	    
	    int y_lvl = 10;
		JLabel label = new JLabel("Type");
		String[] grades = {"Director","Supervisor","Employee"};
		this.addComboBox = new JComboBox<String>(grades);
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	this.addComboBox.setSize(this.gen_w,this.gen_h);
    	this.addComboBox.setLocation(10 + this.gen_w, y_lvl);
    	y_lvl += 30;
		panel.add(label);
		panel.add(this.addComboBox);
    	y_lvl += 10;
		
		label = new JLabel("Name");
		this.addText_name = new JTextField("");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	this.addText_name.setSize(this.gen_w,this.gen_h);
    	this.addText_name.setLocation(10 + this.gen_w, y_lvl);
    	y_lvl += 30;
		panel.add(label);
		panel.add(this.addText_name);
    	y_lvl += 10;
		
		label = new JLabel("ID");
		this.addText_id = new JTextField("");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	this.addText_id.setSize(this.gen_w,this.gen_h);
    	this.addText_id.setLocation(10 + this.gen_w, y_lvl);
    	y_lvl += 30;
		panel.add(label);
		panel.add(this.addText_id);
    	y_lvl += 10;

		label = new JLabel("Rank");
		this.addText_rank = new JTextField("");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	this.addText_rank.setSize(this.gen_w,this.gen_h);
    	this.addText_rank.setLocation(10 + this.gen_w, y_lvl);
    	y_lvl += 30;
		panel.add(label);
		panel.add(this.addText_rank);
    	y_lvl += 10;

    	label = new JLabel("Supervisor ID");
		this.addText_sid = new JTextField("");
    	label.setSize(this.gen_w,this.gen_h);
    	label.setLocation(10, y_lvl);
    	this.addText_sid.setSize(this.gen_w,this.gen_h);
    	this.addText_sid.setLocation(10 + this.gen_w, y_lvl);
    	y_lvl += 30;
		panel.add(label);
		panel.add(this.addText_sid);
    	y_lvl += 10;


		JComboBox<String> grade_ = this.addComboBox;
		JTextField name_ = this.addText_name;
		JTextField id_ = this.addText_id;
		JTextField rank_ = this.addText_rank;
		JTextField sid_ = this.addText_sid;

		this.addComboBox.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String g = grade_.getSelectedItem().toString();
				if(g.equals("Director")){
					id_.setText("000000");
					rank_.setEnabled(true);
					rank_.setText("0");
					sid_.setEnabled(false);
				}
				else if(g.equals("Supervisor")){
					id_.setText("");
					rank_.setText("");
					sid_.setEnabled(true);					
					sid_.setText("");					
				}
				else if(g.equals("Employee")){
					id_.setText("");
					rank_.setText("9999");
					rank_.setEnabled(false);
					sid_.setEnabled(true);
					sid_.setText("");		
				}
			}
		});

		JButton btnGetText = new JButton("Add");
		Company company = this.company;
		btnGetText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextPane textPane = new JTextPane();
				textPane.setSize(500, 100);
				textPane.setPreferredSize(new Dimension(500, textPane.getPreferredSize().height));
				Document doc = textPane.getDocument();

				String grade = grade_.getSelectedItem().toString();
				String name = name_.getText();
				String id = id_.getText();
				int rank;					
				try{
					rank = Integer.parseInt(rank_.getText());					
				}catch(Exception e1){
					try {
						String message = "Rank must be integer!";
						doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, textPane, "Rank", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sid = sid_.getText();
				String message1 = String.format("Staff '%s', '%s' is added successfully!", name, id);
				String message2 = String.format("Cannot add staff '%s', '%s'!", name, id);
				String message = message1;
				if(grade.equals("Director")){
					if(!company.AddDirector(name, id)) message = message2;					
				}
				else if(grade.equals("Supervisor")){
					if(!company.AddNonDirector(Supervisor.class, name, id, sid, rank)) message = message2;					
				}
				else if(grade.equals("Employee")){
					if(!company.AddNonDirector(Employee.class, name, id, sid, rank)) message = message2;					
				}
				try {
					doc.insertString(doc.getLength(), message, new SimpleAttributeSet());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, textPane, "Add Staff", JOptionPane.INFORMATION_MESSAGE);
				parentFrame.setVisible(false);
				try {
					parentFrame.Render();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnGetText.setSize(this.gen_w*2 ,this.gen_h);
		btnGetText.setLocation(10, y_lvl);
		y_lvl += 10;
		panel.add(btnGetText);
		this.addFrame.setContentPane(panel);
		this.addFrame.setSize(this.gen_w*2 + 10*3, 10 + this.gen_h*7 + 10*6 + 10);
		this.addFrame.setLocationByPlatform(true);
		this.addFrame.setVisible(true);
	}

	
	@Override
	public void actionPerformed(ActionEvent e){
		SButton b = (SButton)e.getSource();
		try {
			if(b.type == ButtonType.INFO) this.InfoButton(b);
			else if (b.type == ButtonType.DEL) this.DelButton(b);	
			else if (b.type == ButtonType.ASSIGN) this.AssignButton(b);
			else if (b.type == ButtonType.LEAVE) this.LeaveButton(b);
			else if (b.type == ButtonType.LOAD) this.LoadButton(b);
			else if (b.type == ButtonType.SAVE) this.SaveButton(b);
			else if (b.type == ButtonType.ADD) this.AddButton(b);
		}catch(Throwable e1){
			e1.printStackTrace();
		}
	}


}
