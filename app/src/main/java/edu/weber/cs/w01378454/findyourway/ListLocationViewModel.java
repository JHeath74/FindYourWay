package edu.weber.cs.w01378454.findyourway;

import androidx.lifecycle.ViewModel;

public class ListLocationViewModel extends ViewModel
{
    private String Id;
    private String LocationName;
    private String GPSCoordinates;
    private String LocationComments;

    public String getID() {
        return Id;
    }

    public void setID(String Id) {
        this.Id = Id;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getGPSCoordinates() {
        return GPSCoordinates;
    }

    public void setGPSCoordinates(String GPSCoordinates) {
        this.GPSCoordinates = GPSCoordinates;
    }

    public String getLocationComments() {
        return LocationComments;
    }

    public void setLocationComments(String locationComments) {
        LocationComments = locationComments;
    }

    public ListLocationViewModel() {
    }

    public ListLocationViewModel(String Id, String locationName, String GPSCoordinates, String locationComments) {
        this.Id = Id;
        LocationName = locationName;
        this.GPSCoordinates = GPSCoordinates;
        LocationComments = locationComments;
    }
}
