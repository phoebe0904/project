

import java.util.*;

enum Grade {EMPLOYEE, SUPERVISOR, DIRECTOR}

abstract public class Staff {
		
	protected String name;
	protected String id;
	protected Grade grade;
	protected int rank;
	protected Staff supervisor;
	private ArrayList<Leave> appliedleaves;

	public Staff(String name, String id, int rank)
	{
		this.name = name;
		this.id = id;
		this.supervisor = null;
		this.appliedleaves = new ArrayList<Leave>();
	}
	
	public Boolean IsSupervisor(){
		return grade == Grade.SUPERVISOR;
	}

	public Boolean IsDirector(){
		return grade == Grade.DIRECTOR;
	}
	
	//	s is the supervisor
	static protected Boolean AskSupervisor(Staff e, Staff s, Leave l){
		// supervisor_approved = some UI interaction
		//if(!supervisor_approved) return false;
		return AskSupervisor(e,s.supervisor,l);
	}
	
	public Boolean IsOverlap(Leave l){
		for(int i = 0; i < appliedleaves.size(); i++){
			Leave current = appliedleaves.get(i);
			if(l.IsOverlap(current)) return true;
		}
		return false;
	}
	
	public void AddLeave(Leave l){
		System.out.println("Add leave");
		this.appliedleaves.add(l);
	}
	
	abstract public Boolean SetRank(int rank);
	abstract public int GetRank();
	abstract protected Boolean AddSubordinate(Staff s);
	abstract public Boolean DelSubordinate(Staff s);
	abstract public int GetNumSubordinate();
	abstract public String GetStringInfo();

	public String GetSimpleStringInfo(){
		return this.name + ", " + this.id;
	}
	
	public int GetNumLeave(){ return this.appliedleaves.size(); }
			
	public String GetLeaveString(){
		if(this.appliedleaves.size()==0) return "";
		String lstr = "";
		for(int i = 0; i < this.appliedleaves.size(); i++){
			Leave l = this.appliedleaves.get(i);
			Calendar fr = l.fr;
			Calendar to = l.to;
			lstr += "\nFrom: " + fr.get(Calendar.YEAR) + "-" + fr.get(Calendar.MONTH) + "-" + fr.get(Calendar.DATE);
			lstr += " To: " + to.get(Calendar.YEAR) + "-" + to.get(Calendar.MONTH) + "-" + to.get(Calendar.DATE);
		}
		return lstr;
	}

	public Boolean SetSupervisor(Staff s){
		if(!s.IsSupervisor() && !s.IsDirector()){
			System.out.println("Assignment fails! The staff is not a supervisor.");
			return false;
		}

		Staff curSupervisor = this.supervisor;
		if(!s.AddSubordinate(this)){
			System.out.println("Assignment unsuccessful!");
			return false;
		}
		else{
			if(this.supervisor != null){
				if(!curSupervisor.DelSubordinate(this)){
					System.out.println("Some errors with the hierarchy");
					s.DelSubordinate(this);
					return false;
				}
			}
			this.supervisor = s;
			System.out.println("Assignment successful!");
			return true;
		}
	}

	public Staff GetSupervisor(){
		return this.supervisor;
	}
	
	
	public String getName(){ return this.name; }
	public String getID(){ return this.id; }
	
	static public Staff ReturnNewObject(String name, String id, int rank){
		return null;
	}
	
}




