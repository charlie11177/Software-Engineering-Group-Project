package main.model;

import java.util.List;

public class Airport {
    private String name;
    private String code;
    private List<PhysicalRunWay> runways;
    private String runwayStrip;

    public Airport (String name, String code, List<PhysicalRunWay> runways){
        this.name = name;
        this.code = code;
        this.runways = runways;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }

    public String getCode(){
        return this.code;
    }
    public List<PhysicalRunWay> getRunways(){
        return this.runways;
    }

    public void addNewRunway(PhysicalRunWay runway){
        if (runways.contains(runway))
            System.out.println("This Runway Already Exist");
        else{
            runways.add(runway);
            System.out.println("Runway has been successfully added");
        }
    }
    @Override
    public String toString(){
        return this.name + " (" + this.code + ")";
    }

    public PhysicalRunWay findRunwayByID(List<PhysicalRunWay> runways, int ID){
        for (PhysicalRunWay r : runways){
            if(r.getRunwayID() == ID)
                return r;
        }
        return null;
    }

    public static void main(String args){

        //LogicalRunWay left = new LogicalRunWay("09", LogicalRunWay.Direction.Left,3902,3902,3202,3595);
        //LogicalRunWay right = new LogicalRunWay("27", LogicalRunWay.Direction.Right,3884,3962,3884,0);
        //PhysicalRunWay runWay = new PhysicalRunWay(1, left,right, null);

        //based on figure 3 in spec

        //Airport southampton = new Airport("SOU", "SOU", new ArrayList<>());
        //southampton.addNewRunway(runWay);
        //southampton.addNewRunway(runWay);

    }

}
