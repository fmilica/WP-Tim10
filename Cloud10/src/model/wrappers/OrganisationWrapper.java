package model.wrappers;


public class OrganisationWrapper {
	private String oldName;
	private String name;
	private String description;
	private String logo;
	
	public OrganisationWrapper() {}

	public OrganisationWrapper(String oldName, String name, String description, String logo) {
		super();
		this.oldName = oldName;
		this.name = name;
		this.description = description;
		this.logo = logo;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "OrganisationWrapper [oldName=" + oldName + ", name=" + name + ", description=" + description + ", logo="
				+ logo + "]";
	}
	
	public boolean hasNull() {
		if(this.oldName == null || this.oldName.trim().length() == 0) {
			return true;
		}
		else if(this.name == null || this.name.trim().length() == 0) {
			return true;
		}
		// nije obavezan opis
		/*
		else if(this.description == null || this.description.trim().length() == 0) {
			return true;
		}*/
		//logo moze da bude null a liste su naravno prazne
		return false;
	}
}
