package uk.ac.bham.cs.bhamnav;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;

import android.content.res.XmlResourceParser;

/**
 * @author Adrian Shaw
 */
public class LoadXML {
		
	private XmlResourceParser r;
		
	public LoadXML(XmlResourceParser resource){		
		r = resource;
	}
	
	/**
	 * This is a method which retrieves attribute values 
	 * from a file using the included XmlPullParser class
	 * START_DOC	= 0
	 * END_DOCUMENT = 1
	 * START_TAG 	= 2
	 */
	public ArrayList<Node> convertXML(){
				
		ArrayList<Node> nodes = new ArrayList<Node>();

		try {
			r.next();
			int parsercode = r.getEventType();
						
			// START DOC = 0
			// END DOCUMENT = 1
			// START TAG  = 2
			
			if(parsercode==XmlPullParser.START_DOCUMENT){
				r.next();
				r.next();	// skip root node
			}
			
			while(parsercode!=XmlPullParser.END_DOCUMENT)
			{
				parsercode = r.getEventType();
																
				if (parsercode==XmlPullParser.START_TAG)
				{
					/*
					 * Retrieve values from attributes.
					 * The first parameter indicates whether namespaces are in use,
					 * which they are not.
					 */
					float lat = r.getAttributeFloatValue(null, "geolat", -1);
					float lon = r.getAttributeFloatValue(null, "geolong", -1);
					String name = r.getAttributeValue(null, "name");
					String deptname = r.getAttributeValue(null, "deptname");
					String code = r.getAttributeValue(null, "code");
					String buildingname = r.getAttributeValue(null, "buildingname");
					boolean visibility = r.getAttributeBooleanValue(null, "visible", false);
					String connections = r.getAttributeValue(null, "connectedto");
					String description = r.getAttributeValue(null, "description");
					
					if(name!=null){
//						//Log.i("NavMap","Node="+name+lat+lon+deptname+code+buildingname+visibility+connections+description);
						if(lat!=-1.0){
							
							if(lon!=-1.0){
								int latint = (int) (lat*1e6);
								int lonint = (int) (lon*1e6);
								
								Node newNode = new Node(new GeoPoint(latint, lonint),"",name);
								newNode.setVisible(visibility);
								
								newNode.setName(newNode.getNodeID());
								
								if(deptname!=null){
									newNode.setDepartment(deptname);
									//Log.i("NavMap","Found deptname");
								}
								if(buildingname!=null){
									newNode.setBuildingName(buildingname);
									//Log.i("NavMap", "Found buildingname");
								}
								if(code!=null){
									newNode.setCode(code);
									//Log.i("NavMap","Found code");
								}
								if(description!=null){
									newNode.setDescription(description);
									//Log.i("NavMap", "Found description");
								}
								
								String[] splitted;
								if(connections!=null){
									if(!connections.equalsIgnoreCase("")){
										splitted = connections.split(",");
										newNode.setConnectedTo(splitted);										
									}
								}
								nodes.add(newNode);
//								//Log.i("NavMap", "Added node");
								r.next();
							}
						}
						else {
			//				//Log.i("NavMap","Cannot find attribute 'geolat'");
							r.close();
							break;
						}
					}
					else {
		//				//Log.i("NavMap", "Returned null. Cannot find attribute 'name'");
						r.close();
						break;
					}
				}				
				else if (parsercode==XmlPullParser.END_TAG){
	//				//Log.i("NavMap","Found end tag");
					r.next();
				}
				else {
					//Log.i("NavMap", "Neither start tag nor End tag was found");
					r.close();
					break;
				}
			}
			
			//Log.i("NavMap","Made it out of the loop");
			
			for(int i=0; i<nodes.size();i++)
			{
				//Log.i("NavMap", "Nodes loaded: "+nodes.get(i).getNodeID()+" "+nodes.get(i).getGeoPoint().getLatitudeE6()+" "+nodes.get(i).getGeoPoint().getLongitudeE6()+nodes.get(i).getBuildingName()+nodes.get(i).getCode());
			}
			
		} catch (XmlPullParserException e) {
			//Log.i("NavMap", "Fail in LoadXML: "+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			//Log.i("NavMap", "IOfail in LoadXML "+e.toString());
			e.printStackTrace();
		}
		
		return nodes;
	}
}