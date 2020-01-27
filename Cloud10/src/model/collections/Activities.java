package model.collections;

import java.util.ArrayList;

import model.Activity;

public class Activities {

	public ArrayList<Activity> activitiesList;
	
	public Activities() {}

	public Activities(ArrayList<Activity> activitiesMap) {
		this.activitiesList = activitiesMap;
	}

	public ArrayList<Activity> getActivitiesList() {
		return activitiesList;
	}
	public void setActivitiesList(ArrayList<Activity> activitiesList) {
		this.activitiesList = activitiesList;
	}
}
