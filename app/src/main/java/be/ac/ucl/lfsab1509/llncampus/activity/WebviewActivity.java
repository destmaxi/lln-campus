package be.ac.ucl.lfsab1509.llncampus.activity;

import java.io.IOException;
import java.util.Stack;

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

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
    Copyright (C) 2014 Quentin De Coninck

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *  Activity that define a web view and its behavior.
 *  Related with webview.xml.
 */
public class WebviewActivity extends LLNCampusActivity {

	/** The header of HTML pages. */
	public static final String HEADER = "<html><head></head><body>";
	/** The footer of HTML pages. */
	public static final String FOOTER = "</body></html>";

	private WebView webview;
	private MyWebClient webviewclient; 
	private Handler mHandler = new Handler();
	private WebviewActivity context;
	private Stack<HistoryElement> history = new Stack<HistoryElement>();
	private String encoding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.webview);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setBuiltInZoomControls(true);
		webviewclient = new MyWebClient();
		webview.setWebViewClient(webviewclient);
		encoding = "UTF-8";
		if(getIntent().getStringExtra(EXTRA_ENCODING) != null){
			encoding = getIntent().getStringExtra(EXTRA_ENCODING).toUpperCase(getResources()
					.getConfiguration().locale);
		}
		setTitle(getIntent().getStringExtra(EXTRA_TITLE));
		loadURL(getIntent().getStringExtra(EXTRA_URL));
	}

	/**
	 * Update the web view.
	 * 
	 * @param baseUrl
	 * 			The base URL to fetch.
	 * @param html
	 * 			Data in HTML.
	 * @param customCSS
	 * 			Custom CSS for the page.
	 * @param pushInHistory
	 * 			If set to true, store in history this update.
	 */
	private void updateHTML(String baseUrl, String html, String customCSS, 
			boolean pushInHistory) {
		webview.loadDataWithBaseURL(baseUrl, html + "<style>" + customCSS + "</style>",
				"text/html", encoding, null);
		if(pushInHistory){
			HistoryElement he = new HistoryElement(html, customCSS, baseUrl);
			history.push(he);
		}
	}

	/** 
	 * Load the URL given as parameter in the web view.
	 * @param url
	 * 			The URL to load.
	 */
	public void loadURL(final String url) {
		if (url.equals("about:blank")) 
		{ 
			// Nothing to load.
			return; 
		}


		updateHTML("", HEADER
				+ getString(R.string.loading) + "<br /><small>(" 
				+ getString(R.string.connection_required) + ")</small>" + FOOTER, 
				"body{ margin:40% auto; width:60%; font-size:25px; text-align:center;}", 
				false);
		new Thread(new Runnable() {
			/** @see WebviewActivity#updateHTML */
			public void updateHTML(final String html) {
				mHandler.post(new Runnable() {
					public void run() {
						context.updateHTML(url, html, getIntent().getStringExtra(EXTRA_CSS), true);
					}
				});
			}

			public void run() {
				String html;
				try {
					HttpClient client = ExternalAppUtility.getHttpClient();
					HttpGet request = new HttpGet(url);
					HttpResponse response = client.execute(request);
					html = EntityUtils.toString(response.getEntity());
				} catch (ParseException e) {
					html = HEADER + getString(R.string.error) + " : " + e.getMessage() + FOOTER;
				} catch (IOException e) {
					html = HEADER + getString(R.string.error) + " : " + e.getMessage() + FOOTER;
				}
				updateHTML(html);
			}
		}).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && history.size()>1) {
			history.pop(); // Remove the current page.
			HistoryElement he = history.peek(); // Get the previous page.
			updateHTML(he.baseURL, he.html, he.customCSS, true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Class to represent a web client.
	 */
	public class MyWebClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			loadURL(url);
			return true;
		}
	}

	/**
	 * A node for the history.
	 */
	public class HistoryElement {
		public String html;
		public String customCSS;
		public String baseURL;

		/**
		 * Constructor.
		 * 
		 * @param html
		 * 			The HTML to store.
		 * @param customCSS
		 * 			The CSS to store.
		 * @param baseURL
		 * 			The base URL to store.
		 */
		public HistoryElement(String html, String customCSS, String baseURL) {
			this.html = html;
			this.customCSS = customCSS;
			this.baseURL = baseURL;
		}
	}
}