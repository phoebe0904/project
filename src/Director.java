import java.util.ArrayList;

public class Director extends Staff{

	private ArrayList<Staff> subordinates;

	public Director(String name, String id,int rank)
	{
		super(name,id, rank);
		this.rank = 0;
		this.grade = Grade.DIRECTOR;
		this.subordinates = new ArrayList<Staff>();
	}
	
	@Override
	public Boolean SetRank(int rank) { 
		System.out.println("You can't set a Director's rank!");
		return false;
	}

	@Override
	public int GetRank() {
		return 0; 
	}

	@Override
	protected Boolean AddSubordinate(Staff s) {

		if(s.getID() == this.id) return false;
			
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
	public int GetNumSubordinate() {
		return this.subordinates.size();
	}

	static public Director ReturnNewObject(String name, String id, int rank){
		return new Director(name,id,rank);
	}



	@Override
	public String GetStringInfo() {
		return "Name: " + this.name + ", ID: " + this.id + ", Rank: " + this.rank;
	}


}
