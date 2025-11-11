package com.example.projectmanageweb.model;

public class TaskDependency {
	  private int predecessorId;
	    private int successorId;
	    private String depType; // FS / SS / FF / SF
	    private int lagHours;
		public int getPredecessorId() {
			return predecessorId;
		}
		public void setPredecessorId(int predecessorId) {
			this.predecessorId = predecessorId;
		}
		public int getSuccessorId() {
			return successorId;
		}
		public void setSuccessorId(int successorId) {
			this.successorId = successorId;
		}
		public String getDepType() {
			return depType;
		}
		public void setDepType(String depType) {
			this.depType = depType;
		}
		public int getLagHours() {
			return lagHours;
		}
		public void setLagHours(int lagHours) {
			this.lagHours = lagHours;
		}
	    
	    

}
