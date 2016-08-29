package com.trexworkshop.www.asset.loaders;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class WebTextureLoader extends AsynchronousAssetLoader<Texture, WebTextureLoader.WebTextureParameter> {
	public WebTextureLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	private Pixmap pixmap;
	private int statusCode = -1;
	private Throwable t1 = null;

	@Override
	public void loadAsync (AssetManager manager, String fileName, final FileHandle file, WebTextureParameter parameter) {
		pixmap = null;
		statusCode = -1;
		if(parameter == null || parameter.url == null) {
			throw new IllegalArgumentException("parameter.url must not be null");
		}
		
		final boolean isExternal = Gdx.files.isExternalStorageAvailable() && file.type() == Files.FileType.External;
		if(isExternal && file.exists()) {
			try {
				pixmap = new Pixmap(file);
			} catch(Exception e) {
				statusCode = -101;
			}
		} else {
			Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
			String httpsURL = parameter.url.replace("http://graph.facebook.com/","https://graph.facebook.com/");
			request.setUrl(httpsURL);
			request.setFollowRedirects(true);
			request.setTimeOut(5000);

			Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
				public void handleHttpResponse(Net.HttpResponse httpResponse) {

					statusCode = httpResponse.getStatus().getStatusCode();
					if (statusCode == 200 || statusCode == 302) {
						final byte[] rawImageBytes = httpResponse.getResult();
						try {
							pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
						} catch (Exception e) {
							statusCode = -101;
						}
						if (isExternal && pixmap != null) {
							file.writeBytes(rawImageBytes, false);
						}
					}
				}

				public void failed(Throwable t) {
					statusCode = -102;
					t1 = t;
				}

				public void cancelled() {
					statusCode = -103;
				}
			});
		}
	}

	@Override
	public Texture loadSync (AssetManager manager, String fileName, FileHandle file, WebTextureParameter parameter) {
		if(pixmap != null) {
			Texture texture = new Texture(pixmap);
			pixmap.dispose();
			pixmap = null;
			statusCode = -1;
			return texture;
		} else if(statusCode != -1 && statusCode != 200) {
			String message = statusCode == -101 ? "Create pixmap from bytes from " + parameter.url + " failed" :
				statusCode == -102 ? "Http call to " + parameter.url + " failed " + t1 :
				statusCode == -103 ? "Http call to " + parameter.url + " cancelled" :
				"Http call to " + parameter.url + " responded with status code " + statusCode;
			statusCode = -1;
			throw new RuntimeException(message);
		}
		return null;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, WebTextureParameter parameter) {
		return null;
	}

	static public class WebTextureParameter extends AssetLoaderParameters<Texture> {
		public String url;
	}

	/*

			AsyncExecutor executor = new AsyncExecutor(5);
			executor.submit(asyncTask);
	AsyncTask<Boolean> asyncTask = new AsyncTask<Boolean>()
	{
		@Override
		public Boolean call() throws Exception {
			Gdx.app.postRunnable(new Runnable() {
				public void run() {

					 // Download a url into byte[]

					public static byte[] download (@NotNull String url, Map<String, Object> params) {
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						try {
							HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
							connection.setInstanceFollowRedirects(true);
							connection.setReadTimeout(5000);

							if (params != null && params.size() > 0) {
								StringBuilder postData = new StringBuilder();
								for (Map.Entry<String, Object> param : params.entrySet()) {
									if (postData.length() != 0) postData.append('&');
									postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
									postData.append('=');
									postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
								}
								byte[] postDataBytes = postData.toString().getBytes("UTF-8");

								connection.setRequestMethod("POST");
								connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
								connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
								connection.setDoOutput(true);
								connection.getOutputStream().write(postDataBytes);
							}

							boolean redirect = false;
							// normally, 3xx is redirect
							int status = connection.getResponseCode();
							if (status != HttpURLConnection.HTTP_OK) {
								if (status == HttpURLConnection.HTTP_MOVED_TEMP
										|| status == HttpURLConnection.HTTP_MOVED_PERM
										|| status == HttpURLConnection.HTTP_SEE_OTHER)
									redirect = true;
							}
							if (redirect) {
								// get redirect url from "location" header field
								String newUrl = connection.getHeaderField("Location");

								// get the cookie if need, for login
								String cookies = connection.getHeaderField("Set-Cookie");

								// open the new connnection again
								connection = (HttpURLConnection) new URL(newUrl).openConnection();
								connection.setRequestProperty("Cookie", cookies);
							}
							byte[] chunk = new byte[4096];
							int bytesRead;
							InputStream stream = connection.getInputStream();
							while ((bytesRead = stream.read(chunk)) > 0) {
								outputStream.write(chunk, 0, bytesRead);
							}
							stream.close();
						} catch (Exception e) {
							//e.printStackTrace();
							return null;
						}
						return outputStream.toByteArray();
					}
				}
			});
			return null;
		}
	};
	*/
}