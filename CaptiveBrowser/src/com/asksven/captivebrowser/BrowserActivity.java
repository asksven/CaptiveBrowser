package com.asksven.captivebrowser;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BrowserActivity extends Activity
{
	WebView m_webView;
	
	static final int CONTEXT_MENU_ID	 		= 1;
    
    static final int CONTEXT_TOGGLE_NAV_ID 		= 100;
    static final int CONTEXT_PREFERENCES_ID		= 101;
    static final int CONTEXT_README_ID	 		= 102;


	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.browser);

	    m_webView = (WebView) findViewById(R.id.webview);
	    m_webView.getSettings().setJavaScriptEnabled(true);
	    m_webView.setWebViewClient(new BrowserWebViewClient());
        registerForContextMenu(m_webView); 

    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String url = preferences.getString("default_url", "http://www.google.com");

    	boolean bNavBarVisble = preferences.getBoolean("nav_bar_visible", true);
    	if (!bNavBarVisble)
    	{
        	LinearLayout resultLayout = (LinearLayout) findViewById(R.id.linearLayoutNavBar);
       		resultLayout.setVisibility(View.GONE);
    	}

    	EditText tvUrl = (EditText) findViewById(R.id.editTextUrl);

    	tvUrl.setText(url);
	    m_webView.loadUrl(url);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && m_webView.canGoBack())
	    {
	        m_webView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void onGoClick(View view)
	{
        EditText tvUrl = (EditText) findViewById(R.id.editTextUrl);
        String url = tvUrl.getText().toString();
	    m_webView.loadUrl(url);

	}

	public void onRefreshClick(View view)
	{
        EditText tvUrl = (EditText) findViewById(R.id.editTextUrl);
        String url = tvUrl.getText().toString();
	    m_webView.loadUrl(url);

	}

    /** 
     * Add menu items
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {  
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browsermenu, menu);
        return true;
    }  

    // handle menu selected
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {  
        	case R.id.toggle_nav:  
            	LinearLayout resultLayout = (LinearLayout) findViewById(R.id.linearLayoutNavBar);
            	if (resultLayout.isShown())
            	{
            		resultLayout.setVisibility(View.GONE);
            	}
            	else
            	{
            		resultLayout.setVisibility(View.VISIBLE);
            	}
            		

	        	break;	


        }  
        return false;  
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info)
    {
    	super.onCreateContextMenu(menu, v, info);
        menu.setHeaderTitle("Actions");
    	menu.add(CONTEXT_MENU_ID, CONTEXT_TOGGLE_NAV_ID, Menu.NONE, "Toggle Navigation");
    	menu.add(CONTEXT_MENU_ID, CONTEXT_PREFERENCES_ID, Menu.NONE, "Preferences");
    	menu.add(CONTEXT_MENU_ID, CONTEXT_README_ID, Menu.NONE, "Readme");
        	
   } 
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
    	
    	// check if the called back fragment is the one that has initiated the menu action
    	// based on the group id. if not do noting
    	
    	switch(item.getItemId())
    	{
    		case CONTEXT_TOGGLE_NAV_ID:    	    	
            	LinearLayout resultLayout = (LinearLayout) findViewById(R.id.linearLayoutNavBar);
            	if (resultLayout.isShown())
            	{
            		resultLayout.setVisibility(View.GONE);
            	}
            	else
            	{
            		resultLayout.setVisibility(View.VISIBLE);
            	}
    			return true;
			
    		case CONTEXT_PREFERENCES_ID:
	        	Intent intentPrefs = new Intent(this, PreferencesActivity.class);
	            this.startActivity(intentPrefs);
    			return true;

    		case CONTEXT_README_ID:
	        	Intent intentReadMe = new Intent(this, ReadmeActivity.class);
	            this.startActivity(intentReadMe);
    			return true;
     
    		default:
    			return false;
    	}
     } 

	private class BrowserWebViewClient extends WebViewClient
	{
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url)
	    {
	        view.loadUrl(url);
	        return true;
	    }
	}
}
