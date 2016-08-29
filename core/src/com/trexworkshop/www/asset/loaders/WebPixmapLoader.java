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
import com.badlogic.gdx.utils.Array;

public class WebPixmapLoader extends AsynchronousAssetLoader<Pixmap, WebPixmapLoader.WebPixmapParameter> {
	public WebPixmapLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	Pixmap pixmap;
	private int statusCode = -1;

	@Override
	public void loadAsync (AssetManager manager, String fileName, final FileHandle file, WebPixmapParameter parameter) {
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
			request.setUrl(parameter.url);
			Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
				public void handleHttpResponse(Net.HttpResponse httpResponse) {
					statusCode = httpResponse.getStatus().getStatusCode();
					if(statusCode == 200) {
						final byte[] rawImageBytes = httpResponse.getResult();
						try {
							pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
						} catch(Exception e) {
							statusCode = -101;
						}
						if(isExternal && pixmap != null) {
							file.writeBytes(rawImageBytes, false);
						}
					}
				}
				public void failed(Throwable t) {
					statusCode = -102;
				}
				public void cancelled() {
					statusCode = -103;
				}
			});
		}
	}

	@Override
	public Pixmap loadSync (AssetManager manager, String fileName, FileHandle file, WebPixmapParameter parameter) {
		if(pixmap != null) {
			Pixmap pixmap = this.pixmap;
			this.pixmap = null;
			statusCode = -1;
			return pixmap;
		} else if(statusCode != -1 && statusCode != 200) {
			String message = statusCode == -101 ? "Create pixmap from bytes from " + parameter.url + " failed" :
				statusCode == -102 ? "Http call to " + parameter.url + " failed" :
				statusCode == -103 ? "Http call to " + parameter.url + " cancelled" :
				"Http call to " + parameter.url + " responded with status code " + statusCode;
			statusCode = -1;
			throw new RuntimeException(message);
		}
		return null;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, WebPixmapParameter parameter) {
		return null;
	}

	static public class WebPixmapParameter extends AssetLoaderParameters<Pixmap> {
		public String url;
	}
}