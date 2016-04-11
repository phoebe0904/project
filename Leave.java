
import java.util.*;

public class Leave{
	public Calendar fr;
	public Calendar to;
	
	public Leave(){
		this.fr = Calendar.getInstance();
		this.to = Calendar.getInstance();
	}

	public Boolean SetRange(int fr_y, int fr_m, int fr_d, int to_y, int to_m, int to_d){
		this.fr.set(fr_y,fr_m,fr_d);
		this.to.set(to_y,to_m,to_d);
		if(this.to.before(this.fr)){
			return false;
		}
		return true;
	}
	
	private Boolean Equal(Calendar c1, Calendar c2){
		return c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR) 
				&& c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DATE)==c2.get(Calendar.DATE);
	}

	public Boolean IsOverlap(Leave l){
		System.out.println("From: " + l.fr.get(Calendar.YEAR)+"-"+l.fr.get(Calendar.MONTH)+"-"+l.fr.get(Calendar.DATE) + " To: " + l.to.get(Calendar.YEAR)+"-"+l.to.get(Calendar.MONTH)+"-"+l.to.get(Calendar.DATE));
		Boolean equals = this.Equal(this.fr, l.fr) || this.Equal(this.fr, l.to) 
				|| this.Equal(this.to, l.fr) || this.Equal(this.to, l.to);
		if(equals) return true;		
		if(this.fr.after(l.fr) && this.fr.before(l.to)) return true;
		if(l.fr.after(this.fr) && l.fr.before(this.to)) return true;
		return false;
	}

}
