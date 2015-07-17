package com.example.augmentedlocalisation;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMQuery;


public class MainActivity extends Activity {
	private static final String APP_ID = "applicationID";
	private static final String APP_SECRET = "applicationSecret";
	private static final String APP_ROUTE = "applicationRoute";
	private static final String PROPS_FILE = "bluelist.properties";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        ArrayList itemList = new ArrayList<Item>();
    	// Read from properties file
    	Properties props = new java.util.Properties();
    	Context context = getApplicationContext();
    	try {
    		AssetManager assetManager = context.getAssets();
    		props.load(assetManager.open(PROPS_FILE));
    		
    	} catch (FileNotFoundException e) {
    		
    	} catch (IOException e) {
    		
    	}
    	
    	
    	 // initialize the IBM core backend-as-a-service
        IBMBluemix.initialize(this, props.getProperty(APP_ID), 
        					props.getProperty(APP_SECRET), props.getProperty(APP_ROUTE));
        // initialize the IBM Data Service
        IBMData.initializeService();
        // register the Item Specialization
        Item.registerSpecialization(Item.class);
    	
    }
    public void listItems() {
    	try {
    		IBMQuery<Item> query = IBMQuery.queryForClass(Item.class);
    		// Query all the Item objects from the server
    		query.find().continueWith(new Continuation<List<Item>, Void>() {

    				@Override
    				public Void then(Task<List<Item>> task) throws Exception {
                        final List<Item> objects = task.getResult();
                         // Log if the find was cancelled.
                        if (task.isCancelled()){
                            Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                        }
    					 // Log error message, if the find task fails.
    					else if (task.isFaulted()) {
    						Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
    					}

    					
    					 // If the result succeeds, load the list.
    					else {
                            // Clear local itemList.
                            // We'll be reordering and repopulating from DataService.
                            itemList.clear();
                            for(IBMDataObject item:objects) {
                                itemList.add((Item) item);
                            }
                            sortItems(itemList);
                            lvArrayAdapter.notifyDataSetChanged();
    					}
    					return null;
    				}

					@Override
					public Void then(Task<List<Item>> arg0) throws Exception {
						// TODO Auto-generated method stub
						return null;
					}
    			},Task.UI_THREAD_EXECUTOR);
    			
    		}  catch (IBMDataException error) {
    			Log.e(CLASS_NAME, "Exception : " + error.getMessage());
    		}
    	}

   
}
