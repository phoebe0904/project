
import java.util.*;

public class Employee extends Staff {
		
	public Employee(String name, String id, int rank)
	{
		super(name,id, rank);
		this.grade = Grade.EMPLOYEE;
	}
	
	public Boolean SetRank(int rank) 
	{
		return true; 
	}
	public int GetRank(){
		return Integer.MAX_VALUE; 
	}
	protected Boolean AddSubordinate(Staff s){
		return true;
	}
	public Boolean DelSubordinate(Staff s){
		return true;
	}

	static public Employee ReturnNewObject(String name, String id, int rank){
		return new Employee(name,id,rank);
	}

	@Override
	public int GetNumSubordinate() {
		return 0;
	}

	@Override
	public String GetStringInfo() {
		return "Name: " + this.name + ", ID: " + this.id + ", Supervisor ID: " + this.supervisor.getID();
	}
}




