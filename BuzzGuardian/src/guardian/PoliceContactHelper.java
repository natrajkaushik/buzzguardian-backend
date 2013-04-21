package guardian;

/**
 * Contains methods to contact the appropriate Police (GT or Atlanta) or other Help agencies
 */
public class PoliceContactHelper {

	public static final double GT_CAMPUS_RADIUS = 0.6295848592841651;
	
	// origin: on Atlantic Drive, near CoC building
	public static final double GTCENTER_LAT = 33.7771;
	public static final double GTCENTER_LON = -84.3978;
	
	/**
	 * source: http://stackoverflow.com/questions/120283/working-with-latitude-longitude-values-in-java00
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return distance along earth between (latitude1, longitude1) and (latitude2, longitude2)
	 */
	private static double distFrom(double latitude1, double longitude1, double latitude2, double longitude2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(latitude2-latitude1);
	    double dLng = Math.toRadians(longitude2-longitude1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	}
	
	public static boolean insideGeorgiaTech(double latitude, double longitude)
	{
		return distFrom(GTCENTER_LAT, GTCENTER_LON, latitude, longitude) <= GT_CAMPUS_RADIUS;
	}
	
	public static String getPoliceNumber(double latitude, double longitude){
		return insideGeorgiaTech(latitude, longitude) ? "" : "";
	}
	
	public static void main(String[] args) {
		
		System.out.println(distFrom(33.7771, -84.3978, 33.7815, -84.4074));
		System.out.println(distFrom(33.7771, -84.3978, 33.7861, -84.4017));
		System.out.println(distFrom(33.7771, -84.3978, 33.7724, -84.3920));
	}
	
}
