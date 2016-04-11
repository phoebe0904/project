import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Company {

	private ArrayList<Staff> employees;
	private ArrayList<Staff> supervisors;
	private Director director;

	public Staff GetDirector(){return this.director;}
	public ArrayList<Staff> GetEmployees(){return this.employees;}
	public ArrayList<Staff> GetSupervisors(){return this.supervisors;}
	
	public String companyProfile;
	
	public int GetNumDirector(){
		if(this.director!=null) return 1;
		return 0;
	}
	public int GetNumSupervisor(){
		return this.supervisors.size();
	}
	public int GetNumEmployee(){
		return this.employees.size();
	}
	public int GetNumStaff(){
		return this.GetNumDirector() + this.GetNumSupervisor() + this.GetNumEmployee();		
	}
	
	protected Boolean CreateDirector(JSONObject d){
		try{
			String dName = (String) d.get("name");
			String dID = (String) d.get("id");
			this.director = new Director(dName,dID,0);
			return true;			
		}catch(Exception e){
			return false;
		}
	}

	protected Staff SearchSupervisor(String sid){
		
		System.out.println(sid);
		
		System.out.println(1);
		if(sid.equals(this.director.getID())){
			System.out.println(this.director.getID());			
			return this.director;
		}
		
		if(this.supervisors != null){
			for(int i = 0; i < this.supervisors.size(); i++){
				if(this.supervisors.get(i).getID().equals(sid))
					return this.supervisors.get(i);
			}			
		}

		return null;
	}


	
	protected ArrayList<Staff> CreateStaffList(JSONArray arr, String mode){
		
		ArrayList<Staff> staffs = new ArrayList<Staff>();

		Staff supervisor, current;
		JSONObject jsonSupervisor;
		String name, id, sid;
		int rank;

		Iterator<Object> iterator = arr.iterator();
		while(iterator.hasNext()){
			jsonSupervisor = (JSONObject) iterator.next();
			name = (String) jsonSupervisor.get("name");
			id = (String) jsonSupervisor.get("id");
			sid = (String) jsonSupervisor.get("sid");
			rank = Integer.MAX_VALUE;
			

			System.out.println(jsonSupervisor.toString());
			
			
			if(jsonSupervisor.containsKey("rank"))
				rank = (int)(long)jsonSupervisor.get("rank");
			supervisor = SearchSupervisor(sid);
			if(supervisor==null) return null;
			current = null;
			if(mode.equals("Supervisor")){
				current = new Supervisor(name,id,rank);
			}
			else if(mode.equals("Employee")){
				current = new Employee(name,id,rank);
			}
			else{
				return null;
			}
			if(!current.SetSupervisor(supervisor)) return null;
			staffs.add(current);
		}
		return staffs;
	}
	

	private void ReadJSON(String filename) throws Exception, Throwable, ParseException
	{
		File f = new File(filename);
		if(!f.exists())
			return;
		
		JSONParser parser = new JSONParser();
		String content = new Scanner(f).useDelimiter("\\Z").next();		
		
		Object objProfile = parser.parse(content);
        JSONObject jsonProfile = (JSONObject) objProfile;
		JSONObject jsonDirector = (JSONObject) jsonProfile.get("Director");
		JSONArray jsonSupervisors = (JSONArray) jsonProfile.get("Supervisors");
		JSONArray jsonEmployees = (JSONArray) jsonProfile.get("Employees");

		if(!CreateDirector(jsonDirector)) System.out.println("Director creation fails!");
		this.supervisors = CreateStaffList(jsonSupervisors, "Supervisor");
		if(this.supervisors == null) System.out.println("Supervisors creation fails!");
		this.employees = CreateStaffList(jsonEmployees, "Employee");
		if(this.employees == null) System.out.println("Employees creation fails!");
	}

	
	private <T extends Staff> JSONObject StaffInfoJsonization(Class<T> type, String name, String id, String sid, int rank){
	    JSONObject info = new JSONObject();
//	    if(type.getName().equals("Director")){
	    info.put("name", name);
	    info.put("id", id);	    	
	    if(!type.getName().equals("Director"))
	    	info.put("sid", sid);
	    if(!type.getName().equals("Employee"))
	    	info.put("rank", rank);
		return info;
	}


	private <T extends Staff> JSONArray CreateSortedJSONArray(Class<T> type){
		Boolean b;
		JSONObject info;
		ArrayList<Staff> list = new ArrayList<Staff>();
		JSONArray staffArray = new JSONArray();
//		if(type.getName().equals("Supervisor")) 
		list = this.supervisors;
		if(type.getName().equals("Employee")) list = this.employees;
	    for(int i = 0; i < list.size(); i++){
	    	Staff s = list.get(i);
//	    	if(type.getName().equals("Supervisor"))
	    	info = this.StaffInfoJsonization(s.getClass(), s.getName(), s.getID(), s.GetSupervisor().getID(), s.GetRank());
	    	if(type.getName().equals("Employee"))
	    		info = this.StaffInfoJsonization(s.getClass(), s.getName(), s.getID(), s.GetSupervisor().getID(), Integer.MAX_VALUE);
	    	b = staffArray.add(info);
	    }
	    
	    if(type.getName().equals("Employee")) return staffArray;
	    
	    Comparator<JSONObject> comSupervisor = 
	    		new Comparator<JSONObject>()
	    		{
	    			private static final String KEY_NAME = "rank";
	    			public int compare(JSONObject jX, JSONObject jY) {
	    				System.out.println(jX.get(KEY_NAME));
	    				Integer x = (Integer) jX.get(KEY_NAME);
	    				Integer y = (Integer) jY.get(KEY_NAME);
	    				return x.toString().compareTo(y.toString());
					}
				};
	    staffArray.sort(comSupervisor);
	    return staffArray;
	}


	public Boolean WriteJSON()
	{
		try{
		    JSONObject profile = new JSONObject();

		    Boolean b;
	    	PrintWriter writer = new PrintWriter(this.companyProfile, "UTF-8");
		    if(this.director == null){
		    	writer.println((new JSONObject()).toString());
		    	writer.close();
		    	return true;
		    }
		    
//		    JSONObject director = new JSONObject();
		    profile.put("Director",this.StaffInfoJsonization(Director.class, this.director.getName(), this.director.getID(), "", this.director.GetRank()));
//		    profile.add(director);
		    	    
//		    JSONObject supervisors = new JSONObject();
		    JSONArray supervisorsArray = this.CreateSortedJSONArray(Supervisor.class);
		    profile.put("Supervisors", supervisorsArray);
//		    profile.add(supervisors);
		    
//		    JSONObject employees = new JSONObject();
		    JSONArray employeesArray = this.CreateSortedJSONArray(Employee.class);
		    profile.put("Employees", employeesArray);
//		    profile.add(employees);
		    
			writer.write(profile.toString());
			writer.close();	
			return true;
		}catch(Exception e){
			return false;
		}
	}

	
	
	public Company() throws Exception, Throwable, ParseException
	{
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		this.director = null;
		this.supervisors = new ArrayList<Staff>();
		this.employees = new ArrayList<Staff>();
		this.companyProfile = "profile.json";
		this.ReadJSON(this.companyProfile);

	}

	
	
	
	
	
	
	protected Staff SearchStaff(String sid){

		if(this.employees != null){
			for(int i = 0; i < this.employees.size(); i++){
				if(this.employees.get(i).getID().equals(sid))
					return this.employees.get(i);
			}
		}
		return this.SearchSupervisor(sid);
	}
	
	
	
	
	
	
	
	
	protected Boolean AddDirector(String name, String id){
		if(this.director != null){
			System.out.print("Director already exist!");
			return false;
		}
		this.director = new Director(name, id, 0);
		return true;		
	}

	protected <T extends Staff> T ReturnNewObject(Class<T> type,String name, String id, int rank){
		T newObj = null;
		if(type.getName().equals("Director")) newObj = (T)Director.ReturnNewObject(name, id, rank);
		else if(type.getName().equals("Supervisor")) newObj = (T)Supervisor.ReturnNewObject(name, id, rank);
		else if(type.getName().equals("Employee")) newObj = (T)Employee.ReturnNewObject(name, id, rank);
		return newObj;
	}
	
	protected <T extends Staff> Boolean AddNonDirector(Class<T> type, String name, String id, String sid, int rank){
		Staff supervisor, current;
		supervisor = SearchSupervisor(sid);
		if(supervisor == null)
			return false;

		
		current = this.ReturnNewObject(type, name, id, rank);
		if(current == null) System.out.println("current is null");
		if(!current.SetSupervisor(supervisor)){
			System.out.println("Cannot add staff!");
			return false;
		}
		ArrayList<Staff> tmp = null;
		if(current.getClass().getName().equals("Supervisor")) tmp = this.supervisors; 
		else if(current.getClass().getName().equals("Employee")) tmp = this.employees; 
		tmp.add(current);
		return true;
	}


	public <T extends Staff> Boolean AddStaff(Class<T> type, String name, String id, String sid, int rank){
		// See if the same id already exist
		if(this.SearchStaff(id) != null){
			System.out.print("Staff with the same ID already exist!");
			return false;
		}
		
		if(type.getName().equals("Director")){
			if(!this.AddDirector(name, id))
				return false;			
		}
		else if(!this.AddNonDirector(type, name, id, sid, rank)){
			return false;
		}
		return false;
	}


	
	
	protected <T extends Staff> ArrayList<Staff> ReturnQueue(Class<T> type){
		if(type.getName().equals("Supervisor")) return this.supervisors;
		else if(type.getName().equals("Employee")) return this.employees;
		return null;
	}
	
	
	public <T extends Staff> Boolean DelStaff(Class<T> type, String id){
		// See if the same id already exist
		Staff delStaff = this.SearchStaff(id);
		if(delStaff == null){
			System.out.print("This company doesn't have staff with this ID!");
			return false;
		}
		
		if(delStaff.GetNumSubordinate() != 0){
			System.out.print("The staff has subordinate(s)!");
			System.out.print("Cannot delete staff!");
			return false;
		}

		if(type.getName().equals("Director")){
			Staff tmp = null;
			tmp = this.director;
			this.director = null;
			return true;
		}
		
		if(!this.ReturnQueue(type).remove(delStaff)){
			System.out.print("Cannot remove the staff from the company staff list!");
			return false;
		}
		
		if(!delStaff.GetSupervisor().DelSubordinate(delStaff)){
			System.out.print("Cannot remove the staff from it's supervisor's subordinate list!");
			this.ReturnQueue(type).add(delStaff);
			return false;
		}
		
		return true;		

	}

	
	public Boolean AssignSupervisor(String staffID, String supervisorID){
		Staff staff = this.SearchStaff(staffID);
		Staff supervisor = this.SearchStaff(supervisorID);
		if(staff == null){
			System.out.print("This company doesn't have staff with this ID!");
			return false;
		}
		if(supervisor == null){
			System.out.print("This company doesn't have staff with this ID!");
			return false;
		}				
		return staff.SetSupervisor(supervisor);		
	}

	
	
	
	
	
	
	
}
