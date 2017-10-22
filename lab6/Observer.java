package skeleton;

public class Observer {
	private int id;
	private Subject subject;

	public Observer(int id) {
		this.id = id;
	}

	public int getID(){
		return id;
	}
	
	public void subscribe(Subject sub) {
		this.subject = sub;
	}
	
	public void unsubscribe() {
		subject.unregister(this);
	}
	
	public void update(int newId){
		// TODO: The observer will exit the queue 
		// once the notification has value >= this.id+7.
		// Don't forget they will leave if it is their number too.
		if(newId >= this.id + 7 || newId == this.id) {
			this.unsubscribe();
		}
	}
}
