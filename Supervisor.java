import java.util.ArrayList;

public class Supervisor extends Staff{

	// rank is to avoid circular supervisor structure
	private ArrayList<Staff> subordinates;

	public Supervisor(String name, String id, int rank)
	{
		super(name,id,rank);
		this.grade = Grade.SUPERVISOR;
		this.rank = rank;
		this.subordinates = new ArrayList<Staff>();
	}


	
	@Override
	protected Boolean AddSubordinate(Staff s) {

		if(s.getID() == this.id) return false;
			
		if(s.IsDirector()){
			System.out.println("You can't add the Director as your subordinate!");
			return false;
		}
		if(s.IsSupervisor()){
			if(s.GetRank() <= this.rank){
				System.out.println("You can't add an employee as your subordinate as he/she is more/same senior than/as you!");
				return false;
			}
		}
		this.subordinates.add(s);
		return true;
	}

	@Override
	public Boolean DelSubordinate(Staff s) {

		if(s.getID() == this.id) return false;

		for(int i = 0; i < this.subordinates.size(); i++){
			if(this.subordinates.get(i).getID() == s.getID()){
				this.subordinates.remove(i);
				return true;
			}
		}
		return false;
	}


	@Override
	public Boolean SetRank(int rank) { 
		if(rank <= 0){
			System.out.println("You can't set a supervisor to the Director's rank!");
			return false;
		}			
		this.rank = rank; 
		return true;
	}

	@Override
	public int GetRank() {
		return this.rank; 
	}
	
	@Override
	public int GetNumSubordinate() {
		return this.subordinates.size();
	}

	static public Supervisor ReturnNewObject(String name, String id, int rank){
		return new Supervisor(name,id,rank);
	}


	@Override
	public String GetStringInfo() {
		return "Name: " + this.name + ", ID: " + this.id + ", Rank: " + this.rank + ", Supervisor ID: " + this.supervisor.getID();
	}

	
}
