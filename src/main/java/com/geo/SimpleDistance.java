package com.geo;
/**
 * Original source from: http://rosettacode.org/wiki/Haversine_formula#Java
 * This is the implementation Haversine Distance Algorithm between two places
 */
public class SimpleDistance {
    public static final double R = 3958.75; //Miles
    //public static final double R = 20902200; //Feet
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
    
    public static void main(String[] args) {
    	System.out.println(haversine(41.9999, -88.00, 42.00, -88.00));
    	System.out.println(haversine(36.12, -86.67, 33.94, -118.40)); //1794 miles (9472320 feet)
    }
}
