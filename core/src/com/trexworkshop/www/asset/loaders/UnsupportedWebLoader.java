package com.trexworkshop.www.asset.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class UnsupportedWebLoader<T> extends AsynchronousAssetLoader<T, UnsupportedWebLoader.EmptyParameters<T>> {
	public UnsupportedWebLoader (FileHandleResolver resolver) {
		super(resolver);
	}
	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, EmptyParameters parameter) {
		throw new UnsupportedOperationException("Specified class not supported for web loader");
	}

	@Override
	public T loadSync (AssetManager manager, String fileName, FileHandle file, EmptyParameters parameter) {
		return null;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, EmptyParameters parameter) {
		return null;
	}

	static public class EmptyParameters<T> extends AssetLoaderParameters<T> {}
}