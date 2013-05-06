package be.ac.ucl.lfsab1509.llncampus.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;

public class WebviewActivity extends LLNCampusActivity {
	private WebView webview;
	private Handler mHandler = new Handler();
	private WebviewActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.webview);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setWebViewClient(new myWebClient());
		setTitle(getIntent().getStringExtra("TITLE"));
		loadURL(getIntent().getStringExtra("URL"));
	}


	private void updateHTML(String BASE_URL, String html, String customCSS) {
		String encoding = "utf-8";
		if(getIntent().getStringExtra("ENCODING") != ""){
			encoding = getIntent().getStringExtra("ENCODING") ;
		}
		webview.loadDataWithBaseURL(BASE_URL, html + "<style>"+customCSS+"</style>", "text/html", encoding, null);
	}
	public void loadURL(final String URL) {
		android.util.Log.d("WebviewActivity", "URL : "+URL);
		if (URL.equals("about:blank")) { return; }
		final String header = "<html><head></head><body>";
		final String footer = "</body></html>";
		updateHTML("",header
				+ "Chargement en cours... <br /><small>(Vous devez &ecirc;tre connect&eacute; &agrave; Internet)</small>"
				+ footer, "body{ margin:40% auto; width:60%; font-size:25px; text-align:center;}");
		new Thread(new Runnable() {
			public void updateHTML(final String html) {
				mHandler.post(new Runnable() {
					public void run() {
						context.updateHTML(URL, html, getIntent().getStringExtra("CSS"));
					}
				});
			}

			public void run() {
				String html;
				try {
					HttpClient client = ExternalAppUtility.getHttpClient();
					HttpGet request = new HttpGet(URL);
					HttpResponse response = client.execute(request);
					html = EntityUtils.toString(response.getEntity());

				} catch (ParseException e) {
					html = header + "Erreur : " + e.getMessage() + footer;
				} catch (IOException e) {
					html = header + "Erreur : " + e.getMessage() + footer;
				}
				updateHTML(html);
			}
		}).start();
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
         return true;
     }
     return super.onKeyDown(keyCode, event);
    }
	
	public class myWebClient extends WebViewClient{
	  

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	// TODO Auto-generated method stub

	    	loadURL(url);
	    	return true;

	    }
	}
}